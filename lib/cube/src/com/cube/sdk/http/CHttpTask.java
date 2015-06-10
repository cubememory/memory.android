package com.cube.sdk.http;

import java.net.HttpURLConnection;


public  abstract class CHttpTask implements Runnable{
	protected int maxRetryCount = 1;
	
	protected CHttpRequest request = null;
	protected CHttpResponse response = null;
	protected CHttpHandler handler = null;
	
	protected HttpURLConnection conn = null;
	
	public CHttpTask(String url, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		this.request = new CHttpRequest(url);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}

	public CHttpTask(String hostPath, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		this.request = new CHttpRequest(hostPath, params);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}
	
	public CHttpTask(String host, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		this.request = new CHttpRequest(host, path, params);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}
	
	public CHttpTask(String host, int port, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		this.request = new CHttpRequest(host, port, path, params);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}
	
	public CHttpTask(String remoteFile, String localDir, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		this.request = new CHttpRequest(remoteFile, localDir);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}	
	
	public CHttpTask(String remoteFile, String localDir, String fileName, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		this.request = new CHttpRequest(remoteFile, localDir, fileName);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}
		
	protected abstract void request() throws CHttpException;
		
	private void query() throws Throwable{
		boolean success = false;
		while(this.maxRetryCount-->0 && !success){
			try{
				this.request();
				success = true;
			}catch(Throwable e){
				if(this.maxRetryCount<=0){
					throw e;
				}else{
					if(this.handler != null){
						String msg = e.getMessage();
						if(msg == null){
							msg = "null, unknown error";
						}
						this.handler.onRetry(this.request, msg);
					}
				}
			}
		}
	}

	private void inform() throws CHttpException{
		if(this.handler == null){
			return;
		}
		
		if(this.response == null){
			throw new CHttpException("null response");
		}
		
		if(this.response.getCode() == HttpURLConnection.HTTP_OK){
			this.handler.onSuccess(this.request, this.response);
		}else{
			this.handler.onFailed(this.request, this.response);
		}
	}
	
	public void run() {
		try{
			this.query();
			
			this.inform();
			
		}catch(Throwable e){
			if(this.handler != null)
				this.handler.onError(this.request, e.getMessage());
		}
	}
}