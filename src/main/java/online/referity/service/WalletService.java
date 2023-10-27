package online.referity.service;

import online.referity.dto.request.WalletRequest;
import online.referity.dto.response.WalletResponse;
import online.referity.entity.Account;
import online.referity.entity.Transaction;
import online.referity.entity.Wallet;
import online.referity.enums.AccountType;
import online.referity.enums.TransactionType;

import java.util.UUID;

public interface WalletService {

    public Wallet initWallet(WalletRequest walletRequest, Account account);
    public Wallet initWallet(WalletRequest walletRequest);

    public Wallet updateMoney(UUID walletId, Double money, TransactionType transactionType );

    public Wallet updateWallet(UUID walletId, WalletRequest walletRequest);

    public Transaction acceptWithDraw(UUID walletId, UUID transactionId , String image_evident);
    public Transaction withDraw(UUID walletId,double money);
    public Transaction recharge(UUID walletId, double money);

    public Transaction pay_Transaction(UUID walletId , UUID transactionCode);
    public Transaction updateMoneyAdmin(TransactionType type , AccountType sender  , AccountType receiver ,  String content , double money);

}
