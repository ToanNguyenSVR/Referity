package online.referity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.referity.entity.JobStage;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmBillStageResponse {

    private PresentStageResponse presentStageResponse;

    private NextStageResponse nextStageResponse ;

    private boolean isClose ;


}
