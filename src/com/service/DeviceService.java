package com.service;

import java.util.List;
import java.util.Map;

import com.entity.Device;

public interface DeviceService {
	public List<Device> findDeviceList(Map<String,Object> map);
	
	public List<Device> getChartsData(Map<String,Object> map);
	
	public int getDeviceCount(Map<String,Object> map);
	
	public String getDeviceNewestDate(Map<String,Object> map);
	
	public Device getDeviceByTimeStamp(Map<String,Object> map);
}
