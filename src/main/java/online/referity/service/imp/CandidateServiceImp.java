package online.referity.service.imp;

import online.referity.dto.request.*;
import online.referity.dto.response.CandidateResponse;
import online.referity.dto.response.CvResponse;
import online.referity.dto.response.CvSharedResponse;
import online.referity.dto.response.UpdateRequest;
import online.referity.entity.*;
import online.referity.enums.*;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.Duplicate;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.*;
import online.referity.service.*;
import online.referity.utils.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CandidateServiceImp implements CandidateService {
    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    JobTitleRepository jobTitleRepository;

    @Autowired
    CvSharedRepository cvSharedRepository;
    @Autowired
    HeadhunterRepository headhunterRepository;

    @Autowired
    SkillRepository skillRepository;
    @Autowired
    SkillLevelRepository skillLevelRepository;

    @Autowired
    LanguageLevelRepository languageLevelRepository;
    @Autowired
    WorkingModeRepository workingModeRepository;

    @Autowired
    CertificationService certificationService;

    @Autowired
    ExperienceService experienceService;

    @Autowired
    SkillService skillService;

    @Autowired
    ProgramLanguageService programLanguageService;

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    HeadhunterRequestRepository headhunterRequestRepository;
    @Autowired
    CvRepository cvRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    EmailService emailService;
    @Autowired
    NotificationService notificationService;

    @Autowired
    SystemConfigRepository systemConfigRepository;

    @Autowired
    MatchingService matchingService;

    private CvResponse convertToCVResponse(CV cv){
        CvResponse cvResponse = new CvResponse();

        cvResponse.setId(cv.getId());
        cvResponse.setCode(cv.getCode());
        cvResponse.setFullName(cv.getFullName());
        cvResponse.setPhone(cv.getPhone());
        cvResponse.setEmail(cv.getEmail());
        cvResponse.setAvatar(cv.getCandidate().getAccount().getAvatar());
        cvResponse.setCvUrl(cv.getCvUrl());
        cvResponse.setLinkedInLink(cv.getLinkedInLink());
        cvResponse.setFacebookLink(cv.getFacebookLink());
        cvResponse.setGithubLink(cv.getGithubLink());
        cvResponse.setSummary(cv.getSummary());
        cvResponse.setEducation(cv.getEducation());
        cvResponse.setCreateDate(cv.getCreateAt());
//        cvResponse.setStatus(cv.getStatus());
        cvResponse.setExperiences(cv.getExperiences());
        cvResponse.setCertifications(cv.getCertifications());
        cvResponse.setSkillLevels(cv.getSkillLevels());
        cvResponse.setLanguageLevels(cv.getLanguageLevels());
        cvResponse.setJobTitle(cv.getJobTitle().getPosition());
        cvResponse.setWorkingMode(cv.getWorkingMode().getMode());
        return cvResponse;
    }

    @Override
    public Candidate initCandidate(CandidateRequest data) {
        Account account = accountRepository.checkDuplicateCandidate(data.getEmail(), data.getPhone());
        if (account != null)
            return account.getCandidate();
        account = modelMapper.map(data, Account.class);
        // candidate = modelMapper.map(data,Candidate.class) ;
        Candidate candidate = new Candidate();
        candidate.setStatus(CandidataStatus.VERIFY);
        candidate = candidateRepository.save(candidate);
        account.setAccountType(AccountType.CANDIDATE);
        account.setCandidate(candidate);
        account = accountRepository.save(account);
        candidate.setAccount(account);
        return candidate;
    }

    @Override
    @Transactional
    public CvShared extendCvShared(ExtendRequest extendRequest, UUID uuid) {
        CvShared cvShared = cvSharedRepository.getCvSharedById(extendRequest.getCvSharedId());
        if (cvShared == null)
            throw new BadRequest("CV Shared not found");
        if (cvShared.getStatus() == CvSharedStatus.DELETED)
            throw new BadRequest("CV deleted!");
        HeadhunterRequest headhunterRequest = headhunterRequestRepository.findHeadhunterRequestById(uuid);
        if (!extendRequest.isAccept()) {
            if (headhunterRequest == null)
                throw new BadRequest("Internal server error");
            headhunterRequest.setRequestStatus(RequestStatus.REJECT);
            headhunterRequestRepository.save(headhunterRequest);
            return cvShared;
        }

//        cvShared.getExpireDate().plus(7, ChronoUnit.DAYS)
        cvShared.setExpireDate(LocalDateTime.now().plus(systemConfigRepository.findSystemConfigByName(SystemConfigType.EXPIRE_CV_TIME).getValue(), ChronoUnit.MINUTES));
        if (headhunterRequest == null)
            return cvSharedRepository.save(cvShared);
        headhunterRequest.setRequestStatus(RequestStatus.DONE);
        headhunterRequestRepository.save(headhunterRequest);
        return cvSharedRepository.save(cvShared);
    }

    @Override
    public CvShared updateCvShared(UpdateRequest updateRequest, UUID uuid) {
        CvShared cvShared = cvSharedRepository.getCvSharedById(updateRequest.getCvSharedId());
        if (cvShared == null)
            throw new BadRequest("CV Shared not found");
        HeadhunterRequest headhunterRequest = headhunterRequestRepository.findHeadhunterRequestById(uuid);
        if (!updateRequest.isAccpet()) {
            if (headhunterRequest == null)
                throw new BadRequest("Internal server error");
            headhunterRequest.setRequestStatus(RequestStatus.REJECT);
            headhunterRequestRepository.save(headhunterRequest);
            return cvShared;
        }
        Optional<CV> cv = cvRepository.findById(updateRequest.getNewCvId());
        if (!cv.isPresent())
            throw new BadRequest("CV not found ");
        if (cv.get().getStatus() == CvStatus.DELETED)
            throw new BadRequest("CV deleted");
        cvShared.setCv(cv.get());
        if (headhunterRequest == null)
            return cvSharedRepository.save(cvShared);
        headhunterRequest.setRequestStatus(RequestStatus.DONE);
        headhunterRequestRepository.save(headhunterRequest);
        return cvSharedRepository.save(cvShared);
    }

    @Transactional
    public CvResponse uploadCv(Candidate candidate, CvRequest cv, UUID jobtitleId, UUID workingModeId) {
        String keyCode = "C" + candidate.getId().toString().substring(0,4);
        CV existCv = cvRepository.findCandidate(candidate.getId(), jobtitleId);
        List<CvShared> listCvShared;
        CvShared cvShared;
        if (existCv != null)
            existCv.setStatus(CvStatus.OLD);
        candidate.setCvQuantity(candidate.getCvQuantity() + 1);
        candidate = candidateRepository.save(candidate);
        CV initCv = modelMapper.map(cv, CV.class);
        listCvShared = initCv.getCvSharedList();
        initCv.setCandidate(candidate);
        initCv.setJobTitle(jobTitleRepository.getById(jobtitleId));
        initCv.setWorkingMode(workingModeRepository.findById(workingModeId).get());
        initCv.setCode(Helper.genaralCode(keyCode, candidate.getCvQuantity() + 1));
        initCv.setCreateAt(LocalDateTime.now());
        initCv.setStatus(CvStatus.ACTIVE);

        if (cv.getCertificationRequestList() != null){
            List<Certification> certifications = new ArrayList<>();
            for (CertificationRequest cer : cv.getCertificationRequestList()) {
                certifications.add(certificationService.initCertification(cer, initCv));
            }
            initCv.setCertifications(certifications);
        }

        if (cv.getExperienceRequestList() != null){
            List<CandidateExperience> candidateExperiences = new ArrayList<>();
            for (ExperienceRequest exp : cv.getExperienceRequestList()) {
                candidateExperiences.add(experienceService.initExperience(exp, initCv));
            }
            initCv.setExperiences(candidateExperiences);
        }

        if (cv.getSkillList() != null){
            List<SkillLevel> skillLevels = new ArrayList<>();
            for (SkillListRequest skillRequest : cv.getSkillList()) {
                Skill skill;
                if (skillRequest.getId() != null ) {
                    skill = skillRepository.findSkillById(skillRequest.getId());
                } else {
                    Skill newSkill = new Skill();
                    newSkill.setSkillName(skillRequest.getName());
                    skill = skillRepository.save(newSkill);
                }
                skillLevels.add(skillService.addSkillCandidate(skill, initCv, skillRequest.getPoint()));
            }
            initCv.setSkillLevels(skillLevels);
        }

        if (cv.getLanguageRequests() != null){
            List<LanguageLevel> languageLevels = new ArrayList<>();
            for (LanguageRequest l : cv.getLanguageRequests()) {
                if (l.getLanguageId() != null)
                    languageLevels.add(programLanguageService.addLanguage(initCv, null, l));
            }
            initCv.setLanguageLevels(languageLevels);
        }
        initCv = cvRepository.save(initCv);
        matchingService.matchingByCVID(initCv.getId());
        return null;
    }


    @Override
    @Transactional
    public CvResponse addCv(CreateCandidateRequest candidateRequest, UUID candidateId) {
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if (!candidate.isPresent())
            throw new BadRequest("CandidateID not exist");
        return uploadCv(candidate.get(), candidateRequest.getCvRequest(), candidateRequest.getJobTitle(),
                candidateRequest.getWorkingModeId());
    }

    @Override
    public CvResponse updateCv(CreateCandidateRequest candidateRequest, UUID cvId) {
        CV cv = cvRepository.findCVById(cvId);
        if (cv == null) {
            throw new EntityNotFound("CV not found!");
        }


        return null;
    }

    @Override
    public CvResponse deleteCV(UUID cvId) {
        CV cv = cvRepository.findCVById(cvId);
        if (cv == null) {
            throw new EntityNotFound("CV not found!");
        }
        cv.setStatus(CvStatus.DELETED);

        List<CvShared> cvSharedList = cvSharedRepository.getCVSharedByCVId(cvId);
        for (CvShared cvShared : cvSharedList) {
            cvShared.setStatus(CvSharedStatus.DELETED);
        }

        return modelMapper.map(cvRepository.save(cv), CvResponse.class);
    }

    @Override
    @Transactional
    public CvShared addCvToHeadhunter(UUID cvId, CandidateRequest candidateData,
                                      CreateCandidateRequest candidateRequest, UUID headhunterId, UUID requestId) {
        CvShared cvShare;
        Headhunter headhunter = headhunterRepository.findHeadhunterById(headhunterId);
        CV cvResult = null;

        if (requestId != null) {
            HeadhunterRequest headhunterRequest = headhunterRequestRepository.findHeadhunterRequestById(requestId);
            // if(headhunterRequest == null)throw new BadRequest("Bad request ");
            headhunterRequest.setRequestStatus(RequestStatus.DONE);
            headhunterRequestRepository.save(headhunterRequest);
        }
        if (cvId != null) {
            Optional<CV> cv = cvRepository.findById(cvId);
            if (!cv.isPresent())
                throw new BadRequest("CV Not Found ");
            cvResult = cv.get();

            if (cvResult.getStatus().equals(CvStatus.DELETED))
                throw new BadRequest("CV Deleted!");

        } else {
            Candidate candidate = initCandidate(candidateData);
            CvResponse result = uploadCv(candidate, candidateRequest.getCvRequest(), candidateRequest.getJobTitle(),
                    candidateRequest.getWorkingModeId());
            cvResult = cvRepository.findCVByCode(result.getCode());
        }
        cvShare = new CvShared(null, LocalDateTime.now(), LocalDateTime.now().plus(systemConfigRepository.findSystemConfigByName(SystemConfigType.EXPIRE_CV_TIME).getValue(), ChronoUnit.MINUTES),
                CvSharedStatus.VERIFY, headhunter, cvResult, null, null );
        String body = "Candidate Name " + cvShare.getCv().getFullName().toUpperCase() ;
        notificationService.sendNotification("you just received a new cv" , body , null , headhunter.getAccount().getId());
        return cvSharedRepository.save(cvShare);
    }

    // @Override

    @Override
    public CvShared shareCv(CvSharedRequest cvSharedRequest) {
        CV cv = cvRepository.findById(cvSharedRequest.getCvId()).get();
        CvShared cvShared = shareCv(cv, cvSharedRequest.getHeadhunterId());
        // CvSharedResponse result = new CvSharedResponse();
//        String body = " You was received cv from candidate " + cvShared.getCv().getCandidate().getAccount().getFullName().toUpperCase();
//        notificationService.sendNotification("You was received cv "  , body , null , cvShared.getHeadhunter().getAccount().getId());

        return cvShared;

    }

    public CvShared shareCv(CV cv, UUID headhunterId) {
        Headhunter headhunter = headhunterRepository.findHeadhunterById(headhunterId);
        if (headhunter == null)
            throw new EntityNotFound("Headhunter not found ");
        if (cv.getStatus() == CvStatus.DELETED)
            throw new BadRequest("CV deleted!");
        CvShared cvShared = cvSharedRepository.findByCvIdAndHeadhunterId(cv.getId(), headhunterId);
        if (cvShared != null)
            throw new Duplicate("CV was share to this headhunter");
        cvShared = new CvShared();
        cvShared.setHeadhunter(headhunter);
        cvShared.setCreateDate(LocalDateTime.now());
        cvShared.setStatus(CvSharedStatus.VERIFY);
        cvShared.setCv(cv);
        cvShared.setExpireDate(LocalDateTime.now().plus(systemConfigRepository.findSystemConfigByName(SystemConfigType.EXPIRE_CV_TIME).getValue(), ChronoUnit.MINUTES));
        cvShared = cvSharedRepository.save(cvShared);
        String body = "Candidate Name " + cvShared.getCv().getFullName().toUpperCase() ;
        notificationService.sendNotification("you just received a new cv" , body , null , headhunter.getAccount().getId());
        return cvShared;
    }

    @Override
    public CvResponse getCandidateByCode(String code) {
        CV cv = cvRepository.findCVByCode(code);
        if (cv == null)
            throw new EntityNotFound("Cv Not Found");
        CvResponse result = modelMapper.map(cv, CvResponse.class);
        // modelMapper.map(cv.getCandidate(), result);
        result.setJobTitle(cv.getJobTitle().getPosition());
        result.setWorkingMode(cv.getWorkingMode().getMode());

        return result;
    }

    @Override
    public List<CvResponse> getCandidate(UUID candidateId) {
        List<CV> listCv = cvRepository.findCvByCandidateId(candidateId);
        List<CvResponse> listResult = new ArrayList<>();
        for (CV cv : listCv) {
            CvResponse result = modelMapper.map(cv, CvResponse.class);
            // modelMapper.map(cv.getCandidate(), result);
            result.setId(cv.getId());
            result.setJobTitle(cv.getJobTitle().getPosition());
            result.setWorkingMode(cv.getWorkingMode().getMode());
            listResult.add(result);
        }
        return listResult;
    }

    @Override
    public List<CvShared> getCandidateSharedByHeadhunterId(UUID headhunterId) {
        return cvSharedRepository.findByHeadhunterId(headhunterId);
    }

    @Override
    public List<CandidateResponse> getCvSharedVerifyByHeadhunterId(UUID headhunterId) {
        List<CvShared> cvShareds = cvSharedRepository.findVerifyByHeadhunterId(headhunterId);
        if (cvShareds.isEmpty())
            return null;
        List<CandidateResponse> result = new ArrayList<>();
        for (CvShared cv : cvShareds)
            result.add(convertToCandidateResponse(cv));
        return result;
    }

    @Override
    public List<CandidateResponse> getCvSharedByCandidateId(UUID candidateId) {
        List<CvShared> cvShareds = cvSharedRepository.findByCandidateId(candidateId);
        if (cvShareds.isEmpty())
            return null;
        List<CandidateResponse> result = new ArrayList<>();
        for (CvShared cv : cvShareds)
            result.add(convertToCandidateResponse(cv));
        return result;
    }

    @Override
    public List<CandidateResponse> getCvSharedVerifyByCandidateId(UUID candidateId) {
        List<CvShared> cvShareds = cvSharedRepository.findCvVerifyWithCandidate(candidateId);
        if (cvShareds.isEmpty())
            return null;
        List<CandidateResponse> result = new ArrayList<>();
        for (CvShared cv : cvShareds)
            result.add(convertToCandidateResponse(cv));
        return result;
    }

    @Override
    public Candidate deleteCandidate(UUID id) {
        return null;
    }

    @Override
    public Candidate changeStatus(UUID id, CvStatus cvStatus) {
        return null;
    }

    @Override
    public CvResponse getCandidateInformation(UUID cvId) {
        CV cv = cvRepository.findCVById(cvId);
        List<SkillLevel> skillLevels = skillLevelRepository.findByCvId(cvId);
        List<LanguageLevel> languageLevels = languageLevelRepository.findByCvId(cvId);
        CvResponse result = modelMapper.map(cv, CvResponse.class);
        result.setJobTitle(cv.getJobTitle().getPosition());
        result.setWorkingMode(cv.getWorkingMode().getMode());
        result.setAvatar(cv.getCandidate().getAccount().getAvatar());
        result.setSkillLevels(skillLevels);
        result.setLanguageLevels(languageLevels);
        modelMapper.map(cv.getCertifications(), result.getCertifications());
        modelMapper.map(cv.getExperiences(), result.getExperiences());

        return result;
    }

    @Override
    public void updateStatusDone(UUID candidateId) {

                Optional<Candidate> candidate = candidateRepository.findById(candidateId);
                if (!candidate.isPresent()) throw new BadRequest("Candidate load fail");
                candidate.get().setStatus(CandidataStatus.IN_JOB);
                candidate.get().getCvList().stream().forEach(cv -> {
                    cv.setStatus(CvStatus.HAVE_JOB);
                    cv.getCvSharedList().stream().forEach(cvShared -> cvShared.setStatus(CvSharedStatus.HAVEJOB));

                });
                Candidate result = candidateRepository.save(candidate.get());


    }

    public CandidateResponse convertToCandidateResponse(CvShared cvShared) {
        CandidateResponse candidateResponse = modelMapper.map(cvShared.getCv(), CandidateResponse.class);
        candidateResponse.setCvShared(new CvSharedResponse(cvShared.getId(), cvShared.getCreateDate(),
                cvShared.getExpireDate(), cvShared.getStatus(), cvShared.getHeadhunter().getAccount()));
        // candidateResponse.setCreateDate(cvShared.getCreateDate());
        // candidateResponse.setSkillLevels(cvShared.getCv().getSkillLevels());
        candidateResponse.setWorkingMode(cvShared.getCv().getWorkingMode().getMode());
        candidateResponse.setJobTitle(cvShared.getCv().getJobTitle().getPosition());
        candidateResponse.setAvatar(cvShared.getCv().getCandidate().getAccount().getAvatar());
        // candidateResponse.

        return candidateResponse;
    }

    public List<CvResponse> getAllCandidateCV(){
        List<CvResponse> cvResponses = new ArrayList<>();
        List<CV> cvs = cvRepository.findAll();
        for(CV cv: cvs){
            List<SkillLevel> skillLevels = skillLevelRepository.findByCvId(cv.getId());
            List<LanguageLevel> languageLevels = languageLevelRepository.findByCvId(cv.getId());
            CvResponse result = modelMapper.map(cv, CvResponse.class);
            result.setStatus(cv.getStatus());
            result.setJobTitle(cv.getJobTitle().getPosition());
            result.setWorkingMode(cv.getWorkingMode().getMode());
            result.setAvatar(cv.getCandidate().getAccount().getAvatar());
            result.setSkillLevels(skillLevels);
            result.setLanguageLevels(languageLevels);
            modelMapper.map(cv.getCertifications(), result.getCertifications());
            modelMapper.map(cv.getExperiences(), result.getExperiences());
            cvResponses.add(result);
        }
        return cvResponses;
    }
}
