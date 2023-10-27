package online.referity.dto.response;

import lombok.Getter;
import lombok.Setter;
import online.referity.enums.WalletStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
public class WalletResponse {

    private UUID uuid;

    private Double balance ;

    private Double blockMoney ;

    private String beneficiaryAccount ;

    private String beneficiaryName ;

    private String beneficiaryBank ;

    private WalletStatus status ;

    private Date createDate ;

    private List<TransactionResponse> transaction ;
}
