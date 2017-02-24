package com.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapUtil {
	
	public static Map<String,String> map=new HashMap<String,String>();
	static{
		map.put("TimeStamp", "监测时间");
		map.put("TimeStamp_en", "TimeStamp");
		map.put("`PM2.5_ug`", "PM2.5(µg/m³)");
		map.put("PM10_ug", "PM10(µg/m³)");
		map.put("NO_ug", "NO(µg/m³)");
		map.put("NO_ppb", "PM10(ppb)");
		map.put("NO2_ug", "NO2(µg/m³)");
		map.put("NO2_ppb", "NO2(ppb)");
		map.put("CO_mg", "CO(mg/m³)");
		map.put("CO_ppm", "CO(ppm)");
		map.put("O3_ug", "O3(µg/m³)");
		map.put("O3_ppb", "O3(ppb)");
		map.put("SO2_ug", "SO2(µg/m³)");
		map.put("SO2_ppb", "SO2(ppb)");
		map.put("Temp", "温度(℃)");
		map.put("Temp_en", "Temp(℃)");
		map.put("Humi", "湿度(%)");
		map.put("Humi_en", "Humi(%)");
	}
	
	public static List<String> getValueByKey(String[] key){
		List<String> value=new ArrayList<String>();
		for (String k : key) {
			if(StringUtil.isNotEmpty(map.get(k))){
				value.add(map.get(k));
			}
		}
		return value;
	}
}
