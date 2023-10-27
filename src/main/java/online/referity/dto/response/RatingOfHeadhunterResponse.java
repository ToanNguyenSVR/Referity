package online.referity.dto.response;

import lombok.Data;
import online.referity.enums.RatingStar;

import java.util.HashMap;
import java.util.List;

@Data
public class RatingOfHeadhunterResponse {

    private  float totalStar ;

    HashMap<Integer , Integer> mapStarPercent ;

    List<RatingResponse> ratingResponses;

}
