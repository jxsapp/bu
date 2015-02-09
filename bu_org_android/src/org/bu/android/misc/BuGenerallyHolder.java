package org.bu.android.misc;

import java.util.Date;
import java.util.UUID;

public class BuGenerallyHolder {



	public static String getTimeSerial() {
		return BuTimer.getSDFyyMMdd().format(new Date());
	}

	public static String getGroupId() {
		return BuTimer.getSDFyyyyMMddHHmmss().format(new Date());
	}

	public static String nextSerialNumber() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("\\-", "");
	}


}
