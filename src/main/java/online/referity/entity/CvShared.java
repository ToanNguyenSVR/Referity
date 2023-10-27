package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import online.referity.enums.CvSharedStatus;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class CvShared  {
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private LocalDateTime createDate ;

    private LocalDateTime expireDate ;

    @Enumerated(EnumType.STRING)
    private CvSharedStatus status ;

    @ManyToOne
    @JoinColumn(name = "headhunterId" , referencedColumnName = "id")

    private Headhunter headhunter;

    @ManyToOne
    @JoinColumn(name = "cvId" , referencedColumnName = "id" )
    private  CV cv;

    @OneToMany(mappedBy = "cvShared")
    @Column(nullable = true)
    @JsonIgnore
    private List<HeadhunterRequest> headhunterRequests;

    @OneToMany(mappedBy = "cvShared")
    @Column(nullable = true)
    @JsonIgnore
    private List<CandidateApply> candidateApplies;





}
