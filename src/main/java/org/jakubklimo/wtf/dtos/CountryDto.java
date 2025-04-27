package org.jakubklimo.wtf.dtos;

import jakarta.validation.constraints.NotBlank;

public record CountryDto(@NotBlank String name) {
}
