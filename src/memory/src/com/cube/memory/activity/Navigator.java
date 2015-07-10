package com.cube.memory.activity;

import android.content.Context;
import android.content.Intent;

public class Navigator {
	public static void startHomePage(Context context){
		Intent intent = new Intent(context, ActivityHomePage.class);
		context.startActivity(intent);
	}
}
