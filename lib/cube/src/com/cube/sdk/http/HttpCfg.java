package com.cube.sdk.http;

public interface HttpCfg {
	public final static int DEFAULT_MAX_RETRY_COUNT = 1; //default max retry count
	
	public final static int DEFAULT_HTTP_PORT = 80;
	
	public final static int SIZE_READ_BUFFER = 16*1024;
	
	public final static int CONNECT_TIMEOUT = 15000; //15s
	
	public final static int READ_TIMEOUT = 20000; //20s
}
