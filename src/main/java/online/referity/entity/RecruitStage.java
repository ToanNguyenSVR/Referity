package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import online.referity.enums.NoOfStage;
import lombok.*;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RecruitStage {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private int noOfStage ;
    private String nameProcess;
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private int baseStageRewardPresent;

    private Boolean isDeleted = false;
    @OneToMany(mappedBy = "recruitStage")
    @JsonIgnore
    private List<JobStage> jobStages;
}
