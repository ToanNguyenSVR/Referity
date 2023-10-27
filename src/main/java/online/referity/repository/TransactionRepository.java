package online.referity.repository;

import online.referity.entity.Transaction;
import online.referity.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction , UUID> {

    List<Transaction> findTransactionByWalletId (UUID walletId);

    @Query("SELECT t FROM Transaction t WHERE t.transactionType = 'WITHDRAW'")
    List<Transaction> getAllTransactionWithdraw ();
    Transaction findTransactionById (UUID transactionId);
    @Query("SELECT t FROM Transaction t WHERE t.jobStage.id  = ?1 AND t.transactionType= ?2 ")

    Transaction findByJobStageId (UUID jobStageId , TransactionType transactionType);

    @Query("SELECT SUM(t.money) FROM Transaction t WHERE t.wallet.id = ?1 AND t.transactionType = 'PAY'")
    double countTotalMoneyPay(UUID companyWalletId);

    @Query("SELECT SUM(t.platformFee/100*t.money) FROM Transaction t WHERE t.transactionType = 'PAY'")
    double countTotalRevenueOfSystem();

    @Query("SELECT SUM(t.platformFee/100*t.money) FROM Transaction t WHERE t.transactionType = 'PAY' AND t.createDate > :startTime AND t.createDate < :endTime ")
    Double countTotalRevenueByDate(@Param("startTime") LocalDateTime startTime , @Param("endTime") LocalDateTime endTime);


}
