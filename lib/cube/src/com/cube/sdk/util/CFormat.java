package com.cube.sdk.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CFormat {
	public static String parseDate(String format, long timeInMillionSec){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(timeInMillionSec));
	}
}
