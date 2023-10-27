package online.referity.repository;

import online.referity.entity.LanguageLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LanguageLevelRepository extends JpaRepository<LanguageLevel , UUID> {

    List<LanguageLevel> findByCvId(UUID id);

    List<LanguageLevel> findByJobId(UUID id);
}
