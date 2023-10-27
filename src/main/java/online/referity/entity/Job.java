package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import online.referity.enums.JobLevel;
import online.referity.enums.JobStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Job extends BaseEntity {

    @Id
    @Column(unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")

    private UUID id;

    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String summary;

    private JobLevel level;

    private int salaryForm;

    private int salaryTo;

    private int reward; //

    private String image;

    private int employee_quantity; //
    @Column(columnDefinition = "LONGTEXT")
    private String responsibility;
    @Column(columnDefinition = "LONGTEXT")
    private String jobDescription;
    @Column(columnDefinition = "LONGTEXT")
    private String requirement;
    @Column(columnDefinition = "LONGTEXT")
    private String benefit;

    private boolean isDelete = false;


    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.PENDING;

    @OneToMany(mappedBy = "job")
    @Column(nullable = true)
    private List<SkillLevel> skillLevels;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @Column(nullable = true)
    private List<LanguageLevel> languageLevels;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workingModeId", referencedColumnName = "id")
    @Fetch(value = FetchMode.SELECT)
    private WorkingMode workingMode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jobTitleId", referencedColumnName = "id")
    @Fetch(value = FetchMode.SELECT)
    private JobTitle jobTitle;

    @ManyToOne(optional = false)

    @JoinColumn(name = "campusId", referencedColumnName = "id")
    @Fetch(value = FetchMode.SELECT)
    private Campus campus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "companyStaffId")
    @JsonIgnore
    private CompanyStaff companyStaff;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "assignId")
    @JsonInclude
    private CompanyStaff assignTo;

    @OneToMany(mappedBy = "job")
    @Column(nullable = true)
    @JsonIgnore
    private List<CandidateApply> candidateApplies;

    @OneToMany(mappedBy = "job")
    @Column(nullable = true)
    private List<JobStage> jobStages;

    @OneToMany(mappedBy = "job", fetch = FetchType.EAGER)
    @JsonIgnore
    Set<MatchingScore> matchingScores;
}
