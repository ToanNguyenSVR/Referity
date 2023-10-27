package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import online.referity.enums.ValidateStatus;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    @Column(nullable = false, unique = true)
    private String skillName ;

    private boolean isDelete  = false;

    @OneToMany(mappedBy = "skill")
    @JsonIgnore
    private List<SkillLevel> levelList ;

//    @Enumerated(EnumType.STRING)
    private ValidateStatus validateStatus = ValidateStatus.CHECK ;

    @ManyToOne()
    @JoinColumn(name = "skillGroupId" , referencedColumnName = "id")
    private SkillGroup skillGroup ;

}
