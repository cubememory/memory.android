package com.cube.sdk.task;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CTimer {
	private Timer timer = null;
	
	private static CTimer instance = null;
	
	public static CTimer getInstance(){
		if(instance == null){
			instance = new CTimer();
		}
		return instance;
	}
	
	public void init(){
		timer = new Timer();
	}
	
	public void cancel(){
		this.timer.cancel();
	}
	
	public int purge(){
		return this.timer.purge();
	}
	
	public void schedule(TimerTask task, Date time){
		this.timer.schedule(task, time);
	}
	
	public void schedule(TimerTask task, Date firstTime, long period){
		this.timer.schedule(task, firstTime, period);
	}
	 
	public void schedule(TimerTask task, long delay){
		this.timer.schedule(task, delay);
	}
	 
	public void schedule(TimerTask task, long delay, long period){
		this.timer.schedule(task, delay, period);
	}
	 
	public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period){
		this.timer.schedule(task, firstTime, period);
	}
	 
	public void scheduleAtFixedRate(TimerTask task, long delay, long period){
		this.timer.schedule(task,  delay, period);
	}
	
	
}

