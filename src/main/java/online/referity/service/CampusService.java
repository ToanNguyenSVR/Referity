package online.referity.service;

import online.referity.dto.request.RegistCampus;
import online.referity.entity.Campus;
import online.referity.entity.City;
import online.referity.entity.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface CampusService {

    public Campus addCampus(RegistCampus campus , UUID companyId );

    public List<Campus> getCampus(UUID companyId);
    public Company getCompanyDetail(UUID companyId);

    public City addCity(City city );

    public City updateCity (City city , UUID cityId);

    public Campus updateCampus(RegistCampus campus , UUID campusId);

    public Campus deleteCampus(UUID campusId);

    public List<City> searchCity(String key);

    public List<City> getCity();



    public City deleteCity(UUID id);


}
