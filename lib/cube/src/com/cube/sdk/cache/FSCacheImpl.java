package com.cube.sdk.cache;

import android.content.Context;

import com.cube.sdk.log.FSLogcat;
import com.cube.sdk.task.FSExecutor;
import com.funshion.http.FSHttp;
import com.funshion.http.FSHttpException;

public class FSCacheImpl extends FSCache{
	private static final String TAG = "FSCacheImpl";
	
	/*cache rules*/
	FSCacheRules rules = new FSCacheRules();
	
	@Override
	public void init(Context context) {
		FSCacheFiles.getInstance().init();
		rules.init(context);
	}
	
	/**
	 * access the url and 
	 * @param url
	 */
	@Override
	public void preload(String url){
		try {
			FSHttp.defaultHttpClient().get(url, new FSPreloadHandler(this));
		} catch (FSHttpException e) {
			FSLogcat.d(TAG, e.getMessage());
		}
	}
	
	/**
	 * put content with key to cache file
	 * @param key
	 * @param content
	 */
	@Override
	public void put(String url, String content){
		if(this.rules.needCache(url)){
			FSExecutor.getInstance().submit(new FSCacheWriteTask(url, content));
			FSLogcat.d(TAG, "put cache for url: "+url);
		}
		
	}
	
	@Override
	public boolean get(String url, FSCacheHandler handler){
		FSCacheRules.Rule rule = this.rules.getRule(url);
		if(rule != null){
			if(FSCacheFiles.getInstance().isHit(url)){
				boolean expired = FSCacheFiles.getInstance().isExpired(url, rule.getExpireInMillis());
				if(rule.isStrong()){
					FSExecutor.getInstance().submit(new FSCacheReadTask(url, handler, expired));
					if(!expired){
						return true;
					}
				}else{
					if(!expired){
						FSExecutor.getInstance().submit(new FSCacheReadTask(url, handler, expired));
						return true;
					}
				}
				
				FSLogcat.d(TAG, "get cache for url: " + url);
			}
		}
		
		return false;
	}
}
