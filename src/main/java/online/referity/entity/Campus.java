package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.Type;

import org.hibernate.annotations.GenericGenerator;import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Campus {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    
    private UUID id;

    private  String name  ;

    private String address ;

    private boolean isDeleted = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "companyId" , referencedColumnName = "id")
    @JsonIgnore
    private Company company;

    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id" )
    private City city ;

    @OneToMany(mappedBy = "campus")
    @JsonIgnore
    private List<Job> jobs;

}
