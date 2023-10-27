package online.referity.repository;

import online.referity.entity.Account;
import online.referity.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query("SELECT u FROM Account u WHERE u.phone = ?1 OR u.email = ?1")
    Account findByUsernameOrPhoneOrEmail(String usernameOrPhoneOrEmail);
    @Query("SELECT u FROM Account u WHERE u.phone = ?1 OR u.email = ?1 AND u.accountType = 'CANDIDATE'")
    Account checkDuplicateCandidate( String email , String phone ) ;
    Account findAccountById(UUID id);

    @Query("SELECT u FROM Account u WHERE u.companyStaff.company.id = ?1 AND u.accountType = 'MANAGER'")
    Account getManagerByCompaniId( UUID companyId);
    @Query("SELECT count(u) FROM Account u WHERE  u.accountType = ?1")

    int countAccountByRole (AccountType accountType);




}
