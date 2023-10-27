package online.referity.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.entity.Account;
import online.referity.entity.CandidateApply;
import online.referity.enums.ResultStage;
import online.referity.service.AccountService;
import online.referity.service.HeadhunterService;
import online.referity.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    AccountService accountService;

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    HeadhunterService headhunterService;

    @PatchMapping("verify/{accountId}")
    public ResponseEntity verifyAccount(@PathVariable UUID accountId){
        Account account = accountService.verifyAccount(accountId);
        return responseHandler.response(200, "Verify account success!", account);
    }

    @PatchMapping("block/{accountId}")
    public ResponseEntity blockAccount(@PathVariable UUID accountId){
        Account account = accountService.blockAccount(accountId);
        return responseHandler.response(200, "Block account success!", account);
    }
//    @PatchMapping("confirm-cv/{applyCode}")
//    public ResponseEntity blockAccount(@PathVariable String applyCode , @RequestBody ResultStage resultStage){
//        CandidateApply apply = headhunterService.confirmApply(applyCode , resultStage);
//        return responseHandler.response(200, "Confirm success!", apply);
//    }

}
