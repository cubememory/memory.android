package com.cube.sdk.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FSExecutor {
	private final int CORE_POOL_SIZE = 2;
	private ExecutorService es = null;
	private ScheduledExecutorService ses = null;
	
	private static FSExecutor executor = null;
	
	public static FSExecutor getInstance(){
		if(executor == null){
			executor = new FSExecutor();
		}
		return executor;
	}
	
	public void init(){
		this.es = Executors.newCachedThreadPool();
		this.ses = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
	}
	
	public void submit(Runnable task){
		this.es.submit(task);
	}
	
	public void submit(Runnable task, long delaySeconds){
		this.ses.schedule(task, delaySeconds, TimeUnit.SECONDS);
	}
	
	/**
	 * 添加毫秒级延迟执行任务接口
	 * @param task
	 * @param delayMilliSeconds
	 */
	public void submitDelay(Runnable task, long delayMilliSeconds){
		this.ses.schedule(task, delayMilliSeconds, TimeUnit.MILLISECONDS);
	}
	
	public void destory(){
		
	}
}

