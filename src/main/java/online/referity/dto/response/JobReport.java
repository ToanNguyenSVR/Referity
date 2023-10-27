package online.referity.dto.response;

import lombok.Data;
import online.referity.dto.request.CreateJobRequest;
import online.referity.dto.request.JobStageRequest;
import online.referity.dto.request.LanguageRequest;
import online.referity.dto.request.SkillListRequest;
import online.referity.entity.Campus;
import online.referity.entity.Company;
import online.referity.entity.JobTitle;
import online.referity.entity.WorkingMode;
import online.referity.enums.JobLevel;
import online.referity.enums.JobStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Data
public class JobReport {

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
    private UUID createBy;
    private LocalDateTime createDate;

    private Company company;

    private WorkingMode workingMode; //
    private JobTitle jobTitle; //
    private Campus campus; //

    private List<JobStageRequest> recruiterStages; //

    private List<CandidateApplyReport> candidateApplies ;

    private float availableMoneyOfStage ;

    private double totalMoneyPay ;

    private int CandidateApplied ;

    private int CandidatePass ;

    private int CandidateFail ;
    private int CandidateInProcess ;
//    private int numberCandidateFail ;


}
