package online.referity.dto.response;

import lombok.Data;
import online.referity.enums.JobStatus;
import online.referity.enums.ResultApply;
import online.referity.enums.ResultStage;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;

@Data
public class DashboardCompanyResponse {
    double balance ;
    double blockMoney ;
    double moneyPaid ;

    HashMap<String , Integer> summaryJob ;
    List<YearMonth> yearMonths ;
    List<CountCandidateApply> candidateData ;
    List<StaffReport> staffReports;
}

