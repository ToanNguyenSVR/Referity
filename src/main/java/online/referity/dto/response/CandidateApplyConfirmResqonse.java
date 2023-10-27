package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.dto.request.CreateJobRequest;
import online.referity.enums.JobLevel;

import java.util.List;
@Getter
@Setter
public class CandidateApplyConfirmResqonse {
    CreateJobRequest jobResponse;
    List<ApplyResponse> applyResponses ;
}
