package online.referity.service;


import online.referity.dto.response.MatchingListResponse;
import online.referity.entity.CV;
import online.referity.entity.Job;
import online.referity.entity.MatchingScore;

import java.util.List;
import java.util.UUID;

public interface MatchingService {
    public float calculationMatching(UUID jobId, UUID cvId);

    public void calculationThread();

    public void matchingByCVID(UUID cvId);

    public void matchingByJobID(UUID jobId);

    public List<MatchingListResponse> matching(UUID headhunterId);
}
