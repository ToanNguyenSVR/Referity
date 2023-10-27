package online.referity.repository;

import online.referity.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City , UUID> {

    City getById(UUID id);
    City findCityById(UUID id);

    @Query("SELECT city FROM City city Where city.name LIKE %?1%")
    List<City> search (String key);
}
