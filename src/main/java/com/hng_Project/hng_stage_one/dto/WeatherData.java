package com.hng_Project.hng_stage_one.dto;

public class WeatherData {
	private String city;
	private Double temperature;

	public WeatherData(String city, Double temperature) {
		super();
		this.city = city;
		this.temperature = temperature;
	}

	public String getCity() {
		return city;
	}

	public Double getTemperature() {
		return temperature;
	}

}
