package online.referity.service;

import online.referity.dto.request.CertificationRequest;
import online.referity.entity.CV;
import online.referity.entity.Certification;

import java.util.List;
import java.util.UUID;

public interface CertificationService {

    public Certification initCertification (CertificationRequest certificationRequest  , CV cv);

    public List<Certification> getByCv ( UUID cvId);


}
