package com.cube.sdk.dump;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.content.Context;

public class CDump {
	private final static String TAG = "CDump";
	private static CDump instance = null;
	private DumpHandler dumpHandler = null;
	
	private CDump(){	
	}

	public static CDump getInstance(){
		if(instance == null){
			instance = new CDump();
		}
		return instance;
	}
	
	public void init(Context context){
		try{
			/*initialize the dump handler*/
			dumpHandler = new DumpHandler(Thread.getDefaultUncaughtExceptionHandler());
			Thread.setDefaultUncaughtExceptionHandler(dumpHandler);
		}catch(Exception e){
			
		}
	}
	
	public void destroy(){
		this.dumpHandler = null;
	}
	
	private static class DumpHandler implements UncaughtExceptionHandler{
		private Thread.UncaughtExceptionHandler defaultHandler = null;
		
		public DumpHandler(Thread.UncaughtExceptionHandler defaultHandler){
			this.defaultHandler = defaultHandler;
		}

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			this.dump(ex);
			if(this.defaultHandler != null){
				this.defaultHandler.uncaughtException(thread, ex);
			}
			System.exit(0);
		}
		
		private void dump(Throwable ex){
			PrintWriter pw = null;
			try {		  
				ex.printStackTrace(pw);
				pw.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(pw != null){
					pw.close();
				}
			}
		}
	}
}
