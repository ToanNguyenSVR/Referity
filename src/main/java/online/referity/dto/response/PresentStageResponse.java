package online.referity.dto.response;

import lombok.Data;
import online.referity.entity.JobStage;

import java.util.UUID;

@Data
public class PresentStageResponse {

    private float rewardPercent ;
    private UUID stageId ;
    private String stageName ;
    private Double rewardPredictedForOne;

    private double returnMoney ;
    private double paidMoney ;
    private double blockMoney ;
    private double platformFee ;

    private int personTargetInStage ;
    private int cvPassedInStage ;
    private int cvFailInStage ;
    private int cvAppliedInStage ;
    private int cvInProcessInStage ;
}
