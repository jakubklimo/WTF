package org.jakubklimo.wtf.services;

import org.jakubklimo.wtf.dtos.CityDto;
import org.jakubklimo.wtf.exceptions.EntityNotFoundException;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Country;
import org.jakubklimo.wtf.repositories.CityRepository;
import org.jakubklimo.wtf.repositories.CountryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class CityServiceTest {
    @Mock
    private CityRepository cityRepository;
    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CityService cityService;

    public CityServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private Country sampleCountry() {
        return new Country(1L, "Czech Republic", List.of());
    }

    private City sampleCity() {
        return new City(1L, "Prague", 50.073658, 14.418540, sampleCountry(), List.of());
    }

    private CityDto sampleCityDto() {
        return new CityDto("Prague", 1L, 50.073658, 14.418540);
    }

    @Test
    void findAll_ShouldReturnListOfCities() {
        List<City> cities = List.of(sampleCity());
        when(cityRepository.findAll()).thenReturn(cities);

        List<City> result = cityService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Prague");
    }

    @Test
    void getCityById_ShouldReturnCity_WhenCityExists() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(sampleCity()));

        City result = cityService.getCityById(1L);

        assertThat(result.getName()).isEqualTo("Prague");
        assertThat(result.getCountry().getName()).isEqualTo("Czech Republic");
    }

    @Test
    void getCityById_ShouldThrowException_WhenCityDoesNotExist() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.getCityById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("City with ID 1 not found");
    }

    @Test
    void getCityByName_ShouldReturnCity_WhenCityExists() {
        when(cityRepository.findByName("Prague")).thenReturn(Optional.of(sampleCity()));

        City result = cityService.getCityByName("Prague");

        assertThat(result.getName()).isEqualTo("Prague");
    }

    @Test
    void getCityByName_ShouldThrowException_WhenCityDoesNotExist() {
        when(cityRepository.findByName("Prague")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.getCityByName("Prague"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("City with ID Prague not found");
    }

    @Test
    void createCity_ShouldSaveAndReturnCity() {
        CityDto cityDto = sampleCityDto();
        Country country = sampleCountry();
        City city = sampleCity();

        when(countryRepository.findById(cityDto.country_id())).thenReturn(Optional.of(country));
        when(cityRepository.save(any(City.class))).thenReturn(city);

        City result = cityService.createCity(cityDto);

        assertThat(result.getName()).isEqualTo("Prague");
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    void createCity_ShouldThrowException_WhenCountryNotFound() {
        CityDto cityDto = sampleCityDto();
        when(countryRepository.findById(cityDto.country_id())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.createCity(cityDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Country with ID 1 not found");
    }

    @Test
    void updateCity_ShouldUpdateAndReturnCity() {
        CityDto cityDto = new CityDto("Brno", 1L, 49.1951, 16.6068);
        City existingCity = sampleCity();
        Country country = sampleCountry();

        when(cityRepository.findById(1L)).thenReturn(Optional.of(existingCity));
        when(countryRepository.findById(cityDto.country_id())).thenReturn(Optional.of(country));
        when(cityRepository.save(any(City.class))).thenReturn(existingCity);

        City result = cityService.updateCity(1L, cityDto);

        assertThat(result.getName()).isEqualTo("Brno");
        assertThat(result.getLatitude()).isEqualTo(49.1951);
        assertThat(result.getLongitude()).isEqualTo(16.6068);
    }

    @Test
    void updateCity_ShouldThrowException_WhenCityNotFound() {
        CityDto cityDto = sampleCityDto();
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.updateCity(1L, cityDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("City with ID 1 not found");
    }

    @Test
    void deleteCity_ShouldDeleteCity_WhenExists() {
        City city = sampleCity();
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        doNothing().when(cityRepository).delete(city);

        cityService.deleteCity(1L);

        verify(cityRepository, times(1)).delete(city);
    }

    @Test
    void deleteCity_ShouldThrowException_WhenCityNotFound() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cityService.deleteCity(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("City with ID 1 not found");
    }
}
