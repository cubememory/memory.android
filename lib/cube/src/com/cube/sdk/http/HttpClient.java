package com.cube.sdk.http;

public interface HttpClient {
	public void init();
	
	/*interfaces with retry count default 1*/
	public void get(String url, HttpHandler handler) throws HttpException;
	
	public void get(String hostPath, HttpParams params, HttpHandler handler) throws HttpException;
	
	public void get(String host, String path, HttpParams params, HttpHandler handler) throws HttpException;
	
	public void get(String host, int port, String path, HttpParams params, HttpHandler handler) throws HttpException;
	
	public void get(String remoteFile, String localDir, HttpHandler handler) throws HttpException;
	
	public void get(String remoteFile, String localDir, String fileName, boolean append, HttpHandler handler) throws HttpException;
	
	public void post(String url, HttpHandler handler) throws HttpException;
	
	public void post(String hostPath, HttpParams params, HttpHandler handler) throws HttpException;
	
	public void post(String host, String path, HttpParams params, HttpHandler handler) throws HttpException;
	
	public void post(String host, int port, String path, HttpParams params, HttpHandler handler) throws HttpException;
	
	public void post(String remotePath, String localFile, HttpHandler handler) throws HttpException;

	/*interfaces with specified retry count*/
	public void get(String url, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void get(String hostPath, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void get(String host, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void get(String host, int port, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void get(String remoteFile, String localDir, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void get(String remoteFile, String localDir, String fileName, boolean append, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void post(String url, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void post(String hostPath, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void post(String host, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void post(String host, int port, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void post(String remotePath, String localFile, int maxRetryCount, HttpHandler handler) throws HttpException;
	
	public void destroy();
}
