package com.dao;

import java.util.List;
import java.util.Map;

import com.entity.DeviceInfo;

public interface DeviceInfoDao {
	
	public DeviceInfo getDeviceInfoById(Integer id);
	
	public List<String> checkTableExist(Map<String,Object> map);
	
}
