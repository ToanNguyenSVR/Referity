package online.referity.repository;

import online.referity.entity.SkillGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SkillGroupRepository extends JpaRepository<SkillGroup , UUID> {

    SkillGroup getById(UUID id);
    @Query("SELECT g FROM SkillGroup g WHERE g.groupName LIKE  %?1% ")
    List<SkillGroup> search(String key );
}
