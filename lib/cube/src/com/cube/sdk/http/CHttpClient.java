package com.cube.sdk.http;

public interface CHttpClient {
	public void init();
	
	/*interfaces with retry count default 1*/
	public void get(String url, CHttpHandler handler) throws CHttpException;
	
	public void get(String hostPath, CHttpParams params, CHttpHandler handler) throws CHttpException;
	
	public void get(String host, String path, CHttpParams params, CHttpHandler handler) throws CHttpException;
	
	public void get(String host, int port, String path, CHttpParams params, CHttpHandler handler) throws CHttpException;
	
	public void get(String remoteFile, String localDir, CHttpHandler handler) throws CHttpException;
	
	public void get(String remoteFile, String localDir, String fileName, boolean append, CHttpHandler handler) throws CHttpException;
	
	public void post(String url, CHttpHandler handler) throws CHttpException;
	
	public void post(String hostPath, CHttpParams params, CHttpHandler handler) throws CHttpException;
	
	public void post(String host, String path, CHttpParams params, CHttpHandler handler) throws CHttpException;
	
	public void post(String host, int port, String path, CHttpParams params, CHttpHandler handler) throws CHttpException;
	
	public void post(String remotePath, String localFile, CHttpHandler handler) throws CHttpException;

	/*interfaces with specified retry count*/
	public void get(String url, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void get(String hostPath, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void get(String host, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void get(String host, int port, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void get(String remoteFile, String localDir, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void get(String remoteFile, String localDir, String fileName, boolean append, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void post(String url, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void post(String hostPath, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void post(String host, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void post(String host, int port, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void post(String remotePath, String localFile, int maxRetryCount, CHttpHandler handler) throws CHttpException;
	
	public void destroy();
}
