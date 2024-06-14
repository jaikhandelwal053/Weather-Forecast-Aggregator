package com.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccuWeatherResponse {
	private String weatherText;
	private boolean hasPrecipitation;
	private String precipitationType;
	private boolean isDayTime;
	private Temperature temperature;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Temperature {
		private double value;
		private String unit;
	}

}
