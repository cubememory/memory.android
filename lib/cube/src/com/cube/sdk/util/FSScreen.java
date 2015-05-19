/*************************************************************************************
 * Module Name: 具体模块见相应注释
 * File Name: FSGuideAdapter.java
 * Description: 
 * Author: 张维亚
 * All Rights Reserved
 * 所有版权保护
 * Copyright 2014, Funshion Online Technologies Ltd.
 * 版权 2014，北京风行在线技术有限公司
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of Funshion Online Technologies Ltd.;
 * the contents of this file may not be disclosed to third parties, copied or
 * duplicated in any form, in whole or in part, without the prior written
 * permission of Funshion Online Technologies Ltd.
 * 这是北京风行在线技术有限公司未公开的私有源代码。本文件及相关内容未经风行在线技术有
 * 限公司事先书面同意，不允许向任何第三方透露，泄密部分或全部; 也不允许任何形式的私自备份。
 ***************************************************************************************/

package com.cube.sdk.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Author: 张维亚
 * File Name: ScreenUtil.java
 * 创建时间：2014年5月30日 下午3:27:47
 * 修改时间：2014年5月30日 下午3:27:47
 * Module Name: 具体模块见相应注释
 * Description: 关于屏幕相关的util
 **/
public class FSScreen {

	private static DisplayMetrics getDisplayMetrics(Context ctx) {
		return ctx.getResources().getDisplayMetrics();
	}

	/**
	 * 获得设备的dpi
	 */
	public static float getScreenDpi(Context ctx) {
		return getDisplayMetrics(ctx).densityDpi;
	}

	/**
	 * 获得设备屏幕密度
	 */
	public static float getScreenDensity(Context ctx) {
		DisplayMetrics dm = getDisplayMetrics(ctx);
		return dm.density;
	}

	/**
	 * 获得设备屏幕宽度
	 */
	public static int getScreenWidth(Context ctx) {
		DisplayMetrics dm = getDisplayMetrics(ctx);
		return dm.widthPixels;
	}

	/**
	 * 获得设备屏幕高度
	 * According to phone resolution height
	 */
	public static int getScreenHeight(Context ctx) {
		DisplayMetrics dm = getDisplayMetrics(ctx);
		return dm.heightPixels;
	}

	/**
	 * According to the resolution of the phone from the dp unit will become a px (pixels)
	 */
	public static int dip2px(Context ctx, int dip) {
		float density = getScreenDensity(ctx);
		return (int) (dip * density + 0.5f);
	}
	/**
	 * According to the resolution of the phone from the dp unit will become a px (pixels)
	 */
	public static float dip2px(Context ctx, float dip) {
		float density = getScreenDensity(ctx);
		return  (dip * density + 0.5f);
	}

	/**
	 * Turn from the units of px (pixels) become dp according to phone resolution
	 */
	public static int px2dip(Context ctx, float px) {
		float density = getScreenDensity(ctx);
		return (int) (px / density + 0.5f);
	}
}
