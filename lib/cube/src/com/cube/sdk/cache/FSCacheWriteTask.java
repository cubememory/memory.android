package com.cube.sdk.cache;

import com.cube.sdk.log.FSLogcat;
import com.cube.sdk.task.FSTask;
import com.cube.sdk.util.FSTime;

public class FSCacheWriteTask extends FSTask{
	private final static String TAG = "FSCacheWriteTask";
	
	private String url;
	private String content;
		
	public FSCacheWriteTask(String url, String content){
		this.url = url;
		this.content = content;
	}
	
	@Override
	public void proc() {
		try{
			long stime = FSTime.getTimeInMillis();
			FSCacheFiles.getInstance().write(this.url, this.content);
			long etime = FSTime.getTimeInMillis();
			FSLogcat.d(TAG, "write cache for url: "+this.url+", time used: "+String.valueOf(etime-stime));
		}catch(Exception e){
			FSLogcat.d(TAG, e.getMessage());
		}
	}

}
