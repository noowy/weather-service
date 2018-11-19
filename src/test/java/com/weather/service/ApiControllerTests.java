package com.weather.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.util.ArrayList;

import static com.weather.service.TestUtils.createElevenWeathers;
import static com.weather.service.TestUtils.createWeathersInCity;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiControllerTests
{

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext wac;

	@Autowired
	ApiController apiController;

	@MockBean
	WeatherRepository weatherRepo;

	private ArrayList<Weather> weathers;

	@Before
	public void setUp() throws ParseException
	{
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.apiController).build();
	}

	@Test
	public void getWithoutFilterShouldReturnLastTenWeathers() throws Exception
	{
		weathers = createElevenWeathers();
		Mockito.when(weatherRepo.getLastTenWeathers()).thenReturn(weathers);

		mockMvc.perform(get("/api/get_weather")).
				andExpect(status().isOk()).
				andExpect(jsonPath("$", hasSize(10)));
	}

	@Test
	public void getWithFilterShouldReturnWeathersInCertainCity() throws Exception
	{
		weathers = createWeathersInCity();
		Mockito.when(weatherRepo.getLastTenWeathers(weathers.get(0).city)).thenReturn(weathers);

		mockMvc.perform(get("/api/get_weather").param("city", "krasnoyarsk")).
				andExpect(jsonPath("$[0].city", is("krasnoyarsk")));
	}
}
