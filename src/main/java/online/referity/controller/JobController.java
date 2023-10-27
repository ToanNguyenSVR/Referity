package online.referity.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.CreateJobRequest;
import online.referity.dto.response.JobResponse;
import online.referity.entity.Job;
import online.referity.enums.JobLevel;
import online.referity.service.CompanyService;
import online.referity.service.JobConfigService;
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
public class JobController {

    @Autowired
    CompanyService companyService;

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    JobConfigService jobConfigService;

    @Autowired
    Validation validation;
//    @RequestParam(required = false)String key , @RequestParam(required = false) UUID wokingModeId, @RequestParam(required = false)UUID jobTitleId, @RequestParam(required = false)LevelType levelType, @RequestParam(required = false) , @RequestParam(required = false), @RequestParam(required = false)
    @GetMapping("job")
    public ResponseEntity getJob(@RequestParam(required = false) Integer salaryForm,
                                 @RequestParam(required = false) Integer salaryTo,
                                 @RequestParam(required = false) List<UUID> skills,
                                 @RequestParam(required = false) List<UUID> languages,
                                 @RequestParam(required = false) UUID workingModeId,
                                 @RequestParam(required = false) UUID cityId,
                                 @RequestParam(required = false)List<JobLevel> levels ,
                                 @RequestParam(required = false) String key) {
        List<JobResponse> jobs = companyService.getJob(salaryForm,salaryTo,skills,languages,workingModeId,cityId,levels,key );

        ResponseEntity response = responseHandler.response(201, "Successfully list job of company", jobs);
        return response;
    }

    @GetMapping("job/{companyId}")
    public ResponseEntity getJob(@PathVariable String companyId) {
        List<CreateJobRequest> jobs = companyService.getJobByCompanyId(UUID.fromString(companyId));
        ResponseEntity response = responseHandler.response(201, "Successfully list job of company", jobs);
        return response;
    }
    @GetMapping("job-assign/{staffId}")
    public ResponseEntity getJobByStaff(@PathVariable String staffId) {
        List<CreateJobRequest> jobs = companyService.getJobByStaffId(UUID.fromString(staffId));
        ResponseEntity response = responseHandler.response(201, "Successfully list job of company", jobs);
        return response;
    }
    @GetMapping("job/{companyId}/verify")
    public ResponseEntity getJobVerify(@PathVariable UUID companyId) {
        List<CreateJobRequest> jobs = companyService.getJobVerifyByCompanyId(companyId);
        ResponseEntity response = responseHandler.response(201, "Successfully list job of company", jobs);
        return response;
    }
    @GetMapping("job-confirm")
    public ResponseEntity getJobConfirm() {
        List<CreateJobRequest> jobs = companyService.getJobConfirm();
        ResponseEntity response = responseHandler.response(201, "Successfully get list job confirm ", jobs);
        return response;
    }
    @GetMapping("job/{companyId}/active")
    public ResponseEntity getJobActive(@PathVariable UUID companyId) {
        List<JobResponse> jobs = companyService.getJobActiveByCompanyId(companyId);
        ResponseEntity response = responseHandler.response(201, "Successfully list job of company", jobs);
        return response;
    }

    @GetMapping("job-detail/{jobId}")
    public ResponseEntity getJobById(@PathVariable UUID jobId) {
        CreateJobRequest jobs = companyService.getJobById(jobId);
        ResponseEntity response = responseHandler.response(201, "Successfully list job of company", jobs);
        return response;
    }

    @PostMapping("job")
    public ResponseEntity createJob(@Valid @RequestBody CreateJobRequest data, BindingResult result) {
        validation.validate(result);
        Job job = companyService.createJob(data);
        ResponseEntity response = responseHandler.response(201, "Successfully registered new Job!", job);
        return response;
    }

    @PutMapping("job/{id}")
    public ResponseEntity updateJob(@Valid @RequestBody CreateJobRequest data, @PathVariable UUID id, BindingResult result) {
        validation.validate(result);
        Job loginResponse = companyService.updateJob(data, id);
        return responseHandler.response(200, "Update  success!", loginResponse);

    }

    @DeleteMapping("job/{id}")
    public ResponseEntity deleteJob( @PathVariable UUID id) {

        Job loginResponse = companyService.deleteJob( id);
        return responseHandler.response(200, "Delete success!", loginResponse);

    }



}
