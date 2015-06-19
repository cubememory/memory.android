package com.cube.memory.util;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.MediaStore;

public class CubeUtils {
	
	/**
	 * start camera
	 * @param context: activity context
	 */
	public static void startCamera(Context context) {
	    final Intent captureIntent = new Intent(android.provider.MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
	    final PackageManager packageManager = context.getPackageManager();
	    final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
	    if (listCam.size() > 0) {
			ResolveInfo cameraInfo = listCam.get(0);
			String cameraActivityName = getCameraActivityName(context, cameraInfo);
			if (cameraActivityName != null) {
				Intent intent = new Intent("android.intent.action.MAIN");
				intent.setComponent(new ComponentName(cameraInfo.activityInfo.packageName, cameraActivityName));
				intent.setPackage(cameraInfo.activityInfo.packageName);
				context.startActivity(intent);
			} else {
				Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
				context.startActivity(intent);
			}
	    }
	}
	
	private static String getCameraActivityName(Context context, ResolveInfo cameraInfo) {
		String cameraActivityName = null;
		try {
			PackageManager packageManager = context.getPackageManager();
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setPackage(cameraInfo.activityInfo.packageName);
			List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, 0);
			for (int i = 0; i < infos.size(); i++) {
				ResolveInfo ri = infos.get(i);
				if (ri.activityInfo.name.endsWith(".CameraLauncher")) {
					cameraActivityName = ri.activityInfo.name;
					break;
				}

				if (ri.activityInfo.name.endsWith(".Camera")) {
					cameraActivityName = ri.activityInfo.name;
					break;
				}
			}

			if (cameraActivityName == null) {
				cameraActivityName = cameraInfo.activityInfo.name;
			}
		} catch (Exception e) {

		}
		
		return cameraActivityName;
	}
}
