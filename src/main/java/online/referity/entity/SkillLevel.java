package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SkillLevel {

    @Id

    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    //điểm số của skill đó
    @Column(nullable = false)
    private int point;

    @ManyToOne(optional = false)
    @JoinColumn(name = "skillId", referencedColumnName = "id")

    private Skill skill;

    @ManyToOne()
    @JoinColumn(name = "cvId" , referencedColumnName = "id")
    @JsonIgnore
    private CV cv;

    @ManyToOne()
    @JoinColumn(name = "jobId", referencedColumnName = "id")
    @JsonIgnore
    private Job job;

}
