package online.referity.dto.response;

import lombok.*;
import online.referity.enums.ResultStage;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountCandidateApply {

    ResultStage label ;
    int[] data ;
}
