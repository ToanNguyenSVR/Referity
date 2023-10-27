package online.referity.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter

public class CreateCandidateByUUID {

    private CreateCandidateRequest candidateRequest ;
    private CandidateRequest candidate ;
    private UUID cvId ;
}
