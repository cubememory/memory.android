package com.cube.sdk.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CString {
	
	/**
	 * empty string or null string means empty
	 * @param str
	 * @return
	 */
	public static boolean empty(String str){
		if(str == null){
			return true;
		}
		
		return str.length()==0;
	}
	
	/**
	 * wrap the string @str, return "" when @str is null
	 * @param str
	 * @return
	 */
	public static String wrap(String str){
		if(str == null){
			return "";
		}
		return str;
	}
	
	/**
	 * convert byte array @data to hex string
	 * @param data: byte array data
	 * @return: hex string of byte array @data
	 */
	public static String byte2hex(byte[] data){
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<data.length; i++){
			int high = (data[i]>>4)&0x0F;
			int low = data[i]&0x0F;
			
			sb.append(Integer.toHexString(high));
			sb.append(Integer.toHexString(low));
		}
		
		return sb.toString();
	}
	
	/**
	 * convert hex string to byte array
	 * @param hexstr: hex string
	 * @return:
	 * 		byte array if hex string is valid, otherwise return null
	 */
	public static byte[] hex2byte(String hexstr) throws Exception{
		int lenhexstr = hexstr.length(); 
		if(lenhexstr%2 != 0)
			throw new Exception("error: length of the input hex string must be pow of 2");
		
		try{
			byte[] bytes = new byte[lenhexstr/2];
			int idx = 0;
			for(int i=0; i<lenhexstr; i+=2){
				int high = Integer.valueOf(hexstr.substring(i, i+1), 16);
				int low = Integer.valueOf(hexstr.substring(i+1, i+2), 16);
				
				bytes[idx] = (byte)((high<<4)|low);
				idx++;
			}
			
			return bytes;	
		}catch(Exception e){
			throw new Exception("error: invalid hex string.");
		}
	}
	
	
	/**
	 * get the first group match the regex group condition
	 * @param str
	 * @param regex
	 * @return
	 */
	public static String group(String str, String regex){
		String firstGroupStr = null;
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		if(m.find()){
			firstGroupStr = m.group(1);
		}
		return firstGroupStr;
	}
	
	/**
	 * generate a random string using random long value
	 * @return
	 */
	public static String randomLongString(){
		String randomStr = "";
		try{
			Random random = new Random(System.currentTimeMillis());
			randomStr = String.valueOf(random.nextLong());
		}catch(Exception e){}
		return randomStr;
	}
	
	/**
	 * generate a random mac address using "af" as the prefix
	 * @return
	 */
	public static String randomMacAddress(){
		String randomMac = "afaf";
		try{
			Random random = new Random(System.currentTimeMillis());
			String randomMD5 = CDigest.md5(String.valueOf(random.nextLong()));
			randomMac = randomMac+randomMD5.substring(0, 8);
			
		}catch(Exception e){}
		
		return randomMac;
	}
	
	public static String randomString(int len){
		String randomStr = "";
		try{
			byte[] bytes = new byte[len];
			
			Random random = new Random(System.currentTimeMillis());
			random.nextBytes(bytes);
			
			randomStr = CString.byte2hex(bytes);
		}catch(Exception e){
		}
		return randomStr;
	}
}
