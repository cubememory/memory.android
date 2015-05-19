package com.cube.sdk.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cube.sdk.log.FSLogcat;
import com.cube.sdk.util.FSDevice;

public class FSNetMonitor extends BroadcastReceiver{
	private final static String TAG = "NetMonitor";
	
	//observable object for the net monitor
	private FSNetObservable observable = null;
	
	//single instance of network monitor
	private static FSNetMonitor instance = null;
	
	public static FSNetMonitor getInstance(){
		if(instance == null){
			instance = new FSNetMonitor();
		}
		return instance;
	}
	
	public void init(Context context){
		this.observable = new FSNetObservable(context);
		
		//filter the network connectivity change action broadcast message
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		
		//register the broadcast receiver
		context.registerReceiver(this, filter);
	}
	
	public void addObserver(FSNetObserver observer){
		this.observable.addObserver(observer);
	}
	
	public void delObserver(FSNetObserver observer){
		this.observable.deleteObserver(observer);
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.notifyNetState(context);
	}
	
	private void notifyNetState(Context context){
		try{
			//network state changed, get current network state
			NetworkInfo ni = FSDevice.Network.getCurrentActiveNetwork(context);
			if(ni != null){
				if(!ni.isAvailable()){
					this.observable.notifyObservers(new FSNetObserver.NetAction(false, false, false));
				}else{
					if(ni.getType() == ConnectivityManager.TYPE_WIFI){
						this.observable.notifyObservers(new FSNetObserver.NetAction(true, true, true));
					}else{
						this.observable.notifyObservers(new FSNetObserver.NetAction(true, false, false));
					}
				}
			}else{
				this.observable.notifyObservers(new FSNetObserver.NetAction(false, false, false));
			}
			
		}catch(Exception e){
			FSLogcat.e(TAG, e.getMessage());
		}
	}
	
	public void destory(){
		this.observable.deleteObservers();
	}

}

