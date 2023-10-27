package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import online.referity.enums.CvStatus;
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
public class CV extends BaseEntity {
    @Id
    @Column(unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")

    private UUID id;

    String fullName;
    String email;
    String address;
    String phone;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String cvUrl;

    @Column(nullable = false)
    private String linkedInLink;

    @Column(nullable = false)
    private String facebookLink;

    @Column(nullable = false)
    private String githubLink;
    @Column(columnDefinition = "LONGTEXT")
    private String summary;

    private String education;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private CvStatus status;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL)
    @JsonIgnore
    @Column(nullable = true)
    private List<CandidateExperience> experiences;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL)
    @Column(nullable = true)
    private List<SkillLevel> skillLevels;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL)
    @Column(nullable = true)
    private List<LanguageLevel> languageLevels;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL)
    @Column(nullable = true)
    private List<Certification> certifications;

    @OneToMany(mappedBy = "cv")
    @JsonIgnore
    @Column(nullable = true)
    private List<CvShared> cvSharedList;


    @ManyToOne(optional = false)
    @JoinColumn(name = "workingModeId", referencedColumnName = "id")
    @JsonIgnore
    private WorkingMode workingMode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jobTitleId", referencedColumnName = "id")
    @JsonIgnore
    private JobTitle jobTitle;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidateId", referencedColumnName = "id")
    @JsonIgnore
    private Candidate candidate;

    @OneToMany(mappedBy = "cv", fetch = FetchType.EAGER)
    @JsonIgnore
    Set<MatchingScore> matchingScores;
}
