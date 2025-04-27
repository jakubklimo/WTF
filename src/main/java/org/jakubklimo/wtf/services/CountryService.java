package org.jakubklimo.wtf.services;

import lombok.RequiredArgsConstructor;
import org.jakubklimo.wtf.dtos.CountryDto;
import org.jakubklimo.wtf.exceptions.EntityNotFoundException;
import org.jakubklimo.wtf.models.Country;
import org.jakubklimo.wtf.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<Country> getAllCountries(){
        return countryRepository.findAll();
    }

    public Country getCountryByName(String countryName){
        return countryRepository.findByName(countryName)
                .orElseThrow(() -> new EntityNotFoundException("Country", countryName));
    }

    public Country getCountryById(Long id){
        return countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country", id.toString()));
    }

    public Country createCountry(CountryDto country){
        Country newCountry = new Country();
        newCountry.setName(country.name());
        return countryRepository.save(newCountry);
    }

    public Country updateCountry(Long id, CountryDto country){
        Country countryToUpdate = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country", id.toString()));
        if(country.name() != null){
            countryToUpdate.setName(country.name());
        }
        return countryRepository.save(countryToUpdate);
    }

    public void deleteCountry(Long id){
        Country countryToDelete = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country", id.toString()));
        countryRepository.delete(countryToDelete);
    }
}
