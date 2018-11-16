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
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Controller
public class WeatherController
{
	// TODO: add separate endpoint for updating weather in repo

	@Autowired
	WeatherRepository weatherRepo;

	@PostMapping(path = "/add_weather", consumes = "application/json")
	public ResponseEntity<Integer> handleNewWeather(@Valid @RequestBody Weather weather) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = new Date(System.currentTimeMillis());
		String dateFormatted;

		dateFormatted = formatter.format(currentDate);
		currentDate = formatter.parse(dateFormatted);

		weather.inputTime = currentDate;

		String city = ServiceApplication.geo.retrieveCity(weather.coordinates);
		if (city != null)
			weather.city = ServiceApplication.geo.retrieveCity(weather.coordinates);
		else
			weather.city = "Unknown";

		weatherRepo.save(weather);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(path = "/get_weather")
	public ResponseEntity<Iterable> returnWeather(@RequestParam(name = "coords", required = false) String coordinates)
	{
		return coordinates == null
				?
				new ResponseEntity<>(weatherRepo.findAll(), HttpStatus.OK)
				:
				new ResponseEntity<>(weatherRepo.findByCoords(coordinates), HttpStatus.OK);
	}
}

