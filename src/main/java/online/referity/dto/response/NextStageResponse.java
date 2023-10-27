package online.referity.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class NextStageResponse {

    private int noOfStage;

    private UUID stageId ;
    private String stageName ;
    private float rewardPercent;

    private Double rewardPredictedForOne;

    private Double rewardPredictedForALL;

    private int personQuantity;

    private double totalMoney ;

    private int  platformFee ;


}
