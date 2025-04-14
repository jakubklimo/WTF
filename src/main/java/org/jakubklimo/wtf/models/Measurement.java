package org.jakubklimo.wtf.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    @Id
    private int id;
    private LocalDateTime datetime;
    private double temperature;
    private double pressure;
    private double humidity;
    private double temperature_min;
    private double temperature_max;
    private String weather_main;
    private String weather_desc;
    private double wind_speed;
    private int city_id;
}
