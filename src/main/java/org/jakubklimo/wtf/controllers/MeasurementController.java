package org.jakubklimo.wtf.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.jakubklimo.wtf.dtos.MeasurementDto;
import org.jakubklimo.wtf.dtos.MeasurementStatsDto;
import org.jakubklimo.wtf.models.Measurement;
import org.jakubklimo.wtf.services.MeasurementService;
import org.jakubklimo.wtf.services.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/measurement")
@RequiredArgsConstructor
public class MeasurementController {
    private final MeasurementService measurementService;
    private final WeatherService weatherService;

    @GetMapping("/all")
    public List<Measurement> getAllMeasurements(){
        return measurementService.findAll();
    }

    @GetMapping("/latest/{cityName}")
    public Measurement getAllMeasurementsByCity(@PathVariable String cityName){
        return weatherService.getCurrentMeasurement(cityName);
    }

    @GetMapping("/avg/{cityName}")
    public MeasurementStatsDto getAverageStatsByCity(@PathVariable String cityName, @RequestParam(required = false) Integer days){
        return measurementService.getAverageStats(cityName, days);
    }

    @GetMapping("/{id}")
    public Measurement getMeasurementById(@PathVariable Long id){
        return measurementService.getMeasurementById(id);
    }

    @PutMapping("/{id}")
    public Measurement updateMeasurement(@PathVariable Long id, @RequestBody MeasurementDto measurement){
        return measurementService.updateMeasurement(id, measurement);
    }

    @DeleteMapping("/{id}")
    public void deleteMeasurement(@PathVariable Long id){
        measurementService.deleteMeasurement(id);
    }
}
