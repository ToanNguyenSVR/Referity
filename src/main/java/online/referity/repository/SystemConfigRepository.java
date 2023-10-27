package online.referity.repository;

import online.referity.entity.SystemConfig;
import online.referity.enums.SystemConfigType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SystemConfigRepository extends JpaRepository<SystemConfig , UUID> {

    SystemConfig getByName(SystemConfigType name);

    SystemConfig findSystemConfigById(UUID systemConfigId);

    SystemConfig findSystemConfigByName(SystemConfigType name);
}
