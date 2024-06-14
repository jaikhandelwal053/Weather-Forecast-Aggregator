package com.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.weather.controller.WeatherController;
import com.weather.model.WeatherResponse;
import com.weather.service.WeatherService;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherController weatherController;

    @Test
    public void testGetWeather_SunnyDay() throws ExecutionException {
        WeatherResponse weatherResponse = createWeatherResponse("Sunny", false, null, true, 32.9f, 35.9f, 999, 39, 10000, 1.06f, 82, 3.13f, 1687996522L, 1688046716L);

        when(weatherService.getAggregatedWeatherData("Noida", "110025", "IN")).thenReturn(weatherResponse);

        WeatherResponse response = weatherController.getWeather("Noida", "110025,IN");

        assertEquals("Sunny", response.getWeatherText());
        assertEquals(false, response.isHasPrecipitation());
        assertEquals(null, response.getPrecipitationType());
        assertEquals(true, response.isDayTime());
        assertEquals(32.9f, response.getTemperature().getValue());
        assertEquals("C", response.getTemperature().getUnit());
        assertEquals(35.9f, response.getFeelsLike().getValue());
        assertEquals("C", response.getFeelsLike().getUnit());
        assertEquals(999, response.getPressure());
        assertEquals(39, response.getHumidity());
        assertEquals(10000, response.getVisibility());
        assertEquals(1.06f, response.getWind().getSpeed());
        assertEquals(82, response.getWind().getDeg());
        assertEquals(3.13f, response.getWind().getGust());
        assertEquals(1687996522L, response.getSunrise());
        assertEquals(1688046716L, response.getSunset());
    }

    @Test
    public void testGetWeather_RainyDay() throws ExecutionException {
        WeatherResponse weatherResponse = createWeatherResponse("Rainy", true, "Rain", false, 25.0f, 27.0f, 1012, 89, 5000, 3.0f, 180, 5.0f, 1687996522L, 1688046716L);

        when(weatherService.getAggregatedWeatherData("Mumbai", "400001", "IN")).thenReturn(weatherResponse);

        WeatherResponse response = weatherController.getWeather("Mumbai", "400001,IN");

        assertEquals("Rainy", response.getWeatherText());
        assertEquals(true, response.isHasPrecipitation());
        assertEquals("Rain", response.getPrecipitationType());
        assertEquals(false, response.isDayTime());
        assertEquals(25.0f, response.getTemperature().getValue());
        assertEquals("C", response.getTemperature().getUnit());
        assertEquals(27.0f, response.getFeelsLike().getValue());
        assertEquals("C", response.getFeelsLike().getUnit());
        assertEquals(1012, response.getPressure());
        assertEquals(89, response.getHumidity());
        assertEquals(5000, response.getVisibility());
        assertEquals(3.0f, response.getWind().getSpeed());
        assertEquals(180, response.getWind().getDeg());
        assertEquals(5.0f, response.getWind().getGust());
        assertEquals(1687996522L, response.getSunrise());
        assertEquals(1688046716L, response.getSunset());
    }

    @Test
    public void testGetWeather_NullResponse() throws ExecutionException {
        when(weatherService.getAggregatedWeatherData("Delhi", "110001", "IN")).thenReturn(null);

        WeatherResponse response = weatherController.getWeather("Delhi", "110001,IN");

        assertEquals(null, response);
    }

    @Test
    public void testGetWeather_EmptyCity() throws ExecutionException {
        WeatherResponse weatherResponse = createWeatherResponse("Sunny", false, null, true, 32.9f, 35.9f, 999, 39, 10000, 1.06f, 82, 3.13f, 1687996522L, 1688046716L);

        when(weatherService.getAggregatedWeatherData("", "110025", "IN")).thenReturn(weatherResponse);

        WeatherResponse response = weatherController.getWeather("", "110025,IN");

        assertEquals("Sunny", response.getWeatherText());
        assertEquals(false, response.isHasPrecipitation());
        assertEquals(null, response.getPrecipitationType());
        assertEquals(true, response.isDayTime());
        assertEquals(32.9f, response.getTemperature().getValue());
        assertEquals("C", response.getTemperature().getUnit());
        assertEquals(35.9f, response.getFeelsLike().getValue());
        assertEquals("C", response.getFeelsLike().getUnit());
        assertEquals(999, response.getPressure());
        assertEquals(39, response.getHumidity());
        assertEquals(10000, response.getVisibility());
        assertEquals(1.06f, response.getWind().getSpeed());
        assertEquals(82, response.getWind().getDeg());
        assertEquals(3.13f, response.getWind().getGust());
        assertEquals(1687996522L, response.getSunrise());
        assertEquals(1688046716L, response.getSunset());
    }

    private WeatherResponse createWeatherResponse(String weatherText, boolean hasPrecipitation, String precipitationType, boolean dayTime, float tempValue, float feelsLikeValue, int pressure, int humidity, int visibility, float windSpeed, int windDeg, float windGust, long sunrise, long sunset) {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setWeatherText(weatherText);
        weatherResponse.setHasPrecipitation(hasPrecipitation);
        weatherResponse.setPrecipitationType(precipitationType);
        weatherResponse.setDayTime(dayTime);

        WeatherResponse.Temperature temperature = new WeatherResponse.Temperature();
        temperature.setValue(tempValue);
        temperature.setUnit("C");
        weatherResponse.setTemperature(temperature);

        WeatherResponse.Temperature feelsLike = new WeatherResponse.Temperature();
        feelsLike.setValue(feelsLikeValue);
        feelsLike.setUnit("C");
        weatherResponse.setFeelsLike(feelsLike);

        weatherResponse.setPressure(pressure);
        weatherResponse.setHumidity(humidity);
        weatherResponse.setVisibility(visibility);

        WeatherResponse.Wind wind = new WeatherResponse.Wind();
        wind.setSpeed(windSpeed);
        wind.setDeg(windDeg);
        wind.setGust(windGust);
        weatherResponse.setWind(wind);

        weatherResponse.setSunrise(sunrise);
        weatherResponse.setSunset(sunset);

        return weatherResponse;
    }
}
