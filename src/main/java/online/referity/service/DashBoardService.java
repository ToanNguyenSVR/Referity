package online.referity.service;

import online.referity.dto.response.DashboardAdminResponse;
import online.referity.dto.response.DashboardCompanyResponse;
import online.referity.dto.response.DashboardHeadhunterResponse;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.UUID;

@Service
public interface DashBoardService {

    public DashboardCompanyResponse getCompany(UUID accountId , Year year);
    public DashboardHeadhunterResponse getHeadhunter(UUID headunterId);
    public DashboardAdminResponse getAdmin(UUID accountId , Year year);
}
