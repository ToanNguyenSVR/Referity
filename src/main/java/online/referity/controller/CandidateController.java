package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.ResponseWithDataDTO;
import online.referity.dto.request.*;
import online.referity.dto.response.*;
import online.referity.entity.CvShared;
import online.referity.entity.HeadhunterRequest;
import online.referity.service.CandidateService;
import online.referity.service.HeadhunterService;
import online.referity.service.RequestService;
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
public class CandidateController {

    @Autowired
    CandidateService candidateService;

    @Autowired
    ResponseHandler responseHandler;
    @Autowired
    RequestService requestService;
    @Autowired
    HeadhunterService headhunterService;
    @Autowired
    Validation validation;

    @PostMapping("candidate/{candidateId}")
    @Operation(summary = "Register for new candidate")
    public ResponseEntity AddNewCv(@Valid @RequestBody CreateCandidateRequest data, @PathVariable UUID candidateId,
            BindingResult result) {
        validation.validate(result);
        CvResponse cvResult = candidateService.addCv(data, candidateId);
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200, "Successfull", cvResult);
        return ResponseEntity.ok(response);
    }

    @PutMapping("cv/{cvId}")
    @Operation(summary = "Register for new candidate")
    public ResponseEntity updateCVById(@Valid @RequestBody CreateCandidateRequest data, @PathVariable UUID candidateId,
                                   BindingResult result) {
        validation.validate(result);
        CvResponse cvResult = candidateService.addCv(data, candidateId);
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200, "Successfull", cvResult);
        return ResponseEntity.ok(response);
    }

    @PostMapping("add-cv/{uuidHeadhunter}/{uuidRequestId}")
    @Operation(summary = "Register for new candidate by uuid of Headhunter (no have account )")
    public ResponseEntity AddByHeadhunterUuid(@Valid @RequestBody CreateCandidateByUUID candidate,
            @PathVariable UUID uuidHeadhunter, @PathVariable UUID uuidRequestId, BindingResult result) {
        validation.validate(result);
        CvShared cvResult = candidateService.addCvToHeadhunter(candidate.getCvId(), candidate.getCandidate(),
                candidate.getCandidateRequest(), uuidHeadhunter, uuidRequestId);
        return responseHandler.response(200, "Successfully add CV to headhunter", cvResult);

    }

    @DeleteMapping("cv/{cvId}")
    @Operation(summary = "Delete CV by CV id")
    public ResponseEntity deleteCV(@PathVariable UUID cvId) {
        CvResponse cvResponse = candidateService.deleteCV(cvId);
        return responseHandler.response(200, "Successfully delete CV", cvResponse);
    }

    @GetMapping("candidate/{candidateId}/shared")
    public ResponseEntity getCvShared(@PathVariable UUID candidateId) {
        List<CandidateResponse> result = candidateService.getCvSharedByCandidateId(candidateId);
        return responseHandler.response(200, "Successfully get top headhunter", result);
    }

    @GetMapping("candidate/{candidateId}/shared/verify")
    public ResponseEntity getCvSharedVerify(@PathVariable UUID candidateId) {
        List<CandidateResponse> result = candidateService.getCvSharedVerifyByCandidateId(candidateId);
        return responseHandler.response(200, "Successfully get cv Shared to Headunter", result);
    }


    @GetMapping("candidate/code/{cvCode}")
    @Operation(summary = "Get Canddidate by code ")
    public ResponseEntity getCandidateByCode(@PathVariable String cvCode) {
        CvResponse newUser = candidateService.getCandidateByCode(cvCode);
        return responseHandler.response(201, "Successfully registered new account!", newUser);

    }

    @GetMapping("candidate/{id}")
    @Operation(summary = "Get Candidate by Id")
    public ResponseEntity GetCandidateById(@PathVariable UUID id) {
        List<CvResponse> newUser = candidateService.getCandidate(id);
        ResponseEntity response = responseHandler.response(201, "Successfully registered new account!", newUser);
        return response;
    }

    @GetMapping("candidate/")
    public ResponseEntity getCandidateWithHeadhunterId(@Parameter String headhunterId , @Parameter boolean isVerified){
        List<CandidateResponse> result = null ;
        if(isVerified) {
            result = headhunterService.getCandidate(UUID.fromString(headhunterId), null);
        }else {
            result = candidateService.getCvSharedVerifyByHeadhunterId(UUID.fromString(headhunterId));
        }
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfull" , result);
        return ResponseEntity.ok(response);
    }
    @GetMapping("cv-detail/{cvId}")
    public ResponseEntity getCandidateDetail(@PathVariable UUID cvId){
        CvResponse result = candidateService.getCandidateInformation(cvId);
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfull" , result);
        return ResponseEntity.ok(response);
    }
    //
    @PutMapping("candidate/shareCv")
    @Operation(summary = "Share CV to headhunter")
    public ResponseEntity ShareCvToHeadhunter( @RequestBody CvSharedRequest cvSharedRequest) {
        CvShared cvShared = candidateService.shareCv(cvSharedRequest);
        return responseHandler.response(201, "Successfully shared cv!", cvShared);
    }

    @PutMapping("candidate/request/{uuidRequestId}/extend")
    @Operation(summary = "Share CV to headhunter")
    public ResponseEntity ShareCvToHeadhunter(@Valid @RequestBody ExtendRequest extendRequest,
            @PathVariable UUID uuidRequestId, BindingResult result) {
        validation.validate(result);
        CvShared cvShared = candidateService.extendCvShared(extendRequest, uuidRequestId);
        return responseHandler.response(201, "Successfully shared cv!", cvShared);
    }

    @PutMapping("candidate/request/{uuidRequestId}/update")
    @Operation(summary = "Share CV to headhunter")
    public ResponseEntity ShareCvToHeadhunter(@Valid @RequestBody UpdateRequest updateRequest,
            @PathVariable UUID uuidRequestId, BindingResult result) {
        validation.validate(result);
        CvShared cvShared = candidateService.updateCvShared(updateRequest, uuidRequestId);
        return responseHandler.response(201, "Successfully shared cv!", cvShared);
    }

    @PutMapping("candidate/extend")
    @Operation(summary = "Share CV to headhunter")
    public ResponseEntity ShareCvToHeadhunter(@Valid @RequestBody ExtendRequest extendRequest, BindingResult result) {
        validation.validate(result);
        CvShared account = candidateService.extendCvShared(extendRequest, null);
        return responseHandler.response(201, "Successfully extend cv!", account);
    }

    @GetMapping("candidate/{candidateId}/request")
    @Operation(summary = "Get Request of Candidate ")
    public ResponseEntity getHeadhunterRequest(@PathVariable UUID candidateId) {
        List<HeadhunterRequestResponse> result = requestService.getRequestByCandidateId(candidateId);
        return responseHandler.response(200, "Successfully get  Request", result);
    }

    @GetMapping("candidate")
    @Operation(summary = "Get all candidate ")
    public ResponseEntity getAllCandidateCV() {
        List<CvResponse> result = candidateService.getAllCandidateCV();
        return responseHandler.response(200, "Successfully get  all cv", result);
    }
}
