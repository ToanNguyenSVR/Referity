package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.request.ConfirmCandidateApplyRequest;
import online.referity.dto.response.CandidateApplyConfirmResqonse;
import online.referity.dto.response.CandidateApplyResponse;
import online.referity.dto.response.JobActiveResponse;
import online.referity.dto.response.ProcessCandidateApplyResponse;

import online.referity.entity.CandidateApply;

import online.referity.enums.ResultStage;
import online.referity.service.*;
import online.referity.utils.ResponseHandler;
import online.referity.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class ReferController {

    @Autowired
    ReferService referService;

    @Autowired
    ResponseHandler responseHandler;


    @Autowired
    Validation validation;

    @PostMapping("refer-cv/{jobId}/{cvSharedId}")
    @Operation(summary = "Refer Cv to Job")
    public ResponseEntity referCvToJob(@PathVariable UUID jobId, @PathVariable UUID cvSharedId) {
        CandidateApplyResponse result = referService.referCvToJob(cvSharedId, jobId);
        ResponseEntity response = responseHandler.response(200, "Refer success!", result);
        return response;
    }

    @PostMapping("confirm-cv")
    public ResponseEntity confirm(@RequestBody ConfirmCandidateApplyRequest request) {
        CandidateApply apply = referService.confirmApply(request.getApplyCode(), request.getResultApply() , request.getNote());
        return responseHandler.response(200, "Confirm success!", apply);
    }


    @GetMapping("candidate-apply")
    @Operation(summary = "Get Candidate apply and job apply to ")
    public ResponseEntity getByJobId(@RequestParam(required = false) ResultStage resultStage
            , @RequestParam(required = false) UUID jobId
            , @RequestParam(required = false) UUID headhunterId
            , @RequestParam(required = false) UUID cvId
            ,  @RequestParam(required = false) UUID candidateId) {
        List<ProcessCandidateApplyResponse> apply = referService.get(resultStage, headhunterId, jobId, cvId , candidateId);
        return responseHandler.response(200, "Get success!", apply);
    }


    //    @GetMapping("applied-stage/{jobStageId}")
//    @Operation(summary = "Get apply by job Stage")
//    public ResponseEntity getApplyStage(@Parameter ResultStage resultStage , @PathVariable UUID jobStageId){
//        List<ApplyStage> apply =referService.getApplyStage(resultStage , jobStageId);
//        return responseHandler.response(200, "Get success!", apply);
//    }
    @GetMapping("applied-stage")
    @Operation(summary = "Get apply by job id")
    public ResponseEntity getApplyStageByJobStageId(@Parameter String jobId) {
        JobActiveResponse result = referService.getApplyStage(UUID.fromString(jobId));
        return responseHandler.response(200, "Get apply stage success!", result);
    }

    @GetMapping("apply-confirm")
    @Operation(summary = " Get Candidate apply to job need to confirm by admin ")
    public ResponseEntity getCandidateApplyConfirm(@Parameter String jobId) {
        CandidateApplyConfirmResqonse apply = referService.getCandidateApply(UUID.fromString(jobId));
        return responseHandler.response(200, "Get success!", apply);
    }
}
