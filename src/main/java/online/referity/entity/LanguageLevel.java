package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.util.NavigableMap;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LanguageLevel {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;


    private int ponit ;

    private boolean isBest ;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ProgramLanguage_id")
    private ProgramLanguage programLanguage ;
    @ManyToOne
    @JoinColumn(name = "cvId" , referencedColumnName = "id")
    @JsonIgnore
    private CV cv;

    @ManyToOne()
    @JoinColumn(name = "job_id")
    @JsonIgnore
    private Job job;
}
