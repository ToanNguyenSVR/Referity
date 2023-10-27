package online.referity.dto.request;

import lombok.Data;
import online.referity.enums.ResultApply;

import java.util.UUID;

@Data
public class AcceptApplyRequest {
    private UUID accountCompanyId ;
    private String note;
    private  ResultApply resultApply;

}
