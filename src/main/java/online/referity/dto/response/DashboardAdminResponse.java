package online.referity.dto.response;

import lombok.Data;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
@Data
public class DashboardAdminResponse {

    int totalJob ;
    int totalMoney ;
    int totalRevenue ;
    int totalAdmin;
    int totalHeadhunter;
    int totalCandidate;
    int totalCompany;
    List<YearMonth> yearMonths ;
    List<SystemReportResponse> reports ;

}
