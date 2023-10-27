package online.referity.dto.request;

import lombok.Data;
import online.referity.enums.RequestStatus;
import online.referity.enums.RequestType;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CreateRequest {
    private String requestTitle ;
    private String requestContent ;
    private RequestType requestType ;

    @Email
    private List<String> emailTo ;

    private UUID cvSharedId ;
}
