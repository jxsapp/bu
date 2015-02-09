package org.bu.android.misc;

import org.bu.android.boot.BuApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;

/**
 * @Des 网络状态工具类
 * @Author jiangxs(jxs)
 * @Date 2011-10-21 下午04:35:57
 */
public final class BuOsStatusHelper {

	public static int SETTING_GPS = 24481;
	public static int SETTING_NET_WORK = 3363;

	public static boolean isGPSEnabled(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}



	public static void setting(final Context context, String title, String message, String yesBtn, final String settingProvider) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message).setCancelable(false).setPositiveButton(yesBtn, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface Dialog, int id) {
				context.startActivity(new Intent(settingProvider));
				Dialog.cancel();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface Dialog, int id) {
				Dialog.cancel();
				if (context instanceof Activity) {
					if (Settings.ACTION_WIRELESS_SETTINGS.equals(settingProvider)) {
						if (!isNetworkAvailable()) {
							((Activity) context).finish();
						}
					} else if (Settings.ACTION_LOCATION_SOURCE_SETTINGS.equals(settingProvider)) {
						if (!isGPSEnabled(context)) {
							((Activity) context).finish();
						}
					}
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static boolean isNetworkAvailable() {
		Context context = BuApplication.getApplication();
		/*
		 * ConnectivityManager cm = (ConnectivityManager)
		 * WeiMiApplication.getWeiMiApplication
		 * ().getSystemService(Context.CONNECTIVITY_SERVICE); if (cm == null) {
		 * return false; } else { NetworkInfo[] ni = cm.getAllNetworkInfo(); if
		 * (ni != null) { for (int i = 0; i < ni.length; i++) { if
		 * (ni[i].getState() == NetworkInfo.State.CONNECTED) { return true; } }
		 * } }
		 */
		return hasInternet(context);
	}

	private static boolean hasInternet(Context context) {
		boolean flag = false;
		ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectionManager.getActiveNetworkInfo() != null) {
			flag = connectionManager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}

	/**
	 * 判断当前连接模式是否为WIFI
	 * 
	 * @param context
	 * @return 返回true为wifi
	 */
	public static boolean isWIFIConnection(Context context) {
		return ConnectivityManager.isNetworkTypeValid(ConnectivityManager.TYPE_WIFI);
	}

}
