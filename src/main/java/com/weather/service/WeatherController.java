package com.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@Controller
public class WeatherController
{

	@Autowired
	WeatherRepository weatherRepo;

	@PostMapping(path = "/add_weather", consumes = "application/json")
	public ResponseEntity<Integer> handleNewWeather(@Valid @RequestBody Weather weather)
	{
		weatherRepo.save(weather);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(path = "/get_weather")
	public ResponseEntity<Iterable> returnWeather(@RequestParam(name = "coords", required = false) String coordinates)
	{
		return new ResponseEntity<>(weatherRepo.findAll(), HttpStatus.OK);
	}

	@GetMapping(path = "/check_geo")
	public ResponseEntity<String> geoCheck()
	{
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

