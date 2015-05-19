package com.cube.sdk.cookie;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class FSDao {
	
	public enum Dao{PUSH, DOWNLOAD, FAVORITE, HISTORY, APP_DOWNLOAD, COOKIE, LOCAL_VIDEO,LIVE_ORDER,CHANNEL,SUBSCRIPTION,KK_SUBSTANCE}
	//KK_SUBSTANCE 为kuaikan所用内容列表请求保存hnum，qnum，◦tnum的数据表

	private SQLiteDatabase db;
	
	public FSDao(SQLiteDatabase db){
		this.db = db;
	}
	
	protected void execute(String sql){
		db.execSQL(sql);
	}
	
	protected void execute(String sql, Object[] bindArgs){
		db.execSQL(sql, bindArgs);
	}
	
	protected Cursor query(String sql){
		return db.rawQuery(sql, null);
	}
	
	protected Cursor query(String sql, String[] selectionArgs){
		return db.rawQuery(sql, selectionArgs);
	}
	
	protected String quote(String val){
		return "'"+val+"'";
	}
	
	public long insert(String table, String nullColumnHack, ContentValues values) {
		return db.insert(table, nullColumnHack, values);
	}
	
	public int delete(String table, String selection, String[] selectionArgs){
		return db.delete(table, selection, selectionArgs);
	}

	public int update(String table, ContentValues values, String selection, String[] selectionArgs){
		return db.update(table, values, selection, selectionArgs);
	}

	public Cursor query(String table, String[] projection, String selection, String[] selectionArgs, String sortOrder){
		return db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
	}
	
	protected void beginTransaction(){
		db.beginTransaction();
	}
	
	protected void setTransactionSuccessful(){
		db.setTransactionSuccessful();
	}
	
	protected void endTransaction(){
		db.endTransaction();
	}
}
