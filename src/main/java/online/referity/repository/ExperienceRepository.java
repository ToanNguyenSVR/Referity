package online.referity.repository;

import online.referity.entity.CandidateExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ExperienceRepository extends JpaRepository<CandidateExperience , UUID> {


    public List<CandidateExperience> findByCvId(UUID cvId);


}
