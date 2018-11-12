package com.weather.service;

import javax.persistence.Entity;
import javax.validation.constraints.*;

@Entity
public class Weather
{
	@NotNull(message = "The temperature must not be null")
	@DecimalMin(value = "-100.0f", message = "Temperature is too low")
	@DecimalMax(value = "100.0f", message = "Temperature is too high")
	public Integer temperature;

	@NotNull(message = "")
	@NotEmpty(message = "")
	@Pattern(regexp = "\\-?[1-9]?[0-9]\\.\\d+\\,\\-?[0-1]?[0-8]?[0-9]\\.\\d+",
			message = "Coordinates")
	public String coordinates;
}
