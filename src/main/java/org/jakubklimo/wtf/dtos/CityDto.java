package org.jakubklimo.wtf.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CityDto(@NotBlank String name, @NotNull Long country_id, @NotNull double latitude, @NotNull double longitude) {
}
