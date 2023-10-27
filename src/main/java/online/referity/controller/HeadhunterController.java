package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.ResponseWithDataDTO;
import online.referity.dto.request.CreateCandidateByUUID;
import online.referity.dto.request.CreateRequest;
import online.referity.dto.request.HeadhunterAdditionalInformation;
import online.referity.dto.response.*;
import online.referity.entity.*;
import online.referity.service.*;
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
public class HeadhunterController {

    @Autowired
    HeadhunterService headhunterService;
    @Autowired
    AccountService accountService ;

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    RequestService requestService ;

    @Autowired
    MatchingService matchingService ;

    @Autowired
    Validation validation;

//    @PostMapping("refer-cv/{jobId}/{cvSharedId}")
//    @Operation(summary = "Refer Cv to Job")
//    public ResponseEntity referCvToJob(@PathVariable UUID jobId ,@PathVariable UUID cvSharedId ){
//        CandidateApplyResponse result = headhunterService.referCvToJob( cvSharedId , jobId);
//        ResponseEntity response = responseHandler.response(200, "Refer success!", result);
//        return response;
//    }

    @GetMapping("headhunter/{headhunterId}/candidate")
    public ResponseEntity getCandidate(@PathVariable UUID headhunterId, @RequestParam(required = false) UUID jobId){
        List<CandidateResponse> result = headhunterService.getCandidate(headhunterId, jobId);
        return responseHandler.response(200, "Successfully get candidate by headhunter", result);
    }


    @GetMapping("top-headhunter")
    public ResponseEntity getTopHeadhunter() {
        List<TopHeadhunterResponse> result = headhunterService.getTopHeadhunter();
        return responseHandler.response(200, "Successfully get top headhunter", result);
    }


    @GetMapping("headhunter/{headhunterId}/request")
    @Operation(summary = "Get Request of Headhunter ")
    public ResponseEntity getHeadhunterRequest(@PathVariable UUID headhunterId){
        List<HeadhunterRequestResponse> result = requestService.getRequestByHeadhunterId(headhunterId);
        return responseHandler.response(200 , "Successfully get  headhunter Request" , result);
    }

    @PostMapping("headhunter/{headhunterId}/request")
    @Operation(summary = "Create Headhunter Request ")
    public ResponseEntity createRequest(@RequestBody CreateRequest createRequest , @PathVariable UUID headhunterId ){
        String result = requestService.createRequest( createRequest , headhunterId);
        ResponseEntity response = responseHandler.response(200, "Request success!", result);
        return response;
    }
    @PostMapping("headhunter/{headhunterId}/request/{requestId}")
    @Operation(summary = "update Headhunter Request ")
    public ResponseEntity updateRequest(@RequestBody CreateRequest createRequest , @PathVariable UUID requestId ){
        HeadhunterRequest result = requestService.updateRequest(  requestId , createRequest );
        ResponseEntity response = responseHandler.response(200, "Update Request success!", result);
        return response;
    }

    @PostMapping("headhunter/{accountId}")
    @Operation(summary = "Additional Information for headhunter")
    public  ResponseEntity additionalInformationHeadhunter(@Valid @RequestBody HeadhunterAdditionalInformation headhunterAdditionalInformation, @PathVariable UUID accountId, BindingResult result){
        validation.validate(result);
        Account account = accountService.additionalInformationForHeadhunter(headhunterAdditionalInformation, accountId);
        ResponseEntity response = responseHandler.response(201, "Successfully update information for headhunter!", account);
        return response;
    }
    @PutMapping("headhunter/{accountId}")
    @Operation(summary = "Update Information for headhunter")
    public  ResponseEntity updateInformationHeadhunter(@Valid @RequestBody HeadhunterAdditionalInformation headhunterAdditionalInformation, @PathVariable UUID accountId, BindingResult result){
        validation.validate(result);
        Account account = accountService.updateInformationForHeadhunter(headhunterAdditionalInformation, accountId);
        ResponseEntity response = responseHandler.response(201, "Successfully update information for headhunter!", account);
        return response;
    }
    @PutMapping("accept-cv")
    @Operation(summary = "Accept cv shared for headhunter")
    public  ResponseEntity accpetCV(@Parameter String headhunterId , @Parameter String cvSharedId){
        CvSharedResponse result = headhunterService.acceptCV(UUID.fromString(headhunterId) , UUID.fromString(cvSharedId));
        ResponseEntity response = responseHandler.response(201, "Successfully accept  cv !", result);
        return response;
    }

    @GetMapping("matching/{headhunterId}")
    public ResponseEntity getMatching(@PathVariable UUID headhunterId){
        List<MatchingListResponse> matchingScores = matchingService.matching(headhunterId);
        ResponseEntity response = responseHandler.response(200, "Successfully get matching candidate!", matchingScores);
        return response;
    }

    @GetMapping("rating/{headhunterId}")
    public ResponseEntity getRating(@PathVariable UUID headhunterId){
        RatingOfHeadhunterResponse result = headhunterService.getRatingOfHeadhunter(headhunterId);
        ResponseEntity response = responseHandler.response(200, "Successfully get matching candidate!", result);
        return response;
    }
    @GetMapping("config/{headhunterId}")
    public ResponseEntity getConfig(@PathVariable UUID headhunterId){
        HeadhunterConfigResponse result = headhunterService.getConfigOfHeadhunter(headhunterId);
        ResponseEntity response = responseHandler.response(200, "Successfully get matching candidate!", result);
        return response;
    }

}
