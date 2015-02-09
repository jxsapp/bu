package org.bu.android.misc;

import java.io.IOException;
import java.util.Properties;

public final class BuPropertiesHolder {
	private static Properties props;
	static {
		props = new Properties();
		try {
			props.load(BuPropertiesHolder.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			throw new RuntimeException("载入属性文件失败");
		}
	}

	public static Integer getIntValue(String key, String value) {
		return Integer.valueOf(props.getProperty(key, value));
	}

	public static Integer getIntValue(String key) {
		return getIntValue(key, "0");
	}

	public static Long getLongValue(String key, String value) {
		return Long.valueOf(props.getProperty(key, value));
	}

	public static Long getLongValue(String key) {
		return getLongValue(key, "0");
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}
}