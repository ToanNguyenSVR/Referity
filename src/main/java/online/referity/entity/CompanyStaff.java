package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import online.referity.enums.CompanyRole;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CompanyStaff {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;
    private String nameStaff ;

    private CompanyRole role ;

    private  int candidateQuantity ;

    private boolean isDeleted = false;

    @ManyToOne()
    private Company company ;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

    @OneToMany(mappedBy = "companyStaff", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(mappedBy = "assignTo", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Job> listJobAssign = new HashSet<>();

}
