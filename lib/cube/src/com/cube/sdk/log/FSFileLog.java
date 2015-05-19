package com.cube.sdk.log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class FSFileLog {
	/**
	 * default  file size limit
	 */
	private final static int DEFAULT_MAX_LOG_FILE_SIZE = 1024 * 1024 * 4; // 4M
	
	/**
	 * default log list count limit
	 */
	private final static int DEFAULT_MAX_LOG_LIST_COUNT = 2000;
	
	/**
	 * log file name
	 */
	private final static String LOG_NAME = "/funshion.log";
	
	/**
	 * period of log timer to schedule
	 */
	private final static long LOG_TIMER_PERIOD = 2000;
	
	/**
	 * the format of log daytime
	 */
	private final static SimpleDateFormat DATA_FORMAT = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss.SSS", Locale.getDefault());
	
	/**
	 * log prefix of funshion @b  
	 */
	private final static String BEHARVIOR_LOG_PREFIX = "behavior";
	
	/**
	 * the spliter between log items  
	 */
	private final static String LOG_SPLITER = ",";

	/**
	 * log path
	 */
	private static String mLogPath = null;

	/**
	 * the size limit of log file
	 */
	private static long mLogFileSizeLimit = DEFAULT_MAX_LOG_FILE_SIZE;
	
	/**
	 * the count limit of log list
	 */
	private static int mLogListCountLimit = DEFAULT_MAX_LOG_LIST_COUNT;
	
	/**
	 * log list to save log string item
	 */
	private static ArrayList<String> mLogList = null;
	
	/**
	 * log list for file write thread
	 */
	private static ArrayList<String> mWriteList = null;
	
	/**
	 * true if internal inited, false if otherwise
	 */
	private static boolean isInternalInit = false;

	/**
	 * file writer to write log
	 */
	private static FileWriter mFileWriter = null;
	
	/**
	 * size of log file
	 */
	private static long mFileSize = 0;

	/**
	 * log timer to write log
	 */
	private static Timer mTimer;

	/**
	 * init file log
	 * 
	 * @param logPath :log path 
	 */
	public static void init(String logPath) {
		mLogPath = logPath;
		makeDirs(mLogPath);
	}
	
	/**
	 * init file log
	 * 
	 * @param logPath:log path
	 * @param logFileSizeLimit :size limit of log file,if log file's size exceed this limit, new empty file should be created.
	 * @param logListCountLimit :count limit of log list,if exceed this limit, log will be droped.
	 */
	public static void init(String logPath, int logFileSizeLimit, int logListCountLimit) {
		mLogPath = logPath;
		mLogFileSizeLimit = logFileSizeLimit;
		mLogListCountLimit = logListCountLimit;
		makeDirs(mLogPath);
	}

	/**
	 * log user behavior info
	 * 
	 * @param message:log message
	 */
	public synchronized static void b(String message) {
		try {
			if (!isInternalInit) {
				mLogList = new ArrayList<String>();
				initTimer();
				isInternalInit = true;
			}

			if (message == null || message.length() <= 0 || mLogList.size() > mLogListCountLimit) {
				return;
			}

			Date date = new Date();
			String logTime = DATA_FORMAT.format(date);

			mLogList.add(logTime + LOG_SPLITER + BEHARVIOR_LOG_PREFIX + LOG_SPLITER + message + "\n");
		} catch (Exception e) {

		}
	}

	/**
	 * get log items from @mLogList to @mWriteList,called by timer thread
	 */
	@SuppressWarnings("unchecked")
	private synchronized static void getLog() {
		if (mLogList != null) {
			mWriteList = (ArrayList<String>) mLogList.clone();
			mLogList.clear();
		}
	}

	/**
	 * write @mWriteList to file,called by timer thread
	 */
	private static void writeLog() {		
		if (mFileWriter == null) {
			openLogFile(false);
		}
		
		if (mWriteList == null || mWriteList.isEmpty() || mFileWriter == null) {
			return;
		}

		try {
			for (int i = 0; i < mWriteList.size(); i++) {
				String message = mWriteList.get(i);
				mFileWriter.write(message);
				mFileSize += message.getBytes().length;
				
				if (mFileSize > mLogFileSizeLimit) {
					openLogFile(true);
				}
			}
			mFileWriter.flush();
		} catch (Exception e) {

		}

		mWriteList.clear();
	}

	/**
	 * open log  file
	 * 
	 * @param deleteOld: true if delete old file, false otherwise
	 */
	private static void openLogFile(boolean deleteOld) {
		try {
			if (mLogPath == null || mLogPath.length() <= 0) {
				return;
			}

			if (mFileWriter != null) {
				mFileWriter.close();
				mFileWriter = null;
			}

			Date date = new Date();
			SimpleDateFormat simpleDateFormate = new SimpleDateFormat(".yyyy_MM_dd", Locale.getDefault());
			String logTime = simpleDateFormate.format(date);
			String realFilePath = mLogPath + LOG_NAME + logTime;

			File logFile = new File(realFilePath);
			if (deleteOld && logFile.exists()) {
				logFile.delete();
			}

			mFileWriter = new FileWriter(logFile, true);
			mFileSize = logFile.length();

		} catch (Exception e) {

		}
	}
	
	/**
	 * init timer
	 */
	private static void initTimer() {
		mTimer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				getLog();
				writeLog();
			}
		};
		mTimer.schedule(timerTask, 0, LOG_TIMER_PERIOD);
	}

	/**
	 * check dir and make dir if not exist
	 * @param path
	 */
	private static void makeDirs(String path) {
		File targetDir = new File(path);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
	}

	public static void test() {
		FSFileLog.init("/mnt/sdcard/funshion");

		
		for (int i = 0; i < 1000; i= i+2) {
			FSFileLog.b("test file log. index:" + i);
			FSFileLog.b("test file log.text：中国  index:" + (i + 1));
		}
	}
}
