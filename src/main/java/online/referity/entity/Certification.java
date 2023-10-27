package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Certification {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    @Column(nullable = false)
    private String certificationName ;
    @Column(name = "valueFrom")
    private LocalDateTime dateFrom ;
    @Column(name = "valueTo")
    private LocalDateTime dateTo ;

    @Column(nullable = false)
    private String certificationUrl ;

    private String organization ;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cvId" , referencedColumnName = "id")
    @JsonIgnore
    private CV cv;
}
