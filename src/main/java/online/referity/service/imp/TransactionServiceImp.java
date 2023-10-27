package online.referity.service.imp;

import online.referity.dto.response.CompanyTransactionResponse;
import online.referity.entity.*;
import online.referity.enums.*;
import online.referity.exception.exceptions.BadRequest;
import online.referity.repository.JobRepository;
import online.referity.repository.JobStageRepository;
import online.referity.repository.TransactionRepository;
import online.referity.repository.WalletRepository;
import online.referity.service.TransactionService;
import online.referity.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImp implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    WalletService walletService ;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    JobStageRepository jobStageRepository;

    @Autowired
    JobRepository jobRepository;

    ModelMapper modelMapper = new ModelMapper();


    @Override
    public Transaction createTransaction( ApplyStage applyStage ,JobStage jobStage, String key, Wallet wallet, Double money, TransactionType type, TransactionResult result, String content , String senderCode , String receiverCode ) {
        Transaction transaction = new Transaction();
        transaction.setCode(key);
        transaction.setMoney(money);
        transaction.setCreateDate(LocalDateTime.now());
        if(type.equals(TransactionType.PAY) && applyStage == null ) {
            //  chỉ có thanh toán cho 1 job thì mới có phí
            transaction.setPlatformFee(10.0);
        }
        transaction.setTotalMoney(transaction.getMoney() + (transaction.getMoney()*transaction.getPlatformFee())/100);
        transaction.setCreateBy("SYSTEM AUTOMATION");
        transaction.setSenderCode(senderCode);
        transaction.setReceiverCode(receiverCode);
        transaction.setTransactionType(type);
        transaction.setWallet(wallet);
        if(jobStage != null )transaction.setJobStage(jobStage);
        if(applyStage != null) transaction.setApplyStage(applyStage);
        transaction.setTransferContent(content);
        transaction.setTransactionResult(result);
        transaction.setExpireDay(transaction.getCreateDate().plus(20 , ChronoUnit.DAYS));
        return transactionRepository.save(transaction) ;
    }

    @Override
    public Transaction updateTransaction(UUID transactionId, TransactionResult transactionResult) {
        return null;
    }


    @Override
    public CompanyTransactionResponse getByWalletId(UUID walletId) {
        CompanyTransactionResponse companyTransactionResponse = new CompanyTransactionResponse();
        List<Transaction> list = transactionRepository.findTransactionByWalletId(walletId);
        Wallet wallet = walletRepository.findWalletById(walletId);
        list = list.stream().sorted(Comparator.comparing(Transaction::getCreateDate).reversed()).collect(Collectors.toList());
        companyTransactionResponse.setTransactions(list);
        companyTransactionResponse.setWallet(wallet);
        return companyTransactionResponse;
    }



    @Override
    @Transactional
    public Transaction updateResult(UUID transactionId, PaymentResult paymentResult , UUID walletId) {
        Transaction transaction = transactionRepository.findTransactionById(transactionId);
        Wallet wallet = walletRepository.findWalletById(walletId);
        if(!transaction.getWallet().getId().equals(wallet.getId())) throw new BadRequest("401 Exception ");
        if(paymentResult == PaymentResult.PAYED) {
            walletService.updateMoney(transaction.getWallet().getId() , transaction.getTotalMoney() , transaction.getTransactionType());
            transaction.setTransactionResult(TransactionResult.PAYED);
        }else {
            transaction.setTransactionResult(TransactionResult.EXPIRE);
        }
        if(transaction.getTransactionType() == TransactionType.PAY){
            if(transaction.getJobStage() != null ){
                JobStage jobStage = transaction.getJobStage();
                jobStage.setAvailableMoney(transaction.getMoney());
                jobStage.setJobStageStatus(JobStageStatus.INPROCESS);
                if(jobStage.getNoOfStage() == 1 ){
                    jobStage.getJob().setStatus(JobStatus.ACTIVE);
                    jobRepository.save(jobStage.getJob());
                }
                jobStageRepository.save(jobStage);
            }
            if(transaction.getApplyStage() != null ){
                ApplyStage applyStage = transaction.getApplyStage();
                JobStage jobStage = applyStage.getJobStage();
                jobStage.setAvailableMoney(jobStage.getAvailableMoney() - jobStage.getRewardPredictedForOne());
                jobStageRepository.save(jobStage);
            }
        }

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionWithdraw() {
        List<Transaction> transactions = transactionRepository.getAllTransactionWithdraw();
        return transactions.stream().sorted(Comparator.comparing(Transaction::getCreateDate).reversed()).collect(Collectors.toList());
    }
}
//    JobStage jobStage = transaction.getJobStage();
//            jobStage.setJobStageStatus(JobStageStatus.INPROCESS);
//                    jobStageRepository.save(jobStage);
//                    if(jobStage.getJob().getStatus() == JobStatus.PENDING){
//                    jobStage.getJob().setStatus(JobStatus.ACTIVE);
//                    jobRepository.save(jobStage.getJob());
//                    }