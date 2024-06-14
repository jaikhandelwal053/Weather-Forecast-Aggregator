# Weather Forecast Aggregator

## Overview
This project is a Weather Forecast Aggregator application built using Java 8, Spring Boot, and REST. The application fetches weather data from AccuWeather and OpenWeather APIs concurrently and aggregates the results. It also includes error handling and testing with Mockito.

## Requirements
    Java 8
    Spring Boot
    Lombok
    Maven
    AccuWeather API key
    OpenWeather API key


## API Endpoints

### Api 
URI: `http://localhost:8090/weather?city={cityName}&zip={zipcode},{countrycode}`

GET `http://localhost:8080/weather?city=Noida&zip=110025,IN`

Response:
{
    "weatherText": "Hazy sunshine",
    "hasPrecipitation": false,
    "precipitationType": null,
    "temperature": {
        "value": 45.7,
        "unit": "C"
    },
    "feelsLike": {
        "value": 41.7,
        "unit": "C"
    },
    "pressure": 994,
    "humidity": 7,
    "visibility": 10000,
    "wind": {
        "speed": 7.34,
        "deg": 255,
        "gust": 6.3
    },
    "sunrise": 1716940882,
    "sunset": 1716990327,
    "dayTime": true
}

