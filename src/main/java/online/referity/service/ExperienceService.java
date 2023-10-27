package online.referity.service;

import online.referity.dto.request.CertificationRequest;
import online.referity.dto.request.ExperienceRequest;
import online.referity.entity.CV;
import online.referity.entity.CandidateExperience;
import online.referity.entity.Certification;

import java.util.List;
import java.util.UUID;

public interface ExperienceService {

    public CandidateExperience initExperience (ExperienceRequest experienceRequest  , CV cv);

    public List<CandidateExperience> getByCv (UUID cvId);

}
