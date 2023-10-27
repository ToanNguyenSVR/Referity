package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.referity.enums.RatingStar;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rating {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")

    private UUID id;

    private String comment ;

    private LocalDateTime createAt ;

    private int star ;
    @ManyToOne
    @JoinColumn(name = "headhunterId" , referencedColumnName = "id")
    @JsonIgnore
    private  Headhunter headhunter ;
    @ManyToOne
    @JoinColumn(name = "companyId" , referencedColumnName = "id")
    @JsonIgnore
    private Company company ;
    @ManyToOne
    @JoinColumn(name = "candidateApplyId" , referencedColumnName = "id")
    @JsonIgnore
    private CandidateApply  candidateApply ;


}
