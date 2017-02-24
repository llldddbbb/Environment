package com.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.DeviceDao;
import com.entity.Device;
import com.service.DeviceService;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService{
	
	@Resource
	private DeviceDao deviceDao;
	
	@Override
	public List<Device> findDeviceList(Map<String, Object> map) {
		return deviceDao.findDeviceList(map);
	}

	@Override
	public List<Device> getChartsData(Map<String, Object> map) {
		return deviceDao.getChartsData(map);
	}

	@Override
	public int getDeviceCount(Map<String, Object> map) {
		return deviceDao.getDeviceCount(map);
	}

	@Override
	public String getDeviceNewestDate(Map<String, Object> map) {
		return deviceDao.getDeviceNewestDate(map);
	}

	@Override
	public Device getDeviceByTimeStamp(Map<String, Object> map) {
		return deviceDao.getDeviceByTimeStamp(map);
	}

}
