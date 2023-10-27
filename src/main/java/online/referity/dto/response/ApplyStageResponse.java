package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.entity.Account;
import online.referity.entity.Headhunter;
import online.referity.enums.ResultStage;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Getter
@Setter
public class ApplyStageResponse {

    private UUID id ;

    private String code ;

    private String note ;
    @Enumerated(EnumType.STRING)
    private ResultStage status ;

    private CvResponse cv ;
    private Account headhunter;
}
