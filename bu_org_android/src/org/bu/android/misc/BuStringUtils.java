package org.bu.android.misc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Jiangxs
 * @Date 2011-10-6 下午10:47:21
 * @Des 字符串工具类
 */
public class BuStringUtils {

	public static double getDouble(String text) {
		try {
			return Double.valueOf(text);
		} catch (Exception e) {

		}
		return 0;
	}

	public static String replaceCode_eq_se(String text) {
		text = null == text ? "" : text;
		return text.replaceAll("=", "{eq}").replaceAll(";", "{se}");
	}

	public static String replaceCode_dqm(String text) {
		text = null == text ? "" : text;
		return text.replaceAll("\"", "");
	}

	public static String replaceCode_bracket(String text) {
		text = null == text ? "" : text;
		return text.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
	}

	public static String ReplaceDot(String text) {
		text = null == text ? "" : text;
		return text.replaceAll("，", ",");
	}

	public static String ReplaceDots(String text) {
		text = null == text ? "" : text;
		return text.replaceAll("，", "").replaceAll(",", "");
	}

	/**
	 * @Des 提取字符串中的字符到一个Long的List集合中
	 * @param text
	 * @return
	 */
	public static List<Long> getLongDigit(String text) {
		text = null == text ? "" : text;
		List<Long> digitList = new ArrayList<Long>();
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(text);
		while (m.find()) {
			String find = m.group(1).toString();
			digitList.add(Long.valueOf(find));
		}
		return digitList;
	}

	public static Set<Integer> getNumsFromStr(String text) {
		text = null == text ? "" : text;
		String[] ary = text.replaceAll("[^\\d]", " ").split("\\s+");
		Set<Integer> set = new TreeSet<Integer>();
		for (String num : ary) {
			if (!num.trim().equals("")) {
				set.add(Integer.valueOf(num.trim()));
			}
		}
		return set;
	}

	/**
	 * @Des 提取字符串中的字符到一个String的List集合中
	 * @param text
	 * @return
	 */
	public static List<String> getStringDigit(String text) {
		text = null == text ? "" : text;
		List<String> digitList = new ArrayList<String>();
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(text);
		while (m.find()) {
			String find = m.group(1).toString();
			digitList.add(find);
		}
		return digitList;
	}

	public static String getStringDigits(String text) {
		text = null == text ? "" : text;
		StringBuilder digitList = new StringBuilder("");
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(text);
		while (m.find()) {
			String find = m.group(1).toString();
			digitList.append(find);
		}
		return digitList.toString();
	}

	public static int getE6Integer4DoubleString(String text) {
		text = null == text ? "" : text;
		DecimalFormat decimalFormat = new DecimalFormat("#");
		return Integer.valueOf(decimalFormat.format((Double.valueOf(text) * 1E6)));
	}

	public static boolean isEmpety(String msg) {
		boolean result = false;
		if (null == msg || "".equals(msg.trim())) {
			result = true;
		}
		return result;
	}

	public static String flushLeft(int length, String string) {
		return flushLeft(' ', length, string);
	}

	public static String flushLeft(char fillFlag, int length, String string) {
		String str = "";
		StringBuilder cs = new StringBuilder("");
		if (string.length() > length) {
			str = string;
		} else {
			for (int i = 0; i < length - string.length(); i++) {
				cs.append(fillFlag);
			}
		}
		str = string + cs.toString();
		return str;
	}

	/** 字符串拷贝 */
	public static String getString(byte[] dest, byte[] bytes, int srcPos, int length) {
		System.arraycopy(bytes, srcPos, dest, 0, length);
		return new String(dest);
	}
}
