package online.referity.repository;

import online.referity.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CertificationRepository extends JpaRepository<Certification , UUID> {

    public Certification findByCertificationUrl (String certificationUrl);

    public List<Certification> findByCvId( UUID cvId );

}
