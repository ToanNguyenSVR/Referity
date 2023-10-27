package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.ResponseWithDataDTO;
import online.referity.dto.request.*;
import online.referity.dto.response.ContractResponse;
import online.referity.dto.response.CvResponse;
import online.referity.entity.Campus;
import online.referity.entity.Company;
import online.referity.entity.CompanyStaff;
import online.referity.service.CampusService;
import online.referity.service.CompanyService;
import online.referity.utils.ResponseHandler;
import online.referity.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class CompanyController {


    @Autowired
    CampusService campusService;
    @Autowired
    ResponseHandler responseHandler;
    @Autowired
    Validation validation;

    @Autowired
    CompanyService companyService;



    @GetMapping("company/{companyId}/campus")
    public ResponseEntity getCompany(@PathVariable UUID companyId){
        List<Campus> campuses = campusService.getCampus(companyId);
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfull" , campuses);
        return ResponseEntity.ok(response);
    }

    @GetMapping("company/{companyId}")
    public ResponseEntity getCompanyDetail(@PathVariable UUID companyId){
        Company company = campusService.getCompanyDetail(companyId);
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfully get company detail" , company);
        return ResponseEntity.ok(response);
    }

    @PostMapping("company/{companyId}/campus")
    @Operation(summary = "Add Company  campus")
    public ResponseEntity addCompany(@Valid @RequestBody RegistCampus data, @PathVariable UUID companyId, BindingResult result){
        validation.validate(result);
        Campus Result = campusService.addCampus(data ,companyId );
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfull" , Result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("campus/{campusId}")
    public ResponseEntity deleteCampus(@PathVariable UUID campusId){
        Campus Result = campusService.deleteCampus(campusId);
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfully delete campus" , Result);
        return ResponseEntity.ok(response);
    }
    @PutMapping("campus/{campusId}")
    @Operation(summary = "Register for new candidate")
    public ResponseEntity register(@Valid @RequestBody RegistCampus data, @PathVariable UUID campusId, BindingResult result){
        validation.validate(result);
        Campus Result = campusService.updateCampus(data , campusId);
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfull" , Result);
        return ResponseEntity.ok(response);
    }
    @PostMapping("company/contract/{id}")
    @Operation(summary = "Add contract of company")
    public ResponseEntity addContract(@Valid @RequestBody ContractRequest data, @PathVariable UUID id, BindingResult result){
        validation.validate(result);
        ContractResponse cvResult = companyService.addContract(data ,  id);
        ResponseWithDataDTO response = new ResponseWithDataDTO<>(200 , "Successfull" , cvResult);
        return ResponseEntity.ok(response);
    }
}
