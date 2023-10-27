package online.referity.service.imp;

import online.referity.dto.response.*;
import online.referity.entity.*;
import online.referity.enums.AccountType;
import online.referity.enums.JobStatus;
import online.referity.enums.ResultApply;
import online.referity.enums.ResultStage;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.*;
import online.referity.service.DashBoardService;
import org.hibernate.validator.constraints.ru.INN;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DashboardServiceImp implements DashBoardService {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CandidateApplyRepository candidateApplyRepository;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public DashboardCompanyResponse getCompany(UUID accountId , Year year) {
        DashboardCompanyResponse dashboardCompanyResponse = new DashboardCompanyResponse();
        Account account = accountRepository.findAccountById(accountId);
        List<YearMonth> yearMonths = new ArrayList<>();
        List<StaffReport> staffReports = new ArrayList<>();
        HashMap<String , Integer> countJob =new HashMap<>();
        List<CountCandidateApply> candidateData = new ArrayList<>();
        HashMap<ResultStage , int[]> candidatePointByStage = new HashMap<>();


        if(account == null ) throw  new EntityNotFound("404 Not Found");
        if(account.getAccountType().equals(AccountType.MANAGER)){
            dashboardCompanyResponse.setBalance(account.getWallet().getBalance());
            dashboardCompanyResponse.setBlockMoney(account.getWallet().getBlockMoney());
        }
        LocalDateTime fromTime =LocalDateTime.now().minus(1, ChronoUnit.YEARS);
        if(year != null ){
            fromTime = LocalDateTime.of(year.getValue() ,1 , 1 , 0, 0);
        }else {
            year = Year.of(fromTime.getYear());
        }

        LocalDateTime toTime = fromTime.plus(1 , ChronoUnit.YEARS);
        String[] jobs = jobRepository.countJobByCompanyId(account.getCompanyStaff().getCompany().getId() , fromTime , toTime);
        int totalJob = 0 ;
        for( JobStatus jobStatus : JobStatus.values()){
            countJob.put(jobStatus.toString() , 0);
        }
        for (String job : jobs) {
            String[] jobRecord = job.split(",");
            countJob.put(JobStatus.valueOf(jobRecord[0]).toString() , Integer.valueOf(jobRecord[1]));
            totalJob += Integer.parseInt(jobRecord[1]);
        }
        countJob.put("Total Job" , totalJob);
        dashboardCompanyResponse.setSummaryJob(countJob);


        for (ResultStage status: ResultStage.values()) {
            int[] numbers = {0,0,0,0,0,0,0,0,0,0,0,0};
            candidatePointByStage.put(status , numbers);
        }
        for(int index = 1 ; index <= 12 ; index++ ){
            if(YearMonth.of(year.getValue(),index).compareTo( YearMonth.of(LocalDateTime.now().getYear(),LocalDateTime.now().getMonth())) > 0){
                break;
            }
            yearMonths.add(YearMonth.of(year.getValue(),index));
            int nextIndex = index + 1   ;
            if(index == 12 ) {
                year.plus(1 , ChronoUnit.YEARS)  ;
                nextIndex = 1 ;
            }
            LocalDateTime fromYearMonth = LocalDateTime.of(year.getValue() , index , 1 , 0 , 0);
            LocalDateTime toYearMonth = LocalDateTime.of(year.getValue() , nextIndex  , 1 , 0 , 0);

            List<Object[]> candidateApplies
                    = candidateApplyRepository.countCandidateApplyByCompanyId(account.getCompanyStaff().getCompany().getId() , fromYearMonth , toYearMonth);

                for (Object[] cadidateApply : candidateApplies) {
                    ResultStage result = (ResultStage) cadidateApply[0];
                    int total = ((Number) cadidateApply[1]).intValue();
                    int[] point =candidatePointByStage.get(result) ;
                    point[index -1 ] = total ;
                    candidatePointByStage.putIfAbsent(result , point);
                }
        }
        for (ResultStage status: ResultStage.values()) {
           CountCandidateApply point = new CountCandidateApply();
           point.setLabel(status);
           point.setData(candidatePointByStage.get(status));
           candidateData.add(point);
        }
        for (CompanyStaff staff : account.getCompanyStaff().getCompany().getStaffs() ) {
            HashMap<String , Integer> countJobByStaff =new HashMap<>();
            int totalJobOfStaff = 0 ;
            StaffReport staffReport = new StaffReport();
            staffReport.setNameStaff(staff.getNameStaff());
            staffReport.setFullName(staff.getAccount().getFullName());
            staffReport.setPhone(staff.getAccount().getPhone());
            staffReport.setAvatar(staff.getAccount().getAvatar());
            staffReport.setEmail(staff.getAccount().getEmail());
            String[] jobOfStaff = jobRepository.countJobByCompanyStaffId(staff.getId() , fromTime , toTime);
            for (String job : jobOfStaff) {
                String[] jobRecord = job.split(",");
                countJobByStaff.put(JobStatus.valueOf(jobRecord[0]).toString() , Integer.valueOf(jobRecord[1]));
                totalJobOfStaff += Integer.parseInt(jobRecord[1]);
            }
            countJobByStaff.put("Total Job" , totalJobOfStaff);
            staffReport.setSummaryJob(countJobByStaff);
            staffReports.add(staffReport);

        }
        dashboardCompanyResponse.setStaffReports(staffReports);
        dashboardCompanyResponse.setCandidateData(candidateData);
        dashboardCompanyResponse.setYearMonths(yearMonths);
        try {
            dashboardCompanyResponse.setMoneyPaid(transactionRepository.countTotalMoneyPay(account.getWallet().getId()));
        }catch (Exception e ){

        }


        return dashboardCompanyResponse;
    }

    @Override
    public DashboardHeadhunterResponse getHeadhunter(UUID headhunterId) {
        return null;
    }

    @Override
    public DashboardAdminResponse getAdmin(UUID accountId , Year year) {
        DashboardAdminResponse adminResponse = new DashboardAdminResponse();
        List<YearMonth> yearMonths = new ArrayList<>();
        List<SystemReportResponse > systemReportResponseList = new ArrayList<>();
        Account account = accountRepository.findAccountById(accountId );
        double revenue = transactionRepository.countTotalRevenueOfSystem();
        adminResponse.setTotalRevenue((int)revenue);
        adminResponse.setTotalMoney(account.getWallet().getBalance().intValue());
        int totalJob = jobRepository.countJob();
        adminResponse.setTotalJob(totalJob);
        adminResponse.setTotalCandidate(accountRepository.countAccountByRole(AccountType.CANDIDATE));
        adminResponse.setTotalAdmin(accountRepository.countAccountByRole(AccountType.ADMIN));
        adminResponse.setTotalCompany(accountRepository.countAccountByRole(AccountType.MANAGER));
        adminResponse.setTotalHeadhunter(accountRepository.countAccountByRole(AccountType.HEADHUNTER));
            int[] value = {0,0,0,0,0,0,0,0,0,0,0,0};
            SystemReportResponse systemReportResponse = new SystemReportResponse();
            systemReportResponse.setData(value);
                systemReportResponse.setLabel("Revenue");
            for (int i = 1 ; i <= 12 ;i ++){

                if(YearMonth.of(year.getValue(),i).compareTo( YearMonth.of(LocalDateTime.now().getYear(),LocalDateTime.now().getMonth())) > 0){
                    break;
                }
                yearMonths.add(YearMonth.of(year.getValue(),i));
                LocalDateTime startTime = LocalDateTime.of(year.getValue(), i , 1,0,0);
                LocalDateTime endTime ;
                if(i < 12){
                    endTime = LocalDateTime.of(year.getValue(), i +1 , 1,0,0);
                }else {
                    endTime = LocalDateTime.of(year.getValue() +1  , 1 , 1 ,0 ,0);
                }
                    Double point = transactionRepository.countTotalRevenueByDate(startTime, endTime);
                    if( point != null){
                    systemReportResponse.getData()[i - 1] =  point.intValue();
                    }
            }
            adminResponse.setYearMonths(yearMonths);
            systemReportResponseList.add(systemReportResponse);
            adminResponse.setReports(systemReportResponseList);
        return adminResponse;
    }
}
