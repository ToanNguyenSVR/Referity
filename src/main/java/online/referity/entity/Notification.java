package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.referity.enums.NotiStatus;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Notification {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private String title ;
    @Column(columnDefinition = "LONGTEXT")
    private String body ;

    private String forwardUrl ;

    private LocalDateTime createDate ;

    private NotiStatus notiStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "accountId" , referencedColumnName = "id")
    @JsonIgnore
    private Account account;
}
