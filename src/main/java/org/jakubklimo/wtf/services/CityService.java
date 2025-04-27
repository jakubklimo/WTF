package org.jakubklimo.wtf.services;

import lombok.RequiredArgsConstructor;
import org.jakubklimo.wtf.dtos.CityDto;
import org.jakubklimo.wtf.exceptions.EntityNotFoundException;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Country;
import org.jakubklimo.wtf.repositories.CityRepository;
import org.jakubklimo.wtf.repositories.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public List<City> findAll(){
        return cityRepository.findAll();
    }

    public City getCityById(Long id){
        return cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City", id.toString()));
    }

    public City getCityByName(String name){
        return cityRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("City", name));
    }

    public City createCity(CityDto city){
        City newCity = new City();
        newCity.setName(city.name());
        Country country = countryRepository.findById(city.country_id())
                .orElseThrow(() -> new EntityNotFoundException("Country", city.country_id().toString()));
        newCity.setCountry(country);
        newCity.setLongitude(city.longitude());
        newCity.setLatitude(city.latitude());
        return cityRepository.save(newCity);
    }

    public City updateCity(Long id, CityDto city){
        City cityToUpdate = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City", id.toString()));
        Country country = countryRepository.findById(city.country_id())
                .orElseThrow(() -> new EntityNotFoundException("Country", city.country_id().toString()));
        if(city.name() != null && city.latitude() != 0 && city.longitude() != 0){
            cityToUpdate.setName(city.name());
            cityToUpdate.setLatitude(city.latitude());
            cityToUpdate.setLongitude(city.longitude());
            cityToUpdate.setCountry(country);
        }
        return cityRepository.save(cityToUpdate);
    }

    public void deleteCity(@PathVariable Long id){
        City cityToDelete = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City", id.toString()));
        cityRepository.delete(cityToDelete);
    }
}
