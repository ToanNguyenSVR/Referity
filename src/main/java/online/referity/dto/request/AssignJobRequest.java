package online.referity.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AssignJobRequest {
    private UUID accountManagerId ;
    private UUID accountStaffId;
    private UUID jobId;

}
