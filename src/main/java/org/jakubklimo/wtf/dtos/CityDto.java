package org.jakubklimo.wtf.dtos;

import jakarta.validation.constraints.NotBlank;

public record CityDto(@NotBlank String name, @NotBlank Long country_id, @NotBlank double latitude, @NotBlank double longitude) {
}
