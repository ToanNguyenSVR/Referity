package online.referity.repository;

import online.referity.entity.SkillLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SkillLevelRepository extends JpaRepository<SkillLevel, UUID> {

      @Query("SELECT sl FROM SkillLevel sl WHERE  ( sl.cv.id = ?1   OR sl.job.id = ?3) AND  sl.skill.id = ?2 ")
      SkillLevel checkDuplicate(UUID cvId , UUID skillId , UUID jobId );
      @Query("Update SkillLevel  s set s.point = ?2  where s.id = ?1 ")
     SkillLevel updatePoint (UUID skillLevelId  , int point);

    SkillLevel findSkillLevelById (UUID skillLevelId );

    List<SkillLevel> findByCvId (UUID cvId);
    List<SkillLevel> findByJobId (UUID jobId);

}
