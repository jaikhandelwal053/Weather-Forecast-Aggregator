package com.weather.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weather.model.AccuWeatherResponse;
import com.weather.model.OpenWeatherResponse;
import com.weather.model.WeatherResponse;

@Service
public class WeatherService {

	@Autowired
	public FatchWeatherDataService fatchWeatherDataService;

	public WeatherResponse getAggregatedWeatherData(String city, String zip, String country) throws ExecutionException {
		
		CompletableFuture<AccuWeatherResponse> accuWeatherFuture = fatchWeatherDataService.getAccuWeatherData(city);
		CompletableFuture<OpenWeatherResponse> openWeatherFuture = fatchWeatherDataService.getOpenWeatherData(zip, country);

		CompletableFuture.allOf(accuWeatherFuture, openWeatherFuture).join();

		AccuWeatherResponse accuWeatherData = accuWeatherFuture.join();
		OpenWeatherResponse openWeatherData = openWeatherFuture.join();

		System.out.println("Result of Acc... : " + accuWeatherData);
		System.out.println("Result of Open... : " + openWeatherData);
		return aggregateWeatherData(accuWeatherData, openWeatherData);
	}

	private WeatherResponse aggregateWeatherData(AccuWeatherResponse accuWeatherData,
			OpenWeatherResponse openWeatherData) {
		WeatherResponse response = new WeatherResponse();
		response.setWeatherText(accuWeatherData.getWeatherText());
		response.setHasPrecipitation(accuWeatherData.isHasPrecipitation());
		response.setPrecipitationType(accuWeatherData.getPrecipitationType());
		response.setDayTime(accuWeatherData.isDayTime());
		response.setTemperature(new WeatherResponse.Temperature((accuWeatherData.getTemperature().getValue()),
				(accuWeatherData.getTemperature().getUnit())));
		response.setFeelsLike(
				new WeatherResponse.Temperature((kelvinToCelsius(openWeatherData.getMain().getFeels_like())), "C"));
		response.setPressure(openWeatherData.getMain().getPressure());
		response.setHumidity(openWeatherData.getMain().getHumidity());
		response.setVisibility(openWeatherData.getVisibility());
		response.setWind(new WeatherResponse.Wind((openWeatherData.getWind().getSpeed()),
				(openWeatherData.getWind().getDeg()), (openWeatherData.getWind().getGust())));
		response.setSunrise(openWeatherData.getSys().getSunrise());
		response.setSunset(openWeatherData.getSys().getSunset());
		return response;
	}

	public double kelvinToCelsius(double kelvin) {
		return Math.round((kelvin - 273.15) * 10.0) / 10.0;
	}
}
