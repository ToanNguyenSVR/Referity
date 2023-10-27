package online.referity.service.imp;

//import online.referity.dto.response.CandidateApplyByJobIdResponse;

import online.referity.dto.request.CreateJobRequest;
import online.referity.dto.request.JobStageRequest;
import online.referity.dto.request.LanguageRequest;
import online.referity.dto.request.SkillListRequest;
import online.referity.dto.response.*;
import online.referity.entity.*;
import online.referity.enums.*;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.Duplicate;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.*;
import online.referity.service.CompanyService;
import online.referity.service.NotificationService;
import online.referity.service.ReferService;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReferServiceImp implements ReferService {
    @Autowired
    HeadhunterRepository headhunterRepository;
    @Autowired
    CvSharedRepository cvSharedRepository;

    @Autowired
    NotificationService notificationService;
    @Autowired
    CompanyService companyService;
    @Autowired
    SystemConfigRepository systemConfigRepository;
    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    JobStageRepository jobStageRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    CandidateApplyRepository candidateApplyRepository;

    @Autowired
    ApplyStageRepository applyStageRepository;
    @Autowired
    SkillLevelRepository skillLevelRepository;

    @Autowired
    CvRepository cvRepository;


    ModelMapper modelMapper = new ModelMapper();

    @Override
    public CandidateApplyResponse referCvToJob(UUID cvsharedId, UUID jobId) {
        Optional<CvShared> cvShared = cvSharedRepository.findById(cvsharedId);
//        if (!cvShared.isPresent()) throw new EntityNotFound("Cv not found with id = " + cvsharedId);
        Optional<Job> job = jobRepository.findById(jobId);

            boolean available = checkAvailbeReferByRating(cvShared.get().getHeadhunter().getId());

        if(!available) throw  new BadRequest("The turn of CV referrals in the month has been Run out of.");
//        if (!job.isPresent()) throw new EntityNotFound("Job not found with id = " + jobId);
        if (!job.get().getStatus().equals(JobStatus.ACTIVE)) throw new BadRequest("Job can't active ");
        if (cvShared.get().getCv().getStatus() == CvStatus.HAVE_JOB ||
                cvShared.get().getCv().getStatus() == CvStatus.EMPLOYEE)
            throw new BadRequest("Candidate  ' " + cvShared.get().getCv().getCandidate().getAccount().getFullName().toUpperCase() + " ' was have job ");

        UUID cvId = cvShared.get().getCv().getId();
        List<CV> cv = cvRepository.getCVByJobAndEmailOrPhone(jobId, cvShared.get().getCv().getEmail(), cvShared.get().getCv().getPhone());
        if(!cv.isEmpty()){
            throw new BadRequest("This candidate already apply to this job!");
        }
        CandidateApply candidateApply = candidateApplyRepository.checkDuplicateApply(cvId, jobId);
        System.out.println(cvShared.get().getExpireDate());
        System.out.println(LocalDateTime.now());
        if (LocalDateTime.now().isAfter(cvShared.get().getExpireDate())) {
            throw new BadRequest("CV Expire!");
        }

        if (candidateApply != null)
            throw new Duplicate("Cv :" + cvShared.get().getCv().getFullName() + " was applied to Job : " + job.get().getJobTitle().getPosition());
        String key = "AP" + cvShared.get().getId() + "0" + job.get().getId();
        candidateApply = new CandidateApply();
        candidateApply.setCvShared(cvShared.get());
        candidateApply.setJob(job.get());
        candidateApply.setCode(key);
        candidateApply.setCreateAt(LocalDateTime.now());
        candidateApply.setResultApply(ResultStage.CONFIRM);
        candidateApply = candidateApplyRepository.save(candidateApply);
        CandidateApplyResponse candidateApplyResponse = modelMapper.map(candidateApply, CandidateApplyResponse.class);
        candidateApplyResponse.setJob(modelMapper.map(candidateApply.getJob(), ReferJobResponse.class));
        candidateApplyResponse.setCv(modelMapper.map(candidateApply.getCvShared().getCv(), ReferCvResponse.class));
        return candidateApplyResponse;
    }

    @Override
    public CandidateApply confirmApply(String applyCode, ResultApply resultStage  , String note) {
        CandidateApply candidateApply = candidateApplyRepository.getByCode(applyCode);
        if (candidateApply == null) throw new BadRequest("Bad Request");
        if (candidateApply.getResultApply() != ResultStage.CONFIRM) throw new BadRequest("Candidate was confirmed");
        switch (resultStage) {
            case REJECT:
                candidateApply.setNote(note);
                candidateApply.setResultApply(ResultStage.FAIL);
                break;
            case ACCEPT:
                candidateApply.setNote("Confirmed By ADMIN");
                candidateApply.setResultApply(ResultStage.IN_PROCESS);
                candidateApply.setApprove_date(LocalDateTime.now());
                candidateApply.setUpdateAt(LocalDateTime.now());
                JobStage jobStage = jobStageRepository.getByJobId(candidateApply.getJob().getId(), JobStageStatus.INPROCESS, 1);
                if (jobStage == null) throw new EntityNotFound("Stage is close or stop please check again ");
                ApplyStage applyStage = new ApplyStage();
                applyStage.setCandidateApply(candidateApply);
                applyStage.setJobStage(jobStage);
                applyStage.setStatus(ResultStage.IN_PROCESS);


                applyStage.setCode(" AS " + candidateApply.getId().toString().substring(0 , 3) +"0" + jobStage.getId().toString().substring(0 , 3));
                applyStage = applyStageRepository.save(applyStage);
                List<ApplyStage> applyStages = candidateApply.getApplyStage();
                if (applyStages.isEmpty()) applyStages = new ArrayList<>();
                applyStages.add(applyStage);
                break;
            default:
                throw new BadRequest("Server Error please contact to developer");
        }
        String body = "Candidate " + candidateApply.getCvShared().getCv().getFullName() + " was " + candidateApply.getResultApply()
                + " to job " +  candidateApply.getJob().getJobTitle().getPosition() ;
        notificationService.sendNotification("Admin was confirm your apply ", body , null , candidateApply.getCvShared().getHeadhunter().getAccount().getId() );
        return candidateApplyRepository.save(candidateApply);
    }


    @Override
    public List<ProcessCandidateApplyResponse> get(ResultStage resultStage, UUID headhunterId, UUID jobId, UUID cvId , UUID candidateId) {
        List<ProcessCandidateApplyResponse> result  = new ArrayList<>();
        List<CandidateApply> candidateApplys = null;

            candidateApplys = candidateApplyRepository.get(resultStage , headhunterId , candidateId);


        candidateApplys = candidateApplys.stream().sorted(Comparator.comparing(CandidateApply::getCreateAt)).collect(Collectors.toList());

        for (CandidateApply ca: candidateApplys) {
            CreateJobRequest job = convertToJobResponse(ca.getJob());
            ProcessCandidateApplyResponse response = new ProcessCandidateApplyResponse();
            response.setJobResponse(job);
            response.setCv(ca.getCvShared().getCv());
            int stageSize = ca.getJob().getJobStages().size();
            int stageIn = ca.getPersentProgess()* stageSize /100 ;
            if(stageIn != 0  ){
            JobStage jobStageResult = ca.getJob().getJobStages().stream().filter( jobStage-> jobStage.getNoOfStage() == stageIn  ).findFirst().get();
            ApplyStage applyStageResult = jobStageResult.getApplyStages().stream().filter(applyStage -> applyStage.getCandidateApply().equals(ca)).findFirst().get();
            response.setResultStage(applyStageResult.getStatus());
            }
            if(ca.getResultApply() == ResultStage.FAIL){
                response.setResultStage(ResultStage.FAIL);
            }
            response.setStageIn(stageIn);
            response.setCode(ca.getCode());
            result.add(response);
        }
        return result;
    }

    @Override
    public List<ApplyStage> getApplyStage(ResultStage resultStage, UUID jobId) {


        return applyStageRepository.getApplyByJobStage(jobId, resultStage);
    }

    @Override
    public JobActiveResponse getApplyStage(UUID jobId) {
        Job job = jobRepository.findJobById(jobId);
        if (job == null) throw new BadRequest("Job not found ");
        List<JobStage> jobStages = job.getJobStages().stream().sorted(Comparator.comparing(JobStage::getNoOfStage)).collect(Collectors.toList());
        List<JobStageResponse> jobStageResponseList = new ArrayList<>();
        CreateJobRequest jobConvert = convertToJobResponse(job);
        JobActiveResponse result = modelMapper.map(jobConvert, JobActiveResponse.class);
        for (JobStage js : jobStages) {
            JobStageResponse jobStageResponse = modelMapper.map(js, JobStageResponse.class);
            jobStageResponse.setStageId(js.getId());
            jobStageResponse.setStageName(js.getRecruitStage().getNameProcess());
            List<ApplyStage> applyStages = js.getApplyStages();
            List<ApplyStageResponse> applyStageResponses = new ArrayList<>();
            for (ApplyStage as : applyStages) {
                ApplyStageResponse applyStageResponse = modelMapper.map(as, ApplyStageResponse.class);
                applyStageResponse.setHeadhunter(as.getCandidateApply().getCvShared().getHeadhunter().getAccount());
                CvResponse cvResponse = modelMapper.map(as.getCandidateApply().getCvShared().getCv(), CvResponse.class);
                modelMapper.map(as.getCandidateApply().getCvShared().getCv().getCandidate(), result);
                applyStageResponse.setCv(cvResponse);
                applyStageResponses.add(applyStageResponse);

            }
            jobStageResponse.setApplyStages(applyStageResponses);
            jobStageResponseList.add(jobStageResponse);
        }
        result.setStageResponseList(jobStageResponseList);

        return result;
    }

    public CreateJobRequest convertToJobResponse(Job job) {
        CreateJobRequest createJobRequest = new CreateJobRequest();
        List<SkillLevel> skillLevels = skillLevelRepository.findByJobId(job.getId());
        List<SkillListRequest> skillListRequests = new ArrayList<>();
        List<LanguageRequest> languageRequests = new ArrayList<>();
        List<JobStageRequest> jobStageRequests = new ArrayList<>();

        for (SkillLevel skillLevel : skillLevels) {
            SkillListRequest skillListRequest = new SkillListRequest();
            skillListRequest.setId(skillLevel.getSkill().getId());
            skillListRequest.setName(skillLevel.getSkill().getSkillName());
            skillListRequest.setPoint(skillLevel.getPoint());
            skillListRequests.add(skillListRequest);
        }

        for (LanguageLevel languageLevel : job.getLanguageLevels()) {
            LanguageRequest languageRequest = new LanguageRequest();
            languageRequest.setLanguageId(languageLevel.getId());
            languageRequest.setPonit(languageLevel.getPonit());
            languageRequest.setLanguageName(languageLevel.getProgramLanguage().getLanguage());
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
            jobStageRequests.add(jobStageRequest);
        }
        createJobRequest.setAssignTo(job.getAssignTo().getId());
        createJobRequest.setRecruiterStages(jobStageRequests);
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
        createJobRequest.setStaff(job.getCreateBy());
        createJobRequest.setCompany(job.getCampus().getCompany());
        createJobRequest.setCampusName(job.getCampus().getName());
        return createJobRequest;
    }

    @Override
    public CandidateApplyConfirmResqonse getCandidateApply(UUID jobId) {

        CandidateApplyConfirmResqonse result = new CandidateApplyConfirmResqonse();
        CreateJobRequest job = companyService.getJobById(jobId);
        if (job == null) throw new BadRequest("Entity not found exception ");
        result.setJobResponse(job);
        List<CandidateApply> candidateApplies = candidateApplyRepository.getByJobId(jobId, ResultStage.CONFIRM);

        result.setApplyResponses(new ArrayList<>());
        for (CandidateApply ca : candidateApplies) {
            ApplyResponse applyResponse = modelMapper.map(ca, ApplyResponse.class);
            applyResponse.setCv(modelMapper.map(ca.getCvShared().getCv(), CvResponse.class));
            result.getApplyResponses().add(applyResponse);
        }

        return result;
    }

    @Override
    public ApplyStage acceptApply(String applyStageCode, UUID companyStaffId) {
        return null;
    }

    public Boolean checkAvailbeReferByRating(UUID headhunterId){
        float avg_rating = 0;
        int numberCandidateApplied = 0 ;
        LocalDateTime fromTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0);

        try {

            avg_rating =ratingRepository.countHeadhunterRating(headhunterId);
            numberCandidateApplied = candidateApplyRepository.countApplyIn1MonthByHeadhunterId(headhunterId, fromTime);

        }catch (Exception  e){

        }
        int numberOfJob = systemConfigRepository.getByName(SystemConfigType.NUMBER_JOB_HEADHUNTER_APPLY).getValue();
        int numberJob = (int) avg_rating + numberOfJob;;
        if (numberCandidateApplied >= numberJob) return false;
        return true;


    }
}