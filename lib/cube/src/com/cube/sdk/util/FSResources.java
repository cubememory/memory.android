/*************************************************************************************
 * Module Name: 具体模块见相应注释
 * File Name: ResourcesUtil.java
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.TreeMap;

import android.text.TextUtils;

/**
 * Author: 张维亚
 * 创建时间：2014年6月10日 下午3:38:35 
 * 修改时间：2014年6月10日 下午3:38:35 
 * Description:资源相关操作的工具类
 **/
public class FSResources {

	public static int findId(String packageName, String name) {
		return findResByName(packageName, "id", name);
	}

	public static int findString(String packageName, String name) {
		return findResByName(packageName, "string", name);
	}

	public static int findDrawable(String packageName, String name) {
		return findResByName(packageName, "drawable", name);
	}

	public static int findLayout(String packageName, String name) {
		return findResByName(packageName, "layout", name);
	}

	public static int findResByName(String packageName, String className, String name) {
		try {
			Class<?> desireClass = Class.forName(packageName + ".R$" + className);
			if (desireClass != null)
				return desireClass.getField(name).getInt(desireClass);
		} catch (Exception e) {
		}

		return 0;
	}

	/**
	 * 根据资源名的起始字段和一些关键字查找资源id
	 * @param clazz  
	 * @param rule 资源名的起始部分；比如 resName = icon_splash_360，rule = icon_splash
	 * @param more 更多的判断关键字
	 * @return 返回在查到的ID，如果未找到返回-1；
	 */
	public static int findResourse(Class<?> clazz, String rule, String... more) {
		return findResourse(clazz.getDeclaredFields(), rule, more);
	}

	/**
	 * 根据资源名的起始字段和一些关键字查找资源id
	 * @param fields 
	 * @param rule 资源名的起始部分；比如 resName = icon_splash_360，rule = icon_splash
	 * @param more 更多的判断关键字
	 * @return 返回在查到的ID，如果未找到返回-1；
	 */
	public static int findResourse(Field[] fields, String rule, String... more) {
		for (Field field : fields) {
			String name = field.getName().toLowerCase(Locale.getDefault());

			if (!verifyName(name, rule, more))
				continue;

			try {
				return (Integer) field.get(null);
			} catch (Exception e) {
			}
		}
		return -1;
	}

	/**
	 * 根据资源名的起始字段和一些关键字查找资源id列表
	 * @param clazz 
	 * @param rule 资源名的起始部分；比如 resName = icon_splash_360，rule = icon_splash
	 * @param more 更多的判断关键字
	 * @return 返回在查到的ID列表，如果未找到返回空的map；
	 */
	public static ArrayList<Integer> findResourseList(Class<?> clazz, String rule, String... more){
		return findResourseList(clazz.getDeclaredFields(), rule, more);
	}

	/**
	 * 根据资源名的起始字段和一些关键字查找资源id列表
	 * @param fields 
	 * @param rule 资源名的起始部分；比如 resName = icon_splash_360，rule = icon_splash
	 * @param more 更多的判断关键字
	 * @return 返回在查到的ID列表，如果未找到返回空的map；
	 */
	public static ArrayList<Integer> findResourseList(Field[] fields, String rule, String... more){

		HashMap<String, Integer> map = findResourseMap(fields, rule, more);
		if (map == null || map.size() <= 0)
			return new ArrayList<Integer>(0);

		TreeMap<String, Integer> tree = new TreeMap<String, Integer>(map);
		Collection<Integer> collection = tree.values();
		ArrayList<Integer> list = new ArrayList<Integer>(collection.size());
		list.addAll(collection);
		return list;
	}

	/**
	 * 根据资源名的起始字段和一些关键字查找资源id array
	 * @param clazz 
	 * @param rule 资源名的起始部分；比如 resName = icon_splash_360，rule = icon_splash
	 * @param more 更多的判断关键字
	 * @return 返回在查到的ID列表，如果未找到返回空的map；
	 */
	public static Integer[] findResourseArray(Class<?> clazz, String rule, String... more){
		return findResourseArray(clazz.getDeclaredFields(), rule, more);
	}

	/**
	 * 根据资源名的起始字段和一些关键字查找资源id array
	 * @param fields 
	 * @param rule 资源名的起始部分；比如 resName = icon_splash_360，rule = icon_splash
	 * @param more 更多的判断关键字
	 * @return 返回在查到的ID列表，如果未找到返回空的map；
	 */
	public static Integer[] findResourseArray(Field[] fields, String rule, String... more){
		HashMap<String, Integer> map = findResourseMap(fields, rule, more);
		if (map == null || map.size() <= 0)
			return new Integer[0];
		
		TreeMap<String, Integer> tree = new TreeMap<String, Integer>(map);
		Collection<Integer> collection = tree.values();
		return collection.toArray(new Integer[collection.size()]);
	}

	/**
	 * 根据资源名的起始字段和一些关键字查找资源id列表
	 * @param clazz 
	 * @param rule 资源名的起始部分；比如 resName = icon_splash_360，rule = icon_splash
	 * @param more 更多的判断关键字
	 * @return 返回在查到的ID列表，如果未找到返回空的map；
	 */
	public static HashMap<String, Integer> findResourseMap(Class<?> clazz, String rule, String... more){
		return findResourseMap(clazz.getDeclaredFields(), rule, more);
	}

	/**
	 * 根据资源名的起始字段和一些关键字查找资源id列表
	 * @param fields 
	 * @param rule 资源名的起始部分；比如 resName = icon_splash_360，rule = icon_splash
	 * @param more 更多的判断关键字
	 * @return 返回在查到的ID列表，如果未找到返回空的map；
	 */
	public static HashMap<String, Integer> findResourseMap(Field[] fields, String rule, String... more) {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		for (Field field : fields) {
			String name = field.getName().toLowerCase(Locale.getDefault());

			if (!verifyName(name, rule, more))
				continue;

			try {
				hashMap.put(name, (Integer) field.get(null));
			} catch (Exception e) {
			}
		}
		return hashMap;
	}

	/**
	 * @param name ID 对应的name
	 * @param rule 起始字段
	 * @param more 更多的判断关键字
	 * @return true 符合条件的ID，false 不符合条件的ID
	 */
	private static boolean verifyName(String name, String rule, String... more) {
		if (TextUtils.isEmpty(name)) 
			return false;

		if (!name.startsWith(rule))
			return false;

		if (more == null || more.length <= 0)
			return true;

		for (String string : more) {
			if (name.indexOf(string) == -1)
				return false;
		}

		return true;
	}
}