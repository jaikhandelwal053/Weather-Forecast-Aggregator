package com.weather.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.exception.WeatherServiceException;
import com.weather.model.WeatherResponse;
import com.weather.service.WeatherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public WeatherResponse getWeather(@RequestParam String city, @RequestParam String zip) throws ExecutionException {
        String[] zipParts = zip.split(",");
        if (zipParts.length != 2) {
            throw new WeatherServiceException("Invalid zip code format. Expected format: zip,country");
        }
        return weatherService.getAggregatedWeatherData(city, zipParts[0], zipParts[1]);
    }
}
