package online.referity.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.RecruitStageRequest;
import online.referity.entity.JobTitle;
import online.referity.entity.RecruitStage;
import online.referity.entity.Skill;
import online.referity.service.JobTitleService;
import online.referity.service.SkillService;
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
public class JobTitleContronller {

    @Autowired
    JobTitleService service;

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    Validation validation;


    @GetMapping("job-title")
    public ResponseEntity getJobTitles(){
        List<JobTitle> result = service.getJobTitle();
        return responseHandler.response(200, "Successfully get job title", result);
    }

    @GetMapping("job-title/{key}")
    @Operation(summary = "Seach skill ")
    public ResponseEntity seachSkill (@PathVariable String key ){
        List<JobTitle> result = service.seachJobTitle(key);
        ResponseEntity response = responseHandler.response(201, "Successfully update stage of company!", result);
        return response;
    }
    @PostMapping("job-title")
    @Operation(summary = "Create Skill ")
    public  ResponseEntity create (@Valid @RequestBody JobTitle jobTitle ){
        JobTitle result = service.createJobTitle(jobTitle);
        ResponseEntity response = responseHandler.response(201, "Successfully update stage of company!", result);
        return response;
    }
    @PostMapping("job-titles")
    @Operation(summary = "Create list Skill ")
    public  ResponseEntity create (@Valid @RequestBody List<JobTitle> jobTitles ){
        List<JobTitle> result = service.createJobTitle(jobTitles);
        ResponseEntity response = responseHandler.response(201, "Successfully update stage of company!", result);
        return response;
    }

    @PutMapping("job-title/{jobTitleId}")
    @Operation(summary = "Update stage of system ")
    public ResponseEntity updateStage(@Valid @RequestBody JobTitle jobTitle, @PathVariable UUID jobTitleId) {
        JobTitle result = service.update(jobTitle, jobTitleId);
        ResponseEntity response = responseHandler.response(201, "Successfully update stage of company!", result);
        return response;
    }

    @DeleteMapping("job-title/{jobTitleId}")
    @Operation(summary = "Delete stage of system ")
    public ResponseEntity updateStage(@PathVariable UUID jobTitleId) {
        JobTitle result = service.delete(jobTitleId);
        ResponseEntity response = responseHandler.response(201, "Successfully deleted stage of company!", result);
        return response;
    }

}
