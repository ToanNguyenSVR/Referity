package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.dto.request.JobStageRequest;
import online.referity.dto.request.LanguageRequest;
import online.referity.dto.request.SkillListRequest;
import online.referity.enums.JobLevel;
import online.referity.enums.JobStatus;

import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class JobActiveResponse {
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
    private UUID workingMode; //
    private UUID jobTitle; //
    private UUID campus; //
    private  List<JobStageResponse> stageResponseList ;

}
