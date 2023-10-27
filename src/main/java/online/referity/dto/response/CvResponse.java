package online.referity.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import online.referity.entity.*;
import online.referity.enums.CandidataStatus;
import online.referity.enums.CvSharedStatus;
import online.referity.enums.CvStatus;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CvResponse {

    private UUID id ;

    private String code ;

    private String fullName ;

    private String phone ;

    private String email ;

    private String avatar ;

    private String cvUrl ;

    private String linkedInLink ;

    private String facebookLink ;

    private String githubLink ;

    private String summary ;

    private String education ;

    private LocalDateTime createDate ;

    private CvStatus status ;

    private List<CandidateExperience> experiences;

    private List<Certification> certifications;

    private List<SkillLevel> skillLevels ;
    private List<LanguageLevel> languageLevels ;

    private String jobTitle ;

    private String workingMode ;


}
