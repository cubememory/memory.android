package com.cube.sdk.cookie;

import java.util.ArrayList;
import java.util.List;

import com.funshion.video.exception.FSDbException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FSCookieDao extends FSDao{

	public FSCookieDao(SQLiteDatabase db) {
		super(db);
	}
	
	public void insert(FSDbCookieEntity entity) throws FSDbException{
		try{			
			StringBuilder sb = new StringBuilder("insert into fs_cookie(uri, domain, name, value, path, expires, discard,"
																		+ "secure, version, comment, commenturl, httponly,"
																		+ "portlist, create_tm) values(");
			sb.append(quote(entity.getUri())+",");
			sb.append(quote(entity.getDomain())+",");
			sb.append(quote(entity.getName())+",");
			sb.append(quote(entity.getValue())+",");
			sb.append(quote(entity.getPath())+",");
			sb.append(entity.getExpires()+",");
			sb.append(entity.getDiscard()+",");
			sb.append(entity.getSecure()+",");
			sb.append(entity.getVersion()+",");
			sb.append(quote(entity.getComment())+",");
			sb.append(quote(entity.getCommenturl())+",");
			sb.append(entity.getHttponly()+",");
			sb.append(quote(entity.getPortlist())+",");
			sb.append(entity.getCreate_tm()+")");
			
			super.execute(sb.toString());
		}catch(Exception e){
			throw new FSDbException(e.getMessage());
		}
	}
	
	public void delete(FSDbCookieEntity entity) throws FSDbException {
		
		try{			
			String sql = "delete from fs_cookie where "+
						 "uri="+quote(entity.getUri())+" and "+
						 "domain="+quote(entity.getDomain())+" and "+
						 "name="+quote(entity.getName())+" and "+
						 "path="+quote(entity.getPath())+";";
			super.execute(sql);
		}catch(Exception e){
			throw new FSDbException(e.getMessage());
		}
	}
	
	public List<FSDbCookieEntity> select() throws FSDbException{
		Cursor cursor = null;
		try{
			List<FSDbCookieEntity> cookies = new ArrayList<FSDbCookieEntity>();
			
			String sql = "select * from fs_cookie;";
				
			cursor = super.query(sql);
			boolean hasMore = cursor.moveToFirst();
			while(hasMore){
				FSDbCookieEntity entity = new FSDbCookieEntity();
				entity.setUri(cursor.getString(cursor.getColumnIndex("uri")));
				entity.setDomain(cursor.getString(cursor.getColumnIndex("domain")));
				entity.setName(cursor.getString(cursor.getColumnIndex("name")));
				entity.setValue(cursor.getString(cursor.getColumnIndex("value")));
				entity.setPath(cursor.getString(cursor.getColumnIndex("path")));
				entity.setExpires(cursor.getLong(cursor.getColumnIndex("expires")));
				entity.setDiscard(cursor.getInt(cursor.getColumnIndex("discard")));
				entity.setSecure(cursor.getInt(cursor.getColumnIndex("secure")));
				entity.setVersion(cursor.getInt(cursor.getColumnIndex("version")));
				entity.setComment(cursor.getString(cursor.getColumnIndex("comment")));
				entity.setCommenturl(cursor.getString(cursor.getColumnIndex("commenturl")));
				entity.setHttponly(cursor.getInt(cursor.getColumnIndex("httponly")));
				entity.setPortlist(cursor.getString(cursor.getColumnIndex("portlist")));
				entity.setCreate_tm(cursor.getLong(cursor.getColumnIndex("create_tm")));
				cookies.add(entity);
				
				hasMore = cursor.moveToNext();
			}
			return cookies;
		}catch(Exception e){
			throw new FSDbException(e.getMessage());
		}finally{
			if(cursor != null){
				try{
					cursor.close();
				}catch(Exception e){}
			}
		}		
	}
	
	public void clear(long beforeTM) throws FSDbException{
		try{			
			String sql = "delete from fs_cookie where expires<"+beforeTM+";";		
			super.execute(sql);
		}catch(Exception e){
			throw new FSDbException(e.getMessage());
		}
	}
}
