package online.referity.repository;

import online.referity.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification , UUID> {

    @Query("SELECT n FROM Notification n WHERE n.account.id = ?1 ")
    List<Notification> getbyAccountId (UUID accountId);

    @Query("SELECT n FROM Notification n WHERE n.account.id = ?1 AND n.notiStatus = 'NEW'")
    List<Notification> getbyAccount (UUID accountId);
    @Query("Update Notification n SET n.notiStatus = 'OLD' WHERE n.account.id = :accountId ")
    int updateStatus(@Param("accountId") UUID accountId);

}
