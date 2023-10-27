package online.referity.service;

import online.referity.dto.request.LanguageRequest;
import online.referity.dto.request.UpdateLanguageRequest;
import online.referity.dto.response.LanguageResponse;
import online.referity.entity.CV;
import online.referity.entity.Job;
import online.referity.entity.LanguageLevel;
import online.referity.entity.ProgramLanguage;
import online.referity.enums.LevelType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProgramLanguageService {

    public ProgramLanguage create(String Language);

    public List<ProgramLanguage> creates(List<ProgramLanguage> languages);

    public ProgramLanguage update( UpdateLanguageRequest language  , UUID id );
    public LanguageLevel updateLanguageJob( LanguageRequest languageRequest);
    public ProgramLanguage delete( UUID id  );

    public LanguageLevel addLanguage (CV cv , Job job , LanguageRequest languageRequest);

    public List<ProgramLanguage> seachLanguage( String key );

    public List<LanguageResponse> seachLanguaeLevelById (UUID Id , LevelType Type );

    public List<ProgramLanguage> getProgramLanguage();


}
