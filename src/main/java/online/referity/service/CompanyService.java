package online.referity.service;

import online.referity.dto.request.AssignJobRequest;
import online.referity.dto.request.ContractRequest;
import online.referity.dto.request.CreateJobRequest;
import online.referity.dto.request.RegisterStaffCompanyRequest;
import online.referity.dto.response.CompanyStaffResponse;
import online.referity.dto.response.ContractResponse;
import online.referity.dto.response.JobResponse;
import online.referity.dto.response.StaffResponse;
import online.referity.entity.Company;
import online.referity.entity.CompanyStaff;
import online.referity.entity.Job;
import online.referity.enums.JobLevel;
import online.referity.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;


public interface CompanyService {
    public List<CreateJobRequest> getJobByCompanyId(UUID companyId);
    public List<CreateJobRequest> getJobByStaffId(UUID staffId);
    public CreateJobRequest getJobById(UUID jobId);
    public List<JobResponse> getJob(
            Integer salaryForm, Integer salaryTo,List<UUID> skills,List<UUID> languages,UUID workingModeId,UUID cityId,List<JobLevel> levels ,String key);
    public List<JobResponse> getJobActiveByCompanyId(UUID companyId);
    public List<CreateJobRequest> getJobVerifyByCompanyId(UUID companyId);

    public List<Company> getCompanyVerify();
    public List<Company> getCompanyActive();

    public Job createJob (CreateJobRequest job );
    public Job updateJob (CreateJobRequest job , UUID jobId );
    public Job deleteJob (UUID jobId );

    public ContractResponse addContract (ContractRequest contractRequest  , UUID companyId);

    public CompanyStaffResponse createStaff(RegisterStaffCompanyRequest staff) ;
    public CompanyStaffResponse updateStaff(RegisterStaffCompanyRequest staff, UUID staffId) ;

    public CompanyStaffResponse deleteStaff(UUID staffId);

    public List<CompanyStaffResponse> getCompanyStaffById(UUID companyId);

    public List<StaffResponse> findStaffByJobId (UUID companyId);

    public CompanyStaff assignJob(AssignJobRequest jobRequest);

    public List<CreateJobRequest> getJobConfirm ();

//    public Job UpdateJobInformation(RegistJobRequest job );

    public Job stopJob( UUID jobId) ;

    public Job changeStatus (UUID jobId , JobStatus status );

    public Job getJobbyCode (String code);

    public List<Job> searchJob ( UUID cityId , UUID companyId , String key ) ;
}
