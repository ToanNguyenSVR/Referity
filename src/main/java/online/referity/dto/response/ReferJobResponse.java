package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.enums.JobLevel;
@Setter
@Getter
public class ReferJobResponse {

    String  title ;
    String summary ;

    JobLevel level ;

    int reward ;

    String image ;
}
