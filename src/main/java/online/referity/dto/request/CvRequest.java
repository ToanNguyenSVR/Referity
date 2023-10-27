package online.referity.dto.request;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;

@Data
public class CvRequest {

    @Column(nullable = false)
    private String cvUrl;

    @Column(nullable = false, unique = true)
    private String linkedInLink;

    @Column(nullable = false, unique = true)
    private String facebookLink;

    @Column(nullable = false, unique = true)
    private String githubLink;

    @Size(max = 500, message = "Summary must not exceed 500 characters")
    private String summary;

    @Size(max = 500, message = "Education must not exceed 500 characters")
    private String education;

    @NotNull(message = "Full name cannot be null")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 200, message = "Address must not exceed 200 characters")
    private String address;

    @Pattern(regexp = "\\d{10,12}", message = "Phone number must be 10 to 12 digits")
    private String phone;

    private List<CertificationRequest> certificationRequestList;

    private List<ExperienceRequest> experienceRequestList;

    private List<SkillListRequest> skillList;

    private List<LanguageRequest> languageRequests;

}
