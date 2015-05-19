package com.cube.sdk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.cube.sdk.log.FSLogcat;
import com.funshion.video.exception.FSException;

public class FSFile {
	private final static String TAG = "FSFile";
		
	public static String read(String fpath) throws FSException{
		return read(fpath, Charset.defaultCharset().name());
	}
	
	public static String read(String fpath, String charset) throws FSException{
		return read(new File(fpath), charset);
	}
	
	public static String read(File file) throws FSException{
		return read(file, Charset.defaultCharset().name());
	}
	
	public static String read(File file, String charset) throws FSException{
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[16*1024];
			int rdsz = fis.read(buffer);
			while(rdsz != -1){
				baos.write(buffer, 0, rdsz);
				rdsz = fis.read(buffer);
			}
			fis.close();
			
			return new String(baos.toByteArray(), charset);
		}catch(Exception e){
			throw new FSException(e.getMessage());
		}finally{
			try{
				if(fis != null)
					fis.close();
			}catch(Exception e){
				FSLogcat.d(TAG, e.getMessage());
			}
		}
	}
	
	public static void write(String fpath, String content) throws FSException{
		write(fpath, content, Charset.defaultCharset().name());
	}
	
	public static void write(String fpath, String content, String charset) throws FSException{
		write(new File(fpath), content, charset);
	}
	
	public static void write(File file, String content) throws FSException{
		write(file, content, Charset.defaultCharset().name());
	}
	
	public static void write(File file, String content, String charset) throws FSException{
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(file);
			fos.write(content.getBytes(charset));
			fos.close();
		}catch(Exception e){
			throw new FSException(e.getMessage());
		}finally{
			try{
				if(fos != null)
					fos.close();
			}catch(Exception e){
				FSLogcat.d(TAG, e.getMessage());
			}
		}		
	}
	
	public static void rename(String from, String to){
		try{
			File fFrom = new File(from);
			File fTo = new File(to);
			if(fFrom.exists() && !fTo.exists()){
				fFrom.renameTo(fTo);
			}
		}catch(Exception e){}
	}
	
	public static void forceRename(String from, String to){
		try{
			File fFrom = new File(from);
			File fTo = new File(to);
			if(fFrom.exists()){
				 if(fTo.exists()){
					 fTo.delete();
				 }
				fFrom.renameTo(fTo);
			}
		}catch(Exception e){}
	}
	
	public static boolean zip(File from, File to){
		FileInputStream fis = null;
		ZipOutputStream zos = null;
		try{
			fis = new FileInputStream(from);
			zos = new ZipOutputStream(new FileOutputStream(to));
			zos.putNextEntry(new ZipEntry(from.getName()));
			
			byte[] buffer = new byte[16*1024];
			int rdsz = fis.read(buffer);
			while(rdsz != -1){
				zos.write(buffer, 0, rdsz);
				rdsz = fis.read(buffer);
			}
			zos.flush();
			return true;
		}catch(Exception e){
			
		}finally{
			try{
				if(fis != null){
					fis.close();
				}
				if(zos != null){
					zos.close();
				}
			}catch(Exception e){}
		}
		return false;
	}
}
