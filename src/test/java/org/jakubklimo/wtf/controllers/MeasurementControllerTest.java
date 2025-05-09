package org.jakubklimo.wtf.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jakubklimo.wtf.dtos.MeasurementDto;
import org.jakubklimo.wtf.dtos.MeasurementStatsDto;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Country;
import org.jakubklimo.wtf.models.Measurement;
import org.jakubklimo.wtf.services.MeasurementService;
import org.jakubklimo.wtf.services.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeasurementController.class)
@ActiveProfiles("test")
public class MeasurementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeasurementService measurementService;
    @MockitoBean
    private WeatherService weatherService;
    @Autowired
    private ObjectMapper objectMapper;

    private Measurement sampleMeasurement(){
        Country country = new Country(1L, "Czech Republic", List.of());
        City city = new City(1L, "Prague", 50.073658, 14.418540, country, List.of());
        Measurement measurement = new Measurement();
        measurement.setId(1L);
        measurement.setCity(city);
        measurement.setTemperature(20.4);
        measurement.setPressure(12.9);
        measurement.setHumidity(0.5);
        measurement.setDatetime(LocalDateTime.now());
        measurement.setTemperatureMin(12);
        measurement.setTemperatureMax(22);
        measurement.setWeatherMain("Clear");
        measurement.setWeatherDesc("clear sky");
        measurement.setWindSpeed(12);
        return measurement;
    }

    private MeasurementDto sampleMeasurementDto(){
        MeasurementDto measurementdto = new MeasurementDto(
                LocalDateTime.now(),
                1L,
                20.4,
                12.9,
                0.5,
                12,
                22,
                "Clear",
                "clear sky",
                12
        );
        return measurementdto;
    }

    @Test
    void getAllMeasurements_ShouldReturnListOfMeasurements() throws Exception {
        Measurement measurement1 = sampleMeasurement();
        Measurement measurement2 = sampleMeasurement();
        measurement2.setId(2L);
        List<Measurement> measurements = List.of(measurement1, measurement2);
        when(measurementService.findAll()).thenReturn(measurements);

        mockMvc.perform(get("/api/measurements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getMeasurementById_ShouldReturnMeasurement() throws Exception {
        Measurement measurement = sampleMeasurement();
        when(measurementService.getMeasurementById(1L)).thenReturn(measurement);

        mockMvc.perform(get("/api/measurements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllMeasurementsByCity_ShouldReturnLatestMeasurement() throws Exception {
        Measurement measurement = sampleMeasurement();
        when(weatherService.getCurrentMeasurement("Prague")).thenReturn(measurement);

        mockMvc.perform(get("/api/measurements/latest")
                        .param("cityName", "Prague"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAverageStatsByCity_ShouldReturnStats() throws Exception {
        MeasurementStatsDto stats = new MeasurementStatsDto(20.0, 70.0, 10.0, 5.0);
        when(measurementService.getAverageStats("Prague", 7)).thenReturn(stats);

        mockMvc.perform(get("/api/measurements/stats")
                        .param("cityName", "Prague")
                        .param("days", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avgTemperature").value(20.0))
                .andExpect(jsonPath("$.avgPressure").value(70.0))
                .andExpect(jsonPath("$.avgHumidity").value(10.0))
                .andExpect(jsonPath("$.avgWindSpeed").value(5.0));
    }

    @Test
    void createMeasurement_ShouldReturnCreatedMeasurement() throws Exception {
        MeasurementDto dto = sampleMeasurementDto();
        Measurement measurement = sampleMeasurement();

        when(measurementService.createMeasurement(dto)).thenReturn(measurement);

        LocalDateTime now = LocalDateTime.now();
        mockMvc.perform(post("/api/measurements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateMeasurement_ShouldReturnUpdatedMeasurement() throws Exception {
        MeasurementDto dto = sampleMeasurementDto();
        Measurement updatedMeasurement = sampleMeasurement();
        when(measurementService.updateMeasurement(1L, dto)).thenReturn(updatedMeasurement);

        LocalDateTime now = LocalDateTime.now();
        mockMvc.perform(put("/api/measurements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteMeasurement_ShouldReturnNoContent() throws Exception {
        doNothing().when(measurementService).deleteMeasurement(1L);

        mockMvc.perform(delete("/api/measurements/1"))
                .andExpect(status().isNoContent());
    }
}
