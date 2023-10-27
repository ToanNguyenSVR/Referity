package online.referity.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.response.DashboardAdminResponse;
import online.referity.dto.response.DashboardCompanyResponse;
import online.referity.service.DashBoardService;
import online.referity.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    ResponseHandler responseHandler;

    @Autowired
    DashBoardService dashBoardService;

    @GetMapping("dashboard-company/{accountId}")
    public ResponseEntity getDashBoardCompany(@PathVariable UUID accountId  , @RequestParam ( required = false )Year year){
        DashboardCompanyResponse dashboardCompanyResponse = dashBoardService.getCompany(accountId  , year);
        return responseHandler.response(200, "Successfully get dashboard of company", dashboardCompanyResponse);
    }

    @GetMapping("dashboard-headhunter")
    public ResponseEntity getDashBoardHeadhunter(){
        return null;
    }

    @GetMapping("dashboard-admin/{accountId}")
    public ResponseEntity getDashBoardAdmin(@PathVariable UUID accountId  , @RequestParam ( required = false )Year year){
        DashboardAdminResponse dashboardCompanyResponse = dashBoardService.getAdmin(accountId  , year);
        return responseHandler.response(200, "Successfully get dashboard of admin", dashboardCompanyResponse);
    }
}
