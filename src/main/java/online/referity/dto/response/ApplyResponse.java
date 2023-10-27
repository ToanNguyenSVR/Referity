package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.enums.ResultStage;
@Getter
@Setter
public class ApplyResponse {

    private  String code ;

    private int persentProgess ;

    private Double totalReward ;

    private ResultStage resultApply  ;

    private CvResponse cv ;
}
