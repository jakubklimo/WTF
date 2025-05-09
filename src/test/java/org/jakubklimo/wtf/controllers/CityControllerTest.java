package org.jakubklimo.wtf.controllers;

import org.jakubklimo.wtf.dtos.CityDto;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Country;
import org.jakubklimo.wtf.services.CityService;
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

@WebMvcTest(CityController.class)
@ActiveProfiles("test")
public class CityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CityService cityService;

    @Test
    void getAll_ShouldReturnListOfCities() throws Exception {
        Country country = new Country(1L, "Czech Republic", List.of());
        List<City> cities = List.of(new City(1L, "Prague", 50.073658, 14.418540, country, List.of()), new City(2L, "Liberec", 50.76711, 15.05619, country, List.of()));
        when(cityService.findAll()).thenReturn(cities);

        mockMvc.perform(get("/api/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Prague"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Liberec"));
    }

    @Test
    void getById_ShouldReturnCity_WhenCityExists() throws Exception {
        Country country = new Country(1L, "Czech Republic", List.of());
        City city = new City(1L, "Prague", 50.073658, 14.418540, country, List.of());
        when(cityService.getCityById(1L)).thenReturn(city);

        mockMvc.perform(get("/api/cities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Prague"));
    }

    @Test
    void getByName_ShouldReturnCity_WhenCityExists() throws Exception {
        Country country = new Country(1L, "Czech Republic", List.of());
        City city = new City(1L, "Prague", 50.073658, 14.418540, country, List.of());
        when(cityService.getCityByName("Prague")).thenReturn(city);

        mockMvc.perform(get("/api/cities/name").param("name", "Prague"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Prague"));
    }

    @Test
    void createCity_ShouldReturnCreatedCity() throws Exception {
        CityDto cityDto = new CityDto("Prague", 1L, 50.073658, 14.418540);
        Country country = new Country(1L, "Czech Republic", List.of());
        City city = new City(1L, "Prague", 50.073658, 14.418540, country, List.of());

        when(cityService.createCity(cityDto)).thenReturn(city);

        mockMvc.perform(post("/api/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "name": "Prague",
                    "country_id": 1,
                    "latitude": 50.073658,
                    "longitude": 14.418540
                }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Prague"));
    }

    @Test
    void updateCity_ShouldReturnUpdatedCity() throws Exception {
        CityDto cityDto = new CityDto("Brno", 1L, 50.073658, 14.418540);
        Country country = new Country(1L, "Czech Republic", List.of());
        City updatedCity = new City(1L, "Brno", 50.073658, 14.418540, country, List.of());

        when(cityService.updateCity(1L, cityDto)).thenReturn(updatedCity);

        mockMvc.perform(put("/api/cities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                {
                    "name": "Brno",
                    "country_id": 1,
                    "latitude": 50.073658,
                    "longitude": 14.418540
                }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Brno"));
    }

    @Test
    void deleteCity_ShouldReturnNoContent() throws Exception {
        doNothing().when(cityService).deleteCity(1L);

        mockMvc.perform(delete("/api/cities/1"))
                .andExpect(status().isNoContent());
    }
}
