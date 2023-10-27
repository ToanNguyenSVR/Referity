package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import online.referity.enums.ResultStage;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApplyStage extends BaseEntity{
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private String code ;

    private String note ;
    @Enumerated(EnumType.STRING)
    private ResultStage status ;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jobStageId" , referencedColumnName = "id")
    private JobStage jobStage;

    @OneToOne(mappedBy = "applyStage")
//    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonIgnore
    private Transaction transaction;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidateApplyId" , referencedColumnName = "id")
    private CandidateApply candidateApply;

}
