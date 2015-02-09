package org.bu.android.misc;

public class BuEncryptstr {

	private static String DEFAULT_KEY = "JXS.AnDRoiD.OrG";

	private static String encryptkey(String s, int i) {
		String s1 = s;
		int j = s.length();
		if (i <= j) {
			s1 = s.substring(0, i);
		} else {
			for (; j < i; j = s1.length()) {
				if (j + s.length() >= i) {
					s1 = s1 + s.substring(0, i - j);
				} else {
					s1 = s1 + s;
				}
			}
		}
		return s1;
	}

	/**
	 * @Des 加密
	 * @param str
	 * @return
	 */
	public static String encrypt(String str) {
		String s2 = "";
		if ("".equals(str)) {
			return s2;
		}
		if ("".equals(DEFAULT_KEY)) {
			return s2;
		}
		int length = str.length();
		String s3 = encryptkey(DEFAULT_KEY, length);
		for (int k = 0; k < length; k++) {
			char c = str.charAt(k);
			char c1 = s3.charAt(k);
			char c2 = c;
			char c3 = c1;
			int j = c2 ^ c3;
			j += 1;
			j = 1000 + ((j / 10) % 10) * 100 + (j / 100) * 10 + j % 10;
			s2 = s2 + Integer.toString(j).substring(1);
		}

		return s2;
	}

	/**
	 * @Des 解密
	 * @param str
	 * @return
	 */
	public static String decode(String str) {
		String s2 = "";
		if ("".equals(str)) {
			return s2;
		}
		if ("".equals(DEFAULT_KEY)) {
			return s2;
		}
		int i = str.length() / 3;
		String s3 = encryptkey(DEFAULT_KEY, i);
		for (int l = 0; l < i; l++) {
			String s4 = str.substring(l * 3, (l + 1) * 3);
			char c1 = s3.charAt(l);
			int k = Integer.parseInt(s4);
			k = ((k / 10) % 10) * 100 + (k / 100) * 10 + k % 10;
			char c2 = c1;
			int j = k - 1 ^ c2;
			char c = (char) j;
			s2 = s2 + c;
		}

		return s2;
	}

	public static void main(String args[]) throws Exception {
		// 以下是测试
		System.out.println("以下是测试");
		String str = "123456789043243261";

		str = encrypt(str);// 加密
		System.out.println(str);

		str = decode(str); // 解密
		System.out.println(str);
	}

}
