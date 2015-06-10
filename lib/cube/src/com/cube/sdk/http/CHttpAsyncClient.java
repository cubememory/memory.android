package com.cube.sdk.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * asynchronized http client
 */
public class CHttpAsyncClient implements CHttpClient{
	//a executor service object to execute the http task
	private ExecutorService executorService = null;
	
	public CHttpAsyncClient(){
		this.init();
	}
	
	@Override
	public void init() {
		if(this.executorService == null){
			this.executorService = Executors.newCachedThreadPool();
		}
	}


	@Override
	public void get(String url, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpGetTask(url, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void get(String hostPath, CHttpParams params, CHttpHandler handler) throws CHttpException{
		CHttpTask task = new CHttpGetTask(hostPath, params, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String host, String path, CHttpParams params, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpGetTask(host, path, params, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String host, int port, String path, CHttpParams params, CHttpHandler handler) throws CHttpException{
		CHttpTask task = new CHttpGetTask(host, port, path, params, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void get(String remoteFile, String localDir, CHttpHandler handler)throws CHttpException {
		CHttpTask task = new CHttpFileGetTask(remoteFile, localDir, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String remoteFile, String localDir, String fileName, boolean append, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpFileGetTask(remoteFile, localDir, fileName, append, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void post(String url, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpPostTask(url, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void post(String hostPath, CHttpParams params, CHttpHandler handler) throws CHttpException{
		CHttpTask task = new CHttpPostTask(hostPath, params, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String host, String path, CHttpParams params, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpPostTask(host, path, params, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String host, int port, String path, CHttpParams params, CHttpHandler handler) throws CHttpException{
		CHttpTask task = new CHttpPostTask(host, port, path, params, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String remotePath, String localFile, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpFilePostTask(remotePath, localFile, CHttpCfg.DEFAULT_MAX_RETRY_COUNT, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String url, int maxRetryCount, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpGetTask(url, maxRetryCount, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void get(String hostPath, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		CHttpTask task = new CHttpGetTask(hostPath, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String host, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpGetTask(host, path, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String host, int port, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		CHttpTask task = new CHttpGetTask(host, port, path, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void get(String remoteFile, String localDir, int maxRetryCount, CHttpHandler handler)throws CHttpException {
		CHttpTask task = new CHttpFileGetTask(remoteFile, localDir, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void get(String remoteFile, String localDir, String fileName, boolean append, int maxRetryCount, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpFileGetTask(remoteFile, localDir, fileName, append, maxRetryCount, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void post(String url, int maxRetryCount, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpPostTask(url, maxRetryCount, handler);
		this.executorService.execute(task);
	}
	
	@Override
	public void post(String hostPath, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		CHttpTask task = new CHttpPostTask(hostPath, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String host, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpPostTask(host, path, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String host, int port, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		CHttpTask task = new CHttpPostTask(host, port, path, params, maxRetryCount, handler);
		this.executorService.execute(task);
	}

	@Override
	public void post(String remotePath, String localFile, int maxRetryCount, CHttpHandler handler) throws CHttpException {
		CHttpTask task = new CHttpFilePostTask(remotePath, localFile, maxRetryCount, handler);
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
