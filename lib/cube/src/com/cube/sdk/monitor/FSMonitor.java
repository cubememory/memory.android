package com.cube.sdk.monitor;

import java.util.Observer;

import com.cube.sdk.monitor.FSObservable.BatteryObservable;
import com.cube.sdk.monitor.FSObservable.MediaObservable;
import com.cube.sdk.monitor.FSObservable.NetObservable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FSMonitor extends BroadcastReceiver{
	/*network action observable object*/
	private NetObservable netObservable;
	/*media sd card action observable object*/
	private MediaObservable mediaObservable;
	/*battery action observable object*/
	private BatteryObservable batteryObservable;
	
	private static FSMonitor instance = null;	
	private FSMonitor(){
		
	}
	
	public static FSMonitor getInstance(){
		if(instance == null){
			instance = new FSMonitor();
		}
		return instance;
	}
	
	public void init(Context context){
		
	}
	
	public void addObserver(Observer observer){
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
	}
	
	public void destroy(){
		
	}
}
