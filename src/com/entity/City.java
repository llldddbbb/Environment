package com.entity;

import java.util.List;

public class City {
	
	private int cityID;
	private String cityName;
	private String lat;
	private String lng;
	private String picture;
	private DeviceInfo deviceInfo;
	private String showAQI;
	
	private List<Device> deviceList;
	
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public List<Device> getDeviceList() {
		return deviceList;
	}
	public void setDeviceList(List<Device> deviceList) {
		this.deviceList = deviceList;
	}
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	
	public String getShowAQI() {
		return showAQI;
	}
	public void setShowAQI(String showAQI) {
		this.showAQI = showAQI;
	}
	public City(int cityID, String cityName, String lat, String lng, DeviceInfo deviceInfo) {
		super();
		this.cityID = cityID;
		this.cityName = cityName;
		this.lat = lat;
		this.lng = lng;
		this.deviceInfo = deviceInfo;
	}
	public City() {
		super();
	}
	
	
	
	
}
