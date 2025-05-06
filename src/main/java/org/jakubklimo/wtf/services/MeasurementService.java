package org.jakubklimo.wtf.services;

import lombok.RequiredArgsConstructor;
import org.jakubklimo.wtf.dtos.MeasurementDto;
import org.jakubklimo.wtf.dtos.MeasurementStatsDto;
import org.jakubklimo.wtf.exceptions.EntityNotFoundException;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Measurement;
import org.jakubklimo.wtf.repositories.CityRepository;
import org.jakubklimo.wtf.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final CityRepository cityRepository;

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    public Measurement getMeasurementById(Long id) {
        return measurementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Measurement", id.toString()));
    }

    public MeasurementStatsDto getAverageStats(String cityName, int days) {
        LocalDateTime from = LocalDateTime.now().minusDays(days);
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new EntityNotFoundException("City", cityName));
        List<Measurement> recent = measurementRepository.findRecentMeasurements(city, from);

        DoubleSummaryStatistics tempStats = recent.stream().mapToDouble(Measurement::getTemperature).summaryStatistics();
        DoubleSummaryStatistics pressureStats = recent.stream().mapToDouble(Measurement::getPressure).summaryStatistics();
        DoubleSummaryStatistics humidityStats = recent.stream().mapToDouble(Measurement::getHumidity).summaryStatistics();
        DoubleSummaryStatistics windStats = recent.stream().mapToDouble(Measurement::getWindSpeed).summaryStatistics();

        return new MeasurementStatsDto(
                tempStats.getAverage(),
                pressureStats.getAverage(),
                humidityStats.getAverage(),
                windStats.getAverage()
        );
    }

    public Measurement createMeasurement(MeasurementDto measurement) {
        Measurement newMeasurement = new Measurement();
        City city = cityRepository.findById(measurement.city_id())
                .orElseThrow(() -> new EntityNotFoundException("City", measurement.city_id().toString()));
        newMeasurement.setCity(city);
        newMeasurement.setTemperature(measurement.temperature());
        newMeasurement.setPressure(measurement.pressure());
        newMeasurement.setHumidity(measurement.humidity());
        newMeasurement.setDatetime(measurement.datetime());
        newMeasurement.setTemperatureMin(measurement.temperatureMin());
        newMeasurement.setTemperatureMax(measurement.temperatureMax());
        newMeasurement.setWeatherMain(measurement.weatherMain());
        newMeasurement.setWeatherDesc(measurement.weatherDesc());
        newMeasurement.setWindSpeed(measurement.windSpeed());
        return measurementRepository.save(newMeasurement);
    }

    public Measurement updateMeasurement(Long id, MeasurementDto measurement) {
        Measurement measurementToUpdate = measurementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Measurement", id.toString()));
        City city = cityRepository.findById(measurement.city_id())
                .orElseThrow(() -> new EntityNotFoundException("City", measurement.city_id().toString()));
        if (measurement.datetime() != null && measurement.weatherMain() != null && measurement.weatherDesc() != null) {
            measurementToUpdate.setCity(city);
            measurementToUpdate.setTemperature(measurement.temperature());
            measurementToUpdate.setPressure(measurement.pressure());
            measurementToUpdate.setHumidity(measurement.humidity());
            measurementToUpdate.setDatetime(measurement.datetime());
            measurementToUpdate.setTemperatureMin(measurement.temperatureMin());
            measurementToUpdate.setTemperatureMax(measurement.temperatureMax());
            measurementToUpdate.setWeatherMain(measurement.weatherMain());
            measurementToUpdate.setWeatherDesc(measurement.weatherDesc());
            measurementToUpdate.setWindSpeed(measurement.windSpeed());
        }
        return measurementRepository.save(measurementToUpdate);
    }

    public void deleteMeasurement(Long id) {
        Measurement measurementToDelete = measurementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Measurement", id.toString()));
        measurementRepository.delete(measurementToDelete);
    }
}
