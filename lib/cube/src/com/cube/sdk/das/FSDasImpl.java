package com.cube.sdk.das;

import android.content.Context;

import com.cube.sdk.cache.FSCache;
import com.cube.sdk.log.FSLogcat;
import com.funshion.http.FSHttp;
import com.funshion.http.FSHttpClient;
import com.funshion.http.FSHttpException;
import com.funshion.http.FSHttpParams;
import com.funshion.video.config.FSApp;
import com.funshion.video.config.FSPreference;
import com.funshion.video.config.FSPreference.PrefID;
import com.funshion.video.exception.FSDasException;

public class FSDasImpl extends FSDas {
	private final static String TAG = "FSDasImpl";
	
	private boolean useCache = true;
	
	/**
	 * public parameters for all request
	 */
	private FSHttpParams publicParams = null;
	/**
	 * http client object
	 */
	private FSHttpClient httpclient = FSHttp.newHttpClient();
	
	@Override
	public void init(Context context) {
		this.publicParams = FSHttpParams.newParams().put("cl", FSApp.getInstance().getType())
													.put("ve", FSApp.getInstance().getVersion())
													.put("mac",FSApp.getInstance().getMac())
													.put("uc", FSPreference.getInstance().getString(PrefID.PREF_UC));
		
		if(FSApp.getInstance().getPartner() != null){
			this.publicParams.put("ptnr", FSApp.getInstance().getPartner());
		}
	}
		
	@Override
	public String get(String url, Class<?> entityClass, FSHandler handler) throws FSDasException {
		/*find the request path*/
		try{
			FSDasHandler dasHandler = new FSDasHandler(handler, entityClass);
			if(this.useCache){
				boolean hit = FSCache.getInstance().get(url, dasHandler);
				if(!hit){
					this.httpclient.get(url, dasHandler);	
				}
			}else{
				this.httpclient.get(url, dasHandler);
			}
			
			FSLogcat.d(TAG, "request: "+url);
			return url;
		}catch(Exception e){
			throw new FSDasException(e.getMessage());
		}
	}
	
	@Override
	public String get(String url, Class<?> entityClass, int maxRetryCount, FSHandler handler) throws FSDasException {
		/*find the request path*/
		try{
			FSDasHandler dasHandler = new FSDasHandler(handler, entityClass);
			if(this.useCache){
				boolean hit = FSCache.getInstance().get(url, dasHandler);
				if(!hit){
					this.httpclient.get(url, maxRetryCount, dasHandler);	
				}
			}else{
				this.httpclient.get(url, maxRetryCount, dasHandler);
			}
			
			FSLogcat.d(TAG, "request: "+url);
			return url;
		}catch(Exception e){
			throw new FSDasException(e.getMessage());
		}
	}
	
	@Override
	public String get(String url, Class<?> entityClass, int maxRetryCount, FSHandler handler, boolean enableCache) throws FSDasException {
		/*find the request path*/
		try{
			FSDasHandler dasHandler = new FSDasHandler(handler, entityClass);
			if(this.useCache && enableCache){
				boolean hit = FSCache.getInstance().get(url, dasHandler);
				if(!hit){
					this.httpclient.get(url, maxRetryCount, dasHandler);	
				}
			}else{
				this.httpclient.get(url, maxRetryCount, dasHandler);
			}
			
			FSLogcat.d(TAG, "request: "+url);
			return url;
		}catch(Exception e){
			throw new FSDasException(e.getMessage());
		}
	}

	@Override
	public void enableCache() {
		this.useCache = true;
	}

	@Override
	public void disableCache() {
		this.useCache = false;
	}
}

