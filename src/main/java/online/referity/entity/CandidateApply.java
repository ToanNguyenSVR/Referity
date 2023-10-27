package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import online.referity.enums.ResultStage;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CandidateApply  extends  BaseEntity{
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private  String code ;

    private int persentProgess ;

    private Double totalReward = 0.0;

    @Enumerated(EnumType.STRING)
    private ResultStage resultApply  ;

    private LocalDateTime approve_date = LocalDateTime.now();

    private boolean wasPaid = false ;

    private  String note ;

    @OneToMany(mappedBy = "candidateApply")
    @JsonIgnore
    private List<ApplyStage> applyStage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cvSharedId" , referencedColumnName = "id")
    private CvShared cvShared ;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jobId" , referencedColumnName = "id")
    @JsonIgnore
    private Job job ;

    @OneToMany(mappedBy = "candidateApply")
    @JsonIgnore
    private List<Rating> ratings;


}
