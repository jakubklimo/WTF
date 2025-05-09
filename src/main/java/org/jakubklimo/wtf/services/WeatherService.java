package org.jakubklimo.wtf.services;

import lombok.RequiredArgsConstructor;
import org.jakubklimo.wtf.exceptions.EntityNotFoundException;
import org.jakubklimo.wtf.models.City;
import org.jakubklimo.wtf.models.Measurement;
import org.jakubklimo.wtf.repositories.CityRepository;
import org.jakubklimo.wtf.repositories.MeasurementRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;

import java.time.LocalDateTime;

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final CityRepository cityRepository;
    private final MeasurementRepository measurementRepository;
    private final RestTemplate restTemplate;

    public WeatherService(CityRepository cityRepository, MeasurementRepository measurementRepository, RestTemplate restTemplate) {
        this.cityRepository = cityRepository;
        this.measurementRepository = measurementRepository;
        this.restTemplate = restTemplate;
    }

    @Value("${weather.api.key}")
    private String apiKey;

    public Measurement getCurrentMeasurement(String cityName){
        City city = cityRepository.findByName(cityName)
                .orElseThrow(() -> new EntityNotFoundException("City", cityName));
        try{
            String url = UriComponentsBuilder.fromUriString("https://api.openweathermap.org/data/2.5/weather")
                    .queryParam("lat", city.getLatitude())
                    .queryParam("lon", city.getLongitude())
                    .queryParam("units", "metric")
                    .queryParam("appid", apiKey)
                    .toUriString();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            JSONObject json = new JSONObject(response.getBody());
            LocalDateTime timestamp = LocalDateTime.now();
            double temperature = json.getJSONObject("main").getDouble("temp");
            double pressure = json.getJSONObject("main").getDouble("pressure");
            double humidity = json.getJSONObject("main").getDouble("humidity");
            double tempMin = json.getJSONObject("main").getDouble("temp_min");
            double tempMax = json.getJSONObject("main").getDouble("temp_max");
            String weatherMain = json.getJSONArray("weather").getJSONObject(0).getString("main");
            String weatherDesc = json.getJSONArray("weather").getJSONObject(0).getString("description");
            double windSpeed = json.getJSONObject("wind").getDouble("speed");
            logger.debug("Fetched weather for {}: temp={}°C, pressure={} hPa, humidity={}%, tempMin={}°C, tempMax={}°C, weatherMain={}, weatherDesc={}, windSpeed={} m/s",
                    cityName, temperature, pressure, humidity, tempMin, tempMax, weatherMain, weatherDesc, windSpeed);
            Measurement measurement = new Measurement();
            measurement.setCity(city);
            measurement.setTemperature(temperature);
            measurement.setPressure(pressure);
            measurement.setHumidity(humidity);
            measurement.setDatetime(timestamp);
            measurement.setTemperatureMin(tempMin);
            measurement.setTemperatureMax(tempMax);
            measurement.setWeatherMain(weatherMain);
            measurement.setWeatherDesc(weatherDesc);
            measurement.setWindSpeed(windSpeed);

            measurementRepository.save(measurement);
            return measurement;
        }catch (Exception e){
            throw new RuntimeException("Failed to get current measurement");
        }
    }
}
