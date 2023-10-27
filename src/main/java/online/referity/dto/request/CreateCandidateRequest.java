package online.referity.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class CreateCandidateRequest {



    private CvRequest cvRequest;

    private UUID workingModeId ;

    private UUID jobTitle ;



}
