package online.referity.repository;

import online.referity.entity.WorkingMode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkingModeRepository extends JpaRepository<WorkingMode, UUID> {
}
