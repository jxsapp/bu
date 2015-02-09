package org.bu.android.misc;


public class BuIntegerHolder {

	public static int getInt(String val) {

		try {
			return Integer.valueOf(val);
		} catch (Exception e) {
			return 0;
		}

	}

}
