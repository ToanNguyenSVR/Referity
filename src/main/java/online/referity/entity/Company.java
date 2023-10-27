package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.referity.enums.CompanyStatus;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Company {
    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private String taxCode ;
    private String title;

    private String name;

    private String shortName;
    private String websiteUrl;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String logoUrl;
    private LocalDateTime establishDate;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status ;

    @OneToMany(mappedBy = "company")
    @Column(nullable = true)
    @JsonIgnore
    private List<Campus> campuses;

    @OneToMany(mappedBy = "company")
    @Column(nullable = true)
    private List<Contract> contracts;

    @OneToMany(mappedBy = "company")
    @Column(nullable = true)
    @JsonIgnore
    private List<CompanyStaff> staffs;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private List<Rating> ratings;




}
