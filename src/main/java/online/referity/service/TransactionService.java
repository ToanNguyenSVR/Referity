package online.referity.service;

import online.referity.dto.response.CompanyTransactionResponse;
import online.referity.entity.ApplyStage;
import online.referity.entity.JobStage;
import online.referity.entity.Transaction;
import online.referity.entity.Wallet;
import online.referity.enums.PaymentResult;
import online.referity.enums.TransactionResult;
import online.referity.enums.TransactionType;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    public Transaction createTransaction(
            ApplyStage applyStage ,
            JobStage jobStage,
            String key,
            Wallet wallet,
            Double money,
            TransactionType type,
            TransactionResult result,
            String content ,
            String senderCode ,
            String receiverCode );
    public Transaction updateTransaction (UUID transactionId , TransactionResult transactionResult);

    public CompanyTransactionResponse getByWalletId (UUID walletId);

    public Transaction updateResult(UUID transactionId, PaymentResult paymentResult , UUID walletId);

    public List<Transaction> getAllTransactionWithdraw();
}
