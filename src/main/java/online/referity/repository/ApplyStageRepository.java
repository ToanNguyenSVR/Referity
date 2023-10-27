package online.referity.repository;

import online.referity.entity.ApplyStage;
import online.referity.enums.ResultStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ApplyStageRepository extends JpaRepository<ApplyStage , UUID> {
    @Query("SELECT ap FROM ApplyStage ap WHERE ap.jobStage.id = ?1 ")
    List<ApplyStage> getApplyByJobStage(UUID jobId);
    @Query("SELECT ap FROM ApplyStage ap WHERE ap.jobStage.id = ?1 AND ap.status = ?2 ")
    List<ApplyStage> getApplyByJobStage(UUID jobId , ResultStage resultStage);
    @Query("SELECT ap FROM ApplyStage ap WHERE ap.jobStage.id = ?1 AND ap.status != ?2 ")
    List<ApplyStage> getApplyDifferenceResultStage(UUID jobId , ResultStage resultStage);
    @Query("SELECT ap FROM ApplyStage ap WHERE ap.id = ?1 ")
    ApplyStage getApplyStageById(UUID applyId );
}
