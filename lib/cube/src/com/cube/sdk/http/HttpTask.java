package com.cube.sdk.http;

import java.net.HttpURLConnection;


public  abstract class HttpTask implements Runnable{
	protected int maxRetryCount = 1;
	
	protected HttpRequest request = null;
	protected HttpResponse response = null;
	protected HttpHandler handler = null;
	
	protected HttpURLConnection conn = null;
	
	public HttpTask(String url, int maxRetryCount, HttpHandler handler) throws HttpException{
		this.request = new HttpRequest(url);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}

	public HttpTask(String hostPath, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		this.request = new HttpRequest(hostPath, params);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}
	
	public HttpTask(String host, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		this.request = new HttpRequest(host, path, params);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}
	
	public HttpTask(String host, int port, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		this.request = new HttpRequest(host, port, path, params);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}
	
	public HttpTask(String remoteFile, String localDir, int maxRetryCount, HttpHandler handler) throws HttpException{
		this.request = new HttpRequest(remoteFile, localDir);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}	
	
	public HttpTask(String remoteFile, String localDir, String fileName, int maxRetryCount, HttpHandler handler) throws HttpException{
		this.request = new HttpRequest(remoteFile, localDir, fileName);
		this.maxRetryCount = maxRetryCount;
		this.handler = handler;
	}
		
	protected abstract void request() throws HttpException;
		
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

	private void inform() throws HttpException{
		if(this.handler == null){
			return;
		}
		
		if(this.response == null){
			throw new HttpException("null response");
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