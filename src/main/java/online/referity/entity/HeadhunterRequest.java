package online.referity.entity;

import lombok.*;
import online.referity.enums.RequestStatus;
import online.referity.enums.RequestType;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HeadhunterRequest {
    @Id
    @Column(unique = true)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    private String requestTitle ;
    @Column(columnDefinition = "LONGTEXT")
    private String requestContent ;
    private LocalDateTime deadlineRequest ;
    private LocalDateTime createAt ;
    @Enumerated(EnumType.STRING)
    private RequestType requestType ;
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus ;

    private String emailTo ;

    @ManyToOne(optional = false)
    @JoinColumn(name = "headhunter_id")
    private Headhunter headhunter;

    @ManyToOne()
    @JoinColumn(name = "cvShared_id")
//    @Column(nullable = true)
    private CvShared cvShared;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

}
