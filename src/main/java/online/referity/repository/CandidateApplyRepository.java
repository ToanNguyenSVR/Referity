package online.referity.repository;


import online.referity.dto.response.CountCandidateApply;
import online.referity.entity.CandidateApply;

import online.referity.enums.ResultStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CandidateApplyRepository extends JpaRepository<CandidateApply , UUID> {
    @Query("SELECT ca FROM CandidateApply ca WHERE ca.cvShared.cv.id = ?1 AND ca.job.id = ?2 ")
    CandidateApply checkDuplicateApply (UUID cvId , UUID jobId );
    CandidateApply getByCode ( String code );

    @Query("SELECT COUNT(c) FROM CandidateApply c WHERE c.cvShared.headhunter.id = ?1 AND c.resultApply = 'PASS'")
    Integer countByHeadhunterId( UUID headhunterId);

//    @Query("SELECT ca FROM CandidateApply ca WHERE ca.cvShared.headhunter.id = ?1 AND ca.resultApply LIKE ?2 ")
//    List<CandidateApply> getByHeadhunterId (UUID headhunterId , ResultStage resultStage);
//    @Query("SELECT ca FROM CandidateApply ca WHERE ca.cvShared.cv.id = ?1 AND ca.resultApply LIKE ?2 ")
//    List<CandidateApply> getByCvId ( UUID cvId , ResultStage resultStage);
    @Query("SELECT ca FROM CandidateApply ca WHERE ca.job.id = ?1 AND ca.resultApply  LIKE ?2 ")
    List<CandidateApply> getByJobId (UUID jobId, ResultStage resultStage);

    @Query("SELECT ca FROM CandidateApply ca WHERE ca.resultApply = ?1 ")
    List<CandidateApply> getByJobId ( ResultStage resultStage);

    CandidateApply findCandidateApplyById(UUID id);

    @Query("SELECT ca FROM CandidateApply ca WHERE (:resultStage is null Or ca.resultApply = :resultStage ) AND (:headhunterId is null Or ca.cvShared.headhunter.id = :headhunterId ) AND (:candidateId is null Or ca.cvShared.cv.candidate.id = :candidateId )  ")
    List<CandidateApply> get ( @Param("resultStage") ResultStage resultStage , @Param("headhunterId") UUID headhunterId ,  @Param("candidateId") UUID candidateId  );

    @Query(value = "SELECT ca.resultApply as resultApply , COUNT(ca) as total FROM CandidateApply ca WHERE ca.job.companyStaff.company.id = :companyId AND ca.createAt > :fromTime AND ca.createAt < :toTime Group By ca.resultApply " )
    List<Object[]> countCandidateApplyByCompanyId (@Param("companyId") UUID companyId , @Param("fromTime")LocalDateTime fromTime , @Param("toTime") LocalDateTime toTime );
    @Query(value = "SELECT COUNT(ca) as total FROM CandidateApply ca WHERE ca.cvShared.headhunter.id = :headhunterId AND ca.createAt > :fromTime  " )
    int countApplyIn1MonthByHeadhunterId (@Param("headhunterId") UUID headhunterId , @Param("fromTime")LocalDateTime fromTime  );

}
