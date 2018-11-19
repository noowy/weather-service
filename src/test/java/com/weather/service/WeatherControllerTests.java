package com.weather.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static com.weather.service.TestUtils.getCurrentDate;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherControllerTests
{

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Autowired
	WeatherController weatherController;

	@MockBean
	WeatherRepository weatherRepo;

	private Weather weather;
	private ArrayList<Weather> weathers;

	@Before
	public void setUp()
	{
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.weatherController).build();
		weather = new Weather();
		weathers = new ArrayList<>();
	}

	@Test
	public void addedWeatherShouldReturnHttpOk() throws Exception
	{
		weather.city = "krasnoyarsk";
		weather.coordinates = "56.016143,92.8571573";
		weather.temperature = new BigDecimal(-20.52);
		weather.inputTime = getCurrentDate();
		weathers.add(weather);

		Mockito.when(weatherRepo.findByCoords(weather.coordinates)).thenReturn(weathers);

		mockMvc.perform(post("/add_weather").
				contentType(MediaType.APPLICATION_JSON_UTF8).
				content(TestUtils.convertObjectToJsonBytes(weather))).
				andExpect(status().isCreated());
	}

	@Test
	public void wrongWeatherShouldReturnHttpBadRequest() throws Exception
	{
		weather.city = "krasnoyarsk";
		weather.coordinates = "564.016143,92312.8571573";
		weather.temperature = new BigDecimal(-120.52);
		weather.inputTime = getCurrentDate();
		weathers.add(weather);

		Mockito.when(weatherRepo.findByCoords(weather.coordinates)).thenReturn(weathers);

		mockMvc.perform(post("/add_weather").
				contentType(MediaType.APPLICATION_JSON_UTF8).
				content(TestUtils.convertObjectToJsonBytes(weather))).
				andExpect(status().isBadRequest());
	}

	@Test
	public void getWeatherShouldReturnAllWeathers() throws Exception
	{
		weathers = TestUtils.createElevenWeathers();

		Mockito.when(weatherRepo.findAll()).thenReturn(weathers);

		mockMvc.perform(get("/get_weather")).
				andExpect(status().isOk()).
				andExpect(jsonPath("$", hasSize(11)));
	}

	@Test
	public void getWeatherOnCertainCoordinatesShouldReturnWeather() throws Exception
	{
		weather.city = "krasnoyarsk";
		weather.coordinates = "56.016143,92.8571573";
		weather.temperature = new BigDecimal(-20.52);
		weather.inputTime = TestUtils.getCurrentDate();
		weathers.add(weather);

		Mockito.when(weatherRepo.findByCoords("56.016143,92.8571573")).thenReturn(weathers);

		mockMvc.perform(get("/get_weather").param("coords","56.016143,92.8571573")).
				andExpect(status().isOk()).
				andExpect(jsonPath("$[0].coordinates", is("56.016143,92.8571573")));
	}
}
