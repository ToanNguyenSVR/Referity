package online.referity.dto.request;

import lombok.Data;
import online.referity.entity.Campus;
import online.referity.entity.Company;
import online.referity.enums.JobLevel;
import online.referity.enums.JobStatus;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
public class  CreateJobRequest {
    UUID id;
    private UUID assignTo;
    private String title;
    private String summary;
    private JobLevel level;
    private int salaryForm;
    private int salaryTo;
    private int reward;
    private String image;
    private int employee_quantity;
    private String responsibility;
    private String jobDescription;
    private String requirement;
    private String benefit;
    private JobStatus status;
    private List<SkillListRequest> skillList; //
    private List<LanguageRequest> languageRequests; //
    private UUID staff;
    private LocalDateTime createDate;
    private Company company;

    private UUID workingMode; //
    private UUID jobTitle; //
    private UUID campus; //
    private String campusName; //
    private List<JobStageRequest> recruiterStages; //

}
