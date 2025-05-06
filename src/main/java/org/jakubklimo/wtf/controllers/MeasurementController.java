package org.jakubklimo.wtf.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.jakubklimo.wtf.dtos.MeasurementDto;
import org.jakubklimo.wtf.dtos.MeasurementStatsDto;
import org.jakubklimo.wtf.models.Measurement;
import org.jakubklimo.wtf.services.MeasurementService;
import org.jakubklimo.wtf.services.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/measurements")
@RequiredArgsConstructor
public class MeasurementController {
    private final MeasurementService measurementService;
    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<List<Measurement>> getAllMeasurements(){
        return ResponseEntity.ok(measurementService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Measurement> getMeasurementById(@PathVariable Long id){
        Measurement measurement = measurementService.getMeasurementById(id);
        return ResponseEntity.ok(measurement);
    }

    @GetMapping("/latest")
    public ResponseEntity<Measurement> getAllMeasurementsByCity(@RequestParam String cityName){
        Measurement measurement = weatherService.getCurrentMeasurement(cityName);
        return ResponseEntity.ok(measurement);
    }

    @GetMapping("/stats")
    public ResponseEntity<MeasurementStatsDto> getAverageStatsByCity(@RequestParam String cityName, @RequestParam Integer days){
        MeasurementStatsDto stats = measurementService.getAverageStats(cityName, days);
        return ResponseEntity.ok(stats);
    }

    @PostMapping
    public ResponseEntity<Measurement> createMeasurement(@Valid @RequestBody MeasurementDto dto) {
        Measurement created = measurementService.createMeasurement(dto);
        URI location = URI.create(String.format("/api/measurements/%d", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Measurement> updateMeasurement(@PathVariable Long id, @Valid @RequestBody MeasurementDto measurement){
        Measurement updatedMeasurement = measurementService.updateMeasurement(id, measurement);
        return ResponseEntity.ok(updatedMeasurement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable Long id){
        measurementService.deleteMeasurement(id);
        return ResponseEntity.noContent().build();
    }
}
