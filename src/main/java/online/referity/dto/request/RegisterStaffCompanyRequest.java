package online.referity.dto.request;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class RegisterStaffCompanyRequest {
    AccountRegisterRequest registerRequest ;

    private String nameStaff ;

    private UUID companyId ;
}
