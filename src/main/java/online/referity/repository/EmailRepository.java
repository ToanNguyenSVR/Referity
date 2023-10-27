package online.referity.repository;

import online.referity.entity.TemplateEmail;
import online.referity.enums.Emailtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmailRepository extends JpaRepository<TemplateEmail, Integer> {

    @Query("SELECT e FROM TemplateEmail e WHERE e.emailType = ?1 ")
    TemplateEmail getByType (Emailtype emailtype);
}
