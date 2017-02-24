package com.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.CityDao;
import com.entity.City;
import com.service.CityService;

@Service("cityService")
public class CityServiceImpl implements CityService{
	
	@Resource
	private CityDao cityDao;
	
	@Override
	public List<City> findCityList(Map<String, Object> map) {
		return cityDao.findCityList(map);
	}

	@Override
	public City getCityByCityName(Map<String, Object> map) {
		return cityDao.getCityByCityName(map);
	}

	@Override
	public int updateCity(City city) {
		return cityDao.updateCity(city);
	}

}
