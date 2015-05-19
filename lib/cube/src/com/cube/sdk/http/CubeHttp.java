package com.cube.sdk.http;

/**
 * 
 */
public class CubeHttp {
	private static HttpClient defaultClient = null;
	
	public static synchronized HttpClient defaultHttpClient(){
		if(defaultClient == null)
			defaultClient = new HttpAsyncClient();
		return defaultClient;
	}
	
	public static HttpClient newHttpClient(){
		return new HttpAsyncClient();
	}
}
