package online.referity.dto.request;

import lombok.Getter;
import online.referity.enums.NoOfStage;

@Getter
public class RecruitStageRequest {

    private int noOfStage ;

    private String nameProcess ;

    private String description ;

    private int baseStageRewardPresent ;
}
