package online.referity.service.imp;

import online.referity.dto.response.*;
import online.referity.entity.*;
import online.referity.enums.*;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.Duplicate;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.*;
import online.referity.service.HeadhunterService;
import online.referity.service.NotificationService;
import org.checkerframework.checker.units.qual.A;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.Buffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HeadhunterServiceImpl implements HeadhunterService {

    @Autowired
    HeadhunterRepository headhunterRepository;
    @Autowired
    CvSharedRepository cvSharedRepository;

    @Autowired
    SystemConfigRepository systemConfigRepository;
    @Autowired
    JobStageRepository jobStageRepository;

    @Autowired
    NotificationService notificationService;
    @Autowired
    JobRepository jobRepository;

    @Autowired
    CandidateApplyRepository candidateApplyRepository;

    @Autowired
    ApplyStageRepository applyStageRepository;

    @Autowired
    RatingRepository ratingRepository;


    ModelMapper modelMapper = new ModelMapper();


    @Override
    public CvSharedResponse acceptCV(UUID headhunterId, UUID cvSharedId) {
        CvSharedResponse result = new CvSharedResponse();
        Headhunter headhunter = headhunterRepository.findHeadhunterById(headhunterId);
        if (headhunter == null) throw new BadRequest("Headhunter Not found ");
        CvShared cvShared = cvSharedRepository.getCvSharedById(cvSharedId);
        if (cvShared == null) throw new BadRequest(" Cv Shared not found ");
        if (!cvShared.getHeadhunter().equals(headhunter)) throw new BadRequest("You don't have permission to do ");
        cvShared.setStatus(CvSharedStatus.ACTIVE);
        cvShared = cvSharedRepository.save(cvShared);
        result.setHeadhunter(headhunter.getAccount());
        result.setStatus(cvShared.getStatus());
        result.setCreateDate(cvShared.getCreateDate());
        result.setSharedId(cvSharedId);
        result.setExpireDate(cvShared.getExpireDate());
        String body = "Headhunter " + headhunter.getAccount().getFullName() + " was accept  your cv ";
        notificationService.sendNotification("Your CV was accept by headhunter ", body, null, cvShared.getCv().getCandidate().getAccount().getId());
        return result;
    }

    @Override
    public List<CandidateResponse> getCandidate(UUID headhunterId, UUID jobId) {
        List<CvShared> cvShareds;
        if (jobId == null) {
            cvShareds = cvSharedRepository.findByHeadhunterId(headhunterId);
        } else {
            cvShareds = cvSharedRepository.findByHeadhunterId(headhunterId, jobId);
        }
        if (cvShareds.isEmpty()) return null;
        List<CandidateResponse> result = new ArrayList<>();
        for (CvShared cv : cvShareds) result.add(convertToCandidateResponse(cv, jobId));
        return result.stream().sorted(Comparator.comparing(CandidateResponse::getScore).reversed()).collect(Collectors.toList());
    }

    public TopHeadhunterResponse convertToTop(Headhunter headhunter) {
        TopHeadhunterResponse topHeadhunterResponse = new TopHeadhunterResponse();
        topHeadhunterResponse.setId(headhunter.getId());
//            topHeadhunterResponse.setUuid(headhunterResponse.getUuid());
        topHeadhunterResponse.setAvatar(headhunter.getAccount().getAvatar());
        topHeadhunterResponse.setDescription(headhunter.getDescription());
        topHeadhunterResponse.setFullName(
                headhunter.getAccount().getFullName());
        return topHeadhunterResponse;
    }

    @Override
    public List<TopHeadhunterResponse> getTopHeadhunter() {
        List<Headhunter> headhunters = headhunterRepository.findAll();
        List<TopHeadhunterResponse> result = new ArrayList<>();
        if (headhunters.isEmpty()) return null;
        for (Headhunter headhunter : headhunters) {
            TopHeadhunterResponse topHeadhunter = convertToTop(headhunter);
            float avg = 0 ;
            try {
                avg = ratingRepository.countHeadhunterRating(headhunter.getId());
            }catch (Exception e ){

            }

            topHeadhunter.setAvg_star(avg);
            result.add(topHeadhunter);
        }
        result.sort(Comparator.comparing(TopHeadhunterResponse::getAvg_star).reversed());
        return result;
    }

    @Override
    public RatingOfHeadhunterResponse getRatingOfHeadhunter(UUID headhunterId) {
        Headhunter headhunter = headhunterRepository.findHeadhunterById(headhunterId);
        if(headhunter == null ) throw  new BadRequest("Internal Server Error.");
        RatingOfHeadhunterResponse result = new RatingOfHeadhunterResponse();
        List<RatingResponse> ratingResponses = new ArrayList<>();
        HashMap<Integer , Integer> mapStar = new HashMap<>();
        float avg = 0 ;
        try {
            avg = ratingRepository.countHeadhunterRating(headhunter.getId());
        }catch (Exception e ){

        }

        result.setTotalStar(avg);
        for (int i = 1 ; i<= 5 ; i ++){
            mapStar.put(i , 0 );
        }
        for (Rating rating : headhunter.getRatings()){
            RatingResponse ratingResponse = convertToRatingResponse(rating);
            ratingResponses.add(ratingResponse);
            mapStar.replace(rating.getStar(),  mapStar.get(rating.getStar())+ 1);

        }

        result.setRatingResponses(ratingResponses);
        result.setMapStarPercent(mapStar);
        return  result;
    }

    @Override
    public HeadhunterConfigResponse getConfigOfHeadhunter(UUID headhunterId) {
        HeadhunterConfigResponse headhunterConfigResponse = new HeadhunterConfigResponse();
        Headhunter headhunter = headhunterRepository.findHeadhunterById(headhunterId);
        if(headhunter == null ) throw  new RuntimeException("Internal Server Error");
        LocalDateTime fromTime = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth().getValue() , 1, 0, 0);

        int numberCandidateApplied = 0 ;
        float avg = 0 ;
        try {
            numberCandidateApplied = candidateApplyRepository.countApplyIn1MonthByHeadhunterId(headhunterId, fromTime);
            avg = ratingRepository.countHeadhunterRating(headhunterId);

        }catch (Exception e ){
            e.printStackTrace();
        }
        int baseTurn =systemConfigRepository.getByName(SystemConfigType.NUMBER_JOB_HEADHUNTER_APPLY).getValue();
        headhunterConfigResponse.setReferTurns(baseTurn + (int) avg);
        headhunterConfigResponse.setReferedTurns(numberCandidateApplied);
        headhunterConfigResponse.setBalance(headhunter.getAccount().getWallet().getBalance().intValue());
        return  headhunterConfigResponse;

    }

    public RatingResponse convertToRatingResponse (Rating rating){
        RatingResponse result = modelMapper.map(rating, RatingResponse.class);

        if(rating.getHeadhunter() != null ){
            result.setHeadhunterName(rating.getHeadhunter().getAccount().getFullName());
            result.setHeadhunterAvatar(rating.getHeadhunter().getAccount().getAvatar());
            result.setHeadhunterID(rating.getHeadhunter().getId());

        }
        if(rating.getCompany() != null ){
            result.setCompanyName(rating.getCompany().getName());
            result.setCompanyAvatar(rating.getCompany().getLogoUrl());

        }
        if(rating.getCandidateApply() != null ){
            result.setCandidateEmail(rating.getCandidateApply().getCvShared().getCv().getEmail());
            result.setCandidateName(rating.getCandidateApply().getCvShared().getCv().getFullName());
        }
        return  result;

    }

    public CandidateResponse convertToCandidateResponse(CvShared cvShared, UUID jobId) {
        CandidateResponse candidateResponse = modelMapper.map(cvShared.getCv(), CandidateResponse.class);
        candidateResponse.setCvShared(new CvSharedResponse(cvShared.getId(), cvShared.getCreateDate(), cvShared.getExpireDate(), cvShared.getStatus(), cvShared.getHeadhunter().getAccount()));
//        candidateResponse.setCreateDate(cvShared.getCreateDate());
//        candidateResponse.setSkillLevels(cvShared.getCv().getSkillLevels());
        candidateResponse.setAvatar(cvShared.getCv().getCandidate().getAccount().getAvatar());
        candidateResponse.setWorkingMode(cvShared.getCv().getWorkingMode().getMode());
        candidateResponse.setJobTitle(cvShared.getCv().getJobTitle().getPosition());
        for (MatchingScore matchingScore : cvShared.getCv().getMatchingScores()) {
            if (matchingScore.getJob().getId().equals(jobId)) {
                candidateResponse.setScore(matchingScore.getScore());
            }
        }
        return candidateResponse;
    }
}
