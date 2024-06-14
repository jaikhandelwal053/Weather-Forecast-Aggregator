package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private String weatherText;
    private boolean hasPrecipitation;
    private String precipitationType;
    private boolean isDayTime;
    private Temperature temperature;
    private Temperature feelsLike;
    private int pressure;
    private int humidity;
    private int visibility;
    private Wind wind;
    private long sunrise;
    private long sunset;

    @Data
    public static class Temperature {
        private double value;
        private String unit;

        public Temperature() {}

        public Temperature(double value, String unit) {
            this.value = value;
            this.unit = unit;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Wind {
        private double speed;
        private int deg;
        private double gust;
        
        
    }

}
