package org.jakubklimo.wtf.controllers;

import org.jakubklimo.wtf.dtos.CountryDto;
import org.jakubklimo.wtf.models.Country;
import org.jakubklimo.wtf.services.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
@ActiveProfiles("test")
public class CountryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CountryService countryService;

    @Test
    void getAllCountries_ShouldReturnListOfCountries() throws Exception {
        List<Country> countries = List.of(
                new Country(1L, "Czech Republic", List.of()),
                new Country(2L, "Slovakia", List.of())
        );
        when(countryService.getAllCountries()).thenReturn(countries);

        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Czech Republic"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Slovakia"));
    }

    @Test
    void getCountryById_ShouldReturnCountry_WhenExists() throws Exception {
        Country country = new Country(1L, "Czech Republic", List.of());
        when(countryService.getCountryById(1L)).thenReturn(country);

        mockMvc.perform(get("/api/countries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Czech Republic"));
    }

    @Test
    void getCountryByName_ShouldReturnCountry_WhenExists() throws Exception {
        Country country = new Country(1L, "Czech Republic", List.of());
        when(countryService.getCountryByName("Czech Republic")).thenReturn(country);

        mockMvc.perform(get("/api/countries/name")
                        .param("name", "Czech Republic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Czech Republic"));
    }

    @Test
    void createCountry_ShouldReturnCreatedCountry() throws Exception {
        CountryDto countryDto = new CountryDto("Czech Republic");
        Country country = new Country(1L, "Czech Republic", List.of());

        when(countryService.createCountry(countryDto)).thenReturn(country);

        mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Czech Republic"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/countries/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Czech Republic"));
    }

    @Test
    void updateCountry_ShouldReturnUpdatedCountry() throws Exception {
        CountryDto countryDto = new CountryDto("Slovakia");
        Country updatedCountry = new Country(1L, "Slovakia", List.of());

        when(countryService.updateCountry(1L, countryDto)).thenReturn(updatedCountry);

        mockMvc.perform(put("/api/countries/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "Slovakia"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Slovakia"));
    }

    @Test
    void deleteCountry_ShouldReturnNoContent() throws Exception {
        doNothing().when(countryService).deleteCountry(1L);

        mockMvc.perform(delete("/api/countries/1"))
                .andExpect(status().isNoContent());
    }
}
