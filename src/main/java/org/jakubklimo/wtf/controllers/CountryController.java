package org.jakubklimo.wtf.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jakubklimo.wtf.dtos.CountryDto;
import org.jakubklimo.wtf.models.Country;
import org.jakubklimo.wtf.services.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries(){
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable Long id){
        Country country =  countryService.getCountryById(id);
        return ResponseEntity.ok(country);
    }

    @GetMapping("/name")
    public ResponseEntity<Country> getCountryByName(@RequestParam String name){
        Country country =  countryService.getCountryByName(name);
        return ResponseEntity.ok(country);
    }

    @PostMapping
    public ResponseEntity<Country> createCountry(@Valid @RequestBody CountryDto country){
        Country countryCreated = countryService.createCountry(country);
        URI location = URI.create(String.format("/api/countries/%s", countryCreated.getId()));
        return ResponseEntity.created(location).body(countryCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(@PathVariable Long id, @Valid @RequestBody CountryDto country){
        Country countryUpdated = countryService.updateCountry(id, country);
        return ResponseEntity.ok(countryUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable Long id){
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}
