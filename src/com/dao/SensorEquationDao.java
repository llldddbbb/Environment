package com.dao;

import java.util.List;
import java.util.Map;

import com.entity.SensorEquation;

public interface SensorEquationDao {
	
	public List<SensorEquation> getSensorEquationByItem(Map<String,Object> map);
}
