package org.bu.android.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.bu.android.boot.BuApplication;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

/**
 * 带日志文件输入的，又可控开关的日志调试
 */
@SuppressLint("SimpleDateFormat")
public class BuLog {

	private static String _LOG_DIR = "/" + BuApplication.getApplication().getFileRoot() + "/debug";
	private static boolean _DEBUG_SWITCH = true;// 日志总开关
	private static boolean _WRITE_TO_FILE = true;// 日志写入文件开关

	private static char _WEI_DEBUG_DEFAULT_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
	private static int _SDCARD_DEBUG_FILE_SAVE_DAYS = 0;// sd卡中日志文件的最多保存天数
	private static String _DEBUG_FILE_NAME_FORMAT = "_DEBUG.java";// 本类输出的日志文件名称
	private static SimpleDateFormat DEBUG_SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
	private static SimpleDateFormat DEBUG_FILE = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式

	private static class SdcardHelper {
		public static boolean isHasSdcard() {
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		}
	}

	private static String getRootFilePath() {
		if (SdcardHelper.isHasSdcard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data"; // filePath:
																				// /data/data/
		}
	}

	private static String getFilePath() {
		return getRootFilePath() + _LOG_DIR;// 日志文件在sdcard中的路径
	}

	static {
		File destDir = new File(getFilePath());
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
	}

	public static void w(String tag, Object msg) { // 警告信息
		log(tag, msg.toString(), 'w');
	}

	public static void e(String tag, Object msg) { // 错误信息
		log(tag, msg.toString(), 'e');
	}

	public static void e(String tag, Object msg, Exception e) { // 错误信息
		log(tag, msg.toString(), 'e', e);
	}

	public static void d(String tag, Object msg) {// 调试信息
		log(tag, msg.toString(), 'd');
	}

	public static void i(String tag, Object msg) {//
		log(tag, msg.toString(), 'i');
	}

	public static void v(String tag, Object msg) {
		log(tag, msg.toString(), 'v');
	}

	public static void w(String tag, String text) {
		log(tag, text, 'w');
	}

	public static void w(String tag, String text, Exception e) {
		log(tag, text, 'w', e);
	}

	public static void e(String tag, String text) {
		log(tag, text, 'e');
	}

	public static void d(String tag, String text) {
		log(tag, text, 'd');
	}

	public static void i(String tag, String text) {
		log(tag, text, 'i');
	}

	public static void v(String tag, String text) {
		log(tag, text, 'v');
	}

	/**
	 * 根据tag, msg和等级，输出日志
	 * 
	 * @param tag
	 * @param msg
	 * @param level
	 * @return void
	 * @since v 1.0
	 */
	private static void log(String tag, String msg, char level, Exception e) {
		if (_DEBUG_SWITCH) {
			if ('e' == level && ('e' == _WEI_DEBUG_DEFAULT_TYPE || 'v' == _WEI_DEBUG_DEFAULT_TYPE)) { // 输出错误信息
				if (null != e) {
					Log.e(tag, msg, e);
				} else {
					Log.e(tag, msg);
				}
			} else if ('w' == level && ('w' == _WEI_DEBUG_DEFAULT_TYPE || 'v' == _WEI_DEBUG_DEFAULT_TYPE)) {
				if (null != e) {
					Log.w(tag, msg, e);
				} else {
					Log.w(tag, msg);
				}
			} else if ('d' == level && ('d' == _WEI_DEBUG_DEFAULT_TYPE || 'v' == _WEI_DEBUG_DEFAULT_TYPE)) {
				Log.d(tag, msg);
			} else if ('i' == level && ('d' == _WEI_DEBUG_DEFAULT_TYPE || 'v' == _WEI_DEBUG_DEFAULT_TYPE)) {
				Log.i(tag, msg);
			} else {
				Log.v(tag, msg);
			}
			if (_WRITE_TO_FILE)
				writeDEBUGtoFile(String.valueOf(level), tag, msg);
		}
	}

	private static void log(String tag, String msg, char level) {
		log(tag, msg, level, null);
	}

	/**
	 * 打开日志文件并写入日志
	 * 
	 * @return
	 * **/
	private static void writeDEBUGtoFile(String myDEBUGtype, String tag, String text) {// 新建或打开日志文件
		Date nowtime = new Date();
		String needWriteFiel = DEBUG_FILE.format(nowtime);
		String needWriteMessage = DEBUG_SDF.format(nowtime) + "    " + myDEBUGtype + "    " + tag + "    " + text;
		File file = new File(getFilePath(), needWriteFiel + _DEBUG_FILE_NAME_FORMAT);
		try {
			FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除制定的日志文件
	 * */
	public static void delFile() {// 删除日志文件
		String needDelFiel = DEBUG_FILE.format(getDateBefore());
		File file = new File(getFilePath(), needDelFiel + _DEBUG_FILE_NAME_FORMAT);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
	 * */
	private static Date getDateBefore() {
		Date nowtime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowtime);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - _SDCARD_DEBUG_FILE_SAVE_DAYS);
		return now.getTime();
	}

}
