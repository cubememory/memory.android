package com.cube.sdk.cookie;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.cube.sdk.log.FSLogcat;
import com.funshion.video.local.FSLocal;

@SuppressLint("NewApi")
final class FSCookieStore implements CookieStore {
	private class HttpCookieExt{
		private long expires;
		private HttpCookie cookie;
		
		public HttpCookieExt(long expires, HttpCookie cookie){
			this.expires = expires;
			this.cookie = cookie;
		}
		
		public boolean hasExpired(){
			return expires-(System.currentTimeMillis()/1000) <= 0;
		}
		
		public HttpCookie getCookie() {
			return cookie;
		}
		
		@Override
		public boolean equals(Object object) {
	        if (object == this) {
	            return true;
	        }
	        if (object instanceof HttpCookie) {
	            HttpCookie that = (HttpCookie) object;
	            return cookie.equals(that);
	        }else if( object instanceof HttpCookieExt){
	        	HttpCookieExt that = (HttpCookieExt) object;
	            return cookie.equals(that.getCookie());
	        }else{
	        	return false;	
	        }
	    }
	}
	
	private final String TAG = "FSCookieStore";
	
    /** this map may have null keys! */
    private final Map<URI, List<HttpCookieExt>> map = new HashMap<URI, List<HttpCookieExt>>();

    public FSCookieStore(){
    	super();
    	this.load();
    }
    
    private void load(){
    	try{
    		/*first clear the expires cookies*/
    		FSLocal.getInstance().clearCookies(System.currentTimeMillis()/1000);
    		
    		/*then load the valid cookies from persist database*/
    		List<FSDbCookieEntity> cookies = FSLocal.getInstance().getCookies();
    		if(cookies == null){
    			return;
    		}
    		
    		for(FSDbCookieEntity cookie:cookies){
    			long maxAge = cookie.getExpires()-System.currentTimeMillis()/1000;
    			if(maxAge<0){
    				continue;
    			}
    			
    			HttpCookie httpcookie = new HttpCookie(cookie.getName(), cookie.getValue());
    			httpcookie.setComment(cookie.getComment());
    			httpcookie.setCommentURL(cookie.getCommenturl());
    			httpcookie.setDiscard(cookie.isDiscard());
    			httpcookie.setDomain(cookie.getDomain());
    			httpcookie.setMaxAge(maxAge);
    			httpcookie.setPath(cookie.getPath());
    			if(!cookie.getPortlist().equals("")){
    				httpcookie.setPortlist(cookie.getPortlist());	
    			}
    			httpcookie.setSecure(cookie.isSecure());
    			httpcookie.setVersion(cookie.getVersion());
    			
    			URI uri = new URI(cookie.getUri());
    			
    			if(this.map.containsKey(uri)){
    				this.map.get(uri).add(new HttpCookieExt(cookie.getExpires(), httpcookie));
    			}else{
    				List<HttpCookieExt> cookieList = new ArrayList<HttpCookieExt>();
    				cookieList.add(new HttpCookieExt(cookie.getExpires(), httpcookie));
    				this.map.put(uri, cookieList);
    			}
    		}
    		
    	}catch(Exception e){
    		FSLogcat.e(TAG, e.getMessage());
    	}
    }
    
    public synchronized void add(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            throw new NullPointerException("cookie == null");
        }

        uri = cookiesUri(uri);
        
        long expires = cookie.getMaxAge()+System.currentTimeMillis()/1000;
        HttpCookieExt cookieExt = new HttpCookieExt(expires, cookie);
        
        List<HttpCookieExt> cookies = map.get(uri);
        if (cookies == null) {
            cookies = new ArrayList<HttpCookieExt>();
            cookies.add(cookieExt);
            map.put(uri, cookies);
        } else {
        	if(cookies.contains(cookieExt)){
        		cookies.remove(cookieExt);
        		cookies.add(cookieExt);
        	}else{
        		cookies.add(cookieExt);
        	}
        }
        
        /*persist to database*/
        if(cookie.getMaxAge()>0){
        	FSLocal.getInstance().addCookie(new FSDbCookieEntity(uri, cookie));
        }
    }

    private URI cookiesUri(URI uri) {
        if (uri == null) {
            return null;
        }
        try {
            return new URI("http", uri.getHost(), null, null);
        } catch (URISyntaxException e) {
            return uri; // probably a URI with no host
        }
    }

    public synchronized List<HttpCookie> get(URI uri) {
        if (uri == null) {
            throw new NullPointerException("uri == null");
        }

        List<HttpCookie> result = new ArrayList<HttpCookie>();

        // get cookies associated with given URI. If none, returns an empty list
        uri = cookiesUri(uri);
        List<HttpCookieExt> cookiesForUri = map.get(uri);
        if (cookiesForUri != null) {
            for (Iterator<HttpCookieExt> i = cookiesForUri.iterator(); i.hasNext(); ) {
                HttpCookieExt cookie = i.next();
                if (cookie.hasExpired()) {
                    i.remove(); // remove expired cookies
                } else {
                    result.add(cookie.getCookie());
                }
            }
        }

        // get all cookies that domain matches the URI
        for (Map.Entry<URI, List<HttpCookieExt>> entry : map.entrySet()) {
            if (uri.equals(entry.getKey())) {
                continue; // skip the given URI; we've already handled it
            }

            List<HttpCookieExt> entryCookies = entry.getValue();
            for (Iterator<HttpCookieExt> i = entryCookies.iterator(); i.hasNext(); ) {
                HttpCookieExt cookie = i.next();
                if (!HttpCookie.domainMatches(cookie.getCookie().getDomain(), uri.getHost())) {
                    continue;
                }
                if (cookie.hasExpired()) {
                    i.remove(); // remove expired cookies
                } else if (!result.contains(cookie.getCookie())) {
                    result.add(cookie.getCookie());
                }
            }
        }

        return Collections.unmodifiableList(result);
    }

    public synchronized List<HttpCookie> getCookies() {
        List<HttpCookie> result = new ArrayList<HttpCookie>();
        for (List<HttpCookieExt> list : map.values()) {
            for (Iterator<HttpCookieExt> i = list.iterator(); i.hasNext(); ) {
                HttpCookieExt cookie = i.next();
                if (cookie.hasExpired()) {
                    i.remove(); // remove expired cookies
                } else if (!result.contains(cookie.getCookie())) {
                    result.add(cookie.getCookie());
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<URI> getURIs() {
        List<URI> result = new ArrayList<URI>(map.keySet());
        result.remove(null); // sigh
        return Collections.unmodifiableList(result);
    }

    public synchronized boolean remove(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            throw new NullPointerException("cookie == null");
        }

        List<HttpCookieExt> cookies = map.get(cookiesUri(uri));
        if (cookies != null) {
            return cookies.remove(cookie);
        } else {
            return false;
        }
    }

    public synchronized boolean removeAll() {
        boolean result = !map.isEmpty();
        map.clear();
        return result;
    }
}