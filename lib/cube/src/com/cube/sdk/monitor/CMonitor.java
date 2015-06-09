package com.cube.sdk.monitor;

import java.util.Observer;

import com.cube.sdk.monitor.CObservable.BatteryObservable;
import com.cube.sdk.monitor.CObservable.MediaObservable;
import com.cube.sdk.monitor.CObservable.NetObservable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CMonitor extends BroadcastReceiver{
	/*network action observable object*/
	private NetObservable netObservable;
	/*media sd card action observable object*/
	private MediaObservable mediaObservable;
	/*battery action observable object*/
	private BatteryObservable batteryObservable;
	
	private static CMonitor instance = null;	
	private CMonitor(){
		
	}
	
	public static CMonitor getInstance(){
		if(instance == null){
			instance = new CMonitor();
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
