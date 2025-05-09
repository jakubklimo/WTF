package org.jakubklimo.wtf.services;

import org.jakubklimo.wtf.dtos.CountryDto;
import org.jakubklimo.wtf.exceptions.EntityNotFoundException;
import org.jakubklimo.wtf.models.Country;
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

public class CountryServiceTest {
    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    public CountryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private Country sampleCountry() {
        return new Country(1L, "Czech Republic", List.of());
    }

    private CountryDto sampleCountryDto() {
        return new CountryDto("Czech Republic");
    }

    @Test
    void getAllCountries_ShouldReturnListOfCountries() {
        List<Country> countries = List.of(sampleCountry());
        when(countryRepository.findAll()).thenReturn(countries);

        List<Country> result = countryService.getAllCountries();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Czech Republic");
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void getCountryByName_ShouldReturnCountry_WhenCountryExists() {
        when(countryRepository.findByName("Czech Republic")).thenReturn(Optional.of(sampleCountry()));

        Country result = countryService.getCountryByName("Czech Republic");

        assertThat(result.getName()).isEqualTo("Czech Republic");
        verify(countryRepository, times(1)).findByName("Czech Republic");
    }

    @Test
    void getCountryByName_ShouldThrowException_WhenCountryDoesNotExist() {
        when(countryRepository.findByName("Czech Republic")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.getCountryByName("Czech Republic"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Country with ID Czech Republic not found");
    }

    @Test
    void getCountryById_ShouldReturnCountry_WhenCountryExists() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(sampleCountry()));

        Country result = countryService.getCountryById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Czech Republic");
        verify(countryRepository, times(1)).findById(1L);
    }

    @Test
    void getCountryById_ShouldThrowException_WhenCountryDoesNotExist() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.getCountryById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Country with ID 1 not found");
    }

    @Test
    void createCountry_ShouldSaveAndReturnCountry() {
        CountryDto countryDto = sampleCountryDto();
        Country country = sampleCountry();

        when(countryRepository.save(any(Country.class))).thenReturn(country);

        Country result = countryService.createCountry(countryDto);

        assertThat(result.getName()).isEqualTo("Czech Republic");
        verify(countryRepository, times(1)).save(any(Country.class));
    }

    @Test
    void updateCountry_ShouldUpdateAndReturnCountry() {
        CountryDto countryDto = new CountryDto("Slovakia");
        Country existingCountry = sampleCountry();

        when(countryRepository.findById(1L)).thenReturn(Optional.of(existingCountry));
        when(countryRepository.save(any(Country.class))).thenReturn(existingCountry);

        Country result = countryService.updateCountry(1L, countryDto);

        assertThat(result.getName()).isEqualTo("Slovakia");
        verify(countryRepository, times(1)).save(existingCountry);
    }

    @Test
    void updateCountry_ShouldThrowException_WhenCountryNotFound() {
        CountryDto countryDto = sampleCountryDto();
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.updateCountry(1L, countryDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Country with ID 1 not found");
    }

    @Test
    void deleteCountry_ShouldDeleteCountry_WhenExists() {
        Country country = sampleCountry();
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));
        doNothing().when(countryRepository).delete(country);

        countryService.deleteCountry(1L);

        verify(countryRepository, times(1)).delete(country);
    }

    @Test
    void deleteCountry_ShouldThrowException_WhenCountryNotFound() {
        when(countryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.deleteCountry(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Country with ID 1 not found");
    }
}
