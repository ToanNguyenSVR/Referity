package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UpdateRequest {

    private UUID newCvId ;

    private UUID cvSharedId ;

    private boolean isAccpet ;
}
