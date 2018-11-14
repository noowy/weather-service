package com.weather.service;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api")
public class ApiController
{

	@Autowired
	WeatherRepository weatherRepo;

	@GetMapping(path = "/get_weather")
	public ResponseEntity<Iterable> returnLastTen()
	{
		return new ResponseEntity<>(weatherRepo.getLastTenWeathers(), HttpStatus.OK);
	}
}
