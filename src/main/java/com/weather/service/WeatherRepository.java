package com.weather.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface WeatherRepository extends CrudRepository<Weather, Integer>
{
	@Query(value = "SELECT * FROM Weather ORDER BY INPUT_TIME DESC LIMIT 10", nativeQuery = true)
	ArrayList<Weather> getLastTenWeathers();
}