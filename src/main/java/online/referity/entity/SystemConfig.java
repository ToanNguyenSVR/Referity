package online.referity.entity;

import lombok.Data;
import online.referity.enums.SystemConfigType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class SystemConfig {
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private SystemConfigType name ;
    @Column(columnDefinition = "LONGTEXT")
    private int value;
}
