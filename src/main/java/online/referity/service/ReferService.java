package online.referity.service;

//import online.referity.dto.response.CandidateApplyByJobIdResponse;
import online.referity.dto.response.CandidateApplyConfirmResqonse;
import online.referity.dto.response.CandidateApplyResponse;
import online.referity.dto.response.JobActiveResponse;
import online.referity.dto.response.ProcessCandidateApplyResponse;
import online.referity.entity.ApplyStage;
import online.referity.entity.CandidateApply;
import online.referity.enums.ResultApply;
import online.referity.enums.ResultStage;

import java.util.List;
import java.util.UUID;

public interface ReferService {
    CandidateApplyResponse referCvToJob (UUID cvsharedId , UUID jobId);

    public CandidateApply confirmApply(String applyCode, ResultApply resultStage , String note );

    public List<ProcessCandidateApplyResponse> get(ResultStage resultStage, UUID headhunterId, UUID jobId, UUID cvId , UUID candidateId);
    List<ApplyStage> getApplyStage (ResultStage resultStage  , UUID jobId );
    public JobActiveResponse getApplyStage(UUID jobId);
    public CandidateApplyConfirmResqonse getCandidateApply( UUID jobId);

    ApplyStage acceptApply ( String applyStageCode , UUID companyStaffId);




}
