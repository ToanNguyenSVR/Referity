package online.referity.entity;

import lombok.*;
import online.referity.enums.Emailtype;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TemplateEmail {
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String toAddress;

    private String subject;
    @Column(columnDefinition = "LONGTEXT")
    private String content ;
    private String cc ;

    private String footer ;

    private  String greeting ;

    private boolean isDelete ;

    @Enumerated(EnumType.STRING)
    private Emailtype emailType ;
}
