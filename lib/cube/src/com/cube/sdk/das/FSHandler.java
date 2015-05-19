package com.cube.sdk.das;

import com.cube.sdk.log.FSLogcat;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class FSHandler implements Handler.Callback{
	private final static String TAG = "FSHandler";

	/*message id defined for send message*/
	private final static int SUCCESS_MSG = 1;
	private final static int FAILED_MSG = 2;
	
	/*handler for activity thread to process*/
	private Handler handler = null;
	
	/*for store some object need*/
	protected Object obj = null;
	
	public FSHandler(){
		if(Looper.myLooper() != null){
			this.handler = new Handler(this);
		}
	}
	
	public FSHandler(Object obj){
		if(Looper.myLooper() != null){
			this.handler = new Handler(this);
		}
		
		this.obj = obj;
	}
	
	public FSHandler(Object obj, Handler handler){
		this.handler = handler;
		this.obj = obj;
	}
	
	/**
	 * overwrite this method for handle succeed response
	 * @param sresp: success response message
	 */
	public abstract void onSuccess(SResp sresp);
	
	/**
	 * overwrite this method for handle failed response, which response
	 * is not 200 OK or failed to access the server
	 * @param eresp: error response message
	 */
	public abstract void onFailed(EResp eresp);
	
	
	public void processSuccess(SResp sresp){
		if(this.handler != null){
			/*send message to the caller through handler*/
			Message msg = this.handler.obtainMessage(SUCCESS_MSG, sresp);
			this.handler.sendMessage(msg);
		}else{
			this.onSuccess(sresp);
		}	
	}
	
	public void processError(EResp eresp){
		if(this.handler != null){
			/*send failed message when exception*/
			Message msg = this.handler.obtainMessage(FAILED_MSG, eresp);
			this.handler.sendMessage(msg);
		}else{
			this.onFailed(eresp);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg){
		try{
			switch(msg.what){
			case SUCCESS_MSG:
				this.onSuccess((SResp)msg.obj);
				break;
			case FAILED_MSG:
				this.onFailed((EResp)msg.obj);			
				break;
			default:
				break;
			}
		}catch(Exception e){
			FSLogcat.e(TAG, e.getMessage());
		}
		return true;
	}
	
	/**
	 * success response content
	 */
	public static class SResp{
		public enum Type{CACHE, SERVER};
		
		private Type type;
		private boolean expired; // only used when type is CACHE
		private String url = null;
		private long timeUsed = -1;
		private Object entity = null;
		
		public SResp(Type type, boolean expired, String url, long timeUsed, Object entity){
			this.type = type;
			this.expired = expired;
			this.url = url;
			this.timeUsed = timeUsed;
			this.entity = entity;
		}
		
		public Type getType(){
			return this.type;
		}
		
		public boolean isExpired(){
			return this.expired;
		}

		public String getUrl() {
			return this.url;
		}

		public long getTimeUsed() {
			return this.timeUsed;
		}
		
		public Object getEntity(){
			return this.entity;
		}
		public String toDebugString(){
			return ""+type+" "+url+" "+timeUsed+" "+entity; 
		}
	}
	
	/**
	 *	error response entity
	 */
	public static class EResp{
		private String url;
		private int errCode;
		private int httpCode;
		private String errMsg;
		
		public EResp(String url, int errCode, int httpCode, String errMsg){
			this.url = url;
			this.errCode = errCode;
			this.httpCode = httpCode;
			this.errMsg = errMsg;
		}
		
		public String getUrl(){
			return this.url;
		}

		public int getErrCode() {
			return errCode;
		}

		public String getErrMsg() {
			return errMsg;
		}

		public int getHttpCode() {
			return httpCode;
		}
	}

}
