package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import online.referity.enums.ValidateStatus;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProgramLanguage {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    //
    @Column(unique = true)
    private String language ;

    private boolean isDelete = false;


    @Enumerated(EnumType.STRING)
    private ValidateStatus status;
    @OneToMany(mappedBy = "programLanguage")
    @JsonIgnore
    private List<LanguageLevel> languageLevels ;

}
