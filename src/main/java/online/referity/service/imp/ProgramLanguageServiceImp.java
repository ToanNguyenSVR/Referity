package online.referity.service.imp;

import online.referity.dto.request.LanguageRequest;
import online.referity.dto.request.UpdateLanguageRequest;
import online.referity.dto.response.LanguageResponse;
import online.referity.entity.CV;
import online.referity.entity.Job;
import online.referity.entity.LanguageLevel;
import online.referity.entity.ProgramLanguage;
import online.referity.enums.LevelType;
import online.referity.enums.ValidateStatus;
import online.referity.exception.exceptions.BadRequest;
import online.referity.exception.exceptions.Duplicate;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.LanguageLevelRepository;
import online.referity.repository.ProgramLanguagRepository;
import online.referity.service.ProgramLanguageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProgramLanguageServiceImp implements ProgramLanguageService {

    @Autowired
    ProgramLanguagRepository programLanguagRepository ;

    @Autowired
    LanguageLevelRepository languageLevelRepository ;

    ModelMapper modelMapper = new ModelMapper();
    @Override
    public ProgramLanguage create(String language) {

        if(programLanguagRepository.checkDuplicate(language) != null) throw  new Duplicate("Language is exist ");
        return programLanguagRepository.save(new ProgramLanguage(null , language ,false, ValidateStatus.CHECK , null ));
    }
    @Override
    public List<ProgramLanguage> creates(List<ProgramLanguage> languages) {
        List<ProgramLanguage> list = new ArrayList<>() ;
        for (ProgramLanguage l: languages) {
            if(programLanguagRepository.checkDuplicate(l.getLanguage()) != null) throw  new Duplicate("Language is exist ");
            list.add(new ProgramLanguage(null , l.getLanguage() , false, ValidateStatus.ACTIVE , null ));
        }
        return programLanguagRepository.saveAll(list);
         }

    @Override
    public ProgramLanguage update(UpdateLanguageRequest data , UUID languageId) {
        ProgramLanguage language = programLanguagRepository.getById(languageId);
        if(language == null ) throw new BadRequest("Program Language not found ");
        modelMapper.map(data , language);
        return programLanguagRepository.save(language);
    }

    @Override
    public ProgramLanguage delete(UUID id) {
        ProgramLanguage language = programLanguagRepository.getById(id);
        if(language == null ) throw new BadRequest("Program Language not found ");
        language.setDelete(true);
        return programLanguagRepository.save(language);
    }

    @Override
    public LanguageLevel addLanguage(CV cv , Job job ,LanguageRequest languageRequest) {
       ProgramLanguage programLanguage = programLanguagRepository.getById(languageRequest.getLanguageId());
      LanguageLevel languageLevel = new LanguageLevel();
       if(programLanguage == null ) throw new EntityNotFound("Program Language not found ");
       languageLevel.setProgramLanguage(programLanguage);
       if(cv != null ) languageLevel.setCv(cv);
       if(job != null ) languageLevel.setJob(job);
       if(cv == null && job == null ) throw  new BadRequest("Bad request");
       languageLevel.setPonit(languageRequest.getPonit());
       languageLevel.setBest(languageRequest.isBest());
        return languageLevel;
    }

@Override
    public LanguageLevel updateLanguageJob( LanguageRequest languageRequest) {
        Optional<LanguageLevel> level = languageLevelRepository.findById(languageRequest.getLanguageLevelId());
        if(!level.isPresent()  ) throw new EntityNotFound("Language level not found ");
        Optional<ProgramLanguage> language = programLanguagRepository.findById(languageRequest.getLanguageId()) ;
       if(!language.isPresent()) throw  new EntityNotFound("Program Language not found ");
        level.get().setProgramLanguage(language.get());
        level.get().setPonit(languageRequest.getPonit());
        level.get().setBest(languageRequest.isBest());
        return languageLevelRepository.save(level.get());
    }

    @Override
    public List<ProgramLanguage> seachLanguage(String key) {
        return programLanguagRepository.searchLanguage(key);
    }

    @Override
    public List<LanguageResponse> seachLanguaeLevelById(UUID id, LevelType type) {
       List<LanguageResponse> listResult = new ArrayList<>() ;
       List<LanguageLevel> list = null ;
       switch (type) {
           case CV :list =  languageLevelRepository.findByCvId(id);
           case JOB: list = languageLevelRepository.findByJobId(id);
           default: break;
       }
       if(list == null ) return null ;
       modelMapper.map(list , listResult);
      // modelMapper.map(list.);
        return listResult;
    }

    @Override
    public List<ProgramLanguage> getProgramLanguage() {
        return programLanguagRepository.findAll();
    }
}
