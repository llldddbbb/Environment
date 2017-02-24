package com.util;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	
	public static String formatDateToStr(Date date,String type){
		SimpleDateFormat sdf=new SimpleDateFormat(type);
		return sdf.format(date);
	}
	
	public static String formatDateToStr2(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");
		return "'"+sdf.format(date)+"'";
	}
	
	public static Date formatDate(Date date,String type) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat(type);
		String s=sdf.format(date);
		return sdf.parse(s);
	}
	
	public static Date formatStrToDate(String date,String type)throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat(type);
		return sdf.parse(date);
	}
	
	public static String getCurrentDateStr(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
	
	
	//获取分钟差
	public static long  TimeDifference(String dateStart,String dateStop){
		long result = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
        	Date d1 = format.parse(dateStart);
        	Date d2 = format.parse(dateStop);
            //毫秒ms
            long diff = d2.getTime() - d1.getTime();
            result=diff/60000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
	}
	
	
}
