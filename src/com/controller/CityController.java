package com.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.entity.City;
import com.entity.Device;
import com.service.CityService;
import com.service.DeviceService;
import com.util.AQIUtil;
import com.util.DateUtil;
import com.util.ResponseUtil;
import com.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/city")
public class CityController {
	
	@Resource
	private CityService cityService;
	
	@Resource
	private DeviceService deviceService;
	
	
	@RequestMapping("/list")
	public String list(@RequestParam(value="cityID")String cityID,@RequestParam(value="placesearch",required=false)String placesearch,@RequestParam(value="view",required=false)String view,HttpServletRequest request)throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("cityID", cityID);
		if(StringUtil.isNotEmpty(placesearch)){
			map.put("placesearch", StringUtil.formatSQLlike(placesearch));
			HttpSession session=request.getSession();
			session.setAttribute("placesearch", placesearch);
		}
		List<City> cityList=cityService.findCityList(map);
		HttpSession session=request.getSession();
		String language=(String)session.getAttribute("language");
		for(City c:cityList){
			Map<String,Object> map2=new HashMap<String,Object>();
			if(c.getDeviceInfo().getDeviceName().equals("dev9100d161200007")||c.getDeviceInfo().getDeviceName().equals("dev9100d161200008")){
				map2.put("deviceName",c.getDeviceInfo().getDeviceName()+"_15min");
			}else if(c.getDeviceInfo().getDeviceName().equals("dev9100d170100024")||c.getDeviceInfo().getDeviceName().equals("dev9100d170100027")){
				map2.put("deviceName",c.getDeviceInfo().getDeviceName()+"_15min");
			}else if(c.getDeviceInfo().getDeviceName().equals("dev9002a170100001")||c.getDeviceInfo().getDeviceName().equals("dev9002a170100002")||c.getDeviceInfo().getDeviceName().equals("dev9002a170100003")||c.getDeviceInfo().getDeviceName().equals("dev9002a170100004")||c.getDeviceInfo().getDeviceName().equals("dev9002a170100005")||c.getDeviceInfo().getDeviceName().equals("dev9002a170100006")){
				map2.put("deviceName",c.getDeviceInfo().getDeviceName()+"_original");
			}
			else{
				map2.put("deviceName",c.getDeviceInfo().getDeviceName()+"_1min");
			}
			map2.put("start", 0);
			map2.put("pageSize", 1);
			List<Device> deviceList=deviceService.findDeviceList(map2);
			Map<String,Object> map4=new HashMap<String,Object>();
			map4.put("deviceName", c.getDeviceInfo().getDeviceName()+"_original");
			map4.put("start", 0);
			map4.put("pageSize", 1);
			List<Device> deviceList4=deviceService.findDeviceList(map4);
			for(Device d:deviceList){
				Long differ=DateUtil.TimeDifference(DateUtil.formatDateToStr(deviceList4.get(0).getTimeStamp(),"yyyy-MM-dd HH:mm"), DateUtil.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm"));
				if(language==null||language.equals("Chinese")){
					if(differ>90){
						d.setStatus("离线");
					}else{
						d.setStatus("在线");
					}
				}else{
					if(differ>90){
						d.setStatus("OFFLINE");
					}else{
						d.setStatus("ONLINE");
					}
				}
			}
			c.setDeviceList(deviceList);
		}
		session.setAttribute("cityList", cityList);
		session.setAttribute("e_TimeStamp", DateUtil.formatDateToStr(new Date(), "yyyy-MM-dd HH:mm"));
		if(language==null||language.equals("Chinese")){
			if("map".equals(view)){
				return "redirect:/map.jsp";
			}else{
				return "redirect:/list.jsp";
			}
		}else{
			if("map".equals(view)){
				return "redirect:/en/map.jsp";
			}else{
				return "redirect:/en/list.jsp";
			}
		}
		
	}
	
	
	@RequestMapping("/searchTip")
	public String searchTip(@RequestParam(value="search-text",required=false)String search_text,@RequestParam(value="cityID")String cityID,HttpServletResponse response)throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("cityID", cityID);
		if(StringUtil.isNotEmpty(search_text)){
			map.put("placesearch", StringUtil.formatSQLlike(search_text));
		}
		List<City> cityList=cityService.findCityList(map);
		List<String> cityNameList=new ArrayList<>();
		for(City c:cityList) {
			cityNameList.add(c.getCityName());
		}
		JSONArray result=JSONArray.fromObject(cityNameList);
		ResponseUtil.write(result, response);
		return null;
	}
	
	
	@RequestMapping("/getCityGeo")
	public String getCityGeo(@RequestParam(value="cityID")String cityID,@RequestParam(value="placesearch",required=false)String placesearch,HttpServletResponse response,HttpServletRequest request)throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("cityID", cityID);
		if(StringUtil.isNotEmpty(placesearch)){
			map.put("placesearch", StringUtil.formatSQLlike(placesearch));
			HttpSession session=request.getSession();
			session.removeAttribute("placesearch");
		}
		List<City> cityList=cityService.findCityList(map);
		JSONArray result=new JSONArray();
		for(City c:cityList){
			
			JSONObject jsonObject=new JSONObject();
			float[] geo=new float[2];
			geo[0]=Float.parseFloat(c.getLng().split("E")[0]);
			geo[1]=Float.parseFloat(c.getLat().split("N")[0]);
			jsonObject.put("geo", geo);
			
			Map<String,Object> map2=new HashMap<String,Object>();
			if(c.getDeviceInfo().getDeviceName().equals("dev9100d161200007")||c.getDeviceInfo().getDeviceName().equals("dev9100d161200008")){
				map2.put("deviceName",c.getDeviceInfo().getDeviceName()+"_15min");
			}else if(c.getDeviceInfo().getDeviceName().equals("dev9100d170100024")||c.getDeviceInfo().getDeviceName().equals("dev9100d170100027")){
				map2.put("deviceName",c.getDeviceInfo().getDeviceName()+"_15min");
			}else{
				map2.put("deviceName",c.getDeviceInfo().getDeviceName()+"_1min");
			}
			map2.put("start", 0);
			map2.put("pageSize", 1);
			Device device=new Device();
			if(deviceService.findDeviceList(map2).size()>0){
				device=deviceService.findDeviceList(map2).get(0);
			}
			Map<String,Object> map3=new HashMap<String,Object>();
			map3.put("deviceName",c.getDeviceInfo().getDeviceName()+"_1h");
			map3.put("start", 0);
			map3.put("pageSize", 1);
			Device device_1h=new Device();
			if(deviceService.findDeviceList(map3).size()>0){
				device_1h=deviceService.findDeviceList(map3).get(0);
			}
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
			jsonObject.put("AQI_value",AQI_value);
			if("AQI".equals(c.getShowAQI())){
				jsonObject.put("AQIOrAQHI_value",device_1h.getAQI());
				jsonObject.put("AQIOrAQHI","AQI");
			}else{
				jsonObject.put("AQIOrAQHI_value",device_1h.getAQHI());
				jsonObject.put("AQIOrAQHI","AQHI");
			}
			jsonObject.put("name", c.getCityName());
			jsonObject.put("PM25", device.getPM25_ug());
			jsonObject.put("PM10", device.getPM10_ug());
			jsonObject.put("SO2", device.getSO2_ug());
			jsonObject.put("O3", device.getO3_ug());
			jsonObject.put("CO", device.getCO_mg());
			jsonObject.put("NO2", device.getNO2_ug());
			jsonObject.put("VOC", device.getVOC_ppb());
			jsonObject.put("Temp", device.getTemp());
			jsonObject.put("Humi", device.getHumi());
			if(c.getDeviceInfo().getDeviceName().equals("dev9100d161200007")){
				jsonObject.put("deviceName_alias", "scbs007");
			}else if(c.getDeviceInfo().getDeviceName().equals("dev9100d161200008")){
				jsonObject.put("deviceName_alias", "scbs008");
			}else{
				jsonObject.put("deviceName_alias", c.getDeviceInfo().getDeviceName());
			}
			jsonObject.put("deviceName", c.getDeviceInfo().getDeviceName());
			jsonObject.put("TimeStamp", DateUtil.formatDateToStr(device.getTimeStamp(), "yyyy-MM-dd HH:mm:ss"));
			result.add(jsonObject);
		}
		
		ResponseUtil.write(result, response);
		return null;
	}
	
	
	@RequestMapping("/updateCity")
	public String updateCity(City city,@RequestParam(value = "file",required=false) MultipartFile file,HttpServletRequest request,HttpServletResponse response)throws Exception{
		if(StringUtil.isNotEmpty(file.getOriginalFilename())){
			String fileName=request.getSession().getServletContext().getRealPath("/");
			String imageName=DateUtil.getCurrentDateStr();
			city.setPicture(imageName+"."+file.getOriginalFilename().split("\\.")[1]);
			String filePath=fileName+"img\\cityImg\\"+imageName+"."+file.getOriginalFilename().split("\\.")[1];
			FileUtils.writeByteArrayToFile(new File(filePath),file.getBytes());
		}
		cityService.updateCity(city);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("cityID", city.getCityID());
		List<City> cityList=cityService.findCityList(map);
		HttpSession session=request.getSession();
		session.setAttribute("cityList", cityList);
		String language=(String)session.getAttribute("language");
		if(language==null||language.equals("Chinese")){
			return "redirect:/mgmt.jsp";
		}else{
			return "redirect:/en/mgmt.jsp";
		}
	}
	
	
}
