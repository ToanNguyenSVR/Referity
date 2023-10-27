package online.referity.dto.response;

import lombok.Data;
import online.referity.entity.CV;
import online.referity.entity.Job;
import online.referity.entity.MatchingScore;

import java.util.UUID;

@Data
public class MatchingListResponse {
    UUID cvSharedId;
    Job job;
    CV cv;
    float score;
}
