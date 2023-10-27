package online.referity.dto.request;

import lombok.Getter;

import java.util.Date;
@Getter
public class UpdateJobStageRequest {

    private float rewardPercent ;

    private int personQuantity ;

    private Date finishDate ;
}
