package com.cube.sdk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.KeyguardManager;
import android.content.Context;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

public final class CDevice {
	
	public final static class Dev{
		/**
		 * get the telephone device id, which is the IMEI for GSM and the MEID or ESN for CDMA phones
		 * @param context: current context
		 * @return
		 * 	device id, or null
		 */
		public static String getDeviceID(Context context){
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			if(tm != null){
				return tm.getDeviceId();
			}
			return null;
		}
		
		/**
		 * get the serial number of device
		 * @return
		 * serial number or null
		 */
		public static String getSerialNum(){
			try{
				Class<?> c = Class.forName("android.os.SystemProperties"); 
				Method get = c.getMethod("get", String.class, String.class );     
				String serialNum = (String)(get.invoke(c, "ro.serialno", "unknown"));
				return serialNum;
			}                                                                                
			catch (Exception e){                                                     
				return null;
			}
		}
		
		/**
		 * get the memory information, size in MB
		 * @return
		 * memory information, size in MB
		 */
		public static MemInfo getMemInfo(){
			FileInputStream fis = null;
			try{
				/*the memory information file path under linux os*/
				String mempath = "/proc/meminfo";
				
				/*read data in the memory information file*/
				fis = new FileInputStream(mempath);
				
				byte[] buffer = new byte[4096];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				int sz = fis.read(buffer);
				while(sz != -1){
					baos.write(buffer, 0, sz);
					sz = fis.read(buffer);
				}
				
				fis.close();
				fis = null;
				
				
				/*parse the total memory and left memory*/
				String strMemInfo = baos.toString();
				HashMap<String, Long> infos = new HashMap<String, Long>();
				
				Pattern p = Pattern.compile("(\\w+):\\s*(\\d+)");
				Matcher m = p.matcher(strMemInfo);
				while(m.find()){
					infos.put(m.group(1), Long.valueOf(m.group(2)));
				}
				
				if(infos.containsKey("MemTotal") && infos.containsKey("MemFree") && infos.containsKey("Buffers") && infos.containsKey("Cached"))
					return new MemInfo(infos.get("MemTotal").longValue()/1024, infos.get("MemFree").longValue()/1024,
									   infos.get("Buffers").longValue(), infos.get("Cached").longValue());
				
				return null;
			}catch(Exception e){
				if(fis != null){
					try {
						fis.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				return null;
			}
		}
		
		/**
		 * judge whether it's locked screen status
		 * @param context
		 * @return true indicates that it's locked screen; otherwise is false
		 */
		public static boolean isLockScreen(Context context) {
			if (null == context) {
				return true;
			}

			try {
				KeyguardManager mkeyguardManager = (KeyguardManager) context
						.getSystemService(Context.KEYGUARD_SERVICE);
				return (null != mkeyguardManager && mkeyguardManager
						.inKeyguardRestrictedInputMode());
			} catch (Exception e) {
				Log.d("FSDevice", "isLockScreen() ", e);
			}
			return true;
		}
	}
	
	/**
	 * pack the os utility methods of device
	 */
	public final static class OS{
		
		/**
		 * get the android os version
		 * @return
		 * 	android os version
		 */
		public static String getVersion(){
			return android.os.Build.VERSION.RELEASE;
		}
		
		/**
		 * get brand of the android device
		 * @return
		 * brand
		 */
		public static String getBrand(){
			return android.os.Build.BRAND;
		}
		
		/**
		 * get model of the android device
		 * @return
		 * model
		 */
		public static String getModel(){
			return android.os.Build.MODEL;
		}
		
		/**
		 * get the android id, which is A 64-bit number (as a hex string) that is randomly generated 
		 * on the device's first boot and should remain constant for the lifetime of the device. 
		 * (The value may change if a factory reset is performed on the device)
		 * @return
		 * android id of the device, 
		 */
		public static String getAndroidID(Context context){
			return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		}
	}
	
	/**
	 * pack the wifi utility methods of device
	 */
	public final static class Wifi{
		/**
		 * get the wifi information object
		 * @param context: current context
		 * @return
		 * wifi information object, or null
		 */
		public static WifiInfo getWifiInfo(Context context){
			WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			if(wm != null){
				WifiInfo wi = wm.getConnectionInfo();
				if(wi != null)
					return wi;
			}
			
			return null;
		}
		
		/**
		 * get the mac address of device in format: AABBCCDDEEFF
		 * @param context: current context
		 * @return
		 * 	mac address, or null
		 */
		public static String getMacAddress(Context context){
			try{
				String strMac = getMacAddress1(context);
				if(strMac != null){
					return strMac.replace(":", "").toLowerCase(Locale.getDefault());
				}
			}catch(Exception e){}
			return "000000000000";
		}
		
		/**
		 * get the mac address of device in format: xx:xx:xx:xx:xx:xx
		 * @param context: current context
		 * @return
		 * 	mac address, or null
		 */
		public static String getMacAddress1(Context context){
			try{
				WifiInfo wi = getWifiInfo(context);
				if(wi != null){
					String mac =  wi.getMacAddress();
					if(mac != null){
						return mac.toLowerCase(Locale.getDefault());
					}
				}
			}catch(Exception e){}
				
			return "00:00:00:00:00:00";
		}
		
		/**
		 * get the ip address in format: xxx.xxx.xxx.xxx
		 * @param context: current context
		 * @return
		 * ip address, or null
		 */
		public static String getIPAddress(Context context){
			WifiInfo wi = getWifiInfo(context);
			if(wi != null){
				int ip = wi.getIpAddress();
				int section1 = 0x000000FF&(ip>>24);
				int section2 = 0x000000FF&(ip>>16);
				int section3 = 0x000000FF&(ip>>8);
				int section4 = 0x000000FF&ip;
				String strIP = Integer.toString(section4)+"."
							   +Integer.toString(section3)+"."
							   +Integer.toString(section2)+"."
							   +Integer.toString(section1);
				return strIP;
			}
				
			return null;			
		}
	}
	
	/**
	 * pack the network utility methods of device
	 */
	public final static class Network{
		public enum Type{UNKNOWN, WIFI, MOBILE, MOBILE2G, MOBILE3G, MOBILE4G};
		
		/**
		 * get the current active network information object
		 * @param context: current context
		 * @return
		 *		current active network information object, or null
		 */
		public static NetworkInfo getCurrentActiveNetwork(Context context){
			try{
				ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
				if(cm != null){
					NetworkInfo ni = cm.getActiveNetworkInfo();
					if(ni != null)
						return ni;
				}
			}catch(Exception e){}
			
			return null;
		}
		
		/**
		 * check current active network is available, notice available not means the network
		 * is connected to the internet.
		 * @param context: current context
		 * @return:
		 * 	true if the network is available, otherwise return false
		 */
		public static boolean isAvailable(Context context){
			NetworkInfo ni = getCurrentActiveNetwork(context);
			if(ni != null){
				return ni.isAvailable();
			}
			return false;
		}
		
		/**
		 * check current active network is connected, connected means you may send data to
		 * the internet
		 * @param context: current context
		 * @return:
		 * 	true if the network is available, otherwise return false
		 */
		public static boolean isConnected(Context context){
			NetworkInfo ni = getCurrentActiveNetwork(context);
			if(ni != null){
				return ni.isConnected();
			}
			return false;
		}
		
		/**
		 * get the current active network type
		 * @param context: current context
		 * @return
		 * 	current active network type, or Type.UNKNOWN
		 */
		public static Type getType(Context context){
			Type activeType = Type.UNKNOWN;
			NetworkInfo ni = getCurrentActiveNetwork(context);
			if(ni != null){
				int type = ni.getType();
				switch(type){
				case ConnectivityManager.TYPE_WIFI:
					activeType = Type.WIFI;
					break;
				case ConnectivityManager.TYPE_MOBILE:
					activeType = Type.MOBILE;
					break;
				default:
					break;
				}
			}
			
			return activeType;
		}
		
		/**
		 * get the current active network type name
		 * @param context: current context
		 * @return:
		 * 	network type name, or null
		 */
		public static String getTypeName(Context context){
			NetworkInfo ni = getCurrentActiveNetwork(context);
			if(ni != null){
				return ni.getTypeName();
			}
			
			return null;
		}
		
		/**
		 * if the current active network is mobile, use this method 
		 * to get the mobile network type. 
		 * @param context: current context
		 * @return
		 * 	current mobile network type, or Type.UNKNOWN
		 * 
		 */
		public static Type getSubType(Context context){
			//TODO: !this method is not test, must be test and fix problem later!
			Type mobileType = Type.UNKNOWN;
			NetworkInfo ni = getCurrentActiveNetwork(context);
			if(ni != null && ni.getType() == ConnectivityManager.TYPE_MOBILE){
				int type = ni.getSubtype();
				switch(type){
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_EDGE:
					mobileType = Type.MOBILE2G;
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					mobileType = Type.MOBILE3G;
					break;
				default:
					break;
				}
			}
			
			return mobileType;
		}
		
		/**
		 * get the current active network sub type name
		 * @param context: current context
		 * @return:
		 * 	network sub type name, or null
		 */
		public static String getSubTypeName(Context context){
			NetworkInfo ni = getCurrentActiveNetwork(context);
			if(ni != null){
				return ni.getSubtypeName();
			}
			
			return null;
		}
		
		/**
		 * get ip v4 address by using the java sdk methods
		 * 
		 * @return
		 * 	ip v4 address, like 192.168.1.2, or failed return 0.0.0.0
		 */
		public static String getIPAddress(){
	        try {
	            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	                NetworkInterface intf = en.nextElement();
	                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                    InetAddress inetAddress = enumIpAddr.nextElement();
	                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
	                        return inetAddress.getHostAddress().toString();
	                    }
	                }
	            }
	        } catch (SocketException ex) {}
	        
	        return "0.0.0.0";
		}
	}
	
	/**
	 * pack the file system utility methods of device
	 * @author wangrb
	 */
	public final static class FileSystem {
		/**
		 * test if the given directory is writable
		 * @param dir: directory path to be test
		 * @return
		 *   true if writable, otherwise return false
		 */
		public static boolean isWritable(String dir){
			if(dir == null){
				return false;
			}
			
			try{
				File fdir = new File(dir);
				if(!fdir.exists() && !fdir.mkdirs()){
					return false;
				}
				
				String testDir = CString.randomString(16)+".funshion";
				File fTestDir = new File(dir, testDir);
				if(!fTestDir.mkdirs() && !fTestDir.isDirectory()){
					return false;
				}
				fTestDir.delete();
				
				return true;
			}catch(Exception e){
				return false;
			}
		}
		
		/**
		 * get the state of specified volume by path
		 * @param path
		 * @return
		 * state of the volume, or null
		 */
		public static State getState(String path){
			if(path == null){
				return null;
			}
			
			try{
				StatFs sf = new StatFs(path);
				long total = (long)sf.getBlockCount()*(long)sf.getBlockSize();
				long available = (long)sf.getAvailableBlocks()*(long)sf.getBlockSize();
				long free = (long)sf.getFreeBlocks()*(long)sf.getBlockSize();
				
				return new State(total, available, free);
			}catch(Exception e){
				//add log here
				return null;
			}
		}
		
		/**
		 * get the volume information of specified path
		 * @param path
		 * @return
		 */
		public static Volume getVolume(String path){
			if(path == null){
				return null;
			}
			
			try{
				State state = FileSystem.getState(path);
				if(state != null){
					return new Volume(path, state);
				}else{
					return null;
				}
			}catch(Exception e){
				return null;
			}
		}
		
		/**
		 * get the total size in bytes of volume specified by the input path
		 * @param path
		 * @return
		 * total size in bytes
		 */
		public static long getTotalSize(String path){
			if(path == null){
				return 0;
			}
			
			try{
				StatFs sf = new StatFs(path);
				return (long)sf.getBlockCount()*(long)sf.getBlockSize();
			}catch(Exception e){
				return 0;
			}
		}
		
		/**
		 * get the available size in bytes of volume specified by the input path
		 * @param path
		 * @return
		 * free size in bytes
		 */
		public static long getAvailableSize(String path){
			if(path == null){
				return 0;
			}
			
			try{
				StatFs sf = new StatFs(path);
				return (long)sf.getAvailableBlocks()*(long)sf.getBlockSize();
			}catch(Exception e){
				return 0;
			}
		}
		
		/**
		 * get the free size in bytes of volume specified by the input path
		 * @param path
		 * @return
		 * free size in bytes
		 */	
		public static long getFreeSize(String path){
			if(path == null){
				return 0;
			}
			
			try{
				StatFs sf = new StatFs(path);
				return (long)sf.getFreeBlocks()*(long)sf.getBlockSize();
			}catch(Exception e){
				return 0;
			}
		}
		
		/**
		 * check if the given path is exist directory
		 * @param path
		 * @return
		 * return true when the path is an exist directory, otherwise return false
		 */
		public static boolean isDirectory(String path){
			if(path == null)
				return false;
			
			File f = new File(path);
			if(f.exists() && f.isDirectory())
				return true;
			
			return false;
		}
				
		/**
		 * get the internal storage information, size in MB
		 * @return
		 * internal storage information or null;
		 */
		public static Volume getInternalStorage(){
			try{
				File path = Environment.getDataDirectory();
				StatFs sf = new StatFs(path.getPath());
				
				long total = (long)sf.getBlockCount()*(long)sf.getBlockSize();
				long available = (long)sf.getAvailableBlocks()*(long)sf.getBlockSize();
				long free = (long)sf.getFreeBlocks()*(long)sf.getBlockSize();
				
				return new Volume("内部存储", path.getPath(), new State(total, available, free));
			}catch(Exception e){
				return null;
			}
		}
		
		/**
		 * get the internal stroage data directory path
		 * @return
		 */
		public static String getInternalStorageDir(){
			try{
				File path = Environment.getDataDirectory();
				return path.getAbsolutePath();
			}catch(Exception e){
				return null;
			}
		}
		
		/**
		 * get the external storage information, size in MB
		 * @return
		 * external storage information or null;
		 */
		public static Volume getExternalStorage(){
			try{
				File path = Environment.getExternalStorageDirectory();
				StatFs sf = new StatFs(path.getPath());
				long total = (long)sf.getBlockCount()*(long)sf.getBlockSize();
				long available = (long)sf.getAvailableBlocks()*(long)sf.getBlockSize();
				long free = (long)sf.getFreeBlocks()*(long)sf.getBlockSize();
				
				return new Volume("手机存储", path.getPath(), new State(total, available, free));
			}catch(Exception e){
				return null;
			}
		}
		
		/**
		 * get the external storage path
		 * @return
		 */
		public static String getExternalStorageDir(){
			try{
				File path = Environment.getExternalStorageDirectory();
				return path.getAbsolutePath();
			}catch(Exception e){
				return null;
			}
		}
		
		public static String getAppDataDir(Context context){
			try{
				Class<?> c = Class.forName("android.os.Environment");
				Method getExternalStorageAppDataDirectory = c.getMethod("getExternalStorageAppDataDirectory", String.class);
				File dir = (File)getExternalStorageAppDataDirectory.invoke(Environment.class, context.getPackageName());
				if(dir.exists() || dir.mkdirs()){
					return dir.getAbsolutePath();
				}else{
					return FileSystem.getDefaultAppDataDir(context);
				}
			}catch(Exception e){
				return FileSystem.getDefaultAppDataDir(context);
			}
		}
		
		public static String getDefaultAppDataDir(Context context){
			try{
				File defaultAppDataDir = new File(new File(Environment.getDataDirectory(),"data"), context.getPackageName()+"/data");
				if(defaultAppDataDir.exists() || defaultAppDataDir.mkdirs()){
					return defaultAppDataDir.getAbsolutePath();
				}else{
					return null;
				}
			}catch(Exception e){
				return null;
			}
		}
		
		public static String getAppMediaDir(Context context){
			try{
				Class<?> c = Class.forName("android.os.Environment");
				Method getExternalStorageAppMediaDirectory = c.getMethod("getExternalStorageAppMediaDirectory", String.class);
				File dir = (File)getExternalStorageAppMediaDirectory.invoke(Environment.class, context.getPackageName());
				if(dir.exists() || dir.mkdirs()){
					return dir.getAbsolutePath();
				}else{
					return FileSystem.getDefaultAppMediaDir(context);
				}
			}catch(Exception e){
				return FileSystem.getDefaultAppMediaDir(context);
			}
		}
		
		public static String getDefaultAppMediaDir(Context context){
			try{
				File defaultAppMediaDir = new File(new File(Environment.getDataDirectory(),"data"), context.getPackageName()+"/media");
				if(defaultAppMediaDir.exists() || defaultAppMediaDir.mkdirs()){
					return defaultAppMediaDir.getAbsolutePath();
				}else{
					return null;
				}
			}catch(Exception e){
				return null;
			}
		}
		
		public static String getAppFilesDir(Context context){
			try{
				Class<?> c = Class.forName("android.os.Environment");
				Method getExternalStorageAppFilesDirectory = c.getMethod("getExternalStorageAppFilesDirectory", String.class);
				File dir = (File)getExternalStorageAppFilesDirectory.invoke(Environment.class, context.getPackageName());
				if(dir.exists() || dir.mkdirs()){
					return dir.getAbsolutePath();
				}else{
					return FileSystem.getDefaultAppFilesDir(context);
				}
				
			}catch(Exception e){
				return FileSystem.getDefaultAppFilesDir(context);
			}
		}
		
		public static String getDefaultAppFilesDir(Context context){
			try{
				File filesDir = context.getFilesDir();
				if(filesDir.exists() || filesDir.mkdirs()){
					return filesDir.getAbsolutePath();
				}else{
					return null;
				}
			}catch(Exception e){
				return null;
			}
		}
		
		public static String getAppCacheDir(Context context){
			try{
				Class<?> c = Class.forName("android.os.Environment");
				Method getExternalStorageAppCacheDirectory = c.getMethod("getExternalStorageAppCacheDirectory", String.class);
				File dir = (File)getExternalStorageAppCacheDirectory.invoke(Environment.class, context.getPackageName());
				if(dir.exists() || dir.mkdirs()){
					return dir.getAbsolutePath();
				}else{
					return FileSystem.getDefaultAppCacheDir(context);
				}
			}catch(Exception e){
				return FileSystem.getDefaultAppCacheDir(context);
			}
		}
		
		public static String getDefaultAppCacheDir(Context context){
			try{
				File cacheDir = context.getCacheDir();
				if(cacheDir.exists() || cacheDir.mkdirs()){
					return cacheDir.getAbsolutePath();
				}else{
					return null;
				}
			}catch(Exception e){
				return null;
			}
		}
		
		/**
		 * get all the volume paths
		 * @param context: current context
		 * @return
		 * 		all the volume paths, or null
		 */
		public static String[] getVolumePaths(Context context){
			try{
				Method getSystemService = context.getClass().getMethod("getSystemService", String.class);
				Object sm = getSystemService.invoke(context, "storage");  						
				Method getVolumePaths = sm.getClass().getMethod("getVolumePaths");
				
				return (String[])getVolumePaths.invoke(sm);
				
			}catch(Exception e){
				return null;
			}
		}

		/**
		 * get all valid volumes
		 * @param context
		 * @return
		 */
		public static Volume[] getValidVolumes(Context context){
			try{
				/*for create the app files directory on the sdcard for some device*/
				FileSystem.createAppExternalFilesDir(context);
				
				List<Volume> volumes = new ArrayList<Volume>();

				/*add the external storage directory*/
				Volume externalStorageVolume = FileSystem.getVolume(FileSystem.getExternalStorageDir());
				if(externalStorageVolume != null && externalStorageVolume.isAvaliable() && !FileSystem.existVolumeInList(externalStorageVolume, volumes)){
					externalStorageVolume.setExternal(true);
					volumes.add(externalStorageVolume);
				}

				/*find all the valid volumes*/
				String otherVolumePaths[] = FileSystem.getVolumePaths(context);
				if(otherVolumePaths != null){
					for(String path: otherVolumePaths){
						if(!FileSystem.isWritable(path) && CDir.exist(FileSystem.getAndroidDataDir(path))){
							path = FileSystem.getAndroidDataDir(path)+"/"+context.getPackageName()+"/files";
						}
						
						Volume volume = FileSystem.getVolume(path);
						if(volume != null && !FileSystem.existVolumeInList(volume, volumes) && volume.isAvaliable()){
							volume.setExternal(false);
							volumes.add(volume);
						}
					}
				}

				/*transmit the volumes to array and return*/
				if(!volumes.isEmpty()){
					return volumes.toArray(new Volume[volumes.size()]);
				}else{
					return null;
				}
			}catch(Exception e){
				return null;
			}
		}
		
		private static boolean mayBeSDCard(String path){
			try{
				return path.toLowerCase(Locale.getDefault()).contains("sd");
			}catch(Exception e){
				return false;
			}
		}
		
		private static String getAndroidDataDir(String root){
			if(root == null){
				return "/data/data";
			}
			
			return root+"/Android/data";			
		}
		
		private static void createAppExternalFilesDir(Context context){
			try{
				context.getExternalFilesDir(null);
			}catch(Exception e){}
		}
		
		private static boolean existVolumeInList(Volume volume, List<Volume> volumeList){
			try{
				for(Volume listVolume: volumeList){
					if(volume.equals(listVolume)){
						return true;
					}
				}
				return false;
			}catch(Exception e){
				return false;
			}
		}
	
		/**
		 * storage space data structure of a volume
		 */
		public static class State{
			private long total = 0;// in bytes
			private long available = 0; // in bytes
			private long left = 0;// in bytes
			
			public State(long total, long available, long left){
				this.total = total;
				this.available = available;
				this.left = left;
			}

			public long getTotal() {
				return total;
			}

			public long getAvailable() {
				return available;
			}

			public long getLeft() {
				return left;
			}
			
			public boolean equals(Object obj){
				try{
					if(!(obj instanceof State)){
						return false;
					}
					
					State state = (State)obj;
					
					if(this.total == state.total && this.available == state.available && this.left == state.available){
						return true;
					}
					
					return false;
				}catch(Exception e){
					return false;
				}
			}
		}
		
		/**
		 * volume information data structure
		 */
		public static class Volume{
			private String name = null;
			private String path = null;
			private State state = null;
			private boolean isExternal = false;
			
			public Volume(String path, State state){
				this.name = path;
				this.path = path;
				this.state = state;
			}
			
			public Volume(String name, String path, State state){
				this.name = name;
				this.path = path;
				this.state = state;
			}
			
			public void setName(String name) {
				this.name = name;
			}
			
			public String getName() {
				return this.name;
			}
			
			public void setPath(String path){
				this.path = path;
			}
			
			public String getPath() {
				return this.path;
			}

			public State getState() {
				return this.state;
			}
			
			public boolean isExternal() {
				return isExternal;
			}

			public void setExternal(boolean isExternal) {
				this.isExternal = isExternal;
			}
			
			public boolean isAvaliable(){
				if(this.state!=null && this.state.getAvailable()>0 && FileSystem.isWritable(this.getPath())){
					return true;
				}
				return false;
			}
			
			public boolean isAvaliable(long limitAvaliableSize){
				if(limitAvaliableSize < 0){
					limitAvaliableSize = 0;
				}
				
				if(this.state!=null && this.state.getAvailable()>limitAvaliableSize && FileSystem.isWritable(this.getPath())){
					return true;
				}
				return false;				
			}
			
			public String toString(){
				return "name: "+this.name+", path: "+this.path+", total: "+this.state.getTotal()+"B, available: "+this.state.getAvailable()+"B, left: "+this.state.getLeft()+"B";
			}
			
			public boolean equals(Object obj){
				try{
					if(!(obj instanceof Volume)){
						return false;
					}
					
					Volume volume = (Volume)obj;
					String path1 = volume.getPath()+"/";
					String path2 = this.getPath()+"/";
					if(path1.startsWith(path2) || path2.startsWith(path1)){
						return true;
					}
					
					return false;
				}catch(Exception e){
					return false;
				}
			}
		}
	}
	/**
	 * memory information data structure
	 */
	public static class MemInfo{
		public long total = 0; // in MB
		public long left = 0; // in MB
		public long buffers = 0;// in MB
		public long cached = 0;// in MB
		
		public MemInfo(long total, long left, long buffers, long cached){
			this.total = total;
			this.left = left;
			this.buffers = buffers;
			this.cached = cached;
		}
	}
}

