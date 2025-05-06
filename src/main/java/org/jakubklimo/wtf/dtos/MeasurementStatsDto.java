package org.jakubklimo.wtf.dtos;

public record MeasurementStatsDto(
        double avgTemperature,
        double avgPressure,
        double avgHumidity,
        double avgWindSpeed
) {}