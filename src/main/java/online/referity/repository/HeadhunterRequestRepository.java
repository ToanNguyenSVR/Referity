package online.referity.repository;

import online.referity.entity.HeadhunterRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface HeadhunterRequestRepository  extends JpaRepository<HeadhunterRequest , UUID> {

//        HeadhunterRequest findByUuid(UUID uuid);

    HeadhunterRequest findHeadhunterRequestById(UUID id);
    List<HeadhunterRequest> getByCandidateId(Sort sort, UUID candidateId);
    @Query("SELECT hr FROM HeadhunterRequest hr WHERE hr.headhunter.id = ?1 ")
    List<HeadhunterRequest> getByHeadhunterId(Sort sort,UUID headhunterId);

}