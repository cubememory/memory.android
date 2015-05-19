package com.cube.sdk.cache;

import com.cube.sdk.cache.FSCacheHandler.*;
import com.cube.sdk.log.FSLogcat;
import com.cube.sdk.task.FSTask;
import com.cube.sdk.util.FSTime;

public class FSCacheReadTask extends FSTask{
	private final static String TAG = "FSCacheReadTask";
	
	private String url;
	private FSCacheHandler handler;
	private boolean expired;
	
	public FSCacheReadTask(String url, FSCacheHandler handler, boolean expired){
		this.url = url;
		this.handler = handler;
		this.expired = expired;
	}
	
	@Override
	public void proc() {
		long stime = FSTime.getTimeInMillis();
		try{
			String content = FSCacheFiles.getInstance().read(this.url);
			long etime = FSTime.getTimeInMillis();
			handler.onSCache(new SCache(this.url, etime-stime, expired, content));
		}catch(Exception e){
			long etime = FSTime.getTimeInMillis();
			handler.onECache(new ECache(this.url, etime-stime, ECache.ERROR_CACHE, e.getMessage()));
			FSLogcat.d(TAG, e.getMessage());
		}
	}
}
