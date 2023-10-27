package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import online.referity.enums.JobStageStatus;
import online.referity.enums.NoOfStage;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JobStage {

    @Id
    @Column(unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")

    private UUID id;

    private int noOfStage;

    private float rewardPercent;

    private Double rewardPredictedForOne;

    private Double rewardPredictedForALL;

    private int personQuantity;

    private double availableMoney;

    @Enumerated(EnumType.STRING)

    private JobStageStatus jobStageStatus;

    private Date finishDate;

    private Date createDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jobId", referencedColumnName = "id")
    @JsonIgnore
    private Job job;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recruitStageId", referencedColumnName = "id")
    private RecruitStage recruitStage;

    @OneToMany(mappedBy = "jobStage")
    @JsonIgnore
    List<Transaction> transactions;

    @OneToMany(mappedBy = "jobStage")
    @JsonIgnore
    private List<ApplyStage> applyStages;


}
