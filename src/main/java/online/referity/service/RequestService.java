package online.referity.service;

import online.referity.dto.request.CreateRequest;
import online.referity.dto.response.CvResponse;
import online.referity.dto.response.HeadhunterRequestResponse;
import online.referity.entity.HeadhunterRequest;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    public String createRequest(CreateRequest createRequest , UUID headhunterId);

    public HeadhunterRequest updateRequest(UUID requestId , CreateRequest createRequest );

    public List<HeadhunterRequestResponse> getRequestByCandidateId (UUID candidateId);

    public List<HeadhunterRequestResponse> getRequestByHeadhunterId (UUID headhunterId);


}
