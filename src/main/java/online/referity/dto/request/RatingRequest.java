package online.referity.dto.request;

import lombok.Data;
import online.referity.enums.RatingStar;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RatingRequest {

    private String comment ;

    private int ratingStar ;

    private UUID candidateApplyId;



}
