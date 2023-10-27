package online.referity.dto.request;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Date;
@Data
public class CertificationRequest {
    @Column(nullable = false, unique = true)
    private String certificationName ;
    @Column(name = "valueFrom")
    private LocalDateTime dateFrom ;
    @Column(name = "valueTo")
    private LocalDateTime dateTo ;

    @Column(nullable = false, unique = true)
    private String certificationUrl ;

    private String organization ;
}
