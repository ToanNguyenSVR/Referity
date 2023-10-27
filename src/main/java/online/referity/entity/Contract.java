package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contract {
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private String contractNumber ;

    private String contractUrl ;

    private LocalDate expireDate ;

    private LocalDate createDate ;

    @ManyToOne(optional = false)
    @JoinColumn(name = "companyId" , referencedColumnName = "id")
    @JsonIgnore
    private Company company ;





}
