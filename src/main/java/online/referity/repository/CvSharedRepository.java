package online.referity.repository;

import online.referity.entity.CvShared;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

public interface CvSharedRepository extends JpaRepository<CvShared , UUID> {

    @Query("SELECT c FROM CvShared c Where c.cv.id = ?1 AND c.headhunter.id = ?2 AND c.status = 'ACTIVE' ")
    public CvShared findByCvIdAndHeadhunterId (UUID cvId , UUID headhunterId);

    @Query("SELECT c FROM CvShared c Where c.headhunter.id = ?1 AND c.status != 'VERIFY'   ")
    public List<CvShared> findByHeadhunterId ( UUID headhunterId );

    @Query(value = "SELECT c FROM CvShared c  Where c.headhunter.id = ?1 AND c.id Not In (SELECT ca.cvShared.id  From CandidateApply ca WHERE ca.job.id = ?2) AND c.status != 'VERIFY' AND c.status != 'HAVEJOB' ")
    public List<CvShared> findByHeadhunterId ( UUID headhunterId , UUID jobId );
    @Query("SELECT c FROM CvShared c Where c.cv.candidate.id = ?1")
    public List<CvShared> findByCandidateId ( UUID candidate);

    @Query("SELECT c FROM CvShared c Where c.headhunter.id = ?1 AND c.status = 'VERIFY' ")
    public List<CvShared> findVerifyByHeadhunterId ( UUID headhunterId);

    @Query("SELECT c FROM CvShared c Where c.cv.candidate.id = ?1 AND c.status = 'VERIFY' ")
    public List<CvShared> findCvVerifyWithCandidate (UUID candidateId);
    @Query("SELECT c FROM CvShared c Where  c.status = 'VERIFY' ")
    public List<CvShared> findCvVerify ();
    public CvShared getCvSharedById (UUID cvSharedId);

    @Query("SELECT c FROM CvShared c Where  c.cv.id = ?1 ")
    public List<CvShared> getCVSharedByCVId(UUID cvId);
}
