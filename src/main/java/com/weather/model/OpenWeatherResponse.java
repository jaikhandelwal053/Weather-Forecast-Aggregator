package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor	
public class OpenWeatherResponse {
    private Main main;
    private Wind wind;
    private int visibility;
    private Sys sys;

    @Data
    public static class Main {
        private double temp;
        private double feels_like;
        private int pressure;
        private int humidity;
    }

    @Data
    public static class Wind {
        private double speed;
        private int deg;
        private double gust;
    }

    @Data
    public static class Sys {
        private long sunrise;
        private long sunset;
    }
}
