package online.referity.dto.request;

import lombok.*;
import online.referity.entity.Certification;
import online.referity.entity.Skill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequest {

    @Column(nullable = false)
    private String fullName ;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b", message = "Invalid phone number")
    private String phone ;

    @Column(nullable = false , unique = true)
    @Email
    private String email ;

}
