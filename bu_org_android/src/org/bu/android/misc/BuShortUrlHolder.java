package org.bu.android.misc;

import java.security.MessageDigest;

public class BuShortUrlHolder {
	public static void main(String[] args) {
		String url = "53FE9D0431AC426389AD3484FF94ABEC/default";
		System.out.println(shortText(url));
		
		url = "53FE9D0431AC426389AD3484FF94ABEC/0";
		System.out.println(shortText(url));
	}

	public static String shortText(String string) {
		String key = "JXS_OrG_bu"; // 自定义生成MD5加密字符串前的混合KEY
		String[] chars = new String[] { // 要使用生成URL的字符
		"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
				"w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
				"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

		String hex = md5Emcode(key + string);

		String outChars = "";
		String subHex = hex.substring(0, 8);
		long idx = Long.valueOf("3FFFFFFF", 16) & Long.valueOf(subHex, 16);

		for (int k = 0; k < 6; k++) {
			int index = (int) (Long.valueOf("0000003D", 16) & idx);
			outChars += chars[index];
			idx = idx >> 5;
		}

		return outChars;
	}

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static String md5Emcode(String origin) {
		String resultString = null;
		try {

			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");

			resultString.trim();

			resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
		} catch (Exception ex) {
		}
		return resultString;
	}
}