package online.referity.service.imp;

import online.referity.dto.response.MatchingListResponse;
import online.referity.entity.*;
import online.referity.enums.SystemConfigType;
import online.referity.repository.*;
import online.referity.service.MatchingService;
import online.referity.service.NotificationService;
import online.referity.service.SystemConfigService;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MatchingServiceImp implements MatchingService {

    @Autowired
    SystemConfigRepository systemConfigRepository;

    @Autowired
    CvSharedRepository cvSharedRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    MatchingRepository matchingRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    CandidateApplyRepository candidateApplyRepository;


    @Override
    public float calculationMatching(UUID jobId, UUID cvId) {
        Job job = jobRepository.findJobById(jobId);
        CV cv = cvRepository.findCVById(cvId);
        float score = 0;
        float percentPerSkill = (float) systemConfigRepository.getByName(SystemConfigType.PERCENT_SKILL).getValue() / job.getSkillLevels().size();
        float percentPerLanguage = (float) systemConfigRepository.getByName(SystemConfigType.PERCENT_LANGUAGE).getValue() / job.getLanguageLevels().size();

        if (job.getJobTitle().getId().equals(cv.getJobTitle().getId())) {
            score += systemConfigRepository.getByName(SystemConfigType.PERCENT_JOB_TITLE).getValue();
        }

        if (job.getWorkingMode().getId().equals(cv.getWorkingMode().getId())) {
            score += systemConfigRepository.getByName(SystemConfigType.PERCENT_WORKING_MODE).getValue();
        }

        for (SkillLevel skillLevel : job.getSkillLevels()) {
            for (SkillLevel skillLevelOfCandidate : cv.getSkillLevels()) {
                if (skillLevelOfCandidate.getSkill().getId().equals(skillLevel.getSkill().getId())) {
                    score += percentPerSkill;
                }
            }
        }

        for (LanguageLevel languageLevel : job.getLanguageLevels()) {
            for (LanguageLevel languageLevelOfCandidate : cv.getLanguageLevels()) {
                if (languageLevelOfCandidate.getProgramLanguage().getId().equals(languageLevel.getProgramLanguage().getId())) {
                    score += percentPerLanguage;
                }
            }
        }

        if (score > 50) {
            for (CvShared cvShared : cv.getCvSharedList()) {
                try{
                    notificationService.sendNotification("Matching detection", "Your candidate " + cv.getFullName() + "matching with " + job.getJobTitle(), "", cvShared.getHeadhunter().getId());
                }catch (Exception e){
                    System.err.println("Error sent notification to "+ cvShared.getHeadhunter().getAccount().getFullName());
                }
            }
        }

        return score;
    }

    @Override
    public void calculationThread() {
        float per = 0;
        float count = 0;
        List<Job> jobs = jobRepository.findActiveJob();
        List<CV> cvs = cvRepository.findAll();
        per = 100 / jobs.size();
        System.out.println("Total: " + jobs.size());
        for (Job job : jobs) {
            for (CV cv : cvs) {
                try{
                    MatchingScore matchingScore = matchingRepository.findMatchingScoreByJobIdAndCvId(job.getId(), cv.getId());
                    if (matchingScore == null) {
                        matchingScore = new MatchingScore();
                        matchingScore.setJob(job);
                        matchingScore.setCv(cv);
                    }
                    matchingScore.setScore(calculationMatching(job.getId(), cv.getId()));
                    matchingRepository.save(matchingScore);
                }catch (Exception e){
                    System.out.print("JobID: "+job.getId());
                    System.out.println(" - CVID: "+cv.getId());
                }
            }

            count += per;
            System.out.println("=====>"+count+"%<=====");
        }
    }

    @Transactional
    @Override
    public void matchingByCVID(UUID cvId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<Job> jobs = jobRepository.findActiveJob();
                CV cv = cvRepository.findCVById(cvId);
                for (Job job : jobs) {
                    float score = calculationMatching(job.getId(), cvId);
                    MatchingScore matchingScore = new MatchingScore();
                    matchingScore.setJob(job);
                    matchingScore.setCv(cv);
                    matchingScore.setScore(score);
                    matchingRepository.save(matchingScore);
                }
            }
        };
        new Thread(r).start();
    }

    @Transactional
    @Override
    public void matchingByJobID(UUID jobId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                List<CV> cvs = cvRepository.findAll();
                Job job = jobRepository.findJobById(jobId);
                for (CV cv : cvs) {
                    System.out.println(cv.getId());
                    float score = calculationMatching(job.getId(), cv.getId());
                    MatchingScore matchingScore = new MatchingScore();
                    matchingScore.setJob(job);
                    matchingScore.setCv(cv);
                    matchingScore.setScore(score);
                    matchingRepository.save(matchingScore);
                }
            }
        };
        new Thread(r).start();
    }

    @Override
    public List<MatchingListResponse> matching(UUID headhunterId) {
        List<MatchingScore> matchingScores = null;
        List<MatchingListResponse> matchingScoresResponse = new ArrayList<>();
        try {
            matchingScores = matchingRepository.findMatchingScoreBy(headhunterId);
            for (MatchingScore matchingScore : matchingScores) {
                CandidateApply candidateApply = candidateApplyRepository.checkDuplicateApply(matchingScore.getCv().getId(), matchingScore.getJob().getId());
                if (candidateApply == null) {
                    CvShared cvShared = cvSharedRepository.findByCvIdAndHeadhunterId(matchingScore.getCv().getId(), headhunterId);
                    List<CV> cv = cvRepository.getCVByJobAndEmailOrPhone(matchingScore.getJob().getId(), cvShared.getCv().getEmail(), cvShared.getCv().getPhone());
                    MatchingListResponse matchingListResponse = new MatchingListResponse();
                    matchingListResponse.setJob(matchingScore.getJob());
                    matchingListResponse.setCv(matchingScore.getCv());
                    matchingListResponse.setScore(matchingScore.getScore());
                    matchingListResponse.setCvSharedId(cvShared.getId());
                    if (cv.isEmpty()) {
                        matchingScoresResponse.add(matchingListResponse);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matchingScoresResponse;
    }
}
