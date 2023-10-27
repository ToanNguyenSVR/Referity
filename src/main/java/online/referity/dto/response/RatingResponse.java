package online.referity.dto.response;

import lombok.Data;
import online.referity.enums.RatingStar;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class RatingResponse {
    private UUID id;

    private String comment ;

    private LocalDateTime createAt ;

    private int star ;

    private UUID headhunterID ;
    private String headhunterName ;
    private String headhunterAvatar ;

    private String candidateName ;
    private String candidateEmail ;


    private String companyName ;
    private String companyAvatar ;
    private String logoUrl;




}
