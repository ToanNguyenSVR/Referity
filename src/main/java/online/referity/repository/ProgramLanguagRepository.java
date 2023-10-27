package online.referity.repository;

import online.referity.entity.ProgramLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ProgramLanguagRepository  extends JpaRepository<ProgramLanguage , UUID> {

    ProgramLanguage findProgramLanguageById (UUID id);

    ProgramLanguage getById (UUID id);
    @Query("SELECT p.id FROM ProgramLanguage p ")
    List<UUID> getUUID();
    @Query("SELECT p FROM ProgramLanguage p WHERE p.language = ?1 ")
    ProgramLanguage checkDuplicate (String language);

    @Query("SELECT p FROM ProgramLanguage p WHERE p.language LIKE %?1% ")
    List<ProgramLanguage> searchLanguage(String key);
}
