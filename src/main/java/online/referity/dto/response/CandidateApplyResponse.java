package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.enums.ResultStage;

@Setter
@Getter
public class CandidateApplyResponse {

    private  String code ;

    private int persentProgess ;

    private Double totalReward ;

    private ResultStage resultApply  ;

    private ReferJobResponse job;

    private  ReferCvResponse cv;

}

