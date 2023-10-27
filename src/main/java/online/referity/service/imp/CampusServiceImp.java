package online.referity.service.imp;

import online.referity.dto.request.RegistCampus;
import online.referity.entity.Campus;
import online.referity.entity.City;
import online.referity.entity.Company;
import online.referity.exception.exceptions.EntityNotFound;
import online.referity.repository.CampusRepository;
import online.referity.repository.CityRepository;
import online.referity.repository.CompanyRepository;
import online.referity.service.CampusService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class CampusServiceImp implements CampusService {

    @Autowired
    CityRepository cityRepository ;

    @Autowired
    CampusRepository campusRepository;

    @Autowired
    CompanyRepository companyRepository;

    ModelMapper modelMapper = new ModelMapper();
    @Override
    @Transactional
    public Campus addCampus(RegistCampus data, UUID companyId) {
        Company company = companyRepository.findCompanyById(companyId);
        City city = cityRepository.findCityById(data.getCityId());
       Campus campus =  modelMapper.map(data , Campus.class);
       campus.setCity(city);
       campus.setCompany(company);
       return campusRepository.save(campus);
    }

    @Override
    public List<Campus> getCampus(UUID companyId) {
        List<Campus> companies = campusRepository.findCampusesByCompanyIdAndIsDeletedFalse(companyId);
        return companies;
    }

    @Override
    public Company getCompanyDetail(UUID companyId) {
        return companyRepository.findCompanyById(companyId);
    }

    @Override
    public City addCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    public City updateCity(City city, UUID id) {
        City oldCity = cityRepository.getById(id);
        if(oldCity == null ) throw  new EntityNotFound("City Not found");
        oldCity.setName(city.getName());
        return cityRepository.save(oldCity);
    }

    @Override
    public Campus updateCampus(RegistCampus campus, UUID campusId) {
        Campus campus1 = campusRepository.findCampusById(campusId);
        campus1.setCity(cityRepository.getById(campus.getCityId()));
        campus1.setAddress(campus.getAddress());
        campus1.setName(campus.getName());
        return campusRepository.save(campus1);
    }



    @Override
    public List<City> searchCity(String key) {
        return cityRepository.search(key);
    }

    public List<City> getCity(){
        return cityRepository.findAll();
    }

    @Override
    public Campus deleteCampus(UUID campusId) {
        Campus campus = campusRepository.findCampusById(campusId);
        campus.setDeleted(true);
        return campusRepository.save(campus);
    }

    @Override
    public City deleteCity(UUID id) {
        City city = cityRepository.getById(id);
        city.setDeleted(true);
        return cityRepository.save(city);
    }
}
