package com.cube.sdk.cookie;

import android.annotation.SuppressLint;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

@SuppressLint("NewApi")
final public class FSCookiePolicy implements CookiePolicy{
	private List<String> acceptUris = null;
	
	public FSCookiePolicy(List<String> acceptUris){
		this.acceptUris = acceptUris;
	}
	
	private boolean acceptUri(URI uri){
		boolean accept = false;
		try{
			if(acceptUris != null && acceptUris.size() != 0){
				for(String regexStr:acceptUris){
					accept = uri.getHost().matches(regexStr);
				}
			}else{
				accept = true;
			}
		}catch(Exception e){
		}
		return accept;
	}
	
	@Override
	public boolean shouldAccept(URI uri, HttpCookie cookie) {
		boolean accept = false;
		try{
			//check if the uri is accept
			if(acceptUri(uri)){
				//only accept cookie from original server
				accept = HttpCookie.domainMatches(cookie.getDomain(), uri.getHost());				
			}
			
		}catch(Exception e){
		}
		
		return accept;
	}

}
