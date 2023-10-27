package online.referity.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.FCMRequest;
import online.referity.entity.Account;
import online.referity.repository.AccountRepository;
import online.referity.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class FCMController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ResponseHandler responseHandler;

    @PostMapping("fcm/{accountId}")
    public ResponseEntity updateFCMToken(@PathVariable UUID accountId, @RequestBody FCMRequest fcmRequest){
        Account account = accountRepository.findAccountById(accountId);
        account.setFCMToken(fcmRequest.getToken());
        return responseHandler.response(200, "Update FCM token success!", accountRepository.save(account));
    }
}
