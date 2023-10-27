package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.CompanyAddInformationRequest;
import online.referity.dto.request.HeadhunterAdditionalInformation;
import online.referity.dto.request.RecruitStageRequest;
import online.referity.entity.Account;
import online.referity.entity.Company;
import online.referity.entity.RecruitStage;
import online.referity.service.AccountService;
import online.referity.service.RecruitStageService;
import online.referity.utils.Request;
import online.referity.utils.ResponseHandler;
import online.referity.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class RecruitStageController {

    @Autowired
    RecruitStageService service;

    @Autowired
    Request request;

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    Validation validation;

    @PostMapping("recruit-stage")
    @Operation(summary = "Create stage for system ")
    public ResponseEntity createStage(@Valid @RequestBody RecruitStageRequest request, BindingResult result) {
        validation.validate(result);
        RecruitStage stage = service.createProcess(request);
        ResponseEntity response = responseHandler.response(201, "Successfully add recruit stage ", stage);
        return response;
    }

    @PutMapping("recruit-stage/{stageId}")
    @Operation(summary = "Update stage of system ")
    public ResponseEntity updateStage(@Valid @RequestBody RecruitStageRequest recruitStage, @PathVariable UUID stageId, BindingResult result) {
        validation.validate(result);
        RecruitStage stage = service.update(recruitStage, stageId);
        ResponseEntity response = responseHandler.response(201, "Successfully update stage of company!", stage);
        return response;
    }

    @DeleteMapping("recruit-stage/{stageId}")
    @Operation(summary = "Delete stage of system ")
    public ResponseEntity updateStage(@PathVariable UUID stageId) {
        RecruitStage stage = service.deleted(stageId);
        ResponseEntity response = responseHandler.response(201, "Successfully deleted stage of company!", stage);
        return response;
    }

    @GetMapping("recruit-stage")
    @Operation(summary = "Get system stage")
    public ResponseEntity getInformation() {
        List<RecruitStage> stages = service.get();
        ResponseEntity response = responseHandler.response(200, "Successfully get stage!", stages);
        return response;
    }

}
