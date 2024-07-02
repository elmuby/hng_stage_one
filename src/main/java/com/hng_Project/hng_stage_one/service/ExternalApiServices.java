package com.hng_Project.hng_stage_one.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hng_Project.hng_stage_one.dto.WeatherData;

@Service
public class ExternalApiServices {
	
	@Value("${weatherapi.api.key}")
    private String apiKey;
	
	private RestTemplate restTemplate;
	private ObjectMapper objectMapper;
	
	public ExternalApiServices(RestTemplate restTemplate, ObjectMapper objectMapper) {
		super();
		this.restTemplate = restTemplate;
		this.objectMapper = objectMapper;
	}
	
	public WeatherData getLocationAndWeatherByIp(String ip) throws IOException {
		String url = "http://api.weatherapi.com/v1/current.json?key=" +apiKey + "&q=" + ip;
		String response =  restTemplate.getForObject(url, String.class);
		
//		parsing json response
		JsonNode root = objectMapper.readTree(response);
		String city = root.path("location").path("name").asText();
		double temperature = root.path("current").path("temp_c").asDouble();
		
		return new WeatherData(city, temperature);
		
	}

}
