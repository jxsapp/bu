package org.bu.android.misc;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class BuVersionHolder {
	static public interface OsVersion {
		public final static boolean IS_LESS_SDK_14 = (android.os.Build.VERSION.SDK_INT < 14) ? true : false;
	}

	public static String getPackageName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			packInfo = new PackageInfo();
			packInfo.packageName = "";
		}
		return packInfo.packageName;
	}

	public static String getVersionName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			packInfo = new PackageInfo();
			packInfo.versionName = "";
		}
		return packInfo.versionName;
	}

	public static boolean hasNewVersion(Context context, String dataBaseV) {
		String currentV = getVersionName(context);
		if (!BuStringUtils.isEmpety(dataBaseV)) {
			if (dataBaseV.compareTo(currentV) <= 0) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 下载完成之后安装
	 */
	public static void install(Context context, File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

}
