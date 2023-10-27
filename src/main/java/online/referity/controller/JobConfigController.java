package online.referity.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.AcceptApplyRequest;
import online.referity.dto.request.AssignJobRequest;
import online.referity.dto.request.JobStageRequest;
//import online.referity.dto.request.UpdateJobStageRequest;
import online.referity.dto.response.ConfirmBillStageResponse;
import online.referity.dto.response.JobReport;
import online.referity.entity.*;
//import online.referity.enums.SetUpStage;
import online.referity.service.CompanyService;
import online.referity.service.JobConfigService;
import online.referity.service.JobService;
import online.referity.utils.ResponseHandler;
import online.referity.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
//@ApplicationScope
public class JobConfigController {


    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    JobConfigService jobConfigService;

    @Autowired
    Validation validation;

    @Autowired
    JobService jobService;

    @Autowired
    CompanyService companyService;

    @PutMapping("job-stage/{stageId}")
    public ResponseEntity updateJobStage(@Valid @RequestBody JobStageRequest data, @PathVariable UUID stageId,
                                         BindingResult result) {
        JobStage jobStage = jobConfigService.updateJobStage(data);

        return responseHandler.response(200, "Success update stage of job !", jobStage);
    }
//    @PutMapping("open-job/{jobId}")
//    public ResponseEntity openJob(@PathVariable UUID jobId) {
//        Transaction transaction = jobConfigService.getTransaction(null, jobId , SetUpStage.OPEN  );
//
//        return responseHandler.response(200, "Success get Transaction stage of job !", transaction);
//    }
////    @PutMapping("open-job/{jobId}")
////    public ResponseEntity get(@PathVariable UUID jobId) {
////        Transaction transaction = jobConfigService.getTransaction(null, jobId , SetUpStage.OPEN  );
////
////        return responseHandler.response(200, "Success get Transaction stage of job !", transaction);
////    }
////    @PutMapping("job-stage/")
////    public ResponseEntity close(@Parameter UUID jobId, @Parameter UUID stageId ) {
////        Transaction transaction = jobConfigService.getTransaction(stageId,   jobId , SetUpStage.CLOSE);
////
////        return responseHandler.response(200, "Success Close stage of job !", transaction);
////    }

    @PutMapping("confirm-apply/{applyId}")
    public ResponseEntity accept(@RequestBody AcceptApplyRequest acceptApplyRequest, @PathVariable String applyId , BindingResult result) {
        ApplyStage applyStage = jobConfigService.acceptApply(UUID.fromString(applyId) , acceptApplyRequest.getResultApply() , acceptApplyRequest.getAccountCompanyId() , acceptApplyRequest.getNote());
        return responseHandler.response(200, "Success confirm apply of candidate !", applyStage);
    }

    @PutMapping("assign-job")
    public ResponseEntity assignJob(@RequestBody AssignJobRequest jobRequest ) {

        CompanyStaff job = companyService.assignJob(jobRequest);
        return responseHandler.response(200, "Success assign staff to job !", job);
    }

    @GetMapping("confirm-pay")
    public ResponseEntity getConfirmPay(@Parameter String jobRequest ) {
        ConfirmBillStageResponse result = jobConfigService.getBillOfStage(UUID.fromString(jobRequest));
        return responseHandler.response(200, "Success get Bill of job stage !", result);
    }

    @PutMapping("start-stage/{jobStageId}")
    public ResponseEntity startStage(@PathVariable String jobStageId , @RequestParam String accountId ) {
           JobStage jobStage = jobConfigService.startStage(UUID.fromString(jobStageId) , UUID.fromString(accountId) );
        return responseHandler.response(200, "Success finish round of job !", jobStage);
    }
    @GetMapping("job-report/{jobId}")
    public ResponseEntity getJobReport (@PathVariable UUID jobId ){
        JobReport result = jobService.getJobReport(jobId);
        return responseHandler.response(200, "Success get job report !", result);


    }
}
