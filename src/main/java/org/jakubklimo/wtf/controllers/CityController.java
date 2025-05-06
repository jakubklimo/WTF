package org.jakubklimo.wtf.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jakubklimo.wtf.dtos.CityDto;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.services.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/cities")
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @GetMapping
    public ResponseEntity<List<City>> getAll(){
        return ResponseEntity.ok(cityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getById(@PathVariable Long id){
        City city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }

    @GetMapping("/name")
    public ResponseEntity<City> getByName(@RequestParam String name){
        City city = cityService.getCityByName(name);
        return ResponseEntity.ok(city);
    }

    @PostMapping
    public ResponseEntity<City> createCity(@Valid @RequestBody CityDto city){
        City cityCreated = cityService.createCity(city);
        URI location = URI.create(String.format("/api/cities/%s", cityCreated.getId()));
        return ResponseEntity.created(location).body(cityCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @Valid @RequestBody CityDto city){
        City cityUpdated = cityService.updateCity(id, city);
        return ResponseEntity.ok(cityUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id){
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
