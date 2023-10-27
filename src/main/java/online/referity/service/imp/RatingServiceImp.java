package online.referity.service.imp;

import online.referity.dto.request.RatingRequest;
import online.referity.dto.response.CandidateApplyReport;
import online.referity.dto.response.RatingResponse;
import online.referity.entity.*;
import online.referity.enums.RatingStar;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.*;
import online.referity.service.NotificationService;
import online.referity.service.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class RatingServiceImp implements RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    CandidateApplyRepository candidateApplyRepository;

    ModelMapper modelMapper = new ModelMapper();



    @Override
    public RatingResponse createRating(RatingRequest data) {
        CandidateApply candidateApply = candidateApplyRepository.findCandidateApplyById(data.getCandidateApplyId());
        if(candidateApply == null ) throw  new EntityNotFound("Candidate Apply not found ");
        Headhunter headhunter = candidateApply.getCvShared().getHeadhunter();
//        CvShared cvShared = candidateApply.getCvShared();
        Company company = candidateApply.getJob().getCompanyStaff().getCompany();

        Rating rating = new Rating();
        rating.setCreateAt(LocalDateTime.now());
        rating.setStar(data.getRatingStar());
        rating.setComment(data.getComment());
        rating.setCandidateApply(candidateApply);
        rating.setHeadhunter(headhunter);
        rating.setCompany(company);
        rating = ratingRepository.save(rating);
        String body = " You was received rating from company " + company.getName() + " with " + rating.getStar() + " Star ";
        notificationService.sendNotification("You was received rating from company "  , body , null , headhunter.getAccount().getId());

        return  convertToRatingResponse(rating);
    }

    @Override
    public List<RatingResponse> getRating(Integer ratingStar, UUID headhunterId , UUID companyId ) {
        List<Rating> ratings = new ArrayList<>();
        List<RatingResponse> result = new ArrayList<>();
        boolean haveHeadhunter = false;

        if(headhunterId != null ){
            haveHeadhunter = true ;
            ratings = ratingRepository.findAllByHeadhunterId(headhunterId);
        }
        if(companyId != null ){

            if(haveHeadhunter) {
                ratings = ratings.stream().filter(rating -> rating.getCompany().getId().equals(companyId)).collect(Collectors.toList());
            }else {
                ratings = ratingRepository.findAllByCompanyId(companyId);
            }
        }
        if (ratingStar != null ){
            ratings = ratings.stream().filter(rating -> rating.getStar() == ratingStar).collect(Collectors.toList());
        }

        for(Rating r : ratings) result.add(convertToRatingResponse(r));
        return result.stream().sorted(Comparator.comparing(RatingResponse::getStar)).collect(Collectors.toList());
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
}
