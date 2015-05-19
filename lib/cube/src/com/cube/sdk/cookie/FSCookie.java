package com.cube.sdk.cookie;

import java.util.List;

/**
 * initialize the default cookie handler used by system HttpUrlConnection
 * @author wangrb
 *
 */
public abstract class FSCookie {
	private static FSCookie instance = null;
		
	public static FSCookie getInstance(){
		if(instance == null){
			instance = new FSCookieImpl();
		}
		return instance;
	}
	
	/**
	 * initialize the cookie module
	 * @param cookieDir: cookie data saved directory, must be valied directory
	 * @param acceptUriRegexList: accept uri regex list, could be null
	 */
	public abstract void init(List<String> acceptUriRegexList);
	
	/**
	 * destroy the cookie module
	 */
	public abstract void destroy();
}
