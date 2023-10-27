package online.referity.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class RechangeRequest {

    UUID walletId ;
    double money ;
}
