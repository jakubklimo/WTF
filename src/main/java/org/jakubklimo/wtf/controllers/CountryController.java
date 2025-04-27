package org.jakubklimo.wtf.controllers;

import lombok.RequiredArgsConstructor;
import org.jakubklimo.wtf.dtos.CountryDto;
import org.jakubklimo.wtf.models.Country;
import org.jakubklimo.wtf.services.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/country")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping("/all")
    public List<Country> getAllCountries(){
        return countryService.getAllCountries();
    }

    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Long id){
        return countryService.getCountryById(id);
    }

    @GetMapping("/name/{name}")
    public Country getCountryByName(@PathVariable String name){
        return countryService.getCountryByName(name);
    }

    @PostMapping
    public Country createCountry(@RequestBody CountryDto country){
        return countryService.createCountry(country);
    }

    @PutMapping("/{id}")
    public Country updateCountry(@PathVariable Long id, @RequestBody CountryDto country){
        return countryService.updateCountry(id, country);
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable Long id){
        countryService.deleteCountry(id);
    }
}
