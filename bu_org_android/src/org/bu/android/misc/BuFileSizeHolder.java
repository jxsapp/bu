package org.bu.android.misc;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

public class BuFileSizeHolder {

	public static long getFolderSize(java.io.File file) throws Exception {
		long size = 0;
		java.io.File[] fileList = file.listFiles();
		for (File subFile : fileList) {
			if (subFile.isDirectory()) {
				size = size + getFolderSize(subFile);
			} else {
				size = size + subFile.length();
			}
		}
		return size;
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return double 单位为M
	 * @throws Exception
	 */
	public static String getFolderSize4M(java.io.File file) throws Exception {
		return formatFileSize(getFolderSize(file));
	}

	public static String formatFileSize(long fileSize) {// 转换文件大小

		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize == 0) {
			fileSizeString = "0K";
		} else if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + "B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + "K";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static String formatSize(String strSize) {
		String s;
		int L = strSize.length();
		if (L < 4)
			s = "0." + strSize.substring(0, 1) + "k";
		else if (L > 6)
			s = strSize.substring(0, L - 6) + "." + strSize.substring(6, 7) + "M";
		else if (L == 4)
			s = strSize.substring(0, 1) + "." + strSize.substring(1, 2) + "k";
		else
			s = strSize.substring(0, L - 3) + "k";
		return (s);
	}

	public static String getFormatFileSize(File f) {
		return formatFileSize(BuFileSizeHolder.getFileSizes(f));
	}

	public static long getFileSizes(File f) {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(f);
				s = fis.available();
			} catch (FileNotFoundException e) {
				s = 0;
			} catch (IOException e) {
				s = 0;
			}
		}
		return s;
	}

	public static int getPttTime(File pttFile) {
		int time = 1;
		int size = BuFileHolder.getBytesFromFile(pttFile).length;
		time = size / 62 * 20 / 1000 + 1;
		return time;
	}
}
