package com.cube.sdk.cache;

import com.cube.sdk.util.FSDigest;

public class FSCacheUtil {
	/**
	 * generate cache file name relate with url 
	 * @param url
	 * @return
	 * cache file name, or null
	 */
	public static String getFileName(String url){
		String urlSha1 = FSDigest.sha1(url);
		if(urlSha1 == null)
			return null;
		
		String fileName = urlSha1+".cache";
		
		return fileName;
	}
}
