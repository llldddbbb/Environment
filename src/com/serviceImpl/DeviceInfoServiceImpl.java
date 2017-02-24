package com.serviceImpl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.DeviceInfoDao;
import com.service.DeviceInfoService;

@Service("deviceInfoService")
public class DeviceInfoServiceImpl implements DeviceInfoService{

	@Resource
	private DeviceInfoDao deviceInfoDao;
	
	@Override
	public List<String> checkTableExist(Map<String,Object> map) {
		return deviceInfoDao.checkTableExist(map);
	}

}
