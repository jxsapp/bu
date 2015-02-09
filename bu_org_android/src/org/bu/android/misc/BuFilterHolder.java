package org.bu.android.misc;



public class BuFilterHolder {

	/**
	 * filter the JSON Object key:"null" the mean is key:null
	 * 
	 * @param id
	 * @return
	 */
	public static boolean isEmpety(String id) {
		boolean rst = false;
		if (BuStringUtils.isEmpety(id) || "null".equalsIgnoreCase(id)) {
			rst = true;
		}
		return rst;
	}

}
