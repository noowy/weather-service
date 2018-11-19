package com.weather.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TestUtils
{
	public static ArrayList<Weather> createElevenWeathers() throws ParseException
	{
		ArrayList<Weather> weatherList = new ArrayList<>();
		Weather weather;
		int weathersAmount = 11;
		String[] cities = { "nordtofte",
				"mirranatwa",
				"antanimenakely",
				"phum kas pen",
				"kungtotkol",
				"changtieni",
				"garland",
				"starigrad",
				"evciler",
				"sidodadi empat",
				"krasnoyarsk"};
		BigDecimal[] temperatures = { new BigDecimal(20.54),
				new BigDecimal(30.64),
				new BigDecimal(-12.53),
				new BigDecimal(52.42),
				new BigDecimal(20.21),
				new BigDecimal(-5.24),
				new BigDecimal(-20.51),
				new BigDecimal(15.23),
				new BigDecimal(8.52),
				new BigDecimal(10.52),
				new BigDecimal(-16.24) };
		String[] coordinates = { "62.2666664,-6.5",
				"-37.4166679,142.4166718",
				"-17.7833328,48.3166656",
				"11.9499998,105.4499969",
				"36.8499985,128.2833405",
				"34.4316673,108.5377808",
				"31.5563889,-86.8233337",
				"44.7966652,14.8833332",
				"41.2435989,33.9445305",
				"3.6455901,98.797699",
				"56.016143,92.8571573"};

		for (int i = 0; i < weathersAmount; i++)
		{
			weather = new Weather();
			weather.city = cities[i];
			weather.coordinates = coordinates[i];
			weather.temperature = temperatures[i];
			weather.inputTime = getCurrentDate();
			weatherList.add(weather);
		}

		return weatherList;
	}

	public static ArrayList<Weather> createWeathersInCity() throws ParseException
	{
		ArrayList<Weather> weathers = new ArrayList<>();
		Weather weather = new Weather();

		weather.city = "krasnoyarsk";
		weather.coordinates = "56.016143,92.8571573";
		weather.temperature = new BigDecimal(-20.52);
		weather.inputTime = getCurrentDate();
		weathers.add(weather);

		return weathers;
	}

	public static Date getCurrentDate() throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = new Date(System.currentTimeMillis());
		String dateFormatted;

		dateFormatted = formatter.format(currentDate);
		currentDate = formatter.parse(dateFormatted);

		return currentDate;
	}

	public static byte[] convertObjectToJsonBytes(Object object) throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

}
