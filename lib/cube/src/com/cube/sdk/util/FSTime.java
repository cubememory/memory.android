package com.cube.sdk.util;

import java.util.Calendar;

import com.cube.sdk.log.FSLogcat;

public class FSTime {
	public static int getCurrentYearInt(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static String getCurrentYearString(){
		return String.valueOf(getCurrentYearInt());
	}
	
	public static int getCurrentMonthInt(){
		return Calendar.getInstance().get(Calendar.MONTH)+1;
	}
	
	public static String getCurrentMonthString(){
		return String.valueOf(getCurrentMonthInt());
	}
	
	public static int getCurrentDayInt(){
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	public static String getCurrentDayString(){
		return String.valueOf(getCurrentDayInt());
	}
	
	public static long getTimeInMillis(){
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public static String getTimeInMillisString(){
		return String.valueOf(getTimeInMillis());
	}
	
	/**
	 * 把毫秒级时间转化为"xx:xx:xx"或"xx:xx"的形式的字符串
	 * @param videoTotalTime 毫秒级的时间
	 * @return "xx:xx:xx"或"xx:xx"的形式的字符串
	 */
	public static String getFormatTimeStr(long videoTotalTime) {
		int totalSeconds = (int) (videoTotalTime / 1000L);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		
		String timeStr = null;
		try {
			if (hours > 0) {
				timeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);
			}else{
				timeStr = String.format("%02d:%02d", minutes, seconds);
			}
		} catch (Exception e) {
			FSLogcat.w("getFormatTimeStr() FSTime exception:"+e.toString());
		}
		
		return timeStr;
	}
	
}
