package online.referity.repository;

import online.referity.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet , UUID> {
    Wallet findWalletById ( UUID id);
    @Query("SELECT w FROM Wallet w WHERE w.account.accountType = 'ADMIN' ")
    public  Wallet getAdminWallet();
}