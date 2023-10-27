package online.referity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.entity.City;
import online.referity.entity.Skill;
import online.referity.service.CampusService;
import online.referity.utils.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class LocationController {

    @Autowired
    CampusService campusService;

    @Autowired
    ResponseHandler responseHandler;
    @GetMapping("city/{key}")
    @Operation(summary = "Seach skill ")
    public ResponseEntity seachCity(@PathVariable String key) {
        List<City> result = campusService.searchCity(key);
        return ResponseEntity.ok(result);
    }

    @GetMapping("city")
    @Operation(summary = "Seach skill ")
    public ResponseEntity getCity() {
        List<City> result = campusService.getCity();
        return responseHandler.response(200, "Successfully get list city", result);
    }

    @PostMapping("city")
    @Operation(summary = "Add City")
    public ResponseEntity addCity(@RequestBody City city) {
        City result = campusService.addCity(city);
        return responseHandler.response(200, "Successfully create city", result);
    }

    @PutMapping("city/{id}")
    @Operation(summary = "Seach skill ")
    public ResponseEntity updateCity(@PathVariable UUID id , @RequestBody City city) {
        City result = campusService.updateCity(city , id);
        return responseHandler.response(200, "Successfully update city", result);
    }

    @DeleteMapping("city/{id}")
    @Operation(summary = "Seach skill ")
    public ResponseEntity deleteCity(@PathVariable UUID id) {
        City result = campusService.deleteCity(id);
        return responseHandler.response(200, "Successfully update city", result);
    }
}