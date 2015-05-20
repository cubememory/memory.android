package com.cube.sdk.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cube.sdk.util.CDevice;

public class CNetMonitor extends BroadcastReceiver{
	private final static String TAG = "NetMonitor";
	
	//observable object for the net monitor
	private CNetObservable observable = null;
	
	//single instance of network monitor
	private static CNetMonitor instance = null;
	
	public static CNetMonitor getInstance(){
		if(instance == null){
			instance = new CNetMonitor();
		}
		return instance;
	}
	
	public void init(Context context){
		this.observable = new CNetObservable(context);
		
		//filter the network connectivity change action broadcast message
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		
		//register the broadcast receiver
		context.registerReceiver(this, filter);
	}
	
	public void addObserver(CNetObserver observer){
		this.observable.addObserver(observer);
	}
	
	public void delObserver(CNetObserver observer){
		this.observable.deleteObserver(observer);
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.notifyNetState(context);
	}
	
	private void notifyNetState(Context context){
		try{
			//network state changed, get current network state
			NetworkInfo ni = CDevice.Network.getCurrentActiveNetwork(context);
			if(ni != null){
				if(!ni.isAvailable()){
					this.observable.notifyObservers(new CNetObserver.NetAction(false, false, false));
				}else{
					if(ni.getType() == ConnectivityManager.TYPE_WIFI){
						this.observable.notifyObservers(new CNetObserver.NetAction(true, true, true));
					}else{
						this.observable.notifyObservers(new CNetObserver.NetAction(true, false, false));
					}
				}
			}else{
				this.observable.notifyObservers(new CNetObserver.NetAction(false, false, false));
			}
			
		}catch(Exception e){
			Log.e(TAG, e.getMessage());
		}
	}
	
	public void destory(){
		this.observable.deleteObservers();
	}

}

