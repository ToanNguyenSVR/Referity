package online.referity.repository;

import online.referity.entity.RecruitStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface RecruitStageRepository extends JpaRepository<RecruitStage , UUID> {

    List<RecruitStage> findRecruiterStagesByIsDeletedFalse();

}
