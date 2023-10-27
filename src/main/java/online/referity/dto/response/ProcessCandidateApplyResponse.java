package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.dto.request.CreateJobRequest;
import online.referity.entity.CV;
import online.referity.enums.ResultStage;

@Getter
@Setter
public class ProcessCandidateApplyResponse {

    CreateJobRequest jobResponse ;

    int stageIn ;

    ResultStage resultStage = ResultStage.CONFIRM ;

    String code ;


    CV cv;

}
