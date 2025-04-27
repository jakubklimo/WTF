package org.jakubklimo.wtf.controllers;

import lombok.RequiredArgsConstructor;
import org.jakubklimo.wtf.dtos.CityDto;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.services.CityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/city")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping("/all")
    public List<City> getAll(){
        return cityService.findAll();
    }

    @GetMapping("/{id}")
    public City getById(@PathVariable Long id){
        return cityService.getCityById(id);
    }

    @GetMapping("/name/{name}")
    public City getByName(@PathVariable String name){
        return cityService.getCityByName(name);
    }

    @PostMapping
    public City createCity(@RequestBody CityDto city){
        return cityService.createCity(city);
    }

    @PutMapping("/{id}")
    public City updateCity(@PathVariable Long id, @RequestBody CityDto city){
        return cityService.updateCity(id, city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id){
        cityService.deleteCity(id);
    }
}
