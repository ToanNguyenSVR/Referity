package online.referity.repository;

import online.referity.entity.JobStage;
import online.referity.enums.JobStageStatus;
import online.referity.enums.NoOfStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;


public interface JobStageRepository extends JpaRepository<JobStage , UUID> {

    @Query("SELECT SUM(js.rewardPercent) FROM  JobStage js WHERE js.job.id = ?1 ")
    int getlostPercent(UUID jobId);

    @Query("SELECT st FROM JobStage st WHERE st.job.id = ?1 AND st.jobStageStatus = ?2 AND st.noOfStage = ?3  ")
    JobStage getByJobId(UUID jobId , JobStageStatus jobStageStatus , int noOfStage);
    @Query("SELECT st FROM JobStage st WHERE st.job.id = ?1 AND st.jobStageStatus = 'CLOSE' ORDER BY st.finishDate ASC")
    JobStage getStageLastClose(UUID jobId);

    JobStage findByJobIdAndNoOfStage (UUID jobId , int noOfStage);
    JobStage findByJobIdAndRecruitStageId (UUID jobId , UUID RecruitStageId);
}
