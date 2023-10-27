package online.referity.dto.request;

import lombok.Getter;
import lombok.Setter;
import online.referity.enums.PaymentResult;

import java.util.UUID;

@Getter
@Setter
public class PaymentRequest {

    UUID transactionId ;

    PaymentResult paymentResult ;
}
