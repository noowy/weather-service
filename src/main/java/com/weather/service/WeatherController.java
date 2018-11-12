package com.weather.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
public class WeatherController
{
	@PostMapping(path = "/add_weather", params = {"temp", "coords"})
	public void handleNewWeather(@RequestParam(name = "temp") Integer temperature,
								 @RequestParam(name = "coords") String coordinates)
	{

	}

	@GetMapping(path = "/get_weather")
	public void returnWeather(@RequestParam(name = "coords", required = false) String coordinates)
	{

	}
}

