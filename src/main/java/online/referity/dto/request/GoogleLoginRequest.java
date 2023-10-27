package online.referity.dto.request;

import lombok.Data;
import online.referity.enums.LoginRequestType;

@Data
public class GoogleLoginRequest {
    String token;
    LoginRequestType type;
}
