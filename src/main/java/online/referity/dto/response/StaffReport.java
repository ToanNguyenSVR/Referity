package online.referity.dto.response;

import lombok.Data;
import online.referity.enums.JobStatus;

import java.util.HashMap;
@Data
public class StaffReport {

    private String code ;
    private String nameStaff ;
    private String fullName;
    private String email;
    private String avatar;
    private String phone;

    HashMap<String, Integer> summaryJob ;


}
