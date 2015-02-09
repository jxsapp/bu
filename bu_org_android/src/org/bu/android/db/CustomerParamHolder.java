package org.bu.android.db;

import org.bu.android.boot.BuApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CustomerParamHolder {

	static public interface Value {
		static final String PARAM_YES = "1";
		static final String PARAM_NO = "0";
	}

	static public interface Prefix {
		public static final String _PREFIX = "prefix_";
	}

	public static void saveOrUpdate(String key, String value, String cuid) {

		SharedPreferences sharedPrefs = BuApplication.getApplication().getSharedPreferences(Prefix._PREFIX + cuid, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getParamValue(String key, String cuid) {
		SharedPreferences sharedPrefs = BuApplication.getApplication().getSharedPreferences(Prefix._PREFIX + cuid, Context.MODE_PRIVATE);
		return sharedPrefs.getString(key, "");
	}

}
