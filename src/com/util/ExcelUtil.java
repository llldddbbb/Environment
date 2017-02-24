package com.util;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.entity.Device;
import com.entity.SensorEquation;

public class ExcelUtil {

	public static void fillExcelSheet1Data(List<Device> deviceList, Workbook wb, List<String> headers) throws Exception {
		int rowIndex = 0;
		Sheet sheet = wb.createSheet("data");
		HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		sheet.setColumnWidth(0, 22 * 256);
		for (int i = 1; i < headers.size(); i++) {
			sheet.setColumnWidth(i, 17 * 256);
		}
		Row row = sheet.createRow(rowIndex++);
		for (int i = 0; i < headers.size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellValue(headers.get(i));
			cell.setCellStyle(style);
		}
		
		
		for (Device device : deviceList) {
			if (device != null) {

				row = sheet.createRow(rowIndex++);

				for (int i = 0; i < headers.size(); i++) {
					
					if(headers.contains("监测时间")){
						if (device.getTimeStamp() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(DateUtil.formatDateToStr(device.getTimeStamp(), "yyyy-MM-dd HH:mm:ss"));
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("PM2.5(µg/m³)")){
						if (device.getPM25_ug() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getPM25_ug());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("NO(µg/m³)")){
						if (device.getNO_ug() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getNO_ug());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("PM10(µg/m³)")){
						if (device.getPM10_ug() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getPM10_ug());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("NO(ppb)")){
						if (device.getNO_ppb() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getNO_ppb());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("NO2(µg/m³)")){
						if (device.getNO2_ug() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getNO2_ug());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("NO2(ppb)")){
						if (device.getNO2_ppb() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getNO2_ppb());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("CO(mg/m³)")){
						if (device.getCO_mg() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getCO_mg());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("CO(ppm)")){
						if (device.getCO_ppm() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getCO_ppm());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("O3(µg/m³)")){
						if (device.getO3_ug() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getO3_ug());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("O3(ppb)")){
						if (device.getO3_ppb() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getO3_ppb());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("SO2(µg/m³)")){
						if (device.getSO2_ug() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getSO2_ug());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("SO2(ppb)")){
						if (device.getSO2_ppb() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getSO2_ppb());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("温度(℃)")){
						if (device.getTemp() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getTemp());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("湿度(%)")){
						if (device.getHumi() != null) {
							Cell cell = row.createCell(i);
							cell.setCellValue(device.getHumi());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
				}
			}
		}

	}
	
	public static void fillExcelSheet2Data(List<SensorEquation> sensorEquationList, Workbook wb, List<String> headers,List<String> sheet2AddDate) throws Exception {
		int rowIndex = 0;
		Sheet sheet = wb.createSheet("log");
		HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		for (int i = 0; i < headers.size(); i++) {
			sheet.setColumnWidth(i, 22 * 256);
		}
		Row row = sheet.createRow(rowIndex++);
		for(int i=0;i<=3;i++){
			Cell cell = row.createCell(i);
			cell.setCellValue(headers.get(i));
			cell.setCellStyle(style);
		}
		row = sheet.createRow(rowIndex++);
		for(int i=0;i<=3;i++){
			Cell cell = row.createCell(i);
			cell.setCellValue(sheet2AddDate.get(i));
			cell.setCellStyle(style);
		}
		row = sheet.createRow(rowIndex++);
		row = sheet.createRow(rowIndex++);
		for (int i = 4; i < headers.size(); i++) {
			Cell cell = row.createCell(i-4);
			cell.setCellValue(headers.get(i));
			cell.setCellStyle(style);
		}
		for (SensorEquation sensorEquation : sensorEquationList) {
			if (sensorEquation != null) {
				row = sheet.createRow(rowIndex++);
				for (int i = 4; i < headers.size(); i++) {
					
					if(headers.contains("StartTime")){
						if (sensorEquation.getStartTime() != null) {
							Cell cell = row.createCell(i-4);
							cell.setCellValue(DateUtil.formatDateToStr(sensorEquation.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("EndTime")){
						if (sensorEquation.getEndTime() != null) {
							Cell cell = row.createCell(i-4);
							cell.setCellValue(DateUtil.formatDateToStr(sensorEquation.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
					if(headers.contains("a")){
						if (sensorEquation.getA() != null) {
							Cell cell = row.createCell(i-4);
							cell.setCellValue(sensorEquation.getA());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					if(headers.contains("b")){
						if (sensorEquation.getB() != null) {
							Cell cell = row.createCell(i-4);
							cell.setCellValue(sensorEquation.getB());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					if(headers.contains("c")){
						if (sensorEquation.getC() != null) {
							Cell cell = row.createCell(i-4);
							cell.setCellValue(sensorEquation.getC());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					if(headers.contains("d")){
						if (sensorEquation.getD() != null) {
							Cell cell = row.createCell(i-4);
							cell.setCellValue(sensorEquation.getD());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					if(headers.contains("e")){
						if (sensorEquation.getE() != null) {
							Cell cell = row.createCell(i-4);
							cell.setCellValue(sensorEquation.getE());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					if(headers.contains("f")){
						if (sensorEquation.getF() != null) {
							Cell cell = row.createCell(i-4);
							cell.setCellValue(sensorEquation.getF());
							cell.setCellStyle(style);
							i++;
						} else {
							row.createCell(i).setCellValue("");
							i++;
						}
					}
					
				}
			}
		}
	}
}
