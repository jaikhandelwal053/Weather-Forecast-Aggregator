package com.weather.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.exception.WeatherServiceException;
import com.weather.model.AccuWeatherResponse;
import com.weather.model.AccuWeatherResponse.Temperature;
import com.weather.model.OpenWeatherResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FatchWeatherDataService {

	private final WebClient.Builder webClientBuilder;

	@Value("${accuweather.api.key}")
	private String accuweatherApiKey;

	@Value("${accuweather.api.location.url}")
	private String accuweatherLocationUrl;

	@Value("${accuweather.api.weather.url}")
	private String accuweatherWeatherUrl;

	@Value("${openweather.api.key}")
	private String openweatherApiKey;

	@Value("${openweather.api.geocode.url}")
	private String openweatherGeocodeUrl;

	@Value("${openweather.api.weather.url}")
	private String openweatherWeatherUrl;

	public CompletableFuture<AccuWeatherResponse> getAccuWeatherData(String city) {
		return webClientBuilder.build()
				.get()
				.uri("https://dataservice.accuweather.com/locations/v1/search?q=" + city + "&apikey=" + accuweatherApiKey)
				.retrieve()
				.bodyToMono(String.class).flatMap(locationResponse -> {
					String locationKey = extractLocationKey(locationResponse);
					return webClientBuilder.build()
							.get()
							.uri("https://dataservice.accuweather.com/currentconditions/v1/" + locationKey + "?apikey="+ accuweatherApiKey)
							.retrieve()
							.bodyToMono(String.class)
							.map(weatherResponse -> extractWeatherData(weatherResponse));
				}).toFuture();
	}

	private String extractLocationKey(String response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response);
			if (root.isArray() && root.size() > 0) {
				JsonNode firstResult = root.get(0);
				return firstResult.path("Key").asText();
			}
		} catch (Exception e) {
			throw new WeatherServiceException("Failed to parse AccuWeather location response", e);
		}
		throw new WeatherServiceException("No Key found in AccuWeather response");
	}

	private AccuWeatherResponse extractWeatherData(String response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response);
			if (root.isArray() && root.size() > 0) {
				JsonNode firstResult = root.get(0);
				AccuWeatherResponse accuWeatherResponse = new AccuWeatherResponse();
				accuWeatherResponse.setWeatherText(firstResult.path("WeatherText").asText());
				accuWeatherResponse.setHasPrecipitation(firstResult.path("HasPrecipitation").asBoolean());
				accuWeatherResponse.setPrecipitationType(firstResult.path("PrecipitationType").asText(null));
				accuWeatherResponse.setTemperature(
						new Temperature((firstResult.path("Temperature").path("Metric").path("Value").asDouble()),
								(firstResult.path("Temperature").path("Metric").path("Unit").asText())));

				accuWeatherResponse.setDayTime(firstResult.path("IsDayTime").asBoolean());
				return accuWeatherResponse;
			}
		} catch (Exception e) {
			throw new WeatherServiceException("Failed to parse AccuWeather weather response", e);
		}
		throw new WeatherServiceException("No weather data found in AccuWeather response");
	}

	public CompletableFuture<OpenWeatherResponse> getOpenWeatherData(String zip, String country) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				String geocodeUrl = openweatherGeocodeUrl.replace("{zip}", zip).replace("{country code}", country).replace("{apikey}", openweatherApiKey);

				String geocodeResponse = webClientBuilder.build().get().uri(geocodeUrl).retrieve().bodyToMono(String.class).block();

				String lat = geocodeResponse.substring(geocodeResponse.indexOf("\"lat\":") + 6,geocodeResponse.indexOf(",\"lon\":"));
				String lon = geocodeResponse.substring(geocodeResponse.indexOf("\"lon\":") + 6,geocodeResponse.indexOf("}"));
				String[] lonParts = lon.split(",");

				String weatherUrl = openweatherWeatherUrl.replace("{lat}", lat).replace("{lon}", lonParts[0]).replace("{apikey}", openweatherApiKey);
				
				return webClientBuilder.build()
						.get()
						.uri(weatherUrl)
						.retrieve()
						.bodyToMono(OpenWeatherResponse.class)
						.block();
			} catch (Exception e) {
				throw new WeatherServiceException("Failed to fetch data from OpenWeather", e);
			}
		});
	}

}
