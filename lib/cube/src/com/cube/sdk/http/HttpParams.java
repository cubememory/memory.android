package com.cube.sdk.http;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * class for packaging the request parameters.
 */
public class HttpParams{
	//the default url encoding character
	private static String DEFAULT_ENC = "UTF-8";
	//array list for holding the @Param objects 
	private  List<Param> params = new ArrayList<Param>();
	
	public static HttpParams newParams(){
		return new HttpParams();
	}
	
	/**
	 * put a string <key, value> pair value to the request parameters
	 * @param key: parameter key
	 * @param value: parameter value
	 * @return
	 * 	current @RequestParams object
	 */
	public HttpParams put(String key, String value){
		if(key != null){
			if(value == null){
				params.add(new Param(key, ""));
			}else{
				params.add(new Param(key, value));
			}
		}
		return this;
	}
	
	/**
	 * put a string <key, value> pair value to the request parameters
	 * @param key: parameter key
	 * @param value: parameter value
	 * @return
	 * 	current @RequestParams object
	 */
	public HttpParams put(Map<String, String> params){
		if(params == null)
			return this;
		
		Iterator<String> iter = params.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			this.put(key, params.get(key));
		}
		
		return this;
	}
	
	public String get(String key){
		try{
			for(Param param : this.params){
				if(param.getKey().equals(key)){
					return param.getValue();
				}
			}
		}catch(Exception e){}
		
		return null;
	}
	
	public void replace(String key, String value){
		try{
			for(Param param : this.params){
				if(param.getKey().equals(key)){
					param.value = value;
				}
			}
		}catch(Exception e){}
	}
	
	public boolean contain(String key){
		try{
			for(Param param : this.params){
				if(param.getKey().equals(key)){
					return true;
				}
			}
		}catch(Exception e){}
		
		return false;
	}
	
	/**
	 * add the input params to the head of this params
	 * @param params
	 * @return
	 * this
	 */
	public HttpParams mergeToHead(HttpParams params){
		if(params != null)
			this.params.addAll(0, params.getAll());
		return this;
	}
	
	/**
	 * add the input params to the end of this params
	 * @param params
	 * @return
	 * this
	 */
	public HttpParams mergeToEnd(HttpParams params){
		if(params != null)
			this.params.addAll(params.getAll());
		return this;
	}
	
	private List<Param> getAll(){
		return this.params;
	}
	
	/**
	 * encode all the parameters to an request string like: key1=value1&key2=value2&...
	 * @return
	 * 	encoded url
	 * @throws HttpException
	 */
	public String encode() throws HttpException{
		try{
			StringBuilder sb = new StringBuilder();
			Iterator<Param> iter= params.iterator();
			while(iter.hasNext()){
				Param param = iter.next();
				sb.append(URLEncoder.encode(param.getKey(), DEFAULT_ENC));
				sb.append("=");
				sb.append(URLEncoder.encode(param.getValue(), DEFAULT_ENC));
				if(iter.hasNext())
					sb.append("&");
			}
			return sb.toString();
		}catch(Exception e){
			throw new HttpException(e.getMessage());
		}
	}
	
	/**
	 * <key, value> pair for request parameter
	 */
	private static final class Param{
		private String key = null;
		private String value = null;
		
		public Param(String key, String value){
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}
}