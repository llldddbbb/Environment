package com.util;


public class StringUtil {
	public static boolean isNotEmpty(String str){
		if(str!=null&&!str.trim().equals("")){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isEmpty(String str){
		if(str==null||str.trim().equals("")){
			return true;
		}else{
			return false;
		}
	}
	
	
	public static String formatSQLlike(String str){
		return "%"+str+"%";
	}
}
