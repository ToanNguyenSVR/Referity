package online.referity.repository;

import online.referity.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating , UUID> {

    List<Rating> findAllByHeadhunterId (UUID headhunterId);
    List<Rating> findAllByCompanyId (UUID companyId);

    List<Rating> findByCandidateApplyId(UUID candidateApplyId);
    @Query("SELECT CAST(SUM(r.star)/COUNT(r) AS float) FROM Rating r WHERE r.headhunter.id = ?1 ")
    float countHeadhunterRating (UUID HeadhunterID);


}
