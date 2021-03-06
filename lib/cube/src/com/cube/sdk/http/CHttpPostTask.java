package com.cube.sdk.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class CHttpPostTask extends CHttpTask{

	public CHttpPostTask(String url, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		super(url, maxRetryCount, handler);
	}

	public CHttpPostTask(String hostPath, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		super(hostPath, params, maxRetryCount, handler);
	}
	
	public CHttpPostTask(String host, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		super(host, path, params, maxRetryCount, handler);
	}
	
	public CHttpPostTask(String host, int port, String path, CHttpParams params, int maxRetryCount, CHttpHandler handler) throws CHttpException{
		super(host, port, path, params, maxRetryCount, handler);
	}
	
	protected void request() throws CHttpException{
		//the response content reading input stream
		InputStream in = null;
		ByteArrayOutputStream baos = null;
		try{
			//record the request start time point
			long startTime = System.currentTimeMillis();
			
			//prepare the url connection
			URL url = new URL(request.getProtocol(), request.getHost(), request.getPort(), request.getPath());
			
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setInstanceFollowRedirects(true);
			conn.setConnectTimeout(CHttpCfg.CONNECT_TIMEOUT);
			conn.setReadTimeout(CHttpCfg.READ_TIMEOUT);
			
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
			conn.setRequestProperty("User-Agent", "funshion app/2.0");
			conn.setRequestProperty("Connection", "Close");
			String query = request.getQuery(); 
			if(query != null){
				conn.setDoOutput(true);
				conn.getOutputStream().write(query.getBytes());
			}
			
			//get the response code and message
			int code = conn.getResponseCode();
			String msg = conn.getResponseMessage();
			String encoding = conn.getContentEncoding();
			Map<String, List<String>> headers = conn.getHeaderFields();

			//read the response content
			if(encoding!=null && encoding.toLowerCase().matches("gzip")){
				in = new GZIPInputStream(new BufferedInputStream(conn.getInputStream()));
			}else{
				in = new BufferedInputStream(conn.getInputStream());
			}
			
			//for read response content from remote host
			byte[] buf = new byte[CHttpCfg.SIZE_READ_BUFFER];
			baos = new ByteArrayOutputStream();
			
			//read the response content
			int sz = in.read(buf);
			while(sz != -1){
				baos.write(buf, 0, sz);
				sz = in.read(buf);
			}
			
			//record the request end time point
			long endTime = System.currentTimeMillis();
			
			//make the response object
			this.response = new CHttpResponse(code, msg, headers, baos.toByteArray(), endTime-startTime);
			
		}catch(Exception e){
			throw new CHttpException(e.getMessage());
		}finally{
			try{
				if(in != null){
					in.close();
				}
				
				if(baos != null){
					baos.close();
				}
				
				//close the connection
				conn.disconnect();
			}catch(Exception e){}
		}
	}
}
