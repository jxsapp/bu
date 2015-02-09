package org.bu.android.misc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.bu.android.boot.BuApplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class BuApkHolder {

	public static boolean isAvilible(String packageName) {
		// 获取packagemanager
		final PackageManager packageManager = BuApplication.getApplication().getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		// 用于存储所有已安装程序的包名
		List<String> packageNames = new ArrayList<String>();
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (packageInfos != null) {
			for (int i = 0; i < packageInfos.size(); i++) {
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		// 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}

	public static void startApk(Context context, ResolveInfo resolveInfo) {
		ComponentName componentName = new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setComponent(componentName);
		intent.setAction(Intent.ACTION_VIEW);
		Context mContext = new WeakReference<Context>(context).get();
		mContext.startActivity(intent);
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

	public static ResolveInfo getInstalled(String packageName) {
		// 应用过滤条件
		ResolveInfo rest = null;
		final PackageManager packageManager = BuApplication.getApplication().getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> mAllApps = packageManager.queryIntentActivities(mainIntent, 0);
		for (ResolveInfo rv : mAllApps) {
			if (rv.activityInfo.packageName.equals(packageName)) {
				rest = rv;
				break;
			}
		}

		return rest;
	}
}
