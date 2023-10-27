package online.referity.dto.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
@Data
public class ExperienceRequest {


    @Column(nullable = false, unique = true)
    private String company ;
    @Column(name = "valueFrom")
    private LocalDateTime dateFrom ;
    @Column(name = "valueTo")
    private LocalDateTime dateTo ;

    @Column(nullable = false, unique = true)
    private String jobTitle ;

    @Column(nullable = false, unique = true)
    private  String jobDescription ;

}
