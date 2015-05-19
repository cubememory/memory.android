package com.cube.sdk.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.cube.sdk.log.FSLogcat;
import com.cube.sdk.util.FSDir;
import com.cube.sdk.util.FSTime;
import com.funshion.video.config.FSDirMgmt;
import com.funshion.video.config.FSDirMgmt.WorkDir;

public class FSCacheFiles {
	private final static String TAG = "FSCacheFiles";
	
	private String cacheDir;
	
	private static FSCacheFiles instance = null;
	
	public static FSCacheFiles getInstance(){
		if(instance == null){
			instance = new FSCacheFiles();
		}
		return instance;
	}
	
	public void init(){
		this.cacheDir = FSDirMgmt.getInstance().getPath(WorkDir.CACHE_DAS);
		FSDir.createDirs(this.cacheDir);
	}
	
	public void write(String url, String content) throws FSCacheException{
		FileOutputStream fos = null;
		try{
			if(!FSDir.createDirs(this.cacheDir)){
				FSLogcat.e(TAG, "create directory "+this.cacheDir+" failed!");
			}
			File cacheFile = new File(this.getFilePath(url));
			fos = new FileOutputStream(cacheFile);
			fos.write(content.getBytes());
			fos.close();
		}catch(Exception e){
			throw new FSCacheException(e.getMessage());
		}finally{
			try{
				if(fos != null)
					fos.close();
			}catch(Exception e){
				FSLogcat.d(TAG, e.getMessage());
			}
		}
	}
	
	public String read(String url) throws FSCacheException{
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(this.getFilePath(url));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[16*1024];
			int rdsz = fis.read(buffer);
			while(rdsz != -1){
				baos.write(buffer, 0, rdsz);
				rdsz = fis.read(buffer);
			}
			fis.close();
			
			return new String(baos.toByteArray());
		}catch(Exception e){
			FSLogcat.d(TAG, e.getMessage());
			throw new FSCacheException(e.getMessage());
		}finally{
			try{
				if(fis != null)
					fis.close();
			}catch(Exception e){
				FSLogcat.d(TAG, e.getMessage());
			}
		}
	}
	
	public boolean isHit(String url){
		File file = new File(this.getFilePath(url));
		if(file.exists() && file.isFile())
			return true;
		return false;
	}
	
	public boolean isExpired(String url, long expireInMillis){
		File file = new File(this.getFilePath(url));
		if(file.exists() && file.isFile()){
			long cachedTime = FSTime.getTimeInMillis()-file.lastModified();
			if(cachedTime < expireInMillis)
				return false;
		}
		return true;
	}
		
	private String getFilePath(String url){
		return this.cacheDir+"/"+FSCacheUtil.getFileName(url);
	}
}
