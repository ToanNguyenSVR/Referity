package online.referity.dto.request;

import lombok.Data;
import online.referity.enums.ValidateStatus;
@Data
public class UpdateLanguageRequest {

    private String language ;

    private ValidateStatus status ;
}
