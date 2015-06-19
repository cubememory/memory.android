package com.cube.sdk.adas;

/**
 * async data access service
 * @author polly
 *
 */
public class CAdas {
	private static CAdas instance = new CAdas();
	
		
	private CAdas(){
		
	}
	
	public static CAdas getInstance(){
		return instance;
	}
	
	public void request(Object req){
		
	}
	
	public static interface Callback{
		public void handleResponse(Object req, Object resp);
	}
}
