package online.referity.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class WithDrawRequest {

    UUID walletId ;
    double money ;


}
