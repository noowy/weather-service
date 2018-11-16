package com.weather.service;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Geodecoder
{

	private enum Border
	{
		LEFT,
		RIGHT,
		TOP,
		BOTTOM
	}

	private enum Direction
	{
		TO_LEFT,
		TO_RIGHT,
		UPWARD,
		DOWNWARD,
		HERE
	};

	private ArrayList<City> citiesList;

	public Geodecoder()
	{
		citiesList = new ArrayList<>();
		loadCitiesList();
	}

	public String retrieveCity(String coordinates)
	{
		double latitude = getDoubleLat(coordinates);
		double longitude = getDoubleLong(coordinates);

		City foundCity = binarySearchForCity(latitude, longitude);

		return foundCity == null ? null : foundCity.name;
	}

	private double getKmAtLat(double latitude)
	{
		return 111132.92 -
				559.82 * Math.cos(2 * Math.toRadians(latitude)) +
				1.175 * Math.cos(4 * Math.toRadians(latitude))
				- 0.0023 * Math.cos(6 * Math.toRadians(latitude));
	}

	private double getKmAtLong(double latitude)
	{
		return 111412.84 * Math.cos(Math.toRadians(latitude)) -
				93.5 * Math.cos(3 * Math.toRadians(latitude)) +
				0.118 * Math.cos(5 * Math.toRadians(latitude));
	}

	private double getDoubleLat(String coordinates)
	{
		return Double.parseDouble(coordinates.split("\\,")[0]);
	}

	private double getDoubleLong(String coordinates)
	{
		return Double.parseDouble(coordinates.split("\\,")[1]);
	}

	// launches first to find a range of latitudes and then calls linear
	// to find longitude for given latitude
	private City binarySearchForCity(double latitude, double longitude)
	{
		int start = 0;
		int end = citiesList.size() - 1;
		int middle = (end - start) / 2;
		Direction direction;
		double[] roundingBox;

		while (start <= end)
		{
			roundingBox = getRoundingBox(citiesList.get(middle).latitude,
					citiesList.get(middle).longitude, 1500);
			direction = isInsideBox(roundingBox, latitude, longitude);

			if (direction == Direction.TO_RIGHT)
				start = middle + 1;
			else if (direction == Direction.TO_LEFT)
				end = middle - 1;
			else if (direction == Direction.HERE)
				return citiesList.get(middle);
			else if (direction == Direction.UPWARD || direction == Direction.DOWNWARD)
				return linearSearchForCity(middle, latitude, longitude);

			middle = (start + end) / 2;
		}

		return null;
	}

	private City linearSearchForCity(int cityPosInList, double latitude, double longitude)
	{
		int pos = cityPosInList;
		double[] cityRoundingBox = getRoundingBox(citiesList.get(pos).latitude,
				citiesList.get(pos).longitude, 1500);;
		double[] coordsRoundingBox = getRoundingBox(latitude, longitude, 1500);

		// first it goes to the right of the array on given latitude
		while (doBoxesCrossHorizontal(coordsRoundingBox, cityRoundingBox))
		{
			pos++;
			cityRoundingBox = getRoundingBox(citiesList.get(pos).latitude,
					citiesList.get(pos).longitude, 1500);
			if (isInsideBox(cityRoundingBox, latitude, longitude) == Direction.HERE)
				return citiesList.get(pos);
		}

		pos = cityPosInList;
		cityRoundingBox = getRoundingBox(citiesList.get(pos).latitude,
				citiesList.get(pos).longitude, 1500);;

		// then to the left of the array
		while (doBoxesCrossHorizontal(coordsRoundingBox, cityRoundingBox))
		{
			pos--;
			cityRoundingBox = getRoundingBox(citiesList.get(pos).latitude,
					citiesList.get(pos).longitude, 1500);
			if (isInsideBox(cityRoundingBox, latitude, longitude) == Direction.HERE)
				return citiesList.get(pos);
		}

		return null;
	}

	// returns direction in which search must be continued
	private Direction isInsideBox(double[] roundingBox, double latitude, double longitude)
	{
		if (roundingBox[1] < latitude)
			return Direction.TO_RIGHT;
		else if (roundingBox[0] > latitude)
			return Direction.TO_LEFT;
		else if (roundingBox[3] > longitude)
			return Direction.DOWNWARD;
		else if (roundingBox[2] < longitude)
			return Direction.UPWARD;
		return Direction.HERE;
	}

	private double[] getRoundingBox(double latitude, double longitude, double radiusInMeters)
	{
		double[] roundingBox = new double[4];

		double deltaLat = radiusInMeters / 1000 / getKmAtLat(latitude);
		double deltaLong = radiusInMeters / 1000 / getKmAtLong(latitude);

		// setting corners of rounding box
		roundingBox[0] = latitude - deltaLat;
		roundingBox[1] = latitude + deltaLat;
		roundingBox[2] = longitude + deltaLong;
		roundingBox[3] = longitude - deltaLong;

		return roundingBox;
	}

	private boolean doBoxesCrossHorizontal(double[] box, double[] otherBox)
	{
		return (box[0] <= otherBox[1] && box[1] >= otherBox[1])
				||
				(box[1] >= otherBox[0] && box[0] <= otherBox[0]);
	}

	private void loadCitiesList()
	{
		try(Scanner cities = new Scanner(ResourceUtils.getFile("classpath:cities.csv")))
		{
			while (cities.hasNextLine())
			{
				String buf = cities.nextLine();
				City city = new City();

				city.name = buf.split("\\,")[0];
				city.latitude = Double.parseDouble(buf.split("\\,")[1]);
				city.longitude = Double.parseDouble(buf.split("\\,")[2]);

				citiesList.add(city);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private class City
	{
		public String name;
		public Double latitude;
		public Double longitude;

		public boolean isInferiorThan(City other) // by superior means stands higher or farther on the cartesian plane
		{
			return this.latitude < other.latitude ||
					(this.latitude.equals(other.latitude) && this.longitude < other.longitude);
		}
	}
}

