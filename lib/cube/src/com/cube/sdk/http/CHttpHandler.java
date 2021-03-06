package com.cube.sdk.http;

/**
 * recall handler for http request after the request has executed
 */
public abstract class CHttpHandler{
	//object for recall
	protected Object obj = null;
	
	public CHttpHandler(){
		
	}
	
	public CHttpHandler(Object obj){
		this.obj = obj;
	}
	
	
	/**
	 * notify the invoker that the request has success, which means the
	 * http response code is 200 OK
	 * @param req: the http request
	 * @param resp: the relate response
	 */
	public abstract void onSuccess(CHttpRequest req, CHttpResponse resp);

	/**
	 * notify the invoker that the request has failed, which means the
	 * there is response, but the response code is not 200 OK
	 * @param req: the http request
	 * @param resp: the relate response
	 */
	public abstract void onFailed(CHttpRequest req, CHttpResponse resp);
	
	/**
	 * notify the invoker that the request has an error, which means the
	 * request has not executed
	 * @param req: the http request
	 * @param errMsg: the relate error message
	 */
	public abstract void onError(CHttpRequest req, String errMsg);
	
	/**
	 * notify the invoker that the request will retry later
	 * request has not executed
	 * @param req: the http request
	 * @param reason: the retry reason
	 */
	public abstract void onRetry(CHttpRequest req, String reason);
}

