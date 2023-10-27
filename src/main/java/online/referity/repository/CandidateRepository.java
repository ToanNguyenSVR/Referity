package online.referity.repository;

import online.referity.entity.CV;
import online.referity.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

    @Query("SELECT c FROM Candidate c where c.account.email = ?1")
    Candidate findCandidateByEmail(String email);

//    CV getById(UUID id);


}
