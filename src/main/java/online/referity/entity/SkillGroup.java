package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SkillGroup {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private String groupName ;

    private boolean isDelete ;

    @OneToMany(mappedBy = "skillGroup")
    @Column(nullable = true)
    @JsonIgnore
    private List<Skill> skills ;


}
