package com.cube.sdk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CFormat {
	public static String parseDate(String format, long timeInMillionSec){
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(new Date(timeInMillionSec));
	}
}
