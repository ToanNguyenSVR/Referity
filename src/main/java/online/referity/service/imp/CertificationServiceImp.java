package online.referity.service.imp;

import online.referity.dto.request.CertificationRequest;
import online.referity.entity.CV;
import online.referity.entity.Certification;
import online.referity.repository.CertificationRepository;
import online.referity.service.CertificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CertificationServiceImp implements CertificationService {

    @Autowired
    CertificationRepository certificationRepository ;

    ModelMapper modelMapper = new ModelMapper();


    @Override
    public Certification initCertification(CertificationRequest certificationRequest , CV cv) {
//        Certification certification = certificationRepository.findByCertificationUrl(certificationRequest.getCertificationUrl());
//        if(certification != null) return certification;
        Certification certification = modelMapper.map(certificationRequest , Certification.class);
        certification.setCv(cv);
       return certification;
    }

    @Override
    public List<Certification> getByCv(UUID cvId) {
       return certificationRepository.findByCvId(cvId);
    }
}
