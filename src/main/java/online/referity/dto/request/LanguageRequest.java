package online.referity.dto.request;

import lombok.Data;
import online.referity.entity.LanguageLevel;

import java.util.UUID;

@Data
public class LanguageRequest {

    private UUID LanguageId ;
    private String LanguageName ;
    private UUID languageLevelId ;
    private int ponit ;
    // true đưa lên đầu
    private boolean isBest ;
}
