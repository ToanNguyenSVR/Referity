package online.referity.dto.request;

import lombok.Data;

import java.util.UUID;

@Data

public class CvSharedRequest {

    private UUID cvId ;

    private UUID headhunterId ;
}
