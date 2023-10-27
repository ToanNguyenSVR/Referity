package online.referity.service.imp;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import online.referity.dto.FcmNotification;
import online.referity.entity.Account;
import online.referity.entity.Notification;
import online.referity.enums.NotiStatus;
import online.referity.repository.AccountRepository;
import online.referity.repository.NotificationRepository;
import online.referity.service.NotificationService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImp implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    FcmService fcmService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    private final FirebaseMessaging firebaseMessaging;
    private final FirebaseApp firebaseApp;

    public NotificationServiceImp(FirebaseApp firebaseApp) {
        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
        this.firebaseApp = firebaseApp;
    }


    @Override
    public List<Notification> getByAccount(UUID accountId) {
        List<Notification> list = notificationRepository.getbyAccountId(accountId);
        return list.stream().sorted(Comparator.comparing(Notification::getCreateDate).reversed()).collect(Collectors.toList());

    }

    @Override
    public void sendNotification(String title, String body, String forwardUrl, UUID accountId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Account account = accountRepository.findAccountById(accountId);
                if (account == null) System.out.println("LOG : ACCOUNT NOT FOUND CREATE NOTI");
                Notification notification = new Notification();
                notification.setTitle(title);
                notification.setBody(body);
                notification.setForwardUrl(forwardUrl);
                notification.setNotiStatus(NotiStatus.NEW);
                notification.setAccount(account);
                notification.setCreateDate(LocalDateTime.now());
                notification = notificationRepository.save(notification);
                messagingTemplate.convertAndSend("/topic/notification/" + account.getId(), notification.getTitle()+"~"+notification.getBody());
                try {
                    if (account.getFCMToken() != null) {
                        FcmNotification fcmNotification = new FcmNotification();
                        fcmNotification.setTitle(notification.getTitle());
                        fcmNotification.setBody(notification.getBody());
                        fcmNotification.setToken(account.getFCMToken());
                        fcmService.sendPushNotification(fcmNotification);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public boolean changeStatus(UUID accountId) {
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };
//        new Thread(runnable).start();
        List<Notification> notifications = notificationRepository.getbyAccount(accountId);
        int updatedCount = 0;

        for (Notification notification : notifications) {
            notification.setNotiStatus(NotiStatus.OLD);
            // Optionally, you can perform additional updates to other properties.

            // Save the updated entity.
            notificationRepository.save(notification);
            updatedCount++;
        }

        return true;
    }


}
