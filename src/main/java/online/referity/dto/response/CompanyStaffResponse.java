package online.referity.dto.response;

import lombok.Data;
import online.referity.entity.Account;
import online.referity.enums.AccountStatus;
import online.referity.enums.CompanyRole;

import java.util.UUID;

@Data
public class CompanyStaffResponse {
    private UUID id;
    private String fullname;
    private String phone;
    private String code;
    private String nameStaff;
    private CompanyRole role;
    private int candidateQuantity;
    private String email;
    private String avatar;
    private AccountStatus status;
    private boolean isDeleted = false;
}
