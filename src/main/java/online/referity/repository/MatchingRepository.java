package online.referity.repository;

import online.referity.entity.MatchingScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MatchingRepository extends JpaRepository<MatchingScore, UUID> {
    public MatchingScore findMatchingScoreByJobIdAndCvId(UUID jobId, UUID cvId);
    public List<MatchingScore> findMatchingScoreByJobId(UUID jobId);

    @Query("SELECT ms " +
            "FROM MatchingScore ms " +
            "JOIN ms.cv cv " +
            "JOIN cv.cvSharedList cs " +
            "WHERE cs.headhunter.id = :headhunterId AND ms.score > 50 " +
            "AND ms.job.status = 'ACTIVE' "+
            "AND cv.candidate.status != '3' "+
            "GROUP BY cv.id, ms "+
            "ORDER BY ms.score DESC")
    public List<MatchingScore> findMatchingScoreBy(@Param("headhunterId") UUID headhunterId);

}
