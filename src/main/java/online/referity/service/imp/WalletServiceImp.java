package online.referity.service.imp;

import com.google.type.Money;
import online.referity.dto.request.WalletRequest;
import online.referity.dto.response.WalletResponse;
import online.referity.entity.Account;
import online.referity.entity.Transaction;
import online.referity.entity.Wallet;
import online.referity.enums.*;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.AccountRepository;
import online.referity.repository.TransactionRepository;
import online.referity.repository.WalletRepository;
import online.referity.service.WalletService;
import org.apache.tomcat.jni.Time;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class WalletServiceImp  implements WalletService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    WalletRepository walletRepository ;

    @Autowired
    TransactionRepository transactionRepository ;


    ModelMapper modelMapper = new ModelMapper();

    @Value("${my.property.walletId}")
    private UUID walletAdminId;

    @Override
    @Transactional
    public Wallet initWallet(WalletRequest walletRequest , Account account) {
        try{

            if(account == null  ) throw  new EntityNotFound("Account not found ");
            if( account.getAccountType() == AccountType.CANDIDATE
                    || account.getAccountType() == AccountType.STAFF) throw  new BadRequest("Account not available to add information wallet please try again ");
            if(account.getWallet() != null ) throw  new BadRequest("Account already have wallet " );
            Wallet wallet = modelMapper.map(walletRequest, Wallet.class);
            wallet.setId(UUID.randomUUID());
            wallet.setAccount(account);
            wallet.setStatus(WalletStatus.ACTIVE);
            wallet.setCreateDate(Date.from(Instant.now()));
            wallet = walletRepository.save(wallet);
            account.setWallet(wallet);
            return wallet;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Wallet initWallet(WalletRequest walletRequest) {
        Account account = accountRepository.findAccountById(UUID.fromString(walletRequest.getAccountId()));
        if(account == null  ) throw  new EntityNotFound("Account not found ");
        if( account.getAccountType() == AccountType.CANDIDATE
                || account.getAccountType() == AccountType.STAFF) throw  new BadRequest("Account not available to add information wallet please try again ");
        if(account.getWallet() != null ) throw  new BadRequest("Account already have wallet " );
        Wallet wallet = modelMapper.map(walletRequest, Wallet.class);
        wallet.setId(UUID.randomUUID());
        wallet.setAccount(account);
        wallet.setStatus(WalletStatus.ACTIVE);
        wallet.setCreateDate(Date.from(Instant.now()));
        wallet = walletRepository.save(wallet);
        account.setWallet(wallet);
        return wallet;
    }

    @Override
    @Transactional
    public Wallet updateMoney(UUID walletId, Double money, TransactionType transactionType ) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId) ;
        boolean headhunter = false;
        if(!walletOptional.isPresent()) throw new BadRequest("Server error about wallet not found ") ;
        Wallet wallet = walletOptional.get();
        if(wallet.getAccount().getAccountType() == AccountType.HEADHUNTER){
            headhunter = true ;
            // Là Headhunter đó hihi
        }
        Double blockMoney = wallet.getBlockMoney();
        Double balance = wallet.getBalance();
        switch (transactionType){
            case PlUS_MONEY:
                if(headhunter) {
                    if( blockMoney >= money ) {
                        blockMoney = blockMoney - money ;
                    }
                    else throw new BadRequest("Server Error block money not enough money ") ;
                }
                balance = balance + money ;
                break;
            case WITHDRAW:
                if( balance >= money ) {
                    balance = balance - money ;
                }
                else throw new BadRequest("Server Error balance  not enough money ") ;
                break;
            case PAY:
                if (headhunter){
                   balance = balance + money;
                   break;
                }
                if(balance < money) throw new BadRequest("Your Balance not enough money");
                balance = balance - money;
                blockMoney = blockMoney + money;
                break;
            case RETURN:
                if (headhunter) break;
                blockMoney = blockMoney - money;
                balance += money;
                break;
            default:
                throw new BadRequest("Error type of transaction ");

        }
        wallet.setBalance(balance);
        wallet.setBlockMoney(blockMoney);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet updateWallet(UUID walletId, WalletRequest walletRequest) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId) ;
        boolean headhunter = false;
        if(!walletOptional.isPresent()) throw new BadRequest("Server error about wallet not found ") ;
        Wallet wallet = walletOptional.get();
        modelMapper.map(walletRequest, wallet);
        return walletRepository.save(wallet);
    }

    @Override
    public Transaction withDraw(UUID walletId, double money) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId) ;
        if(!walletOptional.isPresent()) throw  new BadRequest("Wallet not found ");
        Wallet wallet = walletOptional.get();
        if(money > wallet.getBalance())  throw  new BadRequest("Your Wallet don't have enough money");
        Transaction transaction = new Transaction();
        transaction.setMoney(money);
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setWallet(wallet);
        transaction.setCode("WD" );
        transaction.setSenderCode("SYSTEM");
        transaction.setTotalMoney(money);
        transaction.setPlatformFee(0.0);
        transaction.setTransferContent("With draw monet to " + wallet.getAccount().getAccountType() );
        transaction.setReceiverCode(wallet.getAccount().getFullName());
        transaction.setTransactionResult(TransactionResult.ACTIVE);
        transaction.setExpireDay(LocalDateTime.now().plus(15, ChronoUnit.DAYS));
        transaction.setCreateDate(LocalDateTime.now());
        transaction.setCreateBy("SYSTEM");
        transaction = transactionRepository.save(transaction);
        return transaction ;
    }
    @Override
    public Transaction acceptWithDraw(UUID walletId, UUID transactionId , String image_evident) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId) ;
        if(!walletOptional.isPresent()) throw  new BadRequest("Wallet not found ");
        Wallet wallet = walletOptional.get();
        if(wallet.getAccount().getAccountType() != AccountType.ADMIN) throw  new BadRequest("You don't have permission ");
        Transaction transaction = transactionRepository.findTransactionById(transactionId);
        if(transaction == null) throw  new BadRequest("Transaction not found ");
        transaction.getWallet().setBalance(transaction.getWallet().getBalance() - transaction.getMoney());
        transaction.setTransactionResult(TransactionResult.PAYED);
        transaction.setImage_evident(image_evident);
        updateMoneyAdmin(transaction.getTransactionType() , AccountType.ADMIN, transaction.getWallet().getAccount().getAccountType() , "Accept with draw" , transaction.getTotalMoney());
        return  transactionRepository.save(transaction);

    }

    @Override
    public Transaction recharge(UUID walletId, double money) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId) ;
        if(!walletOptional.isPresent()) throw  new BadRequest("Wallet not found ");
        Optional<Wallet> walletAdminOptional = walletRepository.findById(walletId) ;
        Wallet wallet = walletOptional.get();
        Transaction transaction = new Transaction();
        transaction.setMoney(money);
        transaction.setTransactionType(TransactionType.PlUS_MONEY);
        transaction.setWallet(wallet);
        transaction.setCode("RC0 "+ LocalDate.now().toString() );
        transaction.setSenderCode("SYSTEM ");
        transaction.setTotalMoney(money);
        transaction.setPlatformFee(0.0);
        transaction.setTransferContent("Recharge money to " );
        transaction.setReceiverCode(wallet.getAccount().getFullName());
        transaction.setTransactionResult(TransactionResult.PAYED);
//        transaction.setExpireDay(LocalDateTime.now().plus(15, ChronoUnit.DAYS));
        transaction.setCreateDate(LocalDateTime.now());
        transaction.setCreateBy("SYSTEM");
        transaction = transactionRepository.save(transaction);
        wallet.setBalance(wallet.getBalance()+ money);
        updateMoneyAdmin(transaction.getTransactionType(), AccountType.COMPANY , AccountType.ADMIN , "Nạp Tiền Vào Hệ Thống " , transaction.getTotalMoney());
        // thông báo đến company email và noti
        walletRepository.save(wallet);
        return  transaction ;

    }

    @Override
    public Transaction pay_Transaction(UUID walletId, UUID transactionId) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId) ;
        if(!walletOptional.isPresent()) throw  new BadRequest("Wallet not found ");
        Wallet wallet = walletOptional.get();
//        Transaction transaction = ;
        return null ;
    }

    public Transaction updateMoneyAdmin( TransactionType type , AccountType sender  ,AccountType receiver ,String content , double money){
        Wallet wallet = walletRepository.getAdminWallet();
        if(wallet == null ) throw  new BadRequest("Wallet not found ");
        Transaction transaction = new Transaction();
        transaction.setMoney(money);
        transaction.setTotalMoney(money);
        transaction.setTransactionType(type);
        transaction.setWallet(wallet);
        transaction.setPlatformFee(0.0);
        transaction.setCode("RC0"+ System.currentTimeMillis());
        transaction.setSenderCode(sender +" ");
        transaction.setTransferContent(content);
        transaction.setReceiverCode(receiver + " ");
        transaction.setTransactionResult(TransactionResult.PAYED);
//        transaction.setExpireDay(LocalDateTime.now().plus(15, ChronoUnit.DAYS));
        transaction.setCreateDate(LocalDateTime.now());
        transaction.setCreateBy("SYSTEM");
        Double blockMoney = wallet.getBlockMoney();
        Double balance = wallet.getBalance();
        switch (type){
            case PlUS_MONEY:
                balance = balance + money ;
                break;
            case WITHDRAW :
                balance = balance - money ;
                break;
            case PAY:
                if(sender == AccountType.ADMIN) balance = balance - money;
                if(sender == AccountType.COMPANY) balance = balance + money;
                break;
            case RETURN:
                balance = balance - money ;
                break;
            default:
                throw new BadRequest("Error type of transaction ");

        }
        wallet.setBalance(balance);
        wallet.setBlockMoney(blockMoney);
        walletRepository.save(wallet);
        return transactionRepository.save(transaction);
    }
}
