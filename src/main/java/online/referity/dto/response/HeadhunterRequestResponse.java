package online.referity.dto.response;

import lombok.Data;
import online.referity.entity.CvShared;
import online.referity.entity.Headhunter;
import online.referity.enums.RequestStatus;
import online.referity.enums.RequestType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class HeadhunterRequestResponse {
    private UUID id;
    private String requestTitle ;
    private String requestContent ;
    private LocalDateTime deadlineRequest ;
    private LocalDateTime createAt ;
    private String emailTo ;
    private TopHeadhunterResponse headhunter;
    private RequestStatus requestStatus ;
    private RequestType requestType ;
    CvShared cvShared;
}
