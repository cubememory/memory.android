package com.cube.sdk.net;

import java.util.Observable;
import java.util.Observer;

import com.cube.sdk.log.FSLogcat;
import com.cube.sdk.util.FSDevice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class FSNetObservable extends Observable{
	private final String TAG = "FSNetObservable";
	
	//must be application's context
	private Context context = null;
	
	public FSNetObservable(Context context){
		super();
		this.context = context;
	}
	
	@Override
	public void addObserver(Observer observer) {
		try{
			super.addObserver(observer);
			NetworkInfo ni = FSDevice.Network.getCurrentActiveNetwork(context);
			if(ni != null){
				if(!ni.isAvailable()){
					observer.update(this, new FSNetObserver.NetAction(false, false, false));
				}else{
					if(ni.getType() == ConnectivityManager.TYPE_WIFI){
						observer.update(this, new FSNetObserver.NetAction(true, true, true));
					}else{
						observer.update(this, new FSNetObserver.NetAction(true, false, false));
					}
				}
			}else{
				observer.update(this, new FSNetObserver.NetAction(false, false, false));
			}
		}catch(Exception e){
			FSLogcat.e(TAG, e.getMessage());
		}
	}
	
	@Override
	public void notifyObservers(Object data){
		try{
			this.setChanged();
			super.notifyObservers(data);
		}catch(Exception e){
			FSLogcat.e(TAG, e.getMessage());
		}
	}
}
