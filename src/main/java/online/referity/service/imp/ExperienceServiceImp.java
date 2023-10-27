package online.referity.service.imp;

import online.referity.dto.request.ExperienceRequest;
import online.referity.entity.CV;
import online.referity.entity.CandidateExperience;
import online.referity.repository.ExperienceRepository;
import online.referity.service.ExperienceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ExperienceServiceImp  implements ExperienceService
{

    @Autowired
    ExperienceRepository experienceRepository;

    ModelMapper modelMapper = new ModelMapper();
    @Override
    public CandidateExperience initExperience(ExperienceRequest experienceRequest, CV cv) {
        CandidateExperience candidateExperience = modelMapper.map(experienceRequest , CandidateExperience.class);
        candidateExperience.setCv(cv);
        return candidateExperience;
    }

    @Override
    public List<CandidateExperience> getByCv(UUID cvId) {
        return experienceRepository.findByCvId(cvId);
    }
}
