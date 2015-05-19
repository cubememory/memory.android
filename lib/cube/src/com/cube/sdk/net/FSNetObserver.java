package com.cube.sdk.net;

import java.util.Observable;
import java.util.Observer;

public abstract class FSNetObserver implements Observer{

	@Override
	public void update(Observable observable, Object data) {
		this.notify((NetAction)data);
	}
	
	/**
	 * sub class must realize this method to receive notifications
	 * @param action
	 */
	public abstract void notify(final NetAction action);
	
	/**
	 * net action definition
	 * @author wangrb
	 */
	public static class NetAction{
		//indicate if current network is available
		private boolean isAvailable = false;
		//indicate if current network is wifi
		private boolean isWifi = false;
		//if need to stop using the network
		private boolean stopUsing = true;
		
		public NetAction(boolean isAvailable, boolean isWifi, boolean stopUsing){
			this.isAvailable = isAvailable;
			this.isWifi = isWifi;
			this.stopUsing = stopUsing;
		}

		public boolean isAvailable() {
			return isAvailable;
		}
		
		public boolean isWifi() {
			return isWifi;
		}

		public boolean isStopUsing() {
			return stopUsing;
		}
	}
}
