package com.cube.sdk.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class CHttpFileGetTask extends CHttpTask{
	private boolean append = false;
	private long appendPos = -1;

	public CHttpFileGetTask(String remoteFile, String localDir, int maxRetryCount, CHttpHandler handler)	throws CHttpException {
		super(remoteFile, localDir, maxRetryCount, handler);
	}
	
	public CHttpFileGetTask(String remoteFile, String localDir, String fileName, boolean append, int maxRetryCount, CHttpHandler handler) throws CHttpException {
		super(remoteFile, localDir, fileName, maxRetryCount, handler);
		this.append = append;
		if(this.append){
			File f = new File(super.request.getLocalFile());
			if(f.exists() && f.isFile()){
				this.appendPos = f.length();
			}
		}
	}
	
	protected void request() throws CHttpException{
		//the response content reading input stream
		InputStream in = null;
		//the file output stream
		FileOutputStream fos = null;
		
		try{
			//record the request start time point
			long startTime = System.currentTimeMillis();
			
			//prepare the url connection
			URL url = new URL(request.getUrlString());
			
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setInstanceFollowRedirects(true);
			conn.setConnectTimeout(CHttpCfg.CONNECT_TIMEOUT);
			conn.setReadTimeout(CHttpCfg.READ_TIMEOUT);
			
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			conn.setRequestProperty("User-Agent", "funshion app/2.0");
			conn.setRequestProperty("Connection", "Close");
			
			if(this.append && this.appendPos>0){
				conn.setRequestProperty("Range", "bytes="+this.appendPos+"-");
			}
			
			//get the response code and message
			int code = conn.getResponseCode();
			String msg = conn.getResponseMessage();
			String encoding = conn.getContentEncoding();
			Map<String, List<String>> headers = conn.getHeaderFields();
			
			if(code>=200 && code<300){			
				//check the response partial content
				if(this.append && code != 206){
					this.append = false;
				}
				
	
				//get the file name of the request file
				String fileName = super.request.getFileName();
				if(fileName == null){
					fileName = getFileNameFromCD(conn.getHeaderField("Content-Disposition"));
					if(fileName == null){
						fileName = getFileNameFromPath(super.request.getPath());
						if(fileName == null){
							fileName = getUrlMD5(super.request.getUrlString());
						}
					}
				}
				
				//read the response content
				if(encoding!=null && encoding.toLowerCase().matches("gzip")){
					in = new GZIPInputStream(new BufferedInputStream(conn.getInputStream()));
				}else{
					in = new BufferedInputStream(conn.getInputStream());
				}
				
				//output local file path
				String filePath = super.request.getLocalDir()+File.separator+fileName;
				
				//open the out put file stream
				fos = new FileOutputStream(filePath, this.append);
				
				//for read response content from remote host
				byte[] buf = new byte[CHttpCfg.SIZE_READ_BUFFER];
				
				//read the response content
				int sz = in.read(buf);
				while(sz != -1){
					fos.write(buf, 0, sz);
					sz = in.read(buf);
				}
				
				//record the request end time point
				long endTime = System.currentTimeMillis();
				
				//make the response object
				this.response = new CHttpResponse(code, msg, headers, filePath, endTime-startTime);
			}else{
				//record the request end time point
				long endTime = System.currentTimeMillis();
				this.response = new CHttpResponse(code, msg, headers, "", endTime-startTime);
			}
			
		}catch(Exception e){
			throw new CHttpException(e.getMessage());
		}finally{
			try{
				if(in != null){
					in.close();
				}
				
				if(fos != null){
					fos.close();
				}
				
				//close the connection
				conn.disconnect();
			}catch(Exception e){}
		}
	}
	
	private String getFileNameFromCD(String cd){
		if(cd == null || cd.equals(""))
			return null;
		
		Pattern pattern = Pattern.compile("filename=\"*([^;\"]+)");
		Matcher matcher = pattern.matcher(cd);
		if(matcher.find()){
			return matcher.group(1);
		}
		
		return null;
	}
	
	private String getFileNameFromPath(String path){
		if(path == null || path.equals(""))
			return null;
		
		Pattern pattern = Pattern.compile("([^/\\\\]+)$");
		Matcher matcher = pattern.matcher(path);
		if(matcher.find()){
			return matcher.group(1);
		}
		
		return null;
	}
	
	private String getUrlMD5(String url){
		if(url == null || url.equals(""))
			return null;
		
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(url.getBytes());
			byte[] res = md.digest();
			return byte2hex(res);
		}catch(Exception e){
			return null;
		}
	}
	
	private static String byte2hex(byte[] data){
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<data.length; i++){
			int high = (data[i]>>4)&0x0F;
			int low = data[i]&0x0F;
			
			sb.append(Integer.toHexString(high));
			sb.append(Integer.toHexString(low));
		}
		
		return sb.toString();
	}
}
