package online.referity.repository;

import online.referity.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill , UUID> {

     Skill findBySkillName(String skillName);

     Skill getById(UUID id);
     @Query(value = "SELECT s.id FROM Skill s ")
     List<UUID>getUUID ();
     Skill findSkillById(UUID id);

     @Query("Select s From Skill s Where s.skillName like %?1% ")
     List<Skill> search ( String key);
     Skill save(Skill skill);

}
