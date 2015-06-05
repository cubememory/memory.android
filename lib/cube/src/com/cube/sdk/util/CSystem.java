package com.cube.sdk.util;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CSystem {
	private final static String TAG = "CSystem";
	
	public static void install(Context context, String localApk) {
		try {			
			String cmd = "chmod 755 " + localApk;
			Runtime.getRuntime().exec(cmd);
			
			final Uri uri = Uri.fromFile(new File(localApk));
			final Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
			installIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(installIntent);
	
		} catch (Exception e){
			Log.e(TAG, e.getMessage());
		}
	}
	
	public static String getCurrentProcName(Context context){
		int pid = android.os.Process.myPid();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for(ActivityManager.RunningAppProcessInfo rapi : am.getRunningAppProcesses()){
			if(rapi.pid == pid){
				return rapi.processName;
			}
		}
		
		return null;
	}
	
}
