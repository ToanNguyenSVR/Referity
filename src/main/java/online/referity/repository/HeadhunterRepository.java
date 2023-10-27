package online.referity.repository;

import online.referity.dto.response.TopHeadhunterResponse;
import online.referity.entity.Headhunter;
import online.referity.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface HeadhunterRepository extends JpaRepository<Headhunter, UUID> {


    Headhunter findHeadhunterById(UUID id) ;

    @Query("SELECT h FROM Headhunter h WHERE h.id = :uuid")

    Headhunter findByUuid(UUID uuid);

    @Query("SELECT h FROM Headhunter h WHERE h.account.status = ?1  ")
    List<Headhunter> getHeadhunter(AccountStatus accountStatus);
//
//    List<Headhunter> getHeadhunterActive();
//    @Query("SELECT h.id , h.description , h.ac , h.account.fullName as fullName , " +
//            " COUNT(h.rating) AS num_candidates_passed" +
//            " FROM Headhunter h " +
//            " GROUP BY COUNT(h.CvShared.CandidateApply) " +
//            " HAVING h.CvShared.CandidateApply.resultApply = 'PASS' ")
//    List<TopHeadhunterResponse> getAllHeadhunter();

    List<Headhunter> findAll ();

}
