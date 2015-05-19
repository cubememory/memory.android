package com.cube.sdk.log;

import android.util.Log;

public class FSLogcat {
	public static final String TAG = "funshion";
	public static boolean DEBUG = true;

	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, wrapMsg(msg));
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, wrapMsg(msg));
		}
	
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.i(tag, wrapMsg(msg), tr);
		}	
	}
	
	public static void d(String msg) {
		if (DEBUG) {
			Log.d(TAG, wrapMsg(msg));
		}
	}

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, wrapMsg(msg));
		}
	
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.d(tag, wrapMsg(msg), tr);
		}	
	}

	public static void v(String msg) {
		if (DEBUG) {
			Log.v(TAG, wrapMsg(msg));
		}

	}

	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, wrapMsg(msg));
		}
	}
	
	public static void v(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.v(tag, wrapMsg(msg), tr);
		}	
	}

	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, wrapMsg(msg));
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, wrapMsg(msg));
		}
	
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.e(tag, wrapMsg(msg), tr);
		}
	}
	
	
	public static void w(String msg) {
        if (DEBUG) {
            Log.w(TAG, wrapMsg(msg));
        }
    }
	
	public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, wrapMsg(msg));
        }
    }
	
	public static void w(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.w(tag, wrapMsg(msg), tr);
		}	
	}
	
	public static void Logger(String msg) {
		if (DEBUG) {
			Log.e(TAG, wrapMsg(msg));
		}
	}
	
	private static String wrapMsg(String msg){
		if(msg == null)
			return "null";
		return msg;
	}
}
