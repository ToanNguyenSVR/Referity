package online.referity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import online.referity.enums.TransactionResult;
import online.referity.enums.TransactionType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {

    @Id
    @Column(unique = true)
    @Type(type="org.hibernate.type.UUIDCharType")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String code;

    private Double money;

    private Double platformFee  = 0.0 ;

    private Double totalMoney;

    private String transferContent;

    private LocalDateTime expireDay;

    private String senderCode;

    private String ReceiverCode;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionResult transactionResult;

    private LocalDateTime createDate ;

    private String createBy;

    private String image_evident;

    @ManyToOne(optional = false)
    @JoinColumn(name = "walletId", referencedColumnName = "id")
    @JsonIgnore
    private Wallet wallet;

    @ManyToOne()
    @JoinColumn(name = "jobStageId", referencedColumnName = "id")
    @JsonIgnore
    JobStage jobStage;

    @OneToOne
    @JoinColumn(name = "applyStage_id")
    @JsonIgnore
    ApplyStage applyStage;

}
