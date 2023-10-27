package online.referity.dto.request;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class WalletRequest {

    private String beneficiaryAccount ;

    private String beneficiaryName ;

    private String beneficiaryBank ;

    private String accountId ;

}
