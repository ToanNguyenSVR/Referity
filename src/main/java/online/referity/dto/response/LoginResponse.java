package online.referity.dto.response;

import lombok.Data;
import online.referity.entity.Account;

@Data
public class LoginResponse extends Account {
    String token;
}
