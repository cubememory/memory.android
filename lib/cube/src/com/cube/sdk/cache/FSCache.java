package com.cube.sdk.cache;

import android.content.Context;

public abstract class FSCache {
	/*singleton instance of cache*/
	private static FSCache cache;
	
	public static FSCache getInstance(){
		if(cache == null){
			cache = new FSCacheImpl();
		}
		return cache;
	}
	
	/**
	 * initialize the cache object
	 */
	public abstract void init(Context context);
	
	/**
	 * access the url and 
	 * @param url
	 */
	public abstract void preload(String url);

	/**
	 * put url content to cache
	 * @param url
	 * @param content
	 */
	public abstract void put(String url, String content);
	
	/**
	 * get url content from cache asynchronized
	 * @param url
	 * @param handler
	 * @return
	 * true if hit the cache, otherwise false returned
	 */
	public abstract boolean get(String url, FSCacheHandler handler);
	
	
	/**
	 * cache configure
	 * @author wangrb
	 *
	 */
	public static class CacheCfg{
		private String cacheDir;
		private String cfgDir;
		private long ruleUpdateInMillisLimit;
		
		public CacheCfg(String cacheDir, String cfgDir, long ruleUpdateInMillisLimit){
			this.cacheDir = cacheDir;
			this.cfgDir = cfgDir;
			this.ruleUpdateInMillisLimit = ruleUpdateInMillisLimit;
		}

		public String getCacheDir() {
			return cacheDir;
		}

		public void setCacheDir(String cacheDir) {
			this.cacheDir = cacheDir;
		}

		public String getCfgDir() {
			return cfgDir;
		}

		public void setCfgDir(String cfgDir) {
			this.cfgDir = cfgDir;
		}

		public long getRuleUpdateInMillisLimit() {
			return ruleUpdateInMillisLimit;
		}

		public void setRuleUpdateInMillisLimit(long ruleUpdateInMillisLimit) {
			this.ruleUpdateInMillisLimit = ruleUpdateInMillisLimit;
		}
	}
}

