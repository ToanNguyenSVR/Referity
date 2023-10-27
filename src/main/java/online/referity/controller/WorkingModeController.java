package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.entity.JobTitle;
import online.referity.entity.Skill;
import online.referity.entity.WorkingMode;
import online.referity.service.SkillService;
import online.referity.service.WorkingModeService;
import online.referity.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class WorkingModeController {

    @Autowired
    WorkingModeService service ;
    @Autowired
    ResponseHandler responseHandler;

    @GetMapping("working-mode")
//    @Operation(summary = " ")
    public ResponseEntity seachSkill (){
        List<WorkingMode> result = service.get();
        return responseHandler.response(200, "Successfully get working mode", result);
    }
    @PostMapping("working-mode")
    @Operation(summary = "Create workingMode ")
    public  ResponseEntity create (@Valid @RequestBody WorkingMode mode ){
        WorkingMode result = service.create(mode);
        ResponseEntity response = responseHandler.response(201, "Successfully update stage of company!", result);
        return response;
    }

    @PutMapping("working-mode/{modeId}")
    @Operation(summary = "Update stage of system ")
    public ResponseEntity updateStage(@Valid @RequestBody WorkingMode mode, @PathVariable UUID modeId) {
        WorkingMode result = service.update(mode, modeId);
        ResponseEntity response = responseHandler.response(201, "Successfully update stage of company!", result);
        return response;
    }

    @DeleteMapping("working-mode/{modeId}")
    @Operation(summary = "Delete stage of system ")
    public ResponseEntity updateStage(@PathVariable UUID modeId) {
        WorkingMode result = service.delete(modeId);
        ResponseEntity response = responseHandler.response(201, "Successfully deleted stage of company!", result);
        return response;
    }

}
