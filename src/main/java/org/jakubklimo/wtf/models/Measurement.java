package org.jakubklimo.wtf.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime datetime;
    private double temperature;
    private double pressure;
    private double humidity;
    @Column(name = "temperature_min")
    private double temperatureMin;
    @Column(name = "temperature_max")
    private double temperatureMax;
    @Column(name = "weather_main")
    private String weatherMain;
    @Column(name = "weather_desc")
    private String weatherDesc;
    @Column(name = "wind_speed")
    private double windSpeed;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    @JsonBackReference
    private City city;
}
