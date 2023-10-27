package online.referity.service;

import online.referity.dto.request.CandidateRequest;
import online.referity.dto.request.CreateCandidateRequest;
import online.referity.dto.request.CvSharedRequest;
import online.referity.dto.request.ExtendRequest;
import online.referity.dto.response.CandidateResponse;
import online.referity.dto.response.CvResponse;
import online.referity.dto.response.UpdateRequest;
import online.referity.entity.Candidate;
import online.referity.entity.CvShared;
import online.referity.enums.CvStatus;

import java.util.List;
import java.util.UUID;

public interface CandidateService {

    public Candidate initCandidate(CandidateRequest data ) ;
    public CvShared extendCvShared(ExtendRequest extendRequest ,UUID uuid);
    public CvShared updateCvShared(UpdateRequest extendRequest , UUID uuid);

    public CvResponse addCv(CreateCandidateRequest candidateRequest , UUID candidateId );

    public CvResponse updateCv(CreateCandidateRequest candidateRequest, UUID cvId);

    public CvResponse deleteCV(UUID cvId);

    public CvShared addCvToHeadhunter( UUID cvId,CandidateRequest candidateData,CreateCandidateRequest candidateRequest,  UUID headhunterId , UUID requestId );
    //public CvShared addCvToHeadhunterRepository(CandidateRequest candidateData,CreateCandidateRequest candidateRequest  , UUID headhunterId) ;

    public CvShared shareCv(CvSharedRequest cvSharedRequest);
    public CvResponse getCandidateByCode ( String code );

    public List<CvResponse> getCandidate ( UUID candidateId);

    List<CvShared> getCandidateSharedByHeadhunterId(UUID headhunterId);
    List<CandidateResponse> getCvSharedVerifyByHeadhunterId(UUID headhunterId);
    List<CandidateResponse> getCvSharedByCandidateId(UUID candidateId);
    List<CandidateResponse> getCvSharedVerifyByCandidateId(UUID candidateId);
    public Candidate deleteCandidate (UUID id );

    public Candidate changeStatus (UUID Cvid , CvStatus cvStatus);

    public CvResponse getCandidateInformation(UUID cvId) ;

    public void updateStatusDone(UUID candidateId);

    public List<CvResponse> getAllCandidateCV();


}
