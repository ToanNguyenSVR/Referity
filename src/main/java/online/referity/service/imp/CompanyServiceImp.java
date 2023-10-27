package online.referity.service.imp;

import online.referity.dto.request.*;
import online.referity.dto.response.CompanyStaffResponse;
import online.referity.dto.response.ContractResponse;
import online.referity.dto.response.JobResponse;
import online.referity.dto.response.StaffResponse;
import online.referity.entity.*;
import online.referity.enums.*;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.*;
import online.referity.service.*;
import online.referity.utils.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Time;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImp implements CompanyService {

    @Autowired
    AccountService accountService;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    CandidateApplyRepository candidateApplyRepository;

    @Autowired
    ProgramLanguagRepository programLanguagRepository;
    @Autowired
    WorkingModeRepository workingModeRepository;
    @Autowired
    JobTitleRepository jobTitleRepository;

    @Autowired
    CampusRepository campusRepository;

    @Autowired
    CompanyStaffRepository companyStaffRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    ProgramLanguageService programLanguageService;

    @Autowired
    JobConfigService jobConfigService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SkillService skillService;
    @Autowired
    LanguageLevelRepository languageLevelRepository;

    @Autowired
    SkillLevelRepository skillLevelRepository;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    MatchingService matchingService;

    public CreateJobRequest convertToJobResponse(Job job) {
        CreateJobRequest createJobRequest = new CreateJobRequest();
        List<SkillLevel> skillLevels = skillLevelRepository.findByJobId(job.getId());
        List<SkillListRequest> skillListRequests = new ArrayList<>();
        List<LanguageRequest> languageRequests = new ArrayList<>();
        List<JobStageRequest> jobStageRequests = new ArrayList<>();

        for (SkillLevel skillLevel : skillLevels) {
            SkillListRequest skillListRequest = new SkillListRequest();
            skillListRequest.setId(skillLevel.getSkill().getId());
            skillListRequest.setSkillLevelId(skillLevel.getId());
            skillListRequest.setName(skillLevel.getSkill().getSkillName());
            skillListRequest.setPoint(skillLevel.getPoint());
            skillListRequests.add(skillListRequest);
        }

        for (LanguageLevel languageLevel : job.getLanguageLevels()) {
            LanguageRequest languageRequest = new LanguageRequest();
            languageRequest.setLanguageId(languageLevel.getProgramLanguage().getId());
            languageRequest.setLanguageName(languageLevel.getProgramLanguage().getLanguage());
            languageRequest.setPonit(languageLevel.getPonit());
            languageRequest.setLanguageLevelId(languageLevel.getId());
            languageRequests.add(languageRequest);
        }

        for (JobStage jobStage : job.getJobStages()) {
            JobStageRequest jobStageRequest = new JobStageRequest();
            jobStageRequest.setStageId(jobStage.getId());
            jobStageRequest.setNoOf(jobStage.getNoOfStage());
            jobStageRequest.setRecruitStageId(jobStage.getRecruitStage().getId());
            jobStageRequest.setFinishDate(jobStage.getFinishDate());
            jobStageRequest.setCreateDate(jobStage.getCreateDate());
            jobStageRequest.setRewardPercent(jobStage.getRewardPercent());
            jobStageRequest.setPersonQuantity(jobStage.getPersonQuantity());
            jobStageRequest.setRecruitStageName(jobStage.getRecruitStage().getNameProcess());
            jobStageRequest.setStageId(jobStage.getId());
            jobStageRequests.add(jobStageRequest);
        }
        createJobRequest.setAssignTo(job.getAssignTo().getId());
        createJobRequest.setRecruiterStages(jobStageRequests.stream().sorted(Comparator.comparing(JobStageRequest::getNoOf)).collect(Collectors.toList()));
        createJobRequest.setSkillList(skillListRequests);
        createJobRequest.setLanguageRequests(languageRequests);
        createJobRequest.setWorkingMode(job.getWorkingMode().getId());
        createJobRequest.setJobTitle(job.getJobTitle().getId());
        createJobRequest.setCampus(job.getCampus().getId());
        createJobRequest.setTitle(job.getTitle());
        createJobRequest.setSummary(job.getSummary());
        createJobRequest.setLevel(job.getLevel());
        createJobRequest.setSalaryForm(job.getSalaryForm());
        createJobRequest.setSalaryTo(job.getSalaryTo());
        createJobRequest.setReward(job.getReward());
        createJobRequest.setImage(job.getImage());
        createJobRequest.setEmployee_quantity(job.getEmployee_quantity());
        createJobRequest.setResponsibility(job.getResponsibility());
        createJobRequest.setJobDescription(job.getJobDescription());
        createJobRequest.setRequirement(job.getRequirement());
        createJobRequest.setBenefit(job.getBenefit());
        createJobRequest.setStatus(job.getStatus());
        createJobRequest.setId(job.getId());
        createJobRequest.setStaff(job.getCompanyStaff().getId());
        createJobRequest.setCreateDate(job.getCreateAt());
        createJobRequest.setCompany(job.getCampus().getCompany());
        createJobRequest.setCampusName(job.getCampus().getName());
        createJobRequest.setImage(job.getImage());
        return createJobRequest;
    }

    @Override
    public CreateJobRequest getJobById(UUID jobId) {
        Job job = jobRepository.getById(jobId);
        if (job == null) {
            throw new EntityNotFound("Job not found!");
        }
        return convertToJobResponse(job);
    }

    @Override
    public List<CreateJobRequest> getJobByCompanyId(UUID companyId) {
        List<Job> jobs = jobRepository.getCompanyJob(companyId);
        List<CreateJobRequest> jobResponses = new ArrayList<>();

        for (Job job : jobs) {
            jobResponses.add(convertToJobResponse(job));
        }
        return jobResponses;
    }

    @Override
    public List<CreateJobRequest> getJobByStaffId(UUID staffId) {
        List<Job> jobs = jobRepository.getStaffJob(staffId);
        List<CreateJobRequest> jobResponses = new ArrayList<>();

        for (Job job : jobs) {
            jobResponses.add(convertToJobResponse(job));
        }
        return jobResponses;
    }

    @Override
    public List<JobResponse> getJob(Integer salaryForm, Integer salaryTo,List<UUID> skills,List<UUID> languages,UUID workingModeId,UUID cityId,List<JobLevel> levels ,String key) {
//
//        if(skills == null ){
//           skills = skillRepository.getUUID();
//        }
//        if(languages == null ){
//           languages = programLanguagRepository.getUUID();
//        }
        if(levels == null ) levels = Arrays.stream(JobLevel.values()).collect(Collectors.toList());

        List<Job> jobs   = jobRepository.findAll(salaryForm, salaryTo,workingModeId, cityId,
                    key,
                    levels);

        List<JobResponse> jobResponses = new ArrayList<>();
        for (Job job : jobs) {
            JobResponse jobResponse = modelMapper.map(job, JobResponse.class);
            jobResponses.add(jobResponse);
            Duration duration = Duration.between(job.getCreateAt(), LocalDateTime.now());
            jobResponse.setMadeFrom((int) duration.toDays());

        }
        return jobResponses;
    }

    @Override
    public List<JobResponse> getJobActiveByCompanyId(UUID companyId) {
        List<Job> jobs = jobRepository.findActiveJobByCompanyId(Sort.by(Sort.Direction.ASC, "createAt"), companyId);
        List<JobResponse> jobResponses = new ArrayList<>();
        for (Job job : jobs) {
            JobResponse jobResponse = modelMapper.map(job, JobResponse.class);
            jobResponses.add(jobResponse);
            Duration duration = Duration.between(job.getCreateAt(), LocalDateTime.now());
            jobResponse.setMadeFrom((int) duration.toDays());
        }
        return jobResponses;
    }

    @Override
    public List<CreateJobRequest> getJobVerifyByCompanyId(UUID companyId) {
        List<Job> jobs = jobRepository.findVerifyJobByCompanyId(Sort.by(Sort.Direction.ASC, "createAt"), companyId);
        List<CreateJobRequest> jobResponses = new ArrayList<>();
        for (Job job : jobs) {
            jobResponses.add(convertToJobResponse(job));
        }
        return jobResponses;

    }

    @Override
    public List<Company> getCompanyVerify() {
        return companyRepository.getCompanyVerify();
    }

    @Override
    public List<Company> getCompanyActive() {
        return companyRepository.getCompanyActive();
    }


    @Override
    @Transactional
    public Job createJob(CreateJobRequest data) {
        Optional<CompanyStaff> companyStaff = companyStaffRepository.findById(data.getStaff());
        if (!companyStaff.isPresent()) throw new EntityNotFound("Staff Id not found");
        Optional<WorkingMode> workingMode = workingModeRepository.findById(data.getWorkingMode());
        if (!workingMode.isPresent()) throw new EntityNotFound("Working mode not found");
        Optional<JobTitle> jobTitle = jobTitleRepository.findById(data.getJobTitle());
        if (!jobTitle.isPresent()) throw new EntityNotFound("Job title Not Found");
        Optional<Campus> campus = campusRepository.findById(data.getCampus());
        if (!campus.isPresent()) throw new EntityNotFound("Campus not Found");
        Job job = null ;
        job = modelMapper.map(data, Job.class);

        if(data.getAssignTo() != null ) {
            CompanyStaff assignTo = companyStaffRepository.getById(data.getAssignTo());
            job.setAssignTo(assignTo);
        }
        job.setJobTitle(jobTitle.get());
        job.setCampus(campus.get());
        job.setWorkingMode(workingMode.get());
        job.setCompanyStaff(companyStaff.get());
        job.setCreateAt(LocalDateTime.now());
        job.setCreateBy(companyStaff.get().getId());
        job.setStatus(JobStatus.PENDING);
        try {
            job = jobRepository.save(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (SkillListRequest skillR : data.getSkillList()) {
            Skill skill = skillRepository.getById(skillR.getId());
            skillService.addSkillJob(skill, job, skillR.getPoint());
        }
        for (LanguageRequest l : data.getLanguageRequests()) {
            if (l.getLanguageId() != null) programLanguageService.addLanguage(null, job, l);
        }
        for (JobStageRequest jobStageRequest : data.getRecruiterStages()) {

            JobStage jobStage = jobConfigService.createJobStage(jobStageRequest, job.getId());
        }
        matchingService.matchingByJobID(job.getId());
        return job;
    }

    @Override
    @Transactional
    public Job updateJob(CreateJobRequest data, UUID jobId) {

            Job job = jobRepository.findJobById(jobId);
            if(job.getStatus() == JobStatus.INPROCESS || job.getStatus() == JobStatus.DONE)  throw new BadRequest("Can't edit job ");
            Optional<WorkingMode> workingMode = workingModeRepository.findById(data.getWorkingMode());
            if (!workingMode.isPresent()) throw new EntityNotFound("Working mode not found");
            Optional<JobTitle> jobTitle = jobTitleRepository.findById(data.getJobTitle());
            if (!jobTitle.isPresent()) throw new EntityNotFound("Job title Not Found");
            Optional<Campus> campus = campusRepository.findById(data.getCampus());
            if (!campus.isPresent()) throw new EntityNotFound("Campus not Found");
            Optional<CompanyStaff> assginTo = companyStaffRepository.findById(data.getAssignTo());
            if (!assginTo.isPresent()) throw new EntityNotFound("Staff Id not found");
            job.setCampus(campus.get());
            job.setWorkingMode(workingMode.get());
            job.setUpdateAt(LocalDateTime.now());
            job.setAssignTo(assginTo.get());
            job.setRequirement(data.getRequirement());
            job.setBenefit(data.getBenefit());
            job.setJobDescription(data.getJobDescription());
            job.setSummary(data.getSummary());
            job.setEmployee_quantity(data.getEmployee_quantity());
            job.setSalaryTo(data.getSalaryTo());
            job.setSalaryForm(data.getSalaryForm());
            job.setReward(data.getReward());
            job.setTitle(data.getTitle());
            job.setImage(data.getImage());
            job.setResponsibility(data.getResponsibility());
            job.setJobTitle(jobTitle.get());

            try {
                job = jobRepository.save(job);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (SkillListRequest skillR : data.getSkillList()) {
                Skill skill = skillRepository.getById(skillR.getId());
                skillService.updateSkillLevel(skillR.getSkillLevelId(), skillR);
            }
            for (LanguageRequest l : data.getLanguageRequests()) {
                if (l.getLanguageId() != null) programLanguageService.updateLanguageJob(l);
            }
            for (JobStageRequest jobStageRequest : data.getRecruiterStages()) {
                JobStage jobStage = jobConfigService.updateJobStage(jobStageRequest);
            }

            return null ;

    }

    @Override
    public Job deleteJob(UUID jobId) {
        Job job = jobRepository.getById(jobId);
        job.setDelete(true);
        return jobRepository.save(job);
    }

    @Override
    public ContractResponse addContract(ContractRequest contractRequest, UUID companyId) {
        Company company = companyRepository.findCompanyById(companyId);
        if (company == null) throw new BadRequest("Company Id not found ");
        Contract contract = modelMapper.map(contractRequest, Contract.class);
        contract.setCreateDate(LocalDate.now());
        contract.setExpireDate(LocalDate.now().plusMonths(contractRequest.getMothOfContract()));
        company.setStatus(CompanyStatus.ACTIVE);
        if (company.getStaffs() != null && company.getStaffs().get(0) != null) {
            company.getStaffs().get(0).getAccount().setStatus(AccountStatus.ACTIVE);
        }
        contract.setCompany(company);
        contractRepository.save(contract);
        ContractResponse contractResponse = modelMapper.map(contract, ContractResponse.class);
        modelMapper.map(company, contractResponse);
        return contractResponse;
    }

    @Override
    @Transactional
    public CompanyStaffResponse createStaff(RegisterStaffCompanyRequest staff) {
//        Account account = accountService.register(staff.getRegisterRequest());
        Account account = modelMapper.map(staff.getRegisterRequest(), Account.class);
        if(account.getAccountType() == AccountType.COMPANY) account.setAccountType(AccountType.MANAGER);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setStatus(AccountStatus.ACTIVE);
        account = accountRepository.save(account);
        Company company = companyRepository.getById(staff.getCompanyId());
        if (company == null) throw new BadRequest("Bad Request ");

        CompanyStaff companyStaff = new CompanyStaff(null, staff.getNameStaff(), CompanyRole.RECRUITER, 0, false, company, account, null,null);
        return convertToResponse(companyStaffRepository.save(companyStaff));
    }

    @Override
    public CompanyStaffResponse updateStaff(RegisterStaffCompanyRequest staff, UUID staffId) {
        CompanyStaff companyStaff = companyStaffRepository.getById(staffId);
        companyStaff.setNameStaff(staff.getNameStaff());
        return convertToResponse(companyStaffRepository.save(companyStaff));
    }

    @Override
    public CompanyStaffResponse deleteStaff(UUID staffId) {
        CompanyStaff companyStaff = companyStaffRepository.getById(staffId);
        companyStaff.setDeleted(true);
        return convertToResponse(companyStaffRepository.save(companyStaff));
    }

    @Override
    public List<CompanyStaffResponse> getCompanyStaffById(UUID companyId) {
        List<CompanyStaffResponse> companyStaffResponses = new ArrayList<>();
        List<CompanyStaff> staffs = companyStaffRepository.findByCompanyIdAndIsDeletedFalse(companyId);
        staffs.stream().forEach(item -> {
            companyStaffResponses.add(convertToResponse(item));
        });
        return companyStaffResponses;
    }

    @Override
    public List<StaffResponse> findStaffByJobId(UUID companyId) {
        List<CompanyStaff> staffs = companyStaffRepository.findByCompanyId(companyId);
        List listResult = new ArrayList<>();
        modelMapper.map(staffs, listResult);
        System.out.println(listResult);
        return null;
    }

    public CompanyStaffResponse convertToResponse(CompanyStaff companyStaff) {
        CompanyStaffResponse companyStaffResponse = new CompanyStaffResponse();
        companyStaffResponse.setId(companyStaff.getId());

        companyStaffResponse.setEmail(companyStaff.getAccount().getEmail());
        companyStaffResponse.setAvatar(companyStaff.getAccount().getAvatar());
        companyStaffResponse.setNameStaff(companyStaff.getNameStaff());
        companyStaffResponse.setStatus(companyStaff.getAccount().getStatus());
        companyStaffResponse.setRole(companyStaff.getRole());
        companyStaffResponse.setFullname(companyStaff.getAccount().getFullName());
        companyStaffResponse.setPhone(companyStaff.getAccount().getPhone());
        return companyStaffResponse;
    }

    @Override
    @Transactional
    public CompanyStaff assignJob(AssignJobRequest jobRequest) {
        try{
            Job job = jobRepository.findJobById(jobRequest.getJobId());
            if(job.getStatus() == JobStatus.INPROCESS) throw  new BadRequest("Job was start");
            if(job.getCompanyStaff().getId() == jobRequest.getAccountStaffId()) throw  new BadRequest("Staff already in job ");
            Account account = accountService.getAccountDetail(jobRequest.getAccountManagerId());
            if(!account.getAccountType().equals(AccountType.MANAGER)|| !job.getCompanyStaff().getCompany().equals(account.getCompanyStaff().getCompany()))
                throw  new BadRequest("Account don't have permission for this job ");
            CompanyStaff companyStaff = companyStaffRepository.findByIdAndIsDeletedFalse(jobRequest.getAccountStaffId());
            if(companyStaff == null ) throw  new EntityNotFound("Staff not found ");
            if(!companyStaff.getCompany().equals(account.getCompanyStaff().getCompany()))
                throw  new BadRequest("Staff not in your operation");
            job.setCompanyStaff(companyStaff);
            companyStaff.getJobs().add(job);
            return companyStaffRepository.save(companyStaff);
        }catch (Exception e ){
            e.printStackTrace();
        }
        return  null ;
    }

    @Override
    public List<CreateJobRequest> getJobConfirm() {
        List<CreateJobRequest> jobResponses = new ArrayList<>();
        List<Job> jobs = new ArrayList<>();
        List<CandidateApply > candidateApplies = candidateApplyRepository.getByJobId(ResultStage.CONFIRM);
        if(candidateApplies.size() == 0 ) return null ;
        for (CandidateApply ca: candidateApplies) {
            if (!jobs.contains(ca.getJob())){
                jobs.add(ca.getJob());
            }
        }
        for(Job j : jobs) jobResponses.add(convertToJobResponse(j));

        return jobResponses;
    }

//    @Override
//    public Job UpdateJobInformation(RegistJobRequest job) {
//        return null;
//    }

    @Override
    public Job stopJob(UUID jobId) {
        return null;
    }

    @Override
    public Job changeStatus(UUID jobId, JobStatus status) {
        return null;
    }

    @Override
    public Job getJobbyCode(String code) {
        return null;
    }

    @Override
    public List<Job> searchJob(UUID cityId, UUID companyId, String key) {
        return null;
    }


}
