package com.cube.sdk.task;

public abstract class FSTask implements Runnable{
	protected Object obj = null;
	
	public FSTask(){
		super();
	}
	
	public FSTask(Object obj){
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
