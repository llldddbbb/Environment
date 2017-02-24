package com.service;

import java.util.List;
import java.util.Map;

import com.entity.City;

public interface CityService {
	
	public List<City> findCityList(Map<String,Object> map);
	
	public City getCityByCityName(Map<String,Object> map);
	
	public int updateCity(City city);
}
