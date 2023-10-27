package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.*;
import online.referity.dto.response.CompanyTransactionResponse;
import online.referity.entity.Account;
import online.referity.entity.Transaction;
import online.referity.entity.Wallet;
import online.referity.enums.AccountType;
import online.referity.enums.PaymentResult;
import online.referity.enums.TransactionResult;
import online.referity.enums.TransactionType;
import online.referity.service.TransactionService;
import online.referity.service.WalletService;
import online.referity.utils.ResponseHandler;
import online.referity.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class WalletController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    WalletService walletService;




    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    Validation validation;
    @GetMapping("transaction/{walletId}")
    @Operation(summary = "Get Transaction of wallet ")
    public ResponseEntity getTransaction(@PathVariable String walletId ){
        CompanyTransactionResponse result = transactionService.getByWalletId(UUID.fromString(walletId));
        return responseHandler.response(201, "Successfully get Transaction !", result);

    }
    @PutMapping("payment/{walletId}")
    @Operation(summary = "Get Transaction of wallet ")
    public ResponseEntity payment(@PathVariable String walletId ,@RequestBody  PaymentRequest request){
        Transaction result = transactionService.updateResult(request.getTransactionId() , request.getPaymentResult() ,UUID.fromString( walletId));
        return responseHandler.response(201, "Successfully pay for Transaction !", result);
    }

    @PostMapping("wallet")
    @Operation(summary = "Create Wallet for account")
    public ResponseEntity payment(@RequestBody WalletRequest walletRequest   ){

        Wallet result = walletService.initWallet(walletRequest );
        return responseHandler.response(201, "Successfully create account  !", result);
    }

//    @PutMapping("testNoti")
//    @Operation(summary = "Get Transaction of wallet ")
//    public ResponseEntity test(@RequestParam TransactionType type , @RequestParam AccountType sender , @RequestParam AccountType ngnhan , @RequestParam String content , @RequestParam double money){
//        Transaction result = .(type , sender , ngnhan , content , money);
//        return responseHandler.response(201, "Successfully pay for Transaction !", result);
//    }

    @PostMapping("accept-withdraw/{transactionId}")
    @Operation(summary = "admin accept withdraw ")
    public ResponseEntity payment(@RequestBody AcceptWithdrawRequest request , @PathVariable UUID transactionId  ){
        Transaction result = walletService.acceptWithDraw( request.getWalletId(), transactionId  , request.getImageEvident());
        return responseHandler.response(201, "Successfully accept withdraw transaction   !", result);
    }
    @PostMapping("with-draw")
    @Operation(summary = "Create request with draw for account")
    public ResponseEntity withDraw(@RequestBody WithDrawRequest request  ){
        Transaction result = walletService.withDraw( request.getWalletId(), request.getMoney() );
        return responseHandler.response(201, "Successfully create withdraw request   !", result);
    }

    @PostMapping("recharge")
    @Operation(summary = "Create Wallet for account")
    public ResponseEntity recharge(@RequestBody RechangeRequest request  ){
        Transaction result = walletService.recharge(request.getWalletId(), request.getMoney() );
        return responseHandler.response(201, "Successfully add money account  !", result);
    }

    @GetMapping("transaction-withdraw")
    public ResponseEntity getAllWithdrawRequest(){
        List<Transaction> transactions = transactionService.getAllTransactionWithdraw();
        return responseHandler.response(201, "Successfully get all transaction withdraw!", transactions);
    }
}
