package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MatchingScore {
    @Id
    @Column(unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    float score = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "jobId", referencedColumnName = "id")
    Job job;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cvId", referencedColumnName = "id")
    CV cv;
}
