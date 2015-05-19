package com.cube.sdk.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * asynchronized http client
 */
public class HttpAsyncClient implements HttpClient{
	//a executor service object to execute the http task
	private ExecutorService executorService = null;
	
	public HttpAsyncClient(){
		this.init();
	}
	
	@Override
	public void init() {
		if(this.executorService == null){
			this.executorService = Executors.newCachedThreadPool();
		}
	}


	@Override
	public void get(String url, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpGetTask(url, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void get(String hostPath, HttpParams params, HttpHandler handler) throws HttpException{
		HttpTask task = new HttpGetTask(hostPath, params, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String host, String path, HttpParams params, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpGetTask(host, path, params, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String host, int port, String path, HttpParams params, HttpHandler handler) throws HttpException{
		HttpTask task = new HttpGetTask(host, port, path, params, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void get(String remoteFile, String localDir, HttpHandler handler)throws HttpException {
		HttpTask task = new HttpFileGetTask(remoteFile, localDir, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String remoteFile, String localDir, String fileName, boolean append, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpFileGetTask(remoteFile, localDir, fileName, append, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void post(String url, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpPostTask(url, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void post(String hostPath, HttpParams params, HttpHandler handler) throws HttpException{
		HttpTask task = new HttpPostTask(hostPath, params, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String host, String path, HttpParams params, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpPostTask(host, path, params, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String host, int port, String path, HttpParams params, HttpHandler handler) throws HttpException{
		HttpTask task = new HttpPostTask(host, port, path, params, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String remotePath, String localFile, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpFilePostTask(remotePath, localFile, HttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String url, int maxRetryCount, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpGetTask(url, maxRetryCount, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void get(String hostPath, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		HttpTask task = new HttpGetTask(hostPath, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String host, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpGetTask(host, path, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String host, int port, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		HttpTask task = new HttpGetTask(host, port, path, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void get(String remoteFile, String localDir, int maxRetryCount, HttpHandler handler)throws HttpException {
		HttpTask task = new HttpFileGetTask(remoteFile, localDir, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String remoteFile, String localDir, String fileName, boolean append, int maxRetryCount, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpFileGetTask(remoteFile, localDir, fileName, append, maxRetryCount, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void post(String url, int maxRetryCount, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpPostTask(url, maxRetryCount, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void post(String hostPath, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		HttpTask task = new HttpPostTask(hostPath, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String host, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpPostTask(host, path, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String host, int port, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		HttpTask task = new HttpPostTask(host, port, path, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String remotePath, String localFile, int maxRetryCount, HttpHandler handler) throws HttpException {
		HttpTask task = new HttpFilePostTask(remotePath, localFile, maxRetryCount, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void destroy() {
		if(this.executorService != null){
			this.executorService.shutdown();
			this.executorService = null;
		}
	}
}
