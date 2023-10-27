package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkingMode {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String mode ;
    private boolean isDelete = false ;

    @OneToMany(mappedBy = "workingMode")
    @JsonIgnore
    private List<CV> cvs;
    @OneToMany(mappedBy = "workingMode")
    @JsonIgnore
    private List<Job> jobs;
}
