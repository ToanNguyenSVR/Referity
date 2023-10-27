package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.ResponseWithDataDTO;
import online.referity.dto.request.RegisterStaffCompanyRequest;
import online.referity.dto.response.CompanyStaffResponse;
import online.referity.entity.CompanyStaff;
import online.referity.service.CompanyService;
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
public class CompanyStaffController {
    @Autowired
    Validation validation;

    @Autowired
    CompanyService companyService;

    @Autowired
    ResponseHandler responseHandler;

    @PostMapping("staff/")
    @Operation(summary = "Register for new candidate")
    public ResponseEntity register(@Valid @RequestBody RegisterStaffCompanyRequest data, BindingResult result){
        validation.validate(result);
        CompanyStaffResponse Result = companyService.createStaff(data);
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfully create company staff" , Result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("staff/{staffId}")
    @Operation(summary = "Register for new candidate")
    public ResponseEntity updateStaff(@Valid @RequestBody RegisterStaffCompanyRequest data, @PathVariable UUID staffId){
        CompanyStaffResponse Result = companyService.updateStaff(data, staffId);
        return responseHandler.response(200, "Successfully update company staff", Result);
    }

    @DeleteMapping("staff/{staffId}")
    @Operation(summary = "Register for new candidate")
    public ResponseEntity deleteStaff(@PathVariable UUID staffId){
        CompanyStaffResponse Result = companyService.deleteStaff(staffId);
        return responseHandler.response(200, "Successfully delete company staff", Result);
    }

    @GetMapping("staff/{companyId}")
    public ResponseEntity getCompanyStaff(@PathVariable UUID companyId){
        List<CompanyStaffResponse> companyStaffs = companyService.getCompanyStaffById(companyId);
        return responseHandler.response(200, "Successfully get company staff", companyStaffs);
    }
}
