package com.cube.memory.setup;

import android.content.Context;

import com.cube.memory.util.CubeImageLoader;
import com.cube.sdk.task.CExecutor;

public class ModuleManager {
	public static void setup(Context context){
		CubeImageLoader.init(context);
		
		CExecutor.getInstance().init();
	}
}
