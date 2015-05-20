package com.cube.sdk.http;

/**
 * 
 */
public class CHttp {
	private static CHttpClient defaultClient = null;
	
	public static synchronized CHttpClient defaultHttpClient(){
		if(defaultClient == null)
			defaultClient = new CHttpAsyncClient();
		return defaultClient;
	}
	
	public static CHttpClient newHttpClient(){
		return new CHttpAsyncClient();
	}
}
