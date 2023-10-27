package online.referity.dto.response;

import lombok.Data;
import online.referity.enums.ResultStage;

import java.util.UUID;

@Data
public class CandidateApplyReport {


    private UUID candidateApplyId;

    private  String nameStage ;

    private String headhunterName ;

    private boolean isRated = true ;

    private ResultStage status ;

    private String comment ;

    private String candidateAvatar ;

    private String candidateName ;


}
