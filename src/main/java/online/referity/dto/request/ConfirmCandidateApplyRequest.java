package online.referity.dto.request;

import lombok.Getter;
import lombok.Setter;
import online.referity.enums.ResultApply;

@Getter
@Setter
public class ConfirmCandidateApplyRequest {

    String applyCode ;
    String note;
    ResultApply resultApply ;
}
