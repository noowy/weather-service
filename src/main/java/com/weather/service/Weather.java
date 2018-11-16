package com.weather.service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Weather
{

	@NotNull(message = "The temperature must not be null")
	@DecimalMin(value = "-100.0", message = "Temperature is too low")
	@DecimalMax(value = "100.0", message = "Temperature is too high")
	public BigDecimal temperature;

	@Id
	@NotNull(message = "The coordinates must not be null")
	@NotEmpty(message = "The coordinates must be present")
	@Pattern(regexp = "\\-?[1-9]?[0-9]\\.\\d+\\,\\-?[0-1]?[0-8]?[0-9]\\.\\d+",
			message = "Coordinates format is wrong or coordinates are out of domain")
	public String coordinates;

	public String city;

	public Date inputTime;
}
