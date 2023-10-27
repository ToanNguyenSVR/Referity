package online.referity.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.FcmNotification;
import online.referity.entity.City;
import online.referity.entity.Notification;
import online.referity.repository.AccountRepository;
import online.referity.service.NotificationService;
import online.referity.service.imp.FcmService;
import online.referity.utils.ResponseHandler;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    FcmService fcmService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ResponseHandler responseHandler;

    @GetMapping("test-fcm")
    public void senMessage(){
//        String token = accountRepository.findAccountById(14).getFCMToken();
        FcmNotification fcmNotification = new FcmNotification();
        fcmNotification.setTitle("test");
        fcmNotification.setBody("test");
//        fcmNotification.setToken(token);
        messagingTemplate.convertAndSend("/topic/messages", "test");
        try{
            fcmService.sendPushNotification(fcmNotification);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PutMapping("send-noti")
    public void sendNoti(@RequestBody Notification notification , @RequestParam UUID accountId ){
        notificationService.sendNotification(notification.getTitle() , notification.getBody() , notification.getForwardUrl() , accountId);
    }

    @PutMapping("notification")
    public ResponseEntity changeStatus( @RequestParam UUID accountId ){
        boolean check = notificationService.changeStatus(accountId);
        return responseHandler.response(200, "Successfully get list notification", check);
    }

    @GetMapping("notification")
    public ResponseEntity getCity(@RequestParam String accountId) {
        List<Notification> result = notificationService.getByAccount(UUID.fromString(accountId));
        return responseHandler.response(200, "Successfully get list notification", result);
    }
}
