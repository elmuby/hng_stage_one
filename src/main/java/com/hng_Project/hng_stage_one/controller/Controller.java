package com.hng_Project.hng_stage_one.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hng_Project.hng_stage_one.dto.WeatherData;
import com.hng_Project.hng_stage_one.service.ExternalApiServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@RestController()
@RequestMapping("/api")
public class Controller {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Controller.class);

	private ExternalApiServices externalApiServices;

	public Controller(ExternalApiServices externalApiServices) {
		super();
		this.externalApiServices = externalApiServices;
	}

	@GetMapping("/hello")
	public ResponseEntity<Map<String, String>> getLocationAndTemperature(
			@Valid @RequestParam("visitor_name") @NotEmpty String visitor_name, HttpServletRequest request) {

		WeatherData weatherData = null;
		Map<String, String> response = new LinkedHashMap<>();
		try {
			String client_ip = getClientIp(request);

//		to fetch from external api
			try {
				weatherData = externalApiServices.getLocationAndWeatherByIp(client_ip);
			} catch (IOException e) {
				logger.error("error processing request", e);
				return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch weather data"));
			}

			response.put("client_ip:", client_ip);
			response.put("location: ", weatherData.getCity());
			response.put("greeting: ", "Hello, " + visitor_name + "!, the tempeature is " + weatherData.getTemperature()
					+ " degrees Celcius in " + weatherData.getCity());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("error processing request", e);
			response.put("Error", "An unexpected error occoured");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}


	private String getClientIp(HttpServletRequest request) {
		String clientIp = request.getHeader("X-Forwarded-For");
		if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getHeader("X-Real-IP");
		}
		if (clientIp == null || clientIp.isEmpty() || "unknown".equalsIgnoreCase(clientIp)) {
			clientIp = request.getRemoteAddr();
		} else {
			clientIp = clientIp.split(",")[0];
		}
		return clientIp;
	}

}
