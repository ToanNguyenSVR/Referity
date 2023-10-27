package online.referity.service;

import online.referity.entity.SystemConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface SystemConfigService {
    public List<SystemConfig> get();
    public SystemConfig update(UUID systemConfigId, SystemConfig systemConfig);
}
