package online.referity.dto.response;

import online.referity.enums.CompanyRole;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

public class StaffResponse {

    private String code ;
    private String nameStaff ;
    private String fullName;
    private String email;
    private String avatar;
    private String phone;
    private  int candidateQuantity ;

}
