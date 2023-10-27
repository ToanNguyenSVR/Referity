package online.referity.dto.response;

import lombok.Data;
import online.referity.entity.*;
import online.referity.enums.JobLevel;
import online.referity.enums.JobStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class JobResponse {
    private UUID id;

    private String title;

    private String assignFrom;

    private String summary;

    private JobLevel level;

    private int salaryForm;

    private int salaryTo;

    private int reward;

    private int balance;

    private String image;

    private int employee_quantity;

    private String responsibility;

    private String jobDescription;

    private String requirement;

    private String benefit;

    private JobStatus status;

    private Company company;

    private String workingMode ;

    private Campus campus ;

    private LocalDateTime createAt;

    private List<JobStage> jobStages ;

    private List<SkillLevel> skillLevels;
    private List<LanguageLevel> languageLevels;
    private int madeFrom;
}
