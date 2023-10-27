package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.ResponseWithDataDTO;
import online.referity.dto.request.*;
import online.referity.dto.response.HeadhunterResponse;
import online.referity.dto.response.LoginResponse;
import online.referity.dto.response.WalletResponse;
import online.referity.entity.*;
import online.referity.enums.AccountStatus;
import online.referity.enums.TransactionResult;
import online.referity.service.AccountService;
import online.referity.service.CompanyService;
import online.referity.service.TransactionService;
import online.referity.service.WalletService;
import online.referity.utils.Request;
import online.referity.utils.ResponseHandler;
import online.referity.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class AccountController {

    @Autowired
    AccountService userService;
    @Autowired
    CompanyService companyService;

    @Autowired
    Request request;

    @Autowired
    TransactionService transactionService;
    @Autowired
    WalletService walletService;

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    Validation validation;



    @GetMapping("test")
    public ResponseEntity test(){
        System.out.println(request.getCurrentUser());
        return responseHandler.response(100,"aaaa",null);
    }

    @PostMapping("login")
    public  ResponseEntity login(@RequestBody LoginRequest loginRequest, @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){
        LoginResponse loginResponse = userService.login(loginRequest);
        System.out.println(loginResponse);
        return responseHandler.response(200, "Login success!", loginResponse);
    }

    @PostMapping("login-with-token")
    public  ResponseEntity loginWithToken(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = userService.loginWithToken(loginRequest);
        return responseHandler.response(200, "Login success!", loginResponse);
    }

    @PostMapping("login-google")
    public  ResponseEntity login(@RequestBody GoogleLoginRequest googleLoginRequest){
        LoginResponse loginResponse = userService.loginGoogle(googleLoginRequest);
        return responseHandler.response(200, "Login success!", loginResponse);
    }

    @PostMapping("register")
    @Operation(summary = "Register for new account")
    public  ResponseEntity register(@Valid @RequestBody AccountRegisterRequest accountRegisterRequest, BindingResult result){
        validation.validate(result);
        Account newUser = userService.register(accountRegisterRequest);
        ResponseEntity response = responseHandler.response(201, "Successfully registered new account!", newUser);
        return response;
    }

    @PostMapping("company/{accountId}")
    @Operation(summary = "Additional Information for company")
    public  ResponseEntity additionalInformationCompany(@Valid @RequestBody CompanyAddInformationRequest companyAddInformationRequest, @PathVariable UUID accountId, BindingResult result){
        validation.validate(result);
        Company company = userService.additionalInformationForCompany(companyAddInformationRequest, accountId);
        ResponseEntity response = responseHandler.response(201, "Successfully update information for company!", company);
        return response;
    }



    @PutMapping("company/{accountId}")
    @Operation(summary = "Update Information for company")
    public  ResponseEntity updateInformationCompany(@Valid @RequestBody CompanyAddInformationRequest companyAddInformationRequest, @PathVariable UUID accountId, BindingResult result){
        validation.validate(result);
        Company company = userService.updateInformationForCompany(companyAddInformationRequest, accountId);
        ResponseEntity response = responseHandler.response(201, "Successfully update information for company!", company);
        return response;
    }



    @GetMapping("account/{accountId}")
    @Operation(summary = "Get Information of account")
    public  ResponseEntity getInformation(@PathVariable UUID accountId){
        Account account = userService.getAccountDetail(accountId);
        ResponseEntity response = responseHandler.response(200, "Successfully get information of account!", account);
        return response;
    }

    @GetMapping("company/verify")
    public ResponseEntity getCompanyVerify(){
        List<Company> result = companyService.getCompanyVerify();
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfull" , result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("company/active")
    public ResponseEntity getCompanyActive(){
        List<Company> result = companyService.getCompanyActive();
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfull" , result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("headhunter/")
    public ResponseEntity getHeadhunterVerify(@Parameter AccountStatus accountStatus){
        List<HeadhunterResponse> result = userService.getHeadhunter(accountStatus);
        return responseHandler.response(200, "Successfully deleted skill ", result);
    }


//    @GetMapping("headhunter/active")
//    public ResponseEntity getHeadhunterActive(){
//        List<Headhunter> result = userService.getHeadhunterActive();
//        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfull" , result);
//        return ResponseEntity.ok(response);
//    }
}
