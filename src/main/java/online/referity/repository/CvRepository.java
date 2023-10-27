package online.referity.repository;

import online.referity.entity.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CvRepository extends JpaRepository<CV, UUID> {

    @Query("SELECT c FROM CV c WHERE c.candidate.id = ?1 and c.jobTitle.id = ?2 and c.status != 'OLD' ")
    CV findCandidate (UUID candidateId , UUID jobTitleId);

    CV findCVByCode (String Code);
    CV findCVById (UUID id);

    List<CV> findCvByCandidateId(UUID id);

    @Query("SELECT c FROM CV c JOIN c.cvSharedList cvs JOIN cvs.candidateApplies al JOIN al.job j WHERE j.id = ?1 AND (c.email = ?2 OR c.phone = ?3)")
    List<CV> getCVByJobAndEmailOrPhone(UUID jobId, String email, String phone);



}
