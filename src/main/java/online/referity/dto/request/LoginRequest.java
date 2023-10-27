package online.referity.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    String username;
    String password;
    String token;
}
