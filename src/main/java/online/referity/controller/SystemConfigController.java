package online.referity.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.entity.SystemConfig;
import online.referity.service.SystemConfigService;
import online.referity.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class SystemConfigController {

    @Autowired
    SystemConfigService systemConfigService;

    @Autowired
    ResponseHandler responseHandler;

    @GetMapping("system-config")
    public ResponseEntity getSystemConfig(){
        List<SystemConfig> systemConfigs = systemConfigService.get();
        return responseHandler.response(200, "Successfully get system config", systemConfigs);
    }

    @PutMapping("system-config/{systemConfigId}")
    public ResponseEntity getSystemConfig(@PathVariable UUID systemConfigId, @RequestBody SystemConfig systemConfig){
        SystemConfig systemConfigNew = systemConfigService.update(systemConfigId, systemConfig);
        return responseHandler.response(200, "Successfully get system config", systemConfigNew);
    }
}
