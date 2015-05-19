package com.cube.sdk.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.cube.sdk.log.FSLogcat;
import com.cube.sdk.log.FSLogger;
import com.cube.sdk.log.FSLogger.LT;
import com.cube.sdk.util.FSFile;
import com.cube.sdk.util.FSString;
import com.funshion.http.FSHttp;
import com.funshion.http.FSHttpException;
import com.funshion.http.FSHttpHandler;
import com.funshion.http.FSHttpParams;
import com.funshion.http.FSHttpRequest;
import com.funshion.http.FSHttpResponse;
import com.funshion.video.config.FSApp;
import com.funshion.video.config.FSConfig;
import com.funshion.video.config.FSConfig.ConfigID;
import com.funshion.video.config.FSDirMgmt;
import com.funshion.video.config.FSDirMgmt.WorkDir;
import com.funshion.video.config.FSPreference;
import com.funshion.video.config.FSPreference.PrefID;

public class FSCacheRules {
	private final static String TAG = "FSCacheRules";
	
	//local cache file name
	private final String LOCAL_RULE_FILE = "cache.rules";
	
	//local rule file absolute path
	private String localRuleFilePath;
	
	//rule object, default no rule
	private Rules rules; 
	
	
	public void init(Context context){
		try{
			localRuleFilePath = FSDirMgmt.getInstance().getPath(WorkDir.CONFIG)+"/"+LOCAL_RULE_FILE;
			//local rule file object
			File fileRule = new File(localRuleFilePath);
			if(!fileRule.exists() || fileRule.length() == 0){	
				//local rule file not exist, create default rule file
				rules = useDefaultRuleFile(context);
			}else{
				//initialized rules from local rule file
				String content = FSFile.read(fileRule);
				rules = (Rules) JSON.parseObject(content, Rules.class);
			}
			
			//check if need to update the configure file
			long lastUpdateTM = FSPreference.getInstance().getLong(PrefID.PREF_CACHE_RULES_LAST_UPDATE_TIME);
			if(System.currentTimeMillis() - lastUpdateTM > FSConfig.getInstance().getLong(ConfigID.CACHE_RULE_UPDATE_TIME_LIMIT)){
				updateLocalRuleFile(localRuleFilePath);
				FSPreference.getInstance().putLong(PrefID.PREF_CACHE_RULES_LAST_UPDATE_TIME, System.currentTimeMillis());
			}
		}catch(Exception e){
			FSLogcat.d(TAG, e.getMessage());
			if(rules == null){
				rules = new Rules();
			}
		}
	}
	
	private Rules useDefaultRuleFile(Context context){
		
		InputStream is = null;
		try {
			AssetManager am = context.getAssets();
			is = am.open(LOCAL_RULE_FILE);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[16*1024];
			int rdsz = is.read(buffer);
			while(rdsz != -1){
				baos.write(buffer, 0, rdsz);
				rdsz = is.read(buffer);
			}
			is.close();
			
			String content = new String(baos.toByteArray());
			
			return (Rules)JSON.parseObject(content, Rules.class);
		} catch (Exception e) {
			FSLogcat.d(TAG, e.getMessage());
		} finally{
			try{
				if(is != null)
					is.close();
			}catch(Exception e){
				FSLogcat.d(TAG, e.getMessage());
			}
		}
		//use empty rules, means no cache
		return new Rules();
	}
	
	private void updateLocalRuleFile(String fpath){
		try {
			
			String remoteFile = FSConfig.getInstance().getString(ConfigID.URL_UPDATE_CACHE_RULES)+"?"
							   +FSHttpParams.newParams().put("cl", FSApp.getInstance().getType())
							   							.put("ve", FSApp.getInstance().getVersion()).encode();

			String localFile = this.LOCAL_RULE_FILE+".tmp";
			FSHttp.defaultHttpClient().get(remoteFile, FSDirMgmt.getInstance().getPath(WorkDir.CONFIG), localFile,	false,	new RuleUpdateHandler(this));
			
			FSLogcat.d(TAG, "query remote cache rules config: "+remoteFile);
		} catch (FSHttpException e) {
			FSLogcat.d(TAG, e.getMessage());
		}
	}
	
	private void replaceLocalRuleFile(String path){
		File remoteNew = new File(path);
		File localOld = new File(this.localRuleFilePath);
		try{
			if(!remoteNew.exists()){
				return;
			}
			
			//parse the content to verify
			Rules rules = (Rules) JSON.parseObject(FSFile.read(remoteNew), Rules.class);
			if(rules.getRules().size() == 0){
				remoteNew.delete();
				return;
			}else{
				if(localOld.exists()){
					localOld.delete();
				}
				//replace old cache rule file
				remoteNew.renameTo(localOld);
			}			
			
		}catch(Exception e){
			try{
				if(remoteNew.exists()){
					remoteNew.delete();
				}
			}catch(Exception e1){}
			FSLogcat.d(TAG, e.getMessage());
		}
	}
	
	public boolean needCache(String url){
		return rules.match(url);
	}
	
	public Rule getRule(String url){
		return rules.getRule(url);
	}
	
	public static class Rules{
		private List<Rule> rules = new ArrayList<Rule>();
		public List<Rule> getRules() {
			return rules;
		}
		public void setRules(List<Rule> rules) {
			this.rules = rules;
		}
		
		public Rule getRule(String url){
			if(url == null || url.equals(""))
				return null;
			Iterator<Rule> iter = rules.iterator();
			while(iter.hasNext()){
				Rule rule = iter.next();
				if(rule.match(url)){
					return rule;
				}
			}
			
			return null;
		}
		
		public boolean match(String url){
			if(url == null || url.equals(""))
				return false;
			Iterator<Rule> iter = rules.iterator();
			while(iter.hasNext()){
				Rule rule = iter.next();
				if(rule.match(url))
					return true;
			}
			
			return false;
		}
	}

	public static class Rule{
		private String pattern;
		private long expireInMillis;
		private boolean strong;
		
		public boolean match(String url){
			if(url == null)
				return false;
			
			return url.matches(this.pattern);
		}

		public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		public long getExpireInMillis() {
			return expireInMillis;
		}

		public void setExpireInMillis(long expireInMillis) {
			this.expireInMillis = expireInMillis;
		}

		public boolean isStrong() {
			return strong;
		}

		public void setStrong(boolean strong) {
			this.strong = strong;
		}
		
	}
	
	public static class RuleUpdateHandler extends FSHttpHandler{
		private FSCacheRules cacheRules;
		
		public RuleUpdateHandler(FSCacheRules cacheRules){
			this.cacheRules = cacheRules;
		}
		
		@Override
		public void onError(FSHttpRequest req, String err) {
			FSLogger.getInstance().loge(LT.CACHERULE_UPDATE, "cache rule update: "+req.getUrlString()+" failed, error: "+FSString.wrap(err));
		}

		@Override
		public void onFailed(FSHttpRequest req, FSHttpResponse resp) {
			FSLogger.getInstance().logf(LT.CACHERULE_UPDATE, "cache rule update: "+req.getUrlString()+" failed, time used: "+resp.getTimeUsed()+"ms, error: "+FSString.wrap(resp.getMsg()));
		}

		@Override
		public void onSuccess(FSHttpRequest req, FSHttpResponse resp) {
			try {
				cacheRules.replaceLocalRuleFile(resp.getLocalFile());
				FSLogger.getInstance().logs(LT.CACHERULE_UPDATE, "cache rule update: "+req.getUrlString()+" success, time used: "+resp.getTimeUsed()+"ms");
			} catch (Exception e) {
				FSLogger.getInstance().logs(LT.CACHERULE_UPDATE, "cache rule update: "+req.getUrlString()+" success, time used: "+resp.getTimeUsed()+"ms, error: "+FSString.wrap(e.getMessage()));
			}
		}

		@Override
		public void onRetry(FSHttpRequest req, String reason) {
			FSLogger.getInstance().logi(LT.CACHERULE_UPDATE, "cache rule update: "+req.getUrlString()+" retry, reason: "+FSString.wrap(reason));
		}
	}
}
