package online.referity.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ExtendRequest {

    private UUID cvSharedId ;

    private boolean isAccept ;
}
