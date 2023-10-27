package online.referity.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import online.referity.enums.AccountStatus;
import online.referity.enums.Gender;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter
public class HeadhunterResponse {

    private UUID id;
    private UUID accountId;

    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID uuid;

    private String code ;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String citizenIdentification;
    private LocalDate citizenDate;
    private String address;
    private String description;
    private String citizensIdentityFront ;
    private String citizensIdentityBack ;

    private String email;

    private String avatar;

    private String fullName;

    private String phone;

    private AccountStatus status;
}
