package online.referity.repository;

import online.referity.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CampusRepository extends JpaRepository<Campus , UUID> {
    List<Campus> findCampusesByCompanyIdAndIsDeletedFalse(UUID id);

    Campus findCampusById(UUID campusId);
}
