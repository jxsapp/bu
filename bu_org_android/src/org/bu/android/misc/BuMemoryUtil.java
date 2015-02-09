package org.bu.android.misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.bu.android.log.BuLog;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.text.format.Formatter;

@SuppressLint("DefaultLocale")
public class BuMemoryUtil {

	public static String getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		return Formatter.formatFileSize(context, mi.availMem);
	}

	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统内存大小
			if (BuStringUtils.isEmpety(str2)) {
				str2 = "";
			}
			arrayOfString = str2.split("\\s+");
			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位KB
			localBufferedReader.close();
		} catch (IOException e) {

		}
		return Formatter.formatFileSize(context, initial_memory);
	}

	/**
	 * @DES 采用临界内存/可用内存的比 如果 达到80%，即开始清理内存
	 * @param context
	 */
	public static void destroyOtherProcess(Context context) {

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		long availMem = mi.availMem;// 可用内存
		long threshold = mi.threshold;// 临界内存
		if (availMem <= threshold * 5) {
			ActivityManager activityManger = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManger.getRunningAppProcesses();
			for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
				String processName = appProcessInfo.processName;
				if (processName.indexOf("sptas") == -1) {
					BuLog.i("MemoryUtil", "清理掉进程的名字" + processName);
					activityManger.killBackgroundProcesses(processName);
				}
			}
		}

	}

	public static boolean isRun(Context context, Class<?> clazz) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(clazz.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 */
	public static void clearEmptyTask(Context context) {
		ActivityManager activityManger = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = activityManger.getRunningAppProcesses();
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				ActivityManager.RunningAppProcessInfo apinfo = list.get(i);
				String[] pkgList = apinfo.pkgList;
				if (apinfo.importance >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_EMPTY) {// >=300
					android.os.Process.killProcess(apinfo.pid);
					for (int j = 0; j < pkgList.length; j++) {
						activityManger.killBackgroundProcesses(pkgList[j]);
					}
				}
			}
	}

	/**
	 * @Des 判断当前是否是别的应用于左面前端
	 * @return
	 */
	public static boolean currentIsOtherApp(Context context) {
		ActivityManager activityManger = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String currentActivity = "";
		List<ActivityManager.RunningTaskInfo> runEs = activityManger.getRunningTasks(1);
		if (!runEs.isEmpty()) {
			ActivityManager.RunningTaskInfo fristInfo = runEs.get(0);
			if (null != fristInfo)
				currentActivity = fristInfo.topActivity.getClassName(); // 完整类名
		}

		if (!BuStringUtils.isEmpety(currentActivity) && -1 == currentActivity.indexOf("sptas")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Des 杀死包进程
	 * @param activityManger
	 */
	static void destroySelfProcess(ActivityManager activityManger) {
		List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManger.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
			int pid = appProcessInfo.pid;
			String processName = appProcessInfo.processName;
			if (-1 != processName.indexOf("com.wxlh.sptas")) {
				android.os.Process.killProcess(pid);
			}
		}
	}

	/**
	 * 判断是否是注册页面
	 * 
	 * @return
	 */

	public static boolean isCurrentPage(Context context, Class<?> clazz) {
		boolean isRegisterPage = false;
		String currentActivity = "";
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		if (!manager.getRunningTasks(1).isEmpty()) {
			RunningTaskInfo info = manager.getRunningTasks(1).get(0);
			if (null != info)
				currentActivity = info.topActivity.getClassName(); // 完整类名
		}
		if (!BuStringUtils.isEmpety(currentActivity) && (clazz.getName().toLowerCase(Locale.CHINA).equals(currentActivity.toLowerCase(Locale.CHINA)))) {
			isRegisterPage = true;
		}
		return isRegisterPage;
	}

	/**
	 * @Des 杀死工具进程下所有的服务
	 * @param activityManger
	 */
	static void stopSelfService(ActivityManager activityManger) {
		List<ActivityManager.RunningServiceInfo> appProcessList = activityManger.getRunningServices(30);
		for (ActivityManager.RunningServiceInfo appProcessInfo : appProcessList) {
			if (-1 != appProcessInfo.process.indexOf("com.wxlh.sptas:tools")) {
				ComponentName service = appProcessInfo.service;
				activityManger.killBackgroundProcesses(service.getPackageName());
			}
		}
	}

}
