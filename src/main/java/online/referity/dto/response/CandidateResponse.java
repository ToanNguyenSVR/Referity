package online.referity.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import online.referity.dto.request.CandidateRequest;
import online.referity.dto.request.CvRequest;
import online.referity.entity.*;
import online.referity.enums.CvSharedStatus;
import online.referity.enums.CvStatus;
import online.referity.repository.CandidateRepository;
import online.referity.service.CandidateService;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CandidateResponse {

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

    private CvSharedStatus status ;

    private List<CandidateExperience> experiences;

    private List<Certification> certifications;

    private List<SkillLevel> skillLevels ;

    private String jobTitle ;

    private String workingMode ;

    private CvSharedResponse cvShared;

    private float score;

}
