package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.referity.enums.Gender;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Headhunter {


    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private String code ;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String citizenIdentification;
    private LocalDate citizenDate;
    private String address;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String citizensIdentityFront ;
    private String citizensIdentityBack ;


    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

    @OneToMany(mappedBy = "headhunter")
    @JsonIgnore
    private List<CvShared> cvSharedList;

    @OneToMany(mappedBy = "headhunter")
    @JsonIgnore
    private List<HeadhunterRequest> headhunterRequests;

    @OneToMany(mappedBy = "headhunter")
    @JsonIgnore
    private List<Rating> ratings;




}
