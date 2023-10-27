package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class JobTitle extends BaseEntity {
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private String position ;

    private boolean isDelete = false;

    @OneToMany(mappedBy = "jobTitle")
    @JsonIgnore
    private List<CV> CVS;

    @OneToMany(mappedBy = "jobTitle")
    @JsonIgnore
    private List<Job> jobs ;

}
