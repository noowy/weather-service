package com.weather.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeodecoderTests
{
	private static final Geodecoder geo = new Geodecoder();

	@Test
	public void noSuitableCityFoundShouldReturnNull()
	{
		String coordinates = "00.01024,01.204224"; // random point in Atlantic Ocean

		Assert.assertNull(geo.retrieveCity(coordinates));
	}

	@Test
	public void pointInCityRangeShouldReturnCityName()
	{
		String coordinates = "56.016143,92.8571573"; // point within range of Krasnoyarsk City
		String expected = "krasnoyarsk";

		Assert.assertEquals(geo.retrieveCity(coordinates), expected);
	}

	@Test
	public void wrongCoordinatesShouldReturnNull()
	{
		String coordinates = "1000.4214,1224.3232";

		Assert.assertNull(geo.retrieveCity(coordinates));
	}
}
