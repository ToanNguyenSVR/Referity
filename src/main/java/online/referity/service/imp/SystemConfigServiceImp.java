package online.referity.service.imp;

import online.referity.entity.SystemConfig;
import online.referity.repository.SystemConfigRepository;
import online.referity.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SystemConfigServiceImp implements SystemConfigService {

    @Autowired
    SystemConfigRepository systemConfigRepository;
    @Override
    public List<SystemConfig> get() {
        return systemConfigRepository.findAll();
    }

    @Override
    public SystemConfig update(UUID systemConfigId, SystemConfig systemConfig) {
        SystemConfig oldSystemConfig = systemConfigRepository.findSystemConfigById(systemConfigId);
        oldSystemConfig.setValue(systemConfig.getValue());
        return systemConfigRepository.save(oldSystemConfig);
    }
}
