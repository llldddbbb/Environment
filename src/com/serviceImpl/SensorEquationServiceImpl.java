package com.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.SensorEquationDao;
import com.entity.SensorEquation;
import com.service.SensorEquationService;

@Service("sensorEquationService")
public class SensorEquationServiceImpl implements SensorEquationService{

	@Resource
	private SensorEquationDao sensorEquationDao;
	
	@Override
	public List<SensorEquation> getSensorEquationByItem(Map<String,Object> map) {
		return sensorEquationDao.getSensorEquationByItem(map);
	}

}
