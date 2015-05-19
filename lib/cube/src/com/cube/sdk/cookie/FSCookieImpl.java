package com.cube.sdk.cookie;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.List;

public class FSCookieImpl extends FSCookie{
	@Override
	public void init(List<String> acceptUriRegexList) {
		try{
			CookieHandler.setDefault(new CookieManager(new FSCookieStore(), new FSCookiePolicy(acceptUriRegexList)));
		}catch(Exception e){
			CookieHandler.setDefault(new CookieManager()); // use default cookie handler
		}
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
