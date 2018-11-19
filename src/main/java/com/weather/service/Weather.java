package com.weather.service;

import javax.persistence.*;
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
	@Pattern(regexp = "\\-?([0-8]?[0-9]|[9][0])\\.\\d+\\,\\-?([0-1][0-8]|[0-9])?[0-9]\\.\\d+",
			message = "Coordinates format is wrong or coordinates are out of domain")
	public String coordinates;

	@Column(columnDefinition = "VARCHAR(255) DEFAULT \'UNKNOWN\'")
	public String city;

	@Column(columnDefinition = "TIMESTAMP(26,6) NOT NULL DEFAULT CURRENT_TIMESTAMP")
	public Date inputTime;
}
