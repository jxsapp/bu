package org.bu.android.misc;


public class BuASCIIHolder {

	public static int stringToAscii(String value) {
		int sbu = 0;
		value = BuStringUtils.isEmpety(value) ? "" : value;
		for (char chr : value.toCharArray()) {
			sbu += (int) chr;
		}
		return sbu;
	}
}
