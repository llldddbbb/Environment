package com.service;

import java.util.List;
import java.util.Map;

import com.entity.SensorEquation;

public interface SensorEquationService {

	
	public List<SensorEquation> getSensorEquationByItem(Map<String,Object> map);
	
	
}
