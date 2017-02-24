package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.City;
import com.entity.Device;
import com.entity.PageBean;
import com.entity.SensorEquation;
import com.service.CityService;
import com.service.DeviceService;
import com.service.SensorEquationService;
import com.util.AQIUtil;
import com.util.DateUtil;
import com.util.ExcelUtil;
import com.util.HashMapUtil;
import com.util.PageUtil;
import com.util.ResponseUtil;
import com.util.StringUtil;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/device")
public class DeviceController {
	
	@Resource
	private DeviceService deviceService;
	
	@Resource
	private CityService cityService;
	
	@Resource
	private SensorEquationService sensorEquationService;
	
	
	@RequestMapping("/detail")
	public String detail(@RequestParam(value="deviceName")String deviceName,@RequestParam(value="cityName")String cityName,HttpServletRequest request)throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		if(deviceName.equals("dev9100d161200007")||deviceName.equals("dev9100d161200008")){
			map.put("deviceName",deviceName+"_15min");
		}else if(deviceName.equals("dev9100d170100024")||deviceName.equals("dev9100d170100027")){
			map.put("deviceName",deviceName+"_15min");
		}else if(deviceName.equals("dev9002a170100001")||deviceName.equals("dev9002a170100002")||deviceName.equals("dev9002a170100003")||deviceName.equals("dev9002a170100004")||deviceName.equals("dev9002a170100005")||deviceName.equals("dev9002a170100006")){
			map.put("deviceName",deviceName+"_original");
		}
		else{
			map.put("deviceName",deviceName+"_1min");
		}
		map.put("start", 0);
		map.put("pageSize", 1);
		Device device=deviceService.findDeviceList(map).get(0);
		Map<String,Object> map2=new HashMap<String,Object>();
		map2.put("cityName", cityName);
		City city=cityService.getCityByCityName(map2);
		Map<String,Object> map3=new HashMap<String,Object>();
		if(deviceName.equals("dev9002a170100001")||deviceName.equals("dev9002a170100002")||deviceName.equals("dev9002a170100003")||deviceName.equals("dev9002a170100004")||deviceName.equals("dev9002a170100005")||deviceName.equals("dev9002a170100006")){
			map3.put("deviceName",city.getDeviceInfo().getDeviceName()+"_original");
		}else{
			map3.put("deviceName",city.getDeviceInfo().getDeviceName()+"_1h");
		}
		map3.put("start", 0);
		map3.put("pageSize", 1);
		Device device_1h=new Device();
		if(deviceService.findDeviceList(map3).size()>0){
			device_1h=deviceService.findDeviceList(map3).get(0);
		}
		device.setTimeStamp(DateUtil.formatDate(device.getTimeStamp(), "yyyy-MM-dd HH:mm:ss"));
		HttpSession session=request.getSession();
		Map<String,Double> currentIAQITypeMap=new HashMap<>();
		if(device_1h.getPM25_ug()!=null){
			currentIAQITypeMap.put("PM25", Double.valueOf(device_1h.getPM25_ug()));
		}
		if(device_1h.getPM10_ug()!=null){
			currentIAQITypeMap.put("PM10", Double.valueOf(device_1h.getPM10_ug()));
		}
		if(device_1h.getCO_mg()!=null){
			currentIAQITypeMap.put("CO", Double.valueOf(device_1h.getCO_mg()));
		}
		if(device_1h.getNO2_ug()!=null){
			currentIAQITypeMap.put("NO2", Double.valueOf(device_1h.getNO2_ug()));
		}
		if(device_1h.getSO2_ug()!=null){
			currentIAQITypeMap.put("SO2", Double.valueOf(device_1h.getSO2_ug()));
		}
		if(device_1h.getO3_ug()!=null){
			currentIAQITypeMap.put("O3", Double.valueOf(device_1h.getO3_ug()));
		}
		Double AQI_value=AQIUtil.getAQI(currentIAQITypeMap);
		session.setAttribute("AQI_value", AQI_value);
		if("AQI".equals(city.getShowAQI())){
			session.setAttribute("AQIOrAQHI","AQI");
			session.setAttribute("AQIOrAQHI_value",device_1h.getAQI());
		}else{
			session.setAttribute("AQIOrAQHI","AQHI");
			session.setAttribute("AQIOrAQHI_value",device_1h.getAQHI());
		}
		Map<String,Object> map4=new HashMap<String,Object>();
		map4.put("deviceName", city.getDeviceInfo().getDeviceName()+"_original");
		map4.put("start", 0);
		map4.put("pageSize", 1);
		Device d4=deviceService.findDeviceList(map4).get(0);
		session.setAttribute("lastUpdateTime",  DateUtil.formatDateToStr(d4.getTimeStamp(), "yyyy-MM-dd HH:mm"));
		Long differ=DateUtil.TimeDifference(DateUtil.formatDateToStr(d4.getTimeStamp(),"yyyy-MM-dd HH:mm"), DateUtil.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm"));
		String language=(String)session.getAttribute("language");
		if(language==null||language.equals("Chinese")){
			if(differ>90){
				device.setStatus("离线");
			}else{
				device.setStatus("在线");
			}
		}else{
			if(differ>90){
				device.setStatus("OFFLINE");
			}else{
				device.setStatus("ONLINE");
			}
		}
		session.setAttribute("device", device);
		if(deviceName.equals("dev9100d161200007")){
			session.setAttribute("deviceName_alias", "scbs007");
		}else if(deviceName.equals("dev9100d161200008")){
			session.setAttribute("deviceName_alias", "scbs008");
		}else{
			session.setAttribute("deviceName_alias", deviceName.split("_")[0]);
		}
		session.setAttribute("deviceName", deviceName.split("_")[0]);
		session.setAttribute("cityName", cityName);
		session.setAttribute("e_TimeStamp", DateUtil.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm"));
		if(language==null||language.equals("Chinese")){
			return "redirect:/detail.jsp";
		}else{
			return "redirect:/en/detail.jsp";
		}
	}
	
	@RequestMapping("/getChartsData")
	public String getChartsData(@RequestParam(value="deviceName",required=false)String deviceName,@RequestParam(value="airQulityType",required=false)String airQulityType,@RequestParam(value="chartstype",required=false)String chartstype,@RequestParam(value="interval",required=false)String interval,@RequestParam(value="s_TimeStamp",required=false)String s_TimeStamp,@RequestParam(value="e_TimeStamp",required=false)String e_TimeStamp,HttpServletResponse response,HttpServletRequest request)throws Exception{
		JSONObject result=new JSONObject();
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtil.isEmpty(interval)){
			interval="1h";
		}
		if(deviceName.split("_")[0].equals("dev9002a170100001")||deviceName.split("_")[0].equals("dev9002a170100002")||deviceName.split("_")[0].equals("dev9002a170100003")||deviceName.split("_")[0].equals("dev9002a170100004")||deviceName.split("_")[0].equals("dev9002a170100005")||deviceName.split("_")[0].equals("dev9002a170100006")){
			map.put("deviceName",deviceName.split("_")[0]+"_original");
		}else{
			map.put("deviceName",deviceName.split("_")[0]+"_"+interval);
		}
		if(StringUtil.isEmpty(s_TimeStamp)){
			s_TimeStamp="2016-8-15 00:00";
		}
		if(StringUtil.isEmpty(e_TimeStamp)){
			e_TimeStamp=DateUtil.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm");
			
		}
		map.put("s_TimeStamp", "'"+s_TimeStamp+"'");
		map.put("e_TimeStamp", "'"+e_TimeStamp+"'");
		if(StringUtil.isEmpty(airQulityType)||airQulityType.equals("PM25_ug")){
			map.put("airQulityType","PM2.5_ug");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getPM25_ug());
			}
			this.setTableNull2(deviceList);
		}else if(airQulityType.equals("PM10_ug")){
			map.put("airQulityType","PM10_ug");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getPM10_ug());
			}
		}else if(airQulityType.equals("SO2_ug")){
			map.put("airQulityType","SO2_ug");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getSO2_ug());
			}
		}else if(airQulityType.equals("CO_mg")){
			map.put("airQulityType","CO_mg");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getCO_mg());
			}
		}else if(airQulityType.equals("NO_ug")){
			map.put("airQulityType","NO_ug");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getNO_ug());
			}
		}else if(airQulityType.equals("NO2_ug")){
			map.put("airQulityType","NO2_ug");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getNO2_ug());
			}
		}else if(airQulityType.equals("O3_ug")){
			map.put("airQulityType","O3_ug");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getO3_ug());
			}
		}else if(airQulityType.equals("Temp")){
			map.put("airQulityType","Temp");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getTemp());
			}
		}else if(airQulityType.equals("Humi")){
			map.put("airQulityType","Humi");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getHumi());
			}
		}else if(airQulityType.equals("CO2_mg")){
			map.put("airQulityType","CO2_mg");
			List<Device> deviceList=deviceService.getChartsData(map);
			for(Device d:deviceList){
				result.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"),d.getCO2_mg());
			}
		}
		ResponseUtil.write(result, response);
		return null;
	}
	
	@RequestMapping("/findDeviceList")
	public String findDeviceList(String deviceName,String interval,@RequestParam(value="unit",required=false)String unit,@RequestParam(value="cityName",required=false)String cityName,
			@RequestParam(value="page",required=false)String page,@RequestParam(value="cityID",required=false)String cityID,
			@RequestParam(value="airQulityType",required=false)String airQulityType,@RequestParam(value="s_TimeStamp",required=false)String s_TimeStamp,
			@RequestParam(value="e_TimeStamp",required=false)String e_TimeStamp,@RequestParam(value="lineNum",required=false)String lineNum,
			@RequestParam(value="PM25_alerm",required=false)String PM25_alerm,
			@RequestParam(value="PM10_alerm",required=false)String PM10_alerm,
			@RequestParam(value="NO_ug_alerm",required=false)String NO_ug_alerm,@RequestParam(value="NO_ppb_alerm",required=false)String NO_ppb_alerm,
			@RequestParam(value="NO2_ug_alerm",required=false)String NO2_ug_alerm,@RequestParam(value="NO2_ppb_alerm",required=false)String NO2_ppb_alerm,
			@RequestParam(value="CO_mg_alerm",required=false)String CO_mg_alerm,@RequestParam(value="CO_ppm_alerm",required=false)String CO_ppm_alerm,
			@RequestParam(value="O3_ug_alerm",required=false)String O3_ug_alerm,@RequestParam(value="O3_ppb_alerm",required=false)String O3_ppb_alerm,
			@RequestParam(value="SO2_ug_alerm",required=false)String SO2_ug_alerm,@RequestParam(value="SO2_ppb_alerm",required=false)String SO2_ppb_alerm,
			@RequestParam(value="Temp_alerm",required=false)String Temp_alerm,@RequestParam(value="Humi_alerm",required=false)String Humi_alerm,
			HttpServletRequest request)throws Exception{
		HttpSession session=request.getSession();
		if(StringUtil.isEmpty(page)){
			page="1";
		}
		Map<String ,Object> map=new HashMap<String,Object>();
		if(StringUtil.isEmpty(interval)){
			Object o = session.getAttribute("interval");
			if (o != null) {
				interval = (String) o;
			}else{
				interval="1h";
			}
		}else{
			session.setAttribute("interval", interval);
		}
		
		if(StringUtil.isEmpty(lineNum)){
			Object o = session.getAttribute("lineNum");
			if (o != null) {
				lineNum = (String) o;
			}else{
				lineNum="30";
			}
		}else{
			session.setAttribute("lineNum", lineNum);
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(lineNum));
		
		if(StringUtil.isEmpty(cityName)){
			Object o = session.getAttribute("cityName");
			if (o != null) {
				cityName = (String) o;
			}
		}else{
			session.setAttribute("cityName", cityName);
		}
		
		if(StringUtil.isEmpty(unit)){
			Object o = session.getAttribute("unit");
			if (o != null) {
				unit = (String) o;
			}else{
				unit="umgm";
				session.setAttribute("unit", unit);
			}
				
		}else{
			session.setAttribute("unit", unit);
		}
		
		
		
		if(StringUtil.isEmpty(airQulityType)){
			Object o = session.getAttribute("airQulityType");
			if (o != null) {
				airQulityType = (String) o;
			}else{
				airQulityType="TimeStamp,`PM2.5_ug`,PM10_ug,NO2_ug,CO_mg,O3_ug,SO2_ug,Temp,Humi";
				session.setAttribute("airQulityType", airQulityType);
			}
		}else{
			session.setAttribute("airQulityType", airQulityType);
		}
		
		if(StringUtil.isEmpty(s_TimeStamp)){
				Object o = session.getAttribute("s_TimeStamp");
				if (o != null) {
					s_TimeStamp = (String) o;
					map.put("s_TimeStamp", "'"+s_TimeStamp+"'");
				}else{
					s_TimeStamp="";
				}
			
		}else{
			session.setAttribute("s_TimeStamp", s_TimeStamp);
			map.put("s_TimeStamp", "'"+s_TimeStamp+"'");
		}
		
		if(StringUtil.isEmpty(e_TimeStamp)){
			Object o = session.getAttribute("e_TimeStamp");
			
				if (o != null) {
					e_TimeStamp = (String) o;
					map.put("e_TimeStamp", "'"+e_TimeStamp+"'");
				}else{
					e_TimeStamp=DateUtil.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm");
					session.setAttribute("e_TimeStamp", e_TimeStamp);
				}
			
			
		}else{
			session.setAttribute("e_TimeStamp", e_TimeStamp);
			map.put("e_TimeStamp", "'"+e_TimeStamp+"'");
		}
		
		
		if(StringUtil.isEmpty(PM25_alerm)){
			Object o = session.getAttribute("PM25_alerm");
			if (o != null) {
				PM25_alerm = (String) o;
				map.put("PM25_alerm", PM25_alerm);
			}
		}else{
			if("clean".equals(PM25_alerm)){
				session.removeAttribute("PM25_alerm");
			}else{
				session.setAttribute("PM25_alerm", PM25_alerm);
				map.put("PM25_alerm", PM25_alerm);
			}
		}
		
		if(StringUtil.isEmpty(PM10_alerm)){
			Object o = session.getAttribute("PM10_alerm");
			if (o != null) {
				PM10_alerm = (String) o;
				map.put("PM10_alerm", PM10_alerm);
			}
		}else{
			if("clean".equals(PM10_alerm)){
				session.removeAttribute("PM10_alerm");
			}else{
				session.setAttribute("PM10_alerm", PM10_alerm);
				map.put("PM10_alerm", PM10_alerm);
			}
		}
		
		if(unit.equals("ppbm")){
			if(StringUtil.isEmpty(PM10_alerm)){
				Object o = session.getAttribute("PM10_alerm");
				if (o != null) {
					PM10_alerm = (String) o;
					map.put("PM10_alerm", PM10_alerm);
				}
			}else{
				if("clean".equals(PM10_alerm)){
					session.removeAttribute("PM10_alerm");
				}else{
					session.setAttribute("PM10_alerm", PM10_alerm);
					map.put("PM10_alerm", PM10_alerm);
				}
			}
		}else{
			if(StringUtil.isEmpty(PM10_alerm)){
				Object o = session.getAttribute("PM10_alerm");
				if (o != null) {
					PM10_alerm = (String) o;
					map.put("PM10_alerm", PM10_alerm);
				}
			}else{
				if("clean".equals(PM10_alerm)){
					session.removeAttribute("PM10_alerm");
				}else{
					session.setAttribute("PM10_alerm", PM10_alerm);
					map.put("PM10_alerm", PM10_alerm);
				}
			}
		}
		
		
		if(unit.equals("ppbm")){
			if(StringUtil.isEmpty(NO_ppb_alerm)){
				Object o = session.getAttribute("NO_alerm");
				if (o != null) {
					NO_ppb_alerm = (String) o;
					map.put("NO_ppb_alerm", NO_ppb_alerm);
				}
			}else{
				if("clean".equals(NO_ppb_alerm)){
					session.removeAttribute("NO_alerm");
				}else{
					session.setAttribute("NO_alerm", NO_ppb_alerm);
					map.put("NO_ppb_alerm", NO_ppb_alerm);
				}
			}
		}else{
			if(StringUtil.isEmpty(NO_ug_alerm)){
				Object o = session.getAttribute("NO_alerm");
				if (o != null) {
					NO_ug_alerm = (String) o;
					map.put("NO_ug_alerm", NO_ug_alerm);
				}
			}else{
				if("clean".equals(NO_ug_alerm)){
					session.removeAttribute("NO_alerm");
				}else{
					session.setAttribute("NO_alerm", NO_ug_alerm);
					map.put("NO_ug_alerm", NO_ug_alerm);
				}
			}
		}
		
		if(unit.equals("ppbm")){
			if(StringUtil.isEmpty(NO2_ppb_alerm)){
				Object o = session.getAttribute("NO2_alerm");
				if (o != null) {
					NO2_ppb_alerm = (String) o;
					map.put("NO2_ppb_alerm", NO2_ppb_alerm);
				}
			}else{
				if("clean".equals(NO2_ppb_alerm)){
					session.removeAttribute("NO2_alerm");
				}else{
					session.setAttribute("NO2_alerm", NO2_ppb_alerm);
					map.put("NO2_ppb_alerm", NO2_ppb_alerm);
				}
			}
		}else{
			if(StringUtil.isEmpty(NO2_ug_alerm)){
				Object o = session.getAttribute("NO2_alerm");
				if (o != null) {
					NO2_ug_alerm = (String) o;
					map.put("NO2_ug_alerm", NO2_ug_alerm);
				}
			}else{
				if("clean".equals(NO2_ug_alerm)){
					session.removeAttribute("NO2_alerm");
				}else{
					session.setAttribute("NO2_alerm", NO2_ug_alerm);
					map.put("NO2_ug_alerm", NO2_ug_alerm);
				}
			}
		}
		if (unit.equals("ppbm")) {
			if(StringUtil.isEmpty(CO_ppm_alerm)){
				Object o = session.getAttribute("CO_alerm");
				if (o != null) {
					CO_ppm_alerm = (String) o;
					map.put("CO_ppm_alerm", CO_ppm_alerm);
				}
			}else{
				if("clean".equals(CO_ppm_alerm)){
					session.removeAttribute("CO_alerm");
				}else{
					session.setAttribute("CO_alerm", CO_ppm_alerm);
					map.put("CO_ppm_alerm", CO_ppm_alerm);
				}
			}
		} else {
			if(StringUtil.isEmpty(CO_mg_alerm)){
				Object o = session.getAttribute("CO_alerm");
				if (o != null) {
					CO_mg_alerm = (String) o;
					map.put("CO_mg_alerm", CO_mg_alerm);
				}
			}else{
				if("clean".equals(CO_mg_alerm)){
					session.removeAttribute("CO_alerm");
				}else{
					session.setAttribute("CO_alerm", CO_mg_alerm);
					map.put("CO_mg_alerm", CO_mg_alerm);
				}
			}
		}
		if (unit.equals("ppbm")) {
			if(StringUtil.isEmpty(O3_ppb_alerm)){
				Object o = session.getAttribute("O3_alerm");
				if (o != null) {
					O3_ppb_alerm = (String) o;
					map.put("O3_ppb_alerm", O3_ppb_alerm);
				}
			}else{
				if("clean".equals(O3_ppb_alerm)){
					session.removeAttribute("O3_alerm");
				}else{
					session.setAttribute("O3_alerm", O3_ppb_alerm);
					map.put("O3_ppb_alerm", O3_ppb_alerm);
				}
			}
		} else {
			if(StringUtil.isEmpty(O3_ug_alerm)){
				Object o = session.getAttribute("O3_alerm");
				if (o != null) {
					O3_ug_alerm = (String) o;
					map.put("O3_ug_alerm", O3_ug_alerm);
				}
			}else{
				if("clean".equals(O3_ug_alerm)){
					session.removeAttribute("O3_alerm");
				}else{
					session.setAttribute("O3_alerm", O3_ug_alerm);
					map.put("O3_ug_alerm", O3_ug_alerm);
				}
			}
		}
		if (unit.equals("ppbm")) {
			if(StringUtil.isEmpty(SO2_ppb_alerm)){
				Object o = session.getAttribute("SO2_alerm");
				if (o != null) {
					SO2_ppb_alerm = (String) o;
					map.put("SO2_ppb_alerm", SO2_ppb_alerm);
				}
			}else{
				if("clean".equals(SO2_ppb_alerm)){
					session.removeAttribute("SO2_alerm");
				}else{
					session.setAttribute("SO2_alerm", SO2_ppb_alerm);
					map.put("SO2_ppb_alerm", SO2_ppb_alerm);
				}
			}
		} else {
			if(StringUtil.isEmpty(SO2_ug_alerm)){
				Object o = session.getAttribute("SO2_alerm");
				if (o != null) {
					SO2_ug_alerm = (String) o;
					map.put("SO2_ug_alerm", SO2_ug_alerm);
				}
			}else{
				if("clean".equals(SO2_ug_alerm)){
					session.removeAttribute("SO2_alerm");
				}else{
					session.setAttribute("SO2_alerm", SO2_ug_alerm);
					map.put("SO2_ug_alerm", SO2_ug_alerm);
				}
			}
		}
		
		
		if(StringUtil.isEmpty(Temp_alerm)){
			Object o = session.getAttribute("Temp_alerm");
			if (o != null) {
				Temp_alerm = (String) o;
				map.put("Temp_alerm", Temp_alerm);
			}
		}else{
			if("clean".equals(Temp_alerm)){
			}else{
				session.setAttribute("Temp_alerm", Temp_alerm);
				map.put("Temp_alerm", Temp_alerm);
			}
		}
		
		if(StringUtil.isEmpty(Humi_alerm)){
			Object o = session.getAttribute("Humi_alerm");
			if (o != null) {
				Humi_alerm = (String) o;
				map.put("Humi_alerm", Humi_alerm);
			}
		}else{
			if("clean".equals(Humi_alerm)){
				session.removeAttribute("Humi_alerm");
			}else{
				session.setAttribute("Humi_alerm", Humi_alerm);
				map.put("Humi_alerm", Humi_alerm);
			}
		}
		
		map.put("deviceName", deviceName+"_"+interval);
		map.put("start", pageBean.getStart());
		map.put("pageSize", pageBean.getPageSize());
		map.put("airQulityType", airQulityType);
		List<Device> deviceList=deviceService.findDeviceList(map);
		
		
		
		List<String> ariQulityTypeList=Arrays.asList(airQulityType.split(","));
		
		/*Map<String,Object> map4=new HashMap<String,Object>();
		map4.put("deviceName", deviceName+"_1h");
		map4.put("start", 0);
		map4.put("pageSize", 1);
		List<Device> deviceFllNullList=deviceService.findDeviceList(map4);*/
		this.setTableNull(deviceList,ariQulityTypeList,deviceName+"_1h",interval);
		int total=deviceService.getDeviceCount(map);
		
		Map<String,Object> cityMap=new HashMap<String,Object>();
		session.setAttribute("deviceList", deviceList);
		session.setAttribute("deviceName", deviceName);
		if(StringUtil.isNotEmpty(cityID)){
			cityMap.put("cityID", cityID);
			List<City> cityList=cityService.findCityList(cityMap);
			session.setAttribute("cityList", cityList);
		}
		String language=(String)session.getAttribute("language");
		if(language==null||language.equals("Chinese")){
			String pageCode=PageUtil.genPaginationNoParam("/Environment/device/findDeviceList.do?deviceName="+deviceName, total, Integer.parseInt(page), pageBean.getPageSize());
			session.setAttribute("pageCode", pageCode);
		}else{
			String pageCode=PageUtil.genPaginationNoParam_en("/Environment/device/findDeviceList.do?deviceName="+deviceName, total, Integer.parseInt(page), pageBean.getPageSize());
			if(airQulityType.contains("TimeStamp")){
				airQulityType=airQulityType.replaceFirst("TimeStamp", "TimeStamp_en");
			}
			if(airQulityType.contains("Temp")){
				airQulityType=airQulityType.replaceFirst("Temp", "Temp_en");
			}
			if(airQulityType.contains("Humi")){
				airQulityType=airQulityType.replaceFirst("Humi", "Humi_en");
			}
			session.setAttribute("pageCode", pageCode);
		}
		
		session.setAttribute("airType", HashMapUtil.getValueByKey(airQulityType.split(",")));
		if(language==null||language.equals("Chinese")){
			return "redirect:/table.jsp";
		}else{
			return "redirect:/en/table.jsp";
		}
	}
	
	
	@RequestMapping("exportExcel")
	public String exportExcel(String deviceName,String cityName,@RequestParam(value="interval",required=false)String interval,String airQulityType,@RequestParam(value="s_TimeStamp",required=false)String s_TimeStamp,@RequestParam(value="e_TimeStamp",required=false)String e_TimeStamp,
			@RequestParam(value="PM25_alerm",required=false)String PM25_alerm,
			@RequestParam(value="PM10_alerm",required=false)String PM10_alerm,
			@RequestParam(value="NO_ug_alerm",required=false)String NO_ug_alerm,@RequestParam(value="NO_ppb_alerm",required=false)String NO_ppb_alerm,
			@RequestParam(value="NO2_ug_alerm",required=false)String NO2_ug_alerm,@RequestParam(value="NO2_ppb_alerm",required=false)String NO2_ppb_alerm,
			@RequestParam(value="CO_mg_alerm",required=false)String CO_mg_alerm,@RequestParam(value="CO_ppm_alerm",required=false)String CO_ppm_alerm,
			@RequestParam(value="O3_ug_alerm",required=false)String O3_ug_alerm,@RequestParam(value="O3_ppb_alerm",required=false)String O3_ppb_alerm,
			@RequestParam(value="SO2_ug_alerm",required=false)String SO2_ug_alerm,@RequestParam(value="SO2_ppb_alerm",required=false)String SO2_ppb_alerm,
			@RequestParam(value="Temp_alerm",required=false)String Temp_alerm,@RequestParam(value="Humi_alerm",required=false)String Humi_alerm,
			HttpServletResponse response)throws Exception{
		Map<String ,Object> map=new HashMap<String,Object>();
		if(StringUtil.isEmpty(interval)){
			interval="1h";
		}
		map.put("airQulityType", airQulityType);
		map.put("deviceName", deviceName+"_"+interval);
		if(StringUtil.isNotEmpty(s_TimeStamp)){
			map.put("s_TimeStamp", "'"+s_TimeStamp+"'");
		}
		if(StringUtil.isNotEmpty(e_TimeStamp)){
			map.put("e_TimeStamp", "'"+e_TimeStamp+"'");
		}
		map.put("cityName", cityName);
		City city=cityService.getCityByCityName(map);
		
		if(StringUtil.isNotEmpty(PM25_alerm)&&!"clean".equals(PM25_alerm)){
			map.put("PM25_alerm", PM25_alerm);
		}
		
		if(StringUtil.isNotEmpty(PM10_alerm)&&!"clean".equals(PM10_alerm)){
			map.put("PM10_alerm", PM10_alerm);
		}
		
		if(StringUtil.isNotEmpty(NO_ug_alerm)&&!"clean".equals(NO_ug_alerm)){
			map.put("NO_ug_alerm", NO_ug_alerm);
		}
		
		if(StringUtil.isNotEmpty(NO_ppb_alerm)&&!"clean".equals(NO_ppb_alerm)){
			map.put("NO_ppb_alerm", NO_ppb_alerm);
		}
		
		if(StringUtil.isNotEmpty(NO2_ug_alerm)&&!"clean".equals(NO2_ug_alerm)){
			map.put("NO2_ug_alerm", NO2_ug_alerm);
		}
		
		if(StringUtil.isNotEmpty(NO2_ppb_alerm)&&!"clean".equals(NO2_ppb_alerm)){
			map.put("NO2_ppb_alerm", NO2_ppb_alerm);
		}
		
		if(StringUtil.isNotEmpty(CO_mg_alerm)&&!"clean".equals(CO_mg_alerm)){
			map.put("CO_mg_alerm", CO_mg_alerm);
		}
		
		if(StringUtil.isNotEmpty(CO_ppm_alerm)&&!"clean".equals(CO_ppm_alerm)){
			map.put("CO_ppm_alerm", CO_ppm_alerm);
		}
		
		
		if(StringUtil.isNotEmpty(O3_ug_alerm)&&!"clean".equals(O3_ug_alerm)){
			map.put("O3_ug_alerm", O3_ug_alerm);
		}
		
		if(StringUtil.isNotEmpty(O3_ppb_alerm)&&!"clean".equals(O3_ppb_alerm)){
			map.put("O3_ppb_alerm", O3_ppb_alerm);
		}
		
		if(StringUtil.isNotEmpty(SO2_ug_alerm)&&!"clean".equals(SO2_ug_alerm)){
			map.put("SO2_ug_alerm", SO2_ug_alerm);
		}
		
		if(StringUtil.isNotEmpty(SO2_ppb_alerm)&&!"clean".equals(SO2_ppb_alerm)){
			map.put("SO2_ppb_alerm", SO2_ppb_alerm);
		}
		
		if(StringUtil.isNotEmpty(Temp_alerm)&&!"clean".equals(Temp_alerm)){
			map.put("Temp_alerm", Temp_alerm);
		}
		
		if(StringUtil.isNotEmpty(Humi_alerm)&&!"clean".equals(Humi_alerm)){
			map.put("Humi_alerm", Humi_alerm);
		}
		List<Device> deviceList=deviceService.findDeviceList(map);
		
		Workbook wb=new HSSFWorkbook();
		String deviceID= city.getDeviceInfo().getItem();
		map.put("Item", deviceID);
		List<SensorEquation> sensorEquationList=sensorEquationService.getSensorEquationByItem(map);
		List<String> sheet2Header=new ArrayList<String>();
		sheet2Header.add("表格下载时间");
		sheet2Header.add("开始时间");
		sheet2Header.add("结束时间");
		sheet2Header.add("deviceName");
		List<String> sheet2AddData=new ArrayList<String>();
		sheet2AddData.add(DateUtil.formatDateToStr(new Date(), "yyyy-MM-dd HH:ss:mm"));
		sheet2AddData.add(s_TimeStamp);
		sheet2AddData.add(e_TimeStamp);
		sheet2AddData.add(deviceName);
		sheet2Header.add("StartTime");
		sheet2Header.add("EndTime");
		sheet2Header.add("a");
		sheet2Header.add("b");
		sheet2Header.add("c");
		sheet2Header.add("d");
		sheet2Header.add("e");
		sheet2Header.add("f");
		ExcelUtil.fillExcelSheet1Data(deviceList, wb,HashMapUtil.getValueByKey(airQulityType.split(",")));
		ExcelUtil.fillExcelSheet2Data(sensorEquationList, wb, sheet2Header,sheet2AddData);
		ResponseUtil.export(response, wb,deviceID+"_"+deviceName+"_"+cityName+"_"+interval+".xls");
		return null;
	}
	
	
	
	@SuppressWarnings("unused")
	private void setTableNull(List<Device> deviceList, List<String> ariQulityTypeList,String deviceName_1h,String interval) {
		Map<String,Object> map=new HashMap<String,Object>();
		for(Device d:deviceList){
			Device deviceFullNull=null;
			if(StringUtil.isEmpty(d.getPM25_ug())){
				if(ariQulityTypeList.contains("`PM2.5_ug`")||ariQulityTypeList.contains("PM2.5_ug")){
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp",DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setPM25_ug(deviceFullNull.getPM25_ug());
						}
					}else{
						d.setPM25_ug(deviceFullNull.getPM25_ug());
					}
					if(d.getPM25_ug()==null){
						d.setPM25_ug("-");
					}
				}
			}
			
			if(StringUtil.isEmpty(d.getPM10_ug())){
				if(ariQulityTypeList.contains("PM10_ug")){
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setPM10_ug(deviceFullNull.getPM10_ug());
							
						}
					}else{
						d.setPM10_ug(deviceFullNull.getPM10_ug());
					}
					if(d.getPM10_ug()==null){
						d.setPM10_ug("-");
					}
				}
			}
			
			if(StringUtil.isEmpty(d.getNO_ug())){
				if(ariQulityTypeList.contains("NO_ug")){
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setNO_ug(deviceFullNull.getNO_ug());
							
						}
					}else{
						d.setNO_ug(deviceFullNull.getNO_ug());
					}
					if(d.getNO_ug()==null){
						d.setNO_ug("-");
					}
				}
			}
			
			if(StringUtil.isEmpty(d.getNO_ppb())){
				if(ariQulityTypeList.contains("NO_ppb")){
					//d.setNO_ppb(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setNO_ppb(deviceFullNull.getNO2_ppb());
							
						}
					}else{
						d.setNO_ppb(deviceFullNull.getNO2_ppb());
					}
					if(d.getNO2_ppb()==null){
						d.setNO_ppb("-");
					}
				}
			}
			
			if(StringUtil.isEmpty(d.getNO2_ug())){
				if(ariQulityTypeList.contains("NO2_ug")){
//					d.setNO2_ug(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setNO2_ug(deviceFullNull.getNO2_ug());
							
						}
					}else{
						d.setNO2_ug(deviceFullNull.getNO2_ug());
					}
					if(d.getNO2_ug()==null){
						d.setNO2_ug("-");
					}
				}
			}
			
			if(StringUtil.isEmpty(d.getNO2_ppb())){
				if(ariQulityTypeList.contains("NO2_ppb")){
//					d.setNO2_ppb(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setNO2_ppb(deviceFullNull.getNO2_ppb());
							
						}
					}else{
						d.setNO2_ppb(deviceFullNull.getNO2_ppb());
					}
					if(d.getNO2_ppb()==null){
						d.setNO2_ppb("-");
					}
				}
			}
			
			if(StringUtil.isEmpty(d.getCO_mg())){
				if(ariQulityTypeList.contains("CO_mg")){
//					d.setCO_mg(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setCO_mg(deviceFullNull.getCO_mg());
							
						}
					}else{
						d.setCO_mg(deviceFullNull.getCO_mg());
					}
					if(d.getCO_mg()==null){
						d.setCO_mg("-");
					}
				
				}
			}
			
			if(StringUtil.isEmpty(d.getCO_ppm())){
				if(ariQulityTypeList.contains("CO_ppm")){
//					d.setCO_ppm(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setCO_ppm(deviceFullNull.getCO_ppm());
							
						}
					}else{
						d.setCO_ppm(deviceFullNull.getCO_ppm());
					}
					if(d.getCO_ppm()==null){
						d.setCO_ppm("-");
					}
				}
			}
			
			if(StringUtil.isEmpty(d.getO3_ug())){
				if(ariQulityTypeList.contains("O3_ug")){
//					d.setO3_ug(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setO3_ug(deviceFullNull.getO3_ug());
							
						}
					}else{
						d.setO3_ug(deviceFullNull.getO3_ug());
					}
					if(d.getO3_ug()==null){
						d.setO3_ug("-");
					}
				}
			}
			
			if(StringUtil.isEmpty(d.getO3_ppb())){
				if(ariQulityTypeList.contains("O3_ppb")){
//					d.setO3_ppb(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setO3_ppb(deviceFullNull.getO3_ppb());
							
						}
					}else{
						d.setO3_ppb(deviceFullNull.getO3_ppb());
					}
					if(d.getO3_ppb()==null){
						d.setO3_ppb("-");
					}
				}
			}
			
			if(StringUtil.isEmpty(d.getSO2_ug())){
				if(ariQulityTypeList.contains("SO2_ug")){
//					d.setSO2_ug(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setSO2_ug(deviceFullNull.getSO2_ug());
							
						}
					}else{
						d.setSO2_ug(deviceFullNull.getSO2_ug());
					}
					if(d.getSO2_ug()==null){
						d.setSO2_ug("-");
					}
				}
			}
			
		
			if(StringUtil.isEmpty(d.getHumi())){
				if(ariQulityTypeList.contains("Humi")){
//					d.setHumi(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setHumi(deviceFullNull.getHumi());
							
						}
					}else{
						d.setHumi(deviceFullNull.getHumi());
					}
					if(d.getHumi()==null){
						d.setHumi("-");
					}
				}
			}
			if(StringUtil.isEmpty(d.getTemp())){
				if(ariQulityTypeList.contains("Temp")){
//					d.setTemp(" ");
					if(deviceFullNull==null){
						if(!interval.equals("1h")){
							map.put("TimeStamp", DateUtil.formatDateToStr2(d.getTimeStamp()));
							map.put("deviceName", deviceName_1h);
							deviceFullNull=deviceService.getDeviceByTimeStamp(map);
							d.setTemp(deviceFullNull.getTemp());
							
						}
					}else{
						d.setTemp(deviceFullNull.getTemp());
					}
					if(d.getTemp()==null){
						d.setTemp("-");
					}
				}
			}
		}
		
	}
	
	@RequestMapping("/getAllDevice")
	public String getAllDevice(String cityID,@RequestParam(value="airQulityType",required=false)String airQulityType,@RequestParam(value="interval",required=false)String interval,@RequestParam(value="s_TimeStamp",required=false)String s_TimeStamp,@RequestParam(value="e_TimeStamp",required=false)String e_TimeStamp,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("cityID", cityID);
		if(StringUtil.isEmpty(airQulityType)||airQulityType.equals("PM25_ug")){
			airQulityType="PM2.5_ug";
		}
		if(StringUtil.isEmpty(interval)){
			interval="1h";
		}
		if(StringUtil.isEmpty(s_TimeStamp)){
			s_TimeStamp="2016-8-15 00:00";
		}
		if(StringUtil.isEmpty(e_TimeStamp)){
			e_TimeStamp=DateUtil.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm");
		}
		List<City> cityList=cityService.findCityList(map);
		for(City c:cityList){
			JSONObject jsonObject=new JSONObject();
			Map<String,Object> map2=new HashMap<String,Object>();
			map2.put("s_TimeStamp", "'"+s_TimeStamp+"'");
			map2.put("e_TimeStamp", "'"+e_TimeStamp+"'");
			if("dev9002a170100001".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100002".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100003".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100003".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100004".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100005".equals(c.getDeviceInfo().getDeviceName())||"dev9002a170100006".equals(c.getDeviceInfo().getDeviceName())){
				map2.put("deviceName",c.getDeviceInfo().getDeviceName()+"_original");
			}else{
				map2.put("deviceName",c.getDeviceInfo().getDeviceName()+"_"+interval);
			}
			map2.put("airQulityType",airQulityType);
			List<Device> deviceList=deviceService.getChartsData(map2);
			//List<String> ariQulityTypeList=Arrays.asList(airQulityType.split(","));
			//this.setTableNull(deviceList);
			for(Device d:deviceList){
				if(airQulityType.equals("PM2.5_ug")){
					if(d.getPM25_ug()==null){
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), "-");
					}else{
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getPM25_ug());
					}
				}else if(airQulityType.equals("PM10_ug")){
					if(d.getPM10_ug()==null){
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), "-");
					}else{
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getPM10_ug());
					}
				}else if(airQulityType.equals("CO_mg")){
					if( d.getCO_mg()==null){
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), "-");
					}else{
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getCO_mg());
					}
				}else if(airQulityType.equals("O3_ug")){
					if(d.getO3_ug()==null){
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), "-");
					}else{
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getO3_ug());
					}
				}else if(airQulityType.equals("SO2_ug")){
					if(d.getSO2_ug()==null){
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), "-");
					}else{
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getSO2_ug());
					}
				}else if(airQulityType.equals("NO_ug")){
					if(d.getNO_ug()==null){
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), "-");
					}else{
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getNO2_ug());
					}
				}else if(airQulityType.equals("NO2_ug")){
					if(d.getNO2_ug()==null){
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), "-");
					}else{
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getNO2_ug());
					}
				}else if(airQulityType.equals("Temp")){
					if(d.getTemp()==null){
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), "-");
					}else{
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getTemp());
					}
				}else if(airQulityType.equals("Humi")){
					if(d.getHumi()==null){
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), "-");
					}else{
						jsonObject.put(DateUtil.formatDateToStr(d.getTimeStamp(), "MM-dd HH:mm"), d.getHumi());
					}
				}
			}
			result.put(c.getCityName(), jsonObject);
		}
		ResponseUtil.write(result, response);
		return null;
		
	}
	
	private void setTableNull2(List<Device> deviceList) {
		for(Device d:deviceList){
			if(StringUtil.isEmpty(d.getPM25_ug())){
				d.setPM25_ug("-");
			}
			
			if(StringUtil.isEmpty(d.getPM10_ug())){
				d.setPM10_ug("-");
			}
			
			if(StringUtil.isEmpty(d.getNO_ug())){
				d.setNO_ug("-");
			}
			
			if(StringUtil.isEmpty(d.getNO_ppb())){
				d.setNO_ppb("-");
			}
			
			if(StringUtil.isEmpty(d.getNO2_ug())){
				d.setNO2_ug("-");
			}
			
			if(StringUtil.isEmpty(d.getNO2_ppb())){
				d.setNO2_ppb("-");
			}
			
			if(StringUtil.isEmpty(d.getCO_mg())){
				d.setCO_mg("-");
			}
			
			if(StringUtil.isEmpty(d.getCO_ppm())){
				d.setCO_ppm("-");
			}
			
			if(StringUtil.isEmpty(d.getO3_ug())){
				d.setO3_ug("-");
			}
			
			if(StringUtil.isEmpty(d.getO3_ppb())){
				d.setO3_ppb("-");
			}
			
			if(StringUtil.isEmpty(d.getSO2_ug())){
				d.setSO2_ug("-");
			}
			
		
			if(StringUtil.isEmpty(d.getHumi())){
				d.setHumi("-");
			}
			if(StringUtil.isEmpty(d.getTemp())){
				d.setTemp("-");
			}
		}
		
	}
	
	
}
