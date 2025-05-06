package org.jakubklimo.wtf.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record MeasurementDto(
        @NotNull LocalDateTime datetime,
        @NotNull Long city_id,
        @NotNull double temperature,
        @NotNull double pressure,
        @NotNull double humidity,
        @NotNull double temperatureMin,
        @NotNull double temperatureMax,
        @NotBlank String weatherMain,
        @NotBlank String weatherDesc,
        @NotNull double windSpeed
) {
}
