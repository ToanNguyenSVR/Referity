package online.referity.dto.request;

import lombok.Data;
import online.referity.enums.Gender;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.Date;

@Data
public class HeadhunterAdditionalInformation {
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String citizenIdentification;
    private LocalDate citizenDate;
    private String address;

    private String description;

    private Date createDate ;

    private String beneficiaryAccount ;

    private String beneficiaryName ;

    private String beneficiaryBank ;
}
