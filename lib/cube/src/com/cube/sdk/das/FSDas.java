/*************************************************************************************
 * Module Name: 示例模块
 * File Name: example.java/example.h
 * Description: 代码示例
 * Author: 王瑞波
 * Copyright 2007-, Funshion Online Technologies Ltd.
 * All Rights Reserved
 * 版权 2007-，北京风行在线技术有限公司
 * 所有版权保护
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of Funshion Online Technologies Ltd.;
 * the contents of this file may not be disclosed to third parties, copied or
 * duplicated in any form, in whole or in part, without the prior written
 * permission of Funshion Online Technologies Ltd.
 * 这是北京风行在线技术有限公司未公开的私有源代码。本文件及相关内容未经风行在线技术有
 * 限公司事先书面同意，不允许向任何第三方透露，泄密部分或全部; 也不允许任何形式的私自备份。
 ***************************************************************************************/
package com.cube.sdk.das;

import android.content.Context;

import com.funshion.http.FSHttpParams;
import com.funshion.video.exception.FSDasException;

/**
 * Usage example
 * 
<pre>
FSDas.getInstance().get(FSDasReq.PS_SEARCH_HOTWORD, FSHttpParams.newParams(), new FSDasHandler(){
</pre>
 * 
 * @author wangrb
 */

public abstract class FSDas {
	/**
	 * single instance object
	 */
	private static FSDas instance = null;
	
	/**
	 * get the single instance object
	 * @return
	 * single instance object
	 */
	public static FSDas getInstance(){
		if(instance == null){
			instance = new FSDasImpl();
		}
		return instance;
	}
	
	/**
	 * initialize the public parameters for all request, must not be null
	 * this method must be invoked before any other method to be used
	 */
	public abstract void init(Context context);
	
	/**
	 * 	request data by the specified type with relate parameters, the handler will be recalled
	 * when the request finished or failed.
	 * @param url
	 * @param params
	 * @param entityClass
	 * @param handler
	 * @throws FSDasException
	 */
	public abstract String get(String url, Class<?> entityClass, FSHandler handler) throws FSDasException;

	/**
	 * 	request data by the specified type with relate parameters, the handler will be recalled
	 * when the request finished or failed.
	 * @param url
	 * @param params
	 * @param entityClass
	 * @param handler
	 * @throws FSDasException
	 */
	public abstract String get(String url, Class<?> entityClass, int maxRetryCount, FSHandler handler) throws FSDasException;

	/**
	 * 	request data by the specified type with relate parameters, the handler will be recalled
	 * when the request finished or failed.
	 * @param url
	 * @param params
	 * @param entityClass
	 * @param handler
	 * @param disableCache
	 * @throws FSDasException
	 */
	public abstract String get(String url, Class<?> entityClass, int maxRetryCount, FSHandler handler, boolean enableCache) throws FSDasException;
	
	/**
	 * enable cache
	 */
	public abstract void enableCache();
	
	/**
	 * disable cache
	 */
	public abstract void disableCache();
}
