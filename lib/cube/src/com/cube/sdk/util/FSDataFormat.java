/*************************************************************************************
 * Module Name: 播放器
 * File Name: FSDataFormat.java
 * Description: 
 * Author: 聂超
 * Copyright 2014-, Funshion Online Technologies Ltd.
 * All Rights Reserved
 * 版权 2014-，北京风行在线技术有限公司
 * 所有版权保护
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of Funshion Online Technologies Ltd.;
 * the contents of this file may not be disclosed to third parties, copied or
 * duplicated in any form, in whole or in part, without the prior written
 * permission of Funshion Online Technologies Ltd.
 * 这是北京风行在线技术有限公司未公开的私有源代码。本文件及相关内容未经风行在线技术有
 * 限公司事先书面同意，不允许向任何第三方透露，泄密部分或全部; 也不允许任何形式的私自备份。
 ***************************************************************************************/
package com.cube.sdk.util;

import android.text.TextUtils;

import com.cube.sdk.log.FSLogcat;

/**
 * Author: 聂超
 * Reviewer:
 * version:1.0.0.0
 * 创建时间：2014-8-6 上午10:05:04
 * 修改时间：2014-8-6 上午10:05:04
 * Description: 处理各种格式化
 **/
public class FSDataFormat {
	private final static String TAG = "FSDataFormat";
	
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
	
	/**
	 * 计算当前的网速(B/s、kB/s、MB/s)
	 */
	public static String getFormatRate(long rate){
		String currentRate = "0B/s";
		if(rate < 0){
			currentRate = "0B/s";
		}else if(rate >= 0 && rate < 1024){
			currentRate = rate + "B/s";
		}else if(rate >= 1024 && rate < 1048576){ //1024x1024 = 1048576
			currentRate = rate / 1024 + "KB/s";
		}else if(rate >= 1048576){
			currentRate = rate / 1048576 + "MB/s";
		}
		return currentRate;
	}
	
	/**
	 * 获取长视频视频的显示名称。
	 * @param mediaName 媒体名称
	 * @param episodeNum 分集序号
	 * @param episodeName 分集名称
	 * @return
	 */
	public static String getMediaDispName(String mediaName, 
			String episodeNum, String episodeName){
		if (TextUtils.isEmpty(mediaName) || TextUtils.isEmpty(episodeNum)
				|| TextUtils.isEmpty(episodeName)) {
			FSLogcat.d(TAG, "getMediaDispName() any param must not be null");
			return "";//要显示出来，所以不能为null
		}
		
		StringBuffer sbf = new StringBuffer();
		sbf.append(mediaName);
		sbf.append(" ");
		long secondPrm = -1;
		try {
			secondPrm = Long.parseLong(episodeNum);
		} catch (Exception e) {
			FSLogcat.d(TAG, "getMediaDispName() format string to digit", e);
			secondPrm = -1;
		}
		
		if (secondPrm > 10000) { //目前只能以10000作为是否是日期的评判标准
			sbf.append(episodeNum);
			sbf.append(" ");
		}
		
		if (!TextUtils.equals(mediaName, episodeName)) {
			sbf.append(episodeName);
		}
		
		return sbf.toString();
	}
	
}
