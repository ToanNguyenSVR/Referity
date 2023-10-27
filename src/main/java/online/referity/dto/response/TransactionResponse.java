package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.entity.ApplyStage;
import online.referity.entity.JobStage;
import online.referity.enums.TransactionResult;
import online.referity.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TransactionResponse {

    private String code;

    private Double money;

    private Double platformFee;

    private Double totalMoney;

    private String transferContent;

    private String paymentSide;

    private LocalDateTime expireDay;

    private String receiverCode;

    private TransactionType transactionType;

    private TransactionResult transactionResult;

    private LocalDateTime createDate;

    private Date createBy;

    private String image_evident;

    private String stageName;

    private String candidateApply ;

}
