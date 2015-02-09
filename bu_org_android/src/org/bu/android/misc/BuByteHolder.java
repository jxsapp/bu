package org.bu.android.misc;

import java.util.List;

public class BuByteHolder {

	public static void append(byte[] target, byte[]... results) {
		int index = 0;
		for (byte[] bytes : results) {
			System.arraycopy(bytes, 0, target, index, bytes.length);
			index += bytes.length;
		}
	}

	public static void append(byte[] target, List<byte[]> results) {
		int index = 0;
		for (byte[] bytes : results) {
			System.arraycopy(bytes, 0, target, index, bytes.length);
			index += bytes.length;
		}
	}

}
