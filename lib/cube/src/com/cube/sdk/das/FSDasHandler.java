package com.cube.sdk.das;

import com.alibaba.fastjson.JSON;
import com.cube.sdk.cache.FSCache;
import com.cube.sdk.cache.FSCacheHandler;
import com.cube.sdk.das.FSHandler.EResp;
import com.cube.sdk.das.FSHandler.SResp;
import com.cube.sdk.log.FSLogger;
import com.cube.sdk.log.FSLogger.LT;
import com.cube.sdk.util.FSString;
import com.funshion.http.FSHttpHandler;
import com.funshion.http.FSHttpRequest;
import com.funshion.http.FSHttpResponse;
import com.funshion.video.entity.FSBaseEntity;
import com.funshion.video.exception.FSDasException;

public class FSDasHandler extends FSHttpHandler implements FSCacheHandler{	
	/*handler for send message to relate activity*/
	private FSHandler handler = null;
	
	/*for parse the response to entity*/
	private Class<?> entityClass = null;
	
	/*flag indicate if network access return success*/
	private boolean networkAccessSuccess = false;
	
	public FSDasHandler(FSHandler handler, Class<?> entityClass){
		this.handler = handler;
		this.entityClass = entityClass;
	}
	 	
	/**
	 * for parse the response content to entity object
	 * @param content: response content
	 * @return
	 * entity object parsed
	 * @throws FSDasException 
	 */
	private Object onParse(final String content) throws FSDasException{
		if(this.entityClass == null)
			return null;
		
		try{
			if(isJsonArray(content)){
				return JSON.parseArray(content, this.entityClass);
			}else{
				return JSON.parseObject(content, this.entityClass);
			}
		}catch(Exception e){
			throw new FSDasException(FSString.wrap(e.getMessage()));
		}
	}
	
	/**
	 * check if the content is an json array
	 * @param content
	 * @return
	 *   true if json array, otherwise return false
	 */
	private boolean isJsonArray(String content){
		if(content.matches("^\\s*\\[.*")){
			return true;
		}
		
		return false;
	}
	
	/**
	 * notify the invoker that the request has success, which means the
	 * http response code is 200 OK
	 * @param req: the http request
	 * @param resp: the relate response
	 */
	@Override
	public void onSuccess(FSHttpRequest req, FSHttpResponse resp){		
		try{
			/*TODO: decrypt the response message*/
			 
			/*decode the data to string*/
			String charset = "UTF-8";
			String content = new String(resp.getContent(), charset);
			
			/*parse the data to entity object*/
			Object entity = this.onParse(content);
			
			/*check if success response*/
			if(!isJsonArray(content)){
				FSBaseEntity base = (FSBaseEntity)entity;
				if(!base.isOK()){
					this.handler.processError(new EResp(req.getUrlString(), FSDasError.ERROR_REQUEST, resp.getCode(), base.getRetmsg()));
					FSLogger.getInstance().loge(LT.DAS, "query url: "+req.getUrlString()+" success, respcode:"+base.getRetcode()+", msg:"+base.getRetmsg()+"time used:"+resp.getTimeUsed());
					return;
				}
			}
			
			/*create the response message object*/
			SResp sresp = new SResp(SResp.Type.SERVER, false, req.getUrlString(), resp.getTimeUsed(), entity);
			this.handler.processSuccess(sresp);		
			
			/*cache the response data*/
			FSCache.getInstance().put(req.getUrlString(), content);
			
			/*change the network access state flag*/
			this.networkAccessSuccess = true;
						
			FSLogger.getInstance().logs(LT.DAS, "query url: "+req.getUrlString()+" success, time used:"+resp.getTimeUsed());
		}catch(Exception e){
			this.handler.processError(new EResp(req.getUrlString(), FSDasError.ERROR_RESPONSE, resp.getCode(), FSString.wrap(e.getMessage())));
			FSLogger.getInstance().loge(LT.DAS, "query url: "+req.getUrlString()+" failed, errMsg:"+FSString.wrap(e.getMessage()));
		}		
	}

	/**
	 * notify the invoker that the request has failed, which means the
	 * there is response, but the response code is not 200 OK
	 * @param req: the http request
	 * @param resp: the relate response
	 */
	@Override
	public void onFailed(FSHttpRequest req, FSHttpResponse resp){
		this.handler.processError(new EResp(req.getUrlString(), FSDasError.ERROR_RESPONSE, resp.getCode(), resp.getMsg()));
		FSLogger.getInstance().logf(LT.DAS, "query url: "+req.getUrlString()+" failed. response code:"+resp.getCode()+", message:"+resp.getMsg()+", time used:"+resp.getTimeUsed());
	}
	
	/**
	 * notify the invoker that the request has an error, which means the
	 * request has not executed
	 * @param req: the http request
	 * @param errMsg: the relate error message
	 */
	@Override
	public void onError(FSHttpRequest req, String errMsg){
		this.handler.processError(new EResp(req.getUrlString(), FSDasError.ERROR_NETWORK, 499, errMsg));
		FSLogger.getInstance().loge(LT.DAS, "query url: "+req.getUrlString()+" failed, errMsg:"+errMsg);
	}
	
	@Override
	public void onRetry(FSHttpRequest req, String reason) {
		FSLogger.getInstance().loge(LT.DAS, "request: "+req.getUrlString()+" failed, reason: "+reason+", will retry later.");
	}
	
	public void onSCache(SCache scache){
		try{
			/*if network access has succeed, just ignore the cache data*/
			if(this.networkAccessSuccess){
				return;
			}
			
			/*parse the data to entity object*/
			Object entity = this.onParse(scache.getContent());
			
			/*create the response statistic object*/
			SResp sresp = new SResp(SResp.Type.CACHE, scache.isExpired(), scache.getUrl(), scache.getTimeUsed(), entity);
			this.handler.processSuccess(sresp);
			
			FSLogger.getInstance().logs(LT.DAS, "query cache: "+scache.getUrl()+" success, time used:"+scache.getTimeUsed());
		}catch(Exception e){
			this.handler.processError(new EResp(scache.getUrl(), FSDasError.ERROR_CACHE, 400, FSString.wrap(e.getMessage())));
			FSLogger.getInstance().loge(LT.DAS, "query cache: "+scache.getUrl()+" failed, errMsg:"+FSString.wrap(e.getMessage())+", time used:"+scache.getTimeUsed());
		}		
	}
	
	public void onECache(ECache ecache){
		FSLogger.getInstance().loge(LT.DAS, "query cache: "+ecache.getUrl()+" failed. errCode:"+ecache.getErrCode()+", errMsg:"+ecache.getErrMsg()+", time used:"+ecache.getTimeUsed());
	}
}
