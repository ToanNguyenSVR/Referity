package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.entity.ApplyStage;
import online.referity.enums.JobStageStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class JobStageResponse {
    private UUID recruitStageId;

    private UUID stageId ;
    private String stageName  ;

    private int noOfStage;

    private float rewardPercent ;

    private Double rewardPredictedForOne ;

    private Double rewardPredictedForALL ;

    private int personQuantity ;

    private double availableMoney ;

    @Enumerated(EnumType.STRING)
    private JobStageStatus jobStageStatus;

    private Date finishDate ;

    private Date createDate ;


    List<ApplyStageResponse> applyStages;
}
