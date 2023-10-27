package online.referity.service;

import online.referity.dto.request.RatingRequest;
import online.referity.dto.response.RatingResponse;
import online.referity.enums.RatingStar;

import java.util.List;
import java.util.UUID;

public interface RatingService {

    public RatingResponse createRating(RatingRequest data);

    public List<RatingResponse> getRating(Integer ratingStar, UUID headhunterId , UUID companyId );




}
