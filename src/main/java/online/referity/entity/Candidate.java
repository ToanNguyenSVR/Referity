package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import online.referity.enums.CandidataStatus;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Candidate {
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private int cvQuantity = 0 ;
    @Column(nullable = false)

    private CandidataStatus status ;
    @OneToMany(mappedBy = "candidate")
    @JsonIgnore
    private List<CV> cvList ;

    @OneToMany(mappedBy = "candidate")
    @JsonIgnore
    @Column(nullable = true)
    private List<HeadhunterRequest> headhunterRequests;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;




}
