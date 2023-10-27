package online.referity.dto.request;

import lombok.Data;
import online.referity.enums.JobLevel;
import online.referity.enums.JobStatus;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Data
public class JobRequest {
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Title is required")
//    @TemplateEmail(message = "Invalid email a  ddress")
    private String title ;

    @Column(nullable = false)
//    @TemplateEmail(message = "Invalid email a  ddress")
    private String summary ;


    private JobLevel level;

    private int salaryForm;

    private int salaryTo;

    private int reward;

    private int balance;

    private String image;

    private int employee_quantity;

    @Column(nullable = false)
    @Size(max = 1000)
    @Size(min = 20, max = 1000, message = "Responsibility must be over 20 character")
    private String responsibility;

    @Column(nullable = false)
    @Size(max = 1000)
    @Size(min = 20, max = 1000, message = "Job Description must be over 20 character")

    private String jobDescription;

    @Column(nullable = false)
    @Size(min = 20, max = 1000, message = "Requiment must be over 20 character")

    private String requirement;

    @Column(nullable = false)
    @Size(min = 20, max = 1000, message = "Benefit must be over 20 character")
    private String benefit;


    private JobStatus status;

    private List<UUID> skillList;

    private List<LanguageRequest> languageRequests;
}
