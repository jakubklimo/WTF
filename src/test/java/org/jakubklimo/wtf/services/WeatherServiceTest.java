package org.jakubklimo.wtf.services;

import org.jakubklimo.wtf.exceptions.EntityNotFoundException;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Measurement;
import org.jakubklimo.wtf.repositories.CityRepository;
import org.jakubklimo.wtf.repositories.MeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class WeatherServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private MeasurementRepository measurementRepository;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrentMeasurement_ShouldReturnMeasurement_WhenCityExists() throws Exception {
        // Mock data
        City city = new City(1L, "Prague", 50.073658, 14.418540, null, null);
        when(cityRepository.findByName("Prague")).thenReturn(Optional.of(city));

        String jsonResponse = """
        {
            "main": {
                "temp": 25.5,
                "pressure": 1013,
                "humidity": 65,
                "temp_min": 22.0,
                "temp_max": 28.0
            },
            "weather": [
                {
                    "main": "Clear",
                    "description": "clear sky"
                }
            ],
            "wind": {
                "speed": 5.2
            }
        }
        """;

        when(restTemplate.getForEntity(anyString(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(jsonResponse));

        Measurement measurement = weatherService.getCurrentMeasurement("Prague");

        assertThat(measurement.getTemperature()).isEqualTo(25.5);
        assertThat(measurement.getPressure()).isEqualTo(1013);
        assertThat(measurement.getHumidity()).isEqualTo(65);
        assertThat(measurement.getTemperatureMin()).isEqualTo(22.0);
        assertThat(measurement.getTemperatureMax()).isEqualTo(28.0);
        assertThat(measurement.getWeatherMain()).isEqualTo("Clear");
        assertThat(measurement.getWeatherDesc()).isEqualTo("clear sky");
        assertThat(measurement.getWindSpeed()).isEqualTo(5.2);
        assertThat(measurement.getCity()).isEqualTo(city);
    }

    @Test
    void getCurrentMeasurement_ShouldThrowException_WhenCityNotFound() {
        when(cityRepository.findByName("UnknownCity")).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> {
            weatherService.getCurrentMeasurement("UnknownCity");
        });

        verify(measurementRepository, never()).save(any(Measurement.class));
    }
}
