package online.referity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import online.referity.entity.Account;
import online.referity.entity.Headhunter;
import online.referity.enums.CvSharedStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CvSharedResponse {

    private UUID sharedId ;

    private LocalDateTime createDate ;

    private LocalDateTime expireDate ;

    private CvSharedStatus status ;

    private Account headhunter;
}
