package online.referity.dto.request;

import lombok.Data;
import lombok.Getter;
import online.referity.dto.response.TransactionResponse;
import online.referity.entity.JobStage;
import online.referity.enums.JobStageStatus;
import org.mapstruct.Mapping;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Data
public class JobStageRequest {

    private UUID recruitStageId;

    private String recruitStageName;

    @Enumerated(EnumType.STRING)
    private JobStageStatus jobStageStatus;

    private UUID stageId  ;

    private int noOf = 0;

    private float rewardPercent ;

    private int personQuantity ;

    private Date finishDate ;

    private Date createDate ;

    private double totalPayOfStage ;

    private List<TransactionResponse> transactionResponses;
    
}
