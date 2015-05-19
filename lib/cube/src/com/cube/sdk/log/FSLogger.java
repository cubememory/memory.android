package com.cube.sdk.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;

import com.cube.sdk.das.FSDas;
import com.cube.sdk.das.FSDasReq;
import com.cube.sdk.das.FSHandler;
import com.cube.sdk.task.FSExecutor;
import com.cube.sdk.task.FSTask;
import com.cube.sdk.task.FSTimer;
import com.cube.sdk.util.FSDevice;
import com.cube.sdk.util.FSDir;
import com.cube.sdk.util.FSFile;
import com.cube.sdk.util.FSString;
import com.funshion.http.FSHttp;
import com.funshion.http.FSHttpHandler;
import com.funshion.http.FSHttpRequest;
import com.funshion.http.FSHttpResponse;
import com.funshion.video.config.FSApp;
import com.funshion.video.config.FSConfig;
import com.funshion.video.config.FSConfig.ConfigID;
import com.funshion.video.config.FSDirMgmt;
import com.funshion.video.config.FSDirMgmt.WorkDir;
import com.funshion.video.entity.FSUploadLogEntity;

public class FSLogger {
	public static interface LogTransmit{
		public void transmit(String msg);
	}
	
	/*log file prefix*/
	private final String FILE_PREFIX = "funshion";
	/*flush the cached messge to file when records limit reached*/
	private final int FLUSH_RECORDS_LIMIT = 50;
	private final long CLEAN_DAYS_AGO = 3;
	private final long MILLION_SEDS_ADAY = 24*60*60*1000L; 
	/*list for cache messages*/
	private List<String> lstMsg = new ArrayList<String>();
	
	/*log transmit list*/
	private List<LogTransmit> transmits = new ArrayList<LogTransmit>();
	
	private static FSLogger instance;
	
	private FSLogger(){
		
	}
	
	public static FSLogger getInstance(){
		if(instance == null){
			instance = new FSLogger();
		}
		return instance;
	}
	
	public void init(){		
		/*clear the old logs*/
		this.clean();
		
		/*start the timer flush task*/
		this.initFlushTimer();
	}
		
	public void initFlushTimer(){
		try{
			FSTimer.getInstance().schedule(new FlushTask(this), 10000, 60000);
		}catch(Exception e){}
	}
	
	public void addLogTransmit(LogTransmit lt){
		synchronized(transmits){
			try{
				transmits.add(lt);
			}catch(Exception e){}
		}
	}
	
	public void transmitLog(String msg){
		synchronized(transmits){
			try{
				for(LogTransmit lt: transmits){
					lt.transmit(msg);
				}
			}catch(Exception e){}
		}		
	}
	
	/**
	 * only upload the latest file and not more than CLEAN_DAYS_AGO
	 */
	public void upload(){
		try{
			FSDas.getInstance().get(FSDasReq.PO_UPLOAD_LOG, null, new FSHandler(this){
				@Override
				public void onSuccess(SResp sresp) {
					try{
						FSUploadLogEntity logEntity = (FSUploadLogEntity)sresp.getEntity();
						String isUpload = logEntity.getUpload();
						if(isUpload != null && isUpload.equals("1")){
							/*upload log after 30 seconds*/
							FSExecutor.getInstance().submit(new UploadTask(), 30);
						}
						
					}catch(Exception e){
					}
				}

				@Override
				public void onFailed(EResp eresp) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}catch(Exception e){
		}
	}	
	
	public synchronized void logi(LT tag, String msg){
		this.log(tag, LL.INFO, msg);
	}
	
	public synchronized void loge(LT tag, String msg){
		this.log(tag, LL.ERROR, msg);
	}
	
	public synchronized void logd(LT tag, String msg){
		this.log(tag, LL.DEBUG, msg);
	}
	
	public synchronized void logf(LT tag, String msg){
		this.log(tag, LL.FAILED, msg);
	}
	
	public synchronized void logs(LT tag, String msg){
		this.log(tag, LL.SUCCESS, msg);
	}
	
	
	private void log(LT tag, LL level, String msg){
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("[").append(level.getName()).append("]")
			  .append("[").append(tag.getName()).append("]")
			  .append("[").append(FSApp.getInstance().getNetTypeName()).append("]")
			  .append("[").append(this.currentLogMsgTime()).append("]")
			  .append(msg).append("\n");
			
			lstMsg.add(sb.toString());
			
			//transmit a copy to p2p kernel
			this.transmitLog(sb.toString());
					
			if(lstMsg.size() > FLUSH_RECORDS_LIMIT){
				this.flushi();
			}
			
			FSLogcat.d(tag.getName(), msg);
		}catch(Exception e){}
	}
	
	public synchronized void flush(){
		this.flushi();
	}
	
	public void destory(){
		this.logi(LT.ACTION, "terminated.");
		this.flush();
	}
	
	private void flushi(){
		if(lstMsg.size() == 0){
			return;
		}
		
		FileOutputStream fos = null;		
		try{
			FSDir.createDirs( FSDirMgmt.getInstance().getPath(WorkDir.LOG));
			File logFile = new File(FSDirMgmt.getInstance().getPath(WorkDir.LOG)+"/"+this.currentLogFileName());
			
			boolean newLogFile = true;
			if(logFile.exists()){
				newLogFile = false;
			}
			
			fos = new FileOutputStream(logFile, true);
			if(newLogFile){
				fos.write(this.getPublicLogHeader().getBytes(Charset.defaultCharset()));
			}
			
			for(String msg: lstMsg){
				fos.write(msg.getBytes(Charset.defaultCharset()));
			}
			fos.flush();
			lstMsg.clear();
		}catch(Exception e){
			
		}finally{
			if(fos != null){
				try{
					fos.close();
				}catch(Exception e){}
			}
		}
		
	}
	
	private void clean(){
		try{
			File logDir = new File(FSDirMgmt.getInstance().getPath(WorkDir.LOG));
			if(logDir.exists()){
				File[] logFiles = logDir.listFiles(new LogFileNameFilter());
				if(logFiles != null){
					for(File logFile: logFiles){
						if(logFile.isFile() && System.currentTimeMillis()-logFile.lastModified()>CLEAN_DAYS_AGO*MILLION_SEDS_ADAY){
							logFile.delete();
						}
					}
				}
			}
		}catch(Exception e){}
	}
	
	private String currentLogFileName(){
		String time = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
			time = sdf.format(Calendar.getInstance(Locale.getDefault()).getTime());
		}catch(Exception e){
			time = FSString.randomLongString();
		}
		return FILE_PREFIX+"_"+FSApp.getInstance().getType()+"_"+FSApp.getInstance().getVersion()+"_"+FSApp.getInstance().getMac()+"_"+time+".log";
	}

	private String currentLogMsgTime(){
		String time = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
			time = sdf.format(Calendar.getInstance(Locale.getDefault()).getTime());
		}catch(Exception e){}
		
		return time;
	}
	
	private String getPublicLogHeader(){
		StringBuilder sb = new StringBuilder();
		sb.append("DeviceType:"+FSDevice.OS.getModel()+"\t")
		  .append("SDKVersion:"+FSDevice.OS.getVersion()+"\t")
		  .append("Brand:"+FSDevice.OS.getBrand()+"\t")
		  .append("Mac:"+FSApp.getInstance().getMac()+"\t")
		  .append("Type:"+FSApp.getInstance().getType()+"\t")
		  .append("Version:"+FSApp.getInstance().getVersion()+"\n");
		return sb.toString();
	}
	
	private class FlushTask extends TimerTask{
		private FSLogger logger;
		
		public FlushTask(FSLogger logger){
			this.logger = logger;
		}
		
		@Override
		public void run() {
			try{
				if(this.logger != null){
					this.logger.flush();
				}
			}catch(Exception e){}
		}	
	}
	
	private class UploadTask extends FSTask{
		@Override
		public void proc() {
			try{		
				File logDir = new File(FSDirMgmt.getInstance().getPath(WorkDir.LOG));
				if(!logDir.exists()){
					return;
				}
				
				File[] logFiles = logDir.listFiles(new LogFileNameFilter());
				if(logFiles == null){
					return;
				}
					
				File latestLogFile = null;
				for(File logFile: logFiles){
					if(!logFile.isFile() || this.date(logFile.lastModified()).equals(this.today())){
						continue;
					}
					
					if(latestLogFile == null){
						latestLogFile = logFile;
					}else{
						if(logFile.lastModified() > latestLogFile.lastModified()){
							latestLogFile = logFile;
						}
					}
				}
				
				if(latestLogFile != null){					
					File zipLogFile = null;
					if(!latestLogFile.getName().endsWith(".zip")){
						zipLogFile = new File(latestLogFile.getAbsoluteFile()+".zip");
						FSFile.zip(latestLogFile, zipLogFile);
						latestLogFile.delete();
					}else{
						zipLogFile = latestLogFile;
					}
					
					if(zipLogFile.exists()){
						String remotePath = FSConfig.getInstance().getString(ConfigID.URL_POST_LOG_FILE);
						FSHttp.defaultHttpClient().post(remotePath, zipLogFile.getAbsolutePath(), new FSHttpHandler(zipLogFile){
							@Override
							public void onError(FSHttpRequest arg0, String arg1) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onFailed(FSHttpRequest arg0, FSHttpResponse arg1) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onRetry(FSHttpRequest req, String reason) {
								
							}

							@Override
							public void onSuccess(FSHttpRequest arg0, FSHttpResponse arg1) {
								try{
									File file = (File)this.obj;
									file.delete();
								}catch(Exception e){}
							}
							
						});
					}
				}
				
			}catch(Exception e){}
		}
		
		private String today(){
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
				return sdf.format(Calendar.getInstance(Locale.getDefault()).getTime());			
			}catch(Exception e){
				return "";
			}
		}
		
		private String date(long time){
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
				return sdf.format(new Date(time));			
			}catch(Exception e){
				return "";
			}	
		}
	}
	
	private class LogFileNameFilter implements FilenameFilter{
		@Override
		public boolean accept(File dir, String filename) {
			if(filename.startsWith(FILE_PREFIX)){
				return true;
			}
			return false;
		}
		
	}
	
	/**
	 * log type
	 * @author wangrb
	 *
	 */
	public enum LT{
		DAS("das"),
		NORMAL("normal"),
		REPORT("report"),
		ACTION("action"),
		CONFIG_UPDATE("cfgupdate"),
		CACHERULE_UPDATE("crupdate"),
		AD_LOAD_PRELOAD("adpreload"),
		AD_LOAD_STRATEGY("adloads"),
		AD_LOAD_MATERIAL("adloadm"),
		AD_REPORT("adreport");
		
		private String name;
		
		private LT(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
	}
	
	/**
	 * log level
	 * @author wangrb
	 *
	 */
	public enum LL{
		INFO("info"),
		ERROR("error"),
		FAILED("failed"),
		SUCCESS("success"),
		DEBUG("debug");
		
		private String name;
		
		private LL(String name){
			this.name = name;
		}
		
		public String getName(){
			return this.name;
		}
	}
}
