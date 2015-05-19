package com.cube.sdk.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HttpPostTask extends HttpTask{

	public HttpPostTask(String url, int maxRetryCount, HttpHandler handler) throws HttpException{
		super(url, maxRetryCount, handler);
	}

	public HttpPostTask(String hostPath, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		super(hostPath, params, maxRetryCount, handler);
	}
	
	public HttpPostTask(String host, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		super(host, path, params, maxRetryCount, handler);
	}
	
	public HttpPostTask(String host, int port, String path, HttpParams params, int maxRetryCount, HttpHandler handler) throws HttpException{
		super(host, port, path, params, maxRetryCount, handler);
	}
	
	protected void request() throws HttpException{
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
			conn.setConnectTimeout(HttpCfg.CONNECT_TIMEOUT);
			conn.setReadTimeout(HttpCfg.READ_TIMEOUT);
			
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
			byte[] buf = new byte[HttpCfg.SIZE_READ_BUFFER];
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
			this.response = new HttpResponse(code, msg, headers, baos.toByteArray(), endTime-startTime);
			
		}catch(Exception e){
			throw new HttpException(e.getMessage());
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
