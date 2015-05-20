package com.cube.sdk.task;

public abstract class CTask implements Runnable{
	protected Object obj = null;
	
	public CTask(){
		super();
	}
	
	public CTask(Object obj){
		this.obj = obj;
	}
	
	public Object getObj(){
		return this.obj;
	}
	
	public abstract void proc(); 

	@Override
	public void run() {
		this.proc();
	}
}
