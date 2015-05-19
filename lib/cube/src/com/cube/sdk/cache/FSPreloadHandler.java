package com.cube.sdk.cache;

import com.alibaba.fastjson.JSON;
import com.cube.sdk.log.FSLogcat;
import com.funshion.http.FSHttpHandler;
import com.funshion.http.FSHttpRequest;
import com.funshion.http.FSHttpResponse;
import com.funshion.video.entity.FSBaseEntity;

/**
 * http handler for preload request content
 * @author wangrb
 *
 */
public class FSPreloadHandler extends FSHttpHandler{
	private final static String TAG = "FSPreloadHandler";
	
	private FSCache cache;
	
	public FSPreloadHandler(FSCache cache){
		this.cache = cache;
	}

	@Override
	public void onError(FSHttpRequest req, String errMsg) {
		FSLogcat.d(TAG, "request: "+req.getUrlString()+", error: "+errMsg);			
	}

	@Override
	public void onFailed(FSHttpRequest req, FSHttpResponse resp) {
		FSLogcat.d(TAG, "request: "+req.getUrlString()+", response code: "+resp.getCode()+", response message: "+resp.getMsg());
	}
	
	@Override
	public void onRetry(FSHttpRequest req, String reason) {
		FSLogcat.d(TAG, "request: "+req.getUrlString()+" failed, reason: "+reason+", will retry later.");
	}

	@Override
	public void onSuccess(FSHttpRequest req, FSHttpResponse resp) {
		try{
			if(resp.getCode() == 200){
				/*decode the data to string*/
				String charset = "UTF-8";
				String content = new String(resp.getContent(), charset);
				
				/*parse by base entity*/
				FSBaseEntity entity = JSON.parseObject(content, FSBaseEntity.class);
				
				/*only cache the valid response data*/
				if(entity.isOK()){
					this.cache.put(req.getUrlString(), content);
				}
			}
		}catch(Exception e){
			FSLogcat.d(TAG, e.getMessage());
		}
	}
}
