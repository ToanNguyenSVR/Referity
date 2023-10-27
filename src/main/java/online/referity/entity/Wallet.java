package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import online.referity.enums.WalletStatus;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wallet {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    private Double balance = 0.0 ;

    private Double blockMoney = 0.0 ;

    @Enumerated(EnumType.STRING)
    private WalletStatus status ;

    private Date createDate ;

    private String beneficiaryAccount ;

    private String beneficiaryName ;

    private String beneficiaryBank ;

    @OneToMany(mappedBy = "wallet")
    @JsonIgnore
    private List<Transaction> transactions;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;

}
