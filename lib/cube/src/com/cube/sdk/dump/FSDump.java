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

import com.cube.sdk.log.FSLogcat;
import com.cube.sdk.task.FSExecutor;
import com.cube.sdk.task.FSTask;
import com.cube.sdk.util.FSDevice;
import com.cube.sdk.util.FSDir;
import com.cube.sdk.util.FSFile;
import com.cube.sdk.util.FSDevice.OS;
import com.funshion.http.FSHttp;
import com.funshion.http.FSHttpHandler;
import com.funshion.http.FSHttpRequest;
import com.funshion.http.FSHttpResponse;
import com.funshion.video.config.FSApp;
import com.funshion.video.config.FSConfig;
import com.funshion.video.config.FSConfig.ConfigID;
import com.funshion.video.config.FSDirMgmt;
import com.funshion.video.config.FSDirMgmt.WorkDir;

public class FSDump {
	private final static String TAG = "FSDump";
	private static FSDump instance = null;
	private DumpHandler dumpHandler = null;
	
	private FSDump(){	
	}

	public static FSDump getInstance(){
		if(instance == null){
			instance = new FSDump();
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
	
	public void upload(){
		/*upload old dump files for delay several seconds*/
		FSExecutor.getInstance().submit(new UploadTask(), 10);
	}	
	
	public void destroy(){
		this.dumpHandler = null;
	}
		
	private static class UploadTask extends FSTask{

		@Override
		public void proc() {
			try{
				/*clean the expired dump files*/
				this.cleanExpireDumps();
				
				/*process the p2p dump file*/
				this.processP2PDump();
				
				/*process the ffmpeg dump file*/
				this.processFFMpegDump();
				
				/*upload each dump file to remote server*/
				String remote = FSConfig.getInstance().getString(ConfigID.URL_POST_DUMP_FILE);
				
				/*upload the app dump file*/
				File dumpDir = new File(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_APP));
				File[] dumpFiles = dumpDir.listFiles();
				if(dumpFiles != null){
					for(File file: dumpFiles){
						if(file.isFile() && this.isDumpFile(file.getName())){
							FSHttp.defaultHttpClient().post(remote, file.getAbsolutePath(), new UploadHandler(file));
						}
					}
				}
				
				/*upload the p2p dump file*/
				dumpDir = new File(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_P2P));
				dumpFiles = dumpDir.listFiles();
				if(dumpFiles != null){
					for(File file: dumpFiles){
						if(file.isFile() && this.isDumpFile(file.getName())){
							FSHttp.defaultHttpClient().post(remote, file.getAbsolutePath(), new UploadHandler(file));
						}
					}
				}

				/*upload the ffmpeg dump file*/
				dumpDir = new File(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_FFMPEG));
				dumpFiles = dumpDir.listFiles();
				if(dumpFiles != null){
					for(File file: dumpFiles){
						if(file.isFile() && this.isDumpFile(file.getName())){
							FSHttp.defaultHttpClient().post(remote, file.getAbsolutePath(), new UploadHandler(file));
						}
					}
				}
			}catch(Exception e){
				
			}
		}
		
		private void cleanExpireDumps(){
			try{
				final long expireTimeLimit = FSConfig.getInstance().getLong(ConfigID.DUMP_FILE_EXPIRE_TIME); 
				
				FSDir.clear(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_APP), null, expireTimeLimit);
				FSDir.clear(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_P2P), null, expireTimeLimit);
				FSDir.clear(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_FFMPEG), null, expireTimeLimit);
				
			}catch(Exception e){
				
			}
		}
		
		private void processP2PDump(){
			try{
				File dumpDir = new File(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_P2P));
				File[] p2pDumpFiles = dumpDir.listFiles(new FilenameFilter(){
															@Override
															public boolean accept(File dir, String filename) {
																try{
																	if(filename.toLowerCase(Locale.getDefault()).endsWith(".dmp")){
																		return true;
																	}
																}catch(Exception e){}
																return false;
															}
														});
				for(File p2pDumpFile: p2pDumpFiles){
					/*create new zipped file name*/
					FSDir.createDirs(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_P2P));
					
					String dumpFileName = DumpUtil.generateP2PFileName();
					String dumpFullPath = FSDirMgmt.getInstance().getPath(WorkDir.DUMP_P2P)+"/"+dumpFileName;
					File zipDumpFile = new File(dumpFullPath+".zip");
					
					/*compress each file*/
					boolean zipSucc = FSFile.zip(p2pDumpFile, zipDumpFile);
					
					/*delete the uncompressed file*/
					if(zipSucc){
						p2pDumpFile.delete();
					}
				}
			}catch(Exception e){
				
			}
		}
		
		private void processFFMpegDump(){
			try{
				File dumpDir = new File(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_FFMPEG));
				File[] ffmpegDumpFiles = dumpDir.listFiles(new FilenameFilter(){
															@Override
															public boolean accept(File dir, String filename) {
																try{
																	if(filename.toLowerCase(Locale.getDefault()).endsWith(".dmp")){
																		return true;
																	}
																}catch(Exception e){}
																return false;
															}
														});
				for(File ffmpegDumpFile: ffmpegDumpFiles){
					/*create new zipped file name*/
					FSDir.createDirs(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_FFMPEG));
					
					String dumpFileName = DumpUtil.generateFFMpegFileName();
					String dumpFullPath = FSDirMgmt.getInstance().getPath(WorkDir.DUMP_FFMPEG)+"/"+dumpFileName;
					File zipDumpFile = new File(dumpFullPath+".zip");
					
					/*compress each file*/
					boolean zipSucc = FSFile.zip(ffmpegDumpFile, zipDumpFile);
					
					/*delete the uncompressed file*/
					if(zipSucc){
						ffmpegDumpFile.delete();
					}
				}
			}catch(Exception e){
				
			}
		}
		
		private boolean isDumpFile(String fileName){
			if(fileName.startsWith("crash_")){
				return true;
			}
			
			return false;
		}
	}
	
	private static class UploadHandler extends FSHttpHandler{
		public UploadHandler(Object obj){
			super(obj);
		}
	
		@Override
		public void onError(FSHttpRequest req, String errMsg) {
			try{
				FSLogcat.e(TAG, errMsg);
			}catch(Exception e){}
		}

		@Override
		public void onFailed(FSHttpRequest req, FSHttpResponse resp) {
			try{
				FSLogcat.e(TAG, resp.getMsg());
			}catch(Exception e){}
		}

		@Override
		public void onSuccess(FSHttpRequest req, FSHttpResponse resp) {
			try{
				File file = (File)this.obj;
				if(file.exists()){
					file.delete();
				}
			}catch(Exception e){
				FSLogcat.e(TAG, e.getMessage());
			}
		}

		@Override
		public void onRetry(FSHttpRequest req, String reason) {
			FSLogcat.e(TAG, reason);
		}
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
				FSDir.createDirs(FSDirMgmt.getInstance().getPath(WorkDir.DUMP_APP));
				
				String dumpFileName = DumpUtil.generateAppFileName();
				String dumpFullPath = FSDirMgmt.getInstance().getPath(WorkDir.DUMP_APP)+"/"+dumpFileName;
				File dumpFile = new File(dumpFullPath+".zip");
				
				ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dumpFile));
				zos.putNextEntry(new ZipEntry(dumpFileName));
				pw = new PrintWriter(zos);
				pw.append("DeviceType:"+FSDevice.OS.getModel()+"\t")
				  .append("SDKVersion:"+FSDevice.OS.getVersion()+"\t")
				  .append("Brand:"+FSDevice.OS.getBrand()+"\t")
				  .append("Mac:"+FSApp.getInstance().getMac()+"\t")
				  .append("Type:"+FSApp.getInstance().getType()+"\t")
				  .append("Version:"+FSApp.getInstance().getVersion()+"\t")
				  .append("SID:"+FSApp.getInstance().getSid()+"\n");
				  
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
	
	private static class DumpUtil{
		public static String generateAppFileName(){			
			String fileName = "crash_"+FSApp.getInstance().getType()+"_"
							  +FSApp.getInstance().getVersion()+"_"
							  +FSApp.getInstance().getMac()+"_"
							  +DumpUtil.currentTime()
							  +".app.crash";
			
			
			return fileName;
		}

		public static String generateP2PFileName(){			
			String fileName = "crash_"+FSApp.getInstance().getType()+"_"
							  +FSApp.getInstance().getVersion()+"_"
							  +FSApp.getInstance().getMac()+"_"
							  +DumpUtil.currentTime()
							  +".p2p.dmp";
			
			
			return fileName;
		}

		public static String generateFFMpegFileName(){			
			String fileName = "crash_"+FSApp.getInstance().getType()+"_"
							  +FSApp.getInstance().getVersion()+"_"
							  +FSApp.getInstance().getMac()+"_"
							  +DumpUtil.currentTime()
							  +".ffmpeg.dmp";
			
			
			return fileName;
		}
		
		public static String currentTime(){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
			return sdf.format(Calendar.getInstance(Locale.getDefault()).getTime());
			
		}
	}
}
