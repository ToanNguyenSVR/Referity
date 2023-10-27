package online.referity.dto.request;

import lombok.Getter;
import lombok.Setter;
import online.referity.enums.Emailtype;

@Getter
@Setter

public class EmailRequest {


    private String subject;
    private String content;
    private String footer ;
    private  String greeting ;

    private Emailtype emailType ;
}
