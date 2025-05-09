package org.jakubklimo.wtf.services;

import org.jakubklimo.wtf.dtos.MeasurementDto;
import org.jakubklimo.wtf.dtos.MeasurementStatsDto;
import org.jakubklimo.wtf.exceptions.EntityNotFoundException;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Measurement;
import org.jakubklimo.wtf.repositories.CityRepository;
import org.jakubklimo.wtf.repositories.MeasurementRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class MeasurementServiceTest {
    @Mock
    private MeasurementRepository measurementRepository;
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private MeasurementService measurementService;

    public MeasurementServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private City sampleCity() {
        return new City(1L, "Prague", 50.073658, 14.418540, null, List.of());
    }

    private Measurement sampleMeasurement() {
        return new Measurement(1L, LocalDateTime.now(), 20.0, 1000.0, 60.0, 18.0, 22.0, "Clear", "clear sky", 5.0, sampleCity());
    }

    private MeasurementDto sampleMeasurementDto() {
        return new MeasurementDto(LocalDateTime.now(), 1L, 20.0, 1000.0, 60.0, 18.0, 22.0, "Clear", "clear sky", 5.0);
    }

    @Test
    void findAll_ShouldReturnListOfMeasurements() {
        List<Measurement> measurements = List.of(sampleMeasurement());
        when(measurementRepository.findAll()).thenReturn(measurements);

        List<Measurement> result = measurementService.findAll();

        assertThat(result).hasSize(1);
        verify(measurementRepository, times(1)).findAll();
    }

    @Test
    void getMeasurementById_ShouldReturnMeasurement_WhenExists() {
        when(measurementRepository.findById(1L)).thenReturn(Optional.of(sampleMeasurement()));

        Measurement result = measurementService.getMeasurementById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(measurementRepository, times(1)).findById(1L);
    }

    @Test
    void getMeasurementById_ShouldThrowException_WhenNotFound() {
        when(measurementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> measurementService.getMeasurementById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Measurement with ID 1 not found");
    }

    @Test
    void getAverageStats_ShouldReturnStatistics_WhenMeasurementsExist() {
        City city = sampleCity();
        List<Measurement> measurements = List.of(
                new Measurement(1L, LocalDateTime.now(), 20.0, 1000.0, 60.0, 18.0, 22.0, "Clear", "clear sky", 5.0, city),
                new Measurement(2L, LocalDateTime.now(), 22.0, 1005.0, 65.0, 20.0, 25.0, "Clear", "clear sky", 6.0, city)
        );

        when(cityRepository.findByName("Prague")).thenReturn(Optional.of(city));
        when(measurementRepository.findRecentMeasurements(any(City.class), any(LocalDateTime.class)))
                .thenReturn(measurements);

        MeasurementStatsDto stats = measurementService.getAverageStats("Prague", 7);

        assertThat(stats.avgTemperature()).isEqualTo(21.0);
        assertThat(stats.avgPressure()).isEqualTo(1002.5);
        assertThat(stats.avgHumidity()).isEqualTo(62.5);
        assertThat(stats.avgWindSpeed()).isEqualTo(5.5);
    }

    @Test
    void createMeasurement_ShouldSaveAndReturnMeasurement() {
        City city = sampleCity();
        MeasurementDto dto = sampleMeasurementDto();
        Measurement measurement = sampleMeasurement();

        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(measurementRepository.save(any(Measurement.class))).thenReturn(measurement);

        Measurement result = measurementService.createMeasurement(dto);

        assertThat(result.getCity().getName()).isEqualTo("Prague");
        verify(measurementRepository, times(1)).save(any(Measurement.class));
    }

    @Test
    void createMeasurement_ShouldThrowException_WhenCityNotFound() {
        MeasurementDto dto = sampleMeasurementDto();
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> measurementService.createMeasurement(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("City with ID 1 not found");
    }

    @Test
    void updateMeasurement_ShouldUpdateAndReturnMeasurement() {
        City city = sampleCity();
        MeasurementDto dto = sampleMeasurementDto();
        Measurement measurement = sampleMeasurement();

        when(measurementRepository.findById(1L)).thenReturn(Optional.of(measurement));
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(measurementRepository.save(any(Measurement.class))).thenReturn(measurement);

        Measurement result = measurementService.updateMeasurement(1L, dto);

        assertThat(result.getCity().getName()).isEqualTo("Prague");
        verify(measurementRepository, times(1)).save(measurement);
    }

    @Test
    void deleteMeasurement_ShouldDeleteMeasurement_WhenExists() {
        Measurement measurement = sampleMeasurement();
        when(measurementRepository.findById(1L)).thenReturn(Optional.of(measurement));
        doNothing().when(measurementRepository).delete(measurement);

        measurementService.deleteMeasurement(1L);

        verify(measurementRepository, times(1)).delete(measurement);
    }

    @Test
    void deleteMeasurement_ShouldThrowException_WhenNotFound() {
        when(measurementRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> measurementService.deleteMeasurement(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Measurement with ID 1 not found");
    }
}
