package online.referity.service;

import online.referity.dto.response.*;
import online.referity.entity.*;
import online.referity.enums.ResultStage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface HeadhunterService {

//    CandidateApplyResponse referCvToJob (UUID cvsharedId , UUID jobId);
//
    CvSharedResponse acceptCV (UUID headhunterId , UUID cvSharedId);

    List<CandidateResponse> getCandidate(UUID headhunterId, UUID jobId);
//    List<CandidateResponse> getCandidateVerify();

    List<TopHeadhunterResponse> getTopHeadhunter ();

    RatingOfHeadhunterResponse getRatingOfHeadhunter(UUID headhunterId);

    HeadhunterConfigResponse getConfigOfHeadhunter(UUID headhunterId);
}
