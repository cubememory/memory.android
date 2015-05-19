package com.cube.sdk.cookie;

import java.net.HttpCookie;
import java.net.URI;
import java.util.Locale;

import com.funshion.video.db.FSDbEntity;

public class FSDbCookieEntity extends FSDbEntity {
	private int record_id;
	private String uri = "";
	private String domain = "";
	private String name = "";
	private String value = "";
	private String path = "";
	private long expires;
	private int discard;//boolean 1 or 0
	private int secure;//boolean 1 or 0
	private int version;
	private String comment = "";
	private String commenturl = "";
	private int httponly;//boolean 1 or 0
	private String portlist = "";
	private long create_tm = System.currentTimeMillis()/1000;
	
	public FSDbCookieEntity(){
		
	}
	
	public FSDbCookieEntity(URI uri, HttpCookie cookie){
		this.setUri(uri.toString());
		this.setComment(cookie.getComment());
		this.setCommenturl(cookie.getCommentURL());
		this.setDomain(cookie.getDomain());
		this.setName(cookie.getName().toLowerCase(Locale.getDefault()));
		this.setValue(cookie.getValue());
		this.setPath(cookie.getPath());
		this.setExpires(cookie.getMaxAge()+System.currentTimeMillis()/1000);
		this.setDiscard(cookie.getDiscard());
		this.setSecure(cookie.getSecure());
		this.setVersion(cookie.getVersion());
		this.setHttponly(true);
		this.setPortlist(cookie.getPortlist());
	}
	
	public int getRecord_id() {
		return record_id;
	}
	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		if(domain == null){
			return;
		}
			
		this.domain = domain;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name == null){
			return;
		}
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		if(value == null){
			return;
		}
		this.value = value;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		if(path == null){
			return;
		}
		this.path = path;
	}
	public long getExpires() {
		return expires;
	}
	public void setExpires(long expires) {
		this.expires = expires;
	}
	public boolean isDiscard() {
		return discard==0 ? false : true;
	}
	
	public int getDiscard(){
		return this.discard;
	}
	
	public void setDiscard(boolean discard) {
		this.discard = discard ? 1 : 0;
	}
	
	public void setDiscard(int discard) {
		this.discard = discard;
	}
	
	public boolean isSecure() {
		return secure==0 ? false : true;
	}
	public int getSecure(){
		return this.secure;
	}
	public void setSecure(boolean secure) {
		this.secure = secure ? 1 : 0;
	}
	public void setSecure(int secure) {
		this.secure = secure;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		if(comment == null){
			return;
		}
		this.comment = comment;
	}
	public String getCommenturl() {
		return commenturl;
	}
	public void setCommenturl(String commenturl) {
		if(commenturl == null){
			return;
		}
		this.commenturl = commenturl;
	}
	public boolean isHttponly() {
		return httponly==0 ? false : true;
	}
	public int getHttponly(){
		return this.httponly;
	}
	public void setHttponly(boolean httponly) {
		this.httponly = httponly ? 1 : 0;
	}
	public void setHttponly(int httponly) {
		this.httponly = httponly;
	}
	public String getPortlist() {
		return portlist;
	}
	public void setPortlist(String portlist) {
		if(portlist == null){
			return;
		}
		this.portlist = portlist;
	}
	public long getCreate_tm() {
		return create_tm;
	}
	public void setCreate_tm(long create_tm) {
		this.create_tm = create_tm;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		if(uri == null){
			return;
		}
		this.uri = uri;
	}
}
