package online.referity.service;

import online.referity.dto.request.JobStageRequest;
import online.referity.dto.request.UpdateJobStageRequest;
import online.referity.dto.response.ConfirmBillStageResponse;
import online.referity.entity.ApplyStage;
import online.referity.entity.JobStage;
import online.referity.entity.Transaction;
import online.referity.enums.ResultApply;
import online.referity.enums.ResultStage;
import online.referity.enums.SetUpStage;

import java.util.List;
import java.util.UUID;

public interface JobConfigService {
    public JobStage createJobStage (JobStageRequest jobStageRequest , UUID jobId);
    public JobStage updateJobStage(JobStageRequest jobStageRequest);
//    public JobStage updateStatus(UUID jobStageId, UUID jobId ,SetUpStage status);

    public ApplyStage acceptApply(UUID applyId , ResultApply resultApply , UUID accountCompanyId , String note);
    public ConfirmBillStageResponse  getBillOfStage (UUID jobStageId);

    public JobStage startStage(UUID jobStageId , UUID accountId );


//    public String predictionReward (UUID jobId , int predictionPercent);

}
