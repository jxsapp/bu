package org.bu.android.misc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import org.apache.http.util.EncodingUtils;
import org.bu.android.boot.BuApplication;
import org.bu.android.image.ImageLoaderHolder;
import org.bu.android.log.BuLog;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class BuFileHolder {
	private static final String DIR_NAME = BuApplication.getApplication().getFileRoot();
	public static String DIR = getRootFilePath() + "/" + DIR_NAME + "/";
	public static String DIR_DEBUG = getRootFilePath() + "/" + DIR_NAME + "/debug/";
	public static String DIR_PIC = getRootFilePath() + "/" + DIR_NAME + "/pic/";
	public static String DIR_DOCS = getRootFilePath() + "/" + DIR_NAME + "/docs/";

	static public class RandomFileName {
		public static String getBuFileName() {
			return DIR_PIC + "bu_org.jpg";
		}

		public static String getPicFileName() {
			return DIR_PIC + BuGenerallyHolder.nextSerialNumber() + ".jpg";
		}

		public static String getPicFileName(String url) {
			return DIR_PIC + ImageLoaderHolder.getFileName(url);
		}

		public static String getVersionApk(String url) {
			return DIR_DOCS + BuShortUrlHolder.shortText(url) + ".apk";
		}

	}

	public static File getDCIMFolder() {
		String folder_name = "_camera";
		File file = null;
		if (folder_name.lastIndexOf('/') == folder_name.length() - 1) {
			// ignore final '/' character
			folder_name = folder_name.substring(0, folder_name.length() - 1);
		}
		// if( folder_name.contains("/") ) {
		if (folder_name.startsWith("/")) {
			file = new File(folder_name);
		} else {
			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), folder_name);
		}

		if (!file.exists()) {
			if (!file.mkdirs()) {
				return null;
			}
		}
		return file;
	}

	public static enum Suffix {
		MIN_SUFFIX("_min"), MID_SUFFIX("_mid"), MAX_SUFFIX("_max");
		public String suffix;

		private Suffix(String suffix) {
			this.suffix = suffix;
		}

	}

	public static String getSuffix(String fileName, Suffix suffix) {
		int i = fileName.lastIndexOf('.');
		String name = "";
		String file_suffix = "";
		if ((i > -1) && (i < (fileName.length() - 1))) {
			name = fileName.substring(0, i);
			file_suffix = fileName.substring(i);
		}
		return name + suffix.suffix + file_suffix;
	}

	static public interface HeadIConConfig {

		static final String FILE_NAME_SUFFIX = ".jpg";

		// 头像尺寸大小：
		static final int ICON_MIN_WIDTH = 54;
		static final int ICON_MID_WIDTH = 80;
		// 圆角弧度
		static final int ROUND_CORNE_MIN_PIXELS = 10;
		static final int ROUND_CORNE_MID_PIXELS = 15;

		public static class FileNameHolder {
			static public String getMaxFileName(String uid) {
				if (BuStringUtils.isEmpety(uid)) {
					return "";
				}
				return uid + FILE_NAME_SUFFIX;
			}

			static public String getMinFileName(String uid) {
				if (BuStringUtils.isEmpety(uid)) {
					return "";
				}
				return uid + Suffix.MIN_SUFFIX.suffix + FILE_NAME_SUFFIX;
			}

			static public String getMidFileName(String uid) {
				if (BuStringUtils.isEmpety(uid)) {
					return "";
				}
				return uid + Suffix.MID_SUFFIX.suffix + FILE_NAME_SUFFIX;
			}

		}

	}

	public static String getRootFilePath() {
		if (SdcardHelper.isHasSdcard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();//
		} else {
			return getDataFilesPath();
		}

	}

	public static String getDataFilesPath() {
		return Environment.getDataDirectory().getAbsolutePath() + "/data/" + BuVersionHolder.getPackageName(BuApplication.getApplication()) + "/files"; // filePath:
	}

	public static void createDir() {
		if (SdcardHelper.isHasSdcard()) {
			File destDir = new File(DIR);
			File destPicDir = new File(DIR_PIC);
			File destlogDir = new File(DIR_DEBUG);
			File destDocsDir = new File(DIR_DOCS);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			if (!destPicDir.exists()) {
				destPicDir.mkdirs();
			}
			if (!destlogDir.exists()) {
				destlogDir.mkdirs();
			}
			if (!destDocsDir.exists()) {
				destDocsDir.mkdirs();
			}
		}
	}

	public static class SdcardHelper {
		public static boolean isHasSdcard() {
			String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static String saveFile(byte[] datas, String uri) {
		FileOutputStream fos = null;
		try {
			File file = new File(uri);
			fos = new FileOutputStream(file);
			if (null != fos) {
				fos.write(datas);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uri;
	}

	public static String getFormatFileSize(File f) {
		return FormetFileSize(getFileSizes(f));
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

	public static String FormetFileSize(long fileS) {// 转换文件大小

		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static void savePic(Bitmap b, File file) {
		if (null == b || null == file) {
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
				b.recycle();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void savePic(Bitmap b, String strFileName) {
		if (null == b || BuStringUtils.isEmpety(strFileName)) {
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFileName);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
				b.recycle();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getLoacalBitmap(String url) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(url, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	public static byte[] getBytesFromFile(File f) {
		if (f == null || !f.isFile() || !f.exists()) {
			return new byte[1];
		}
		try {
			return toByteArray2(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] toByteArray2(File f) throws IOException {

		if (!f.exists()) {
			throw new FileNotFoundException(f.getName());
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			String revicedData = Charset.defaultCharset().decode(byteBuffer).toString();
			Log.e("content:", revicedData + "");
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * @param image
	 * @return
	 */
	public static Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap decodeFile(File f) {
		return decodeFile(f, 150, 150, true, BuFileHolder.HeadIConConfig.ROUND_CORNE_MIN_PIXELS);
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	public static Bitmap decodeFile(File file, int width, int height, boolean toRoundCorner, int pixels) {
		try {
			Bitmap bitmap = null;
			// decode image size
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, opts);

			int width_tmp = opts.outWidth, height_tmp = opts.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < width || height_tmp / 2 < height)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
			if (toRoundCorner && null != bitmap) {
				bitmap = BuFileHolder.toRoundCorner(bitmap, pixels);
			}
			return bitmap;
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	public static void deleteFile(File file) {
		if (null != file && file.exists()) {
			file.delete();
		}
	}

	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (null != file && file.exists()) {
			file.delete();
		}
	}

	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			BuLog.e("", "CopyStream catch Exception...");
		}
	}

	/**
	 * 删除指定目录
	 * 
	 * @param method
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		boolean success = true;

		if (dir.exists()) {
			File[] list = dir.listFiles();
			if (list != null) {
				int len = list.length;
				for (int i = 0; i < len; ++i) {
					if (list[i].isDirectory()) {
						deleteDir(list[i]);
					} else {
						boolean ret = list[i].delete();
						if (!ret) {
							success = false;
						}
					}
				}
			}
		} else
			success = false;

		if (success)
			dir.delete();

		return success;
	}

	/**
	 * 删除指定目录
	 * 
	 * @param method
	 * @return
	 */
	public static boolean deleteDir(String path) {
		File dir = new File(path);
		return deleteDir(dir);
	}

	/**
	 * InputStream转字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String getString(InputStream is) {
		try {
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			return EncodingUtils.getString(buffer, "UTF8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();
	}

	public static void copyInputStream(BufferedReader in, BufferedWriter out) throws IOException {
		char[] buffer = new char[1024];
		int len;
		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * 保存为一个文件
	 */
	public static boolean saveFile(File file, String s) {
		boolean ret = false;
		BufferedOutputStream stream = null;
		try {
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(s.getBytes());
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 保存为一个文件
	 */
	public static boolean saveFile(File file, BufferedReader br) {
		boolean ret = false;
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(file));
			copyInputStream(br, output);
			ret = true;
		} catch (Exception e) {
			file.delete();
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 保存为一个文件
	 */
	public static boolean saveFile(File file, InputStream is) {
		boolean ret = false;
		BufferedOutputStream stream = null;
		try {
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);

			copyInputStream(is, stream);
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 保存为一个文件
	 */
	public static boolean saveFile(File dir, String filename, InputStream is) {
		if (!dir.exists())
			dir.mkdirs();

		File file = new File(dir.getPath(), filename);

		if (file.exists()) {
			return true;
		}

		return saveFile(file, is);
	}

	/**
	 * 把字节数组保存为一个文件
	 */
	public static boolean saveFile(File file, byte[] b) {
		return saveFile(file, b, 0, b.length);
	}

	/**
	 * 把字节数组保存为一个文件
	 */
	public static boolean saveFile(File file, byte[] b, int offset, int length) {
		boolean ret = false;
		BufferedOutputStream stream = null;
		try {
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b, offset, length);
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * 把字节数组保存为一个文件
	 */
	public static boolean saveFile(File dir, String filename, byte[] b, int offset, int length) {
		if (!dir.exists())
			dir.mkdirs();

		File file = new File(dir.getPath(), filename);
		return saveFile(file, b, offset, length);
	}

	/**
	 * 把字节数组保存为一个文件
	 */
	public static boolean saveFile(File dir, String filename, byte[] b) {
		return saveFile(dir, filename, b, 0, b.length);
	}

	/**
	 * 读取文件 返回字节数组
	 */
	public static byte[] readFile(File file) {
		try {
			if (!file.exists())
				return null;

			byte[] bs = new byte[(int) file.length()];
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bs);
			fileInputStream.close();
			return (bs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 复制文件
	 * 
	 * @param fs
	 *            源文件
	 * @param fd
	 *            目标文件
	 * @return
	 */
	public static boolean copyFile(InputStream inputStream, File fd) {
		try {
			if (fd.exists())
				fd.delete();
			return saveFile(fd, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean copyFile(File fs, File fd) {
		try {
			if (!fs.exists())
				return false;

			if (fd.exists())
				fd.delete();

			FileInputStream fileInputStream = new FileInputStream(fs);
			return saveFile(fd, fileInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 过滤<, >,\n 字符的方法。
	 * 
	 * @param input
	 *            需要过滤的字符
	 * @return 完成过滤以后的字符串
	 */
	public static String html_filter(String input) {
		if (input == null) {
			return null;
		}
		if (input.length() == 0) {
			return input;
		}
		input = input.replaceAll("&", "&amp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll(" ", "");
		input = input.replaceAll("'", "&#39;");
		input = input.replaceAll("\"", "&quot;");
		input = input.replaceAll("\n", "<br>");

		return html_ConvertURL(input);
	}

	@SuppressLint("DefaultLocale")
	public static String html_ConvertURL(String input) {
		// Check if the string is null or zero length -- if so, return
		// what was sent in.
		if (input == null || input.length() == 0) {
			return input;
		} else {
			StringBuffer buf = new StringBuffer();

			int i = 0, j = 0, oldend = 0;
			int len = input.length();
			char cur;
			while ((i = input.indexOf("http://", oldend)) >= 0) {
				j = i + 7;
				cur = input.charAt(j);
				while (j < len) {
					// Is a space?
					if (cur == ' ')
						break;
					// Is html?
					if (cur == '<')
						break;
					// Is a Win32 newline?
					if (cur == '\n')
						break;
					// Is Unix newline?
					if (cur == '\r' && j < len - 1 && input.charAt(j + 1) == '\n')
						break;

					j++;
					if (j < len) {
						cur = input.charAt(j);
					}
				}
				buf.append(input.substring(oldend, i));
				buf.append("<a href =\"");
				buf.append(input.substring(i, j));
				buf.append("\">");
				buf.append(input.substring(i, j));
				buf.append("</a>");
				oldend = j;
			}
			buf.append(input.substring(j, len));
			return buf.toString();
		}
	}

	public static boolean isImageFile(String filename) {
		String s = filename.toLowerCase();
		return (s.endsWith(".jpeg") || s.endsWith(".jpg") || s.endsWith(".gif") || s.endsWith(".bmp") || s.endsWith(".png"));
	}

	/**
	 * 字符串重编码
	 * 
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String reEncode(String str, String charset) {
		if (charset.toUpperCase().equals("GB2312"))
			return str;

		try {
			return EncodingUtils.getString(str.getBytes("GB2312"), charset);
		} catch (Exception e) {
			return str;
		}
	}

	/**
	 * 字符串重编码
	 * 
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String reEncode2(String str, String charset) {
		if (charset.toUpperCase().equals("UTF8"))
			return str;

		try {
			return EncodingUtils.getString(str.getBytes("UTF8"), charset);
		} catch (Exception e) {
			return str;
		}
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
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

	public static JSONObject readJsonFromAsset(Resources resources, String path) {
		JSONObject res = new JSONObject();
		try {
			BufferedInputStream bis = new BufferedInputStream(resources.getAssets().open(path, Context.MODE_PRIVATE));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int c = bis.read();// 读取bis流中的下一个字节
			while (c != -1) {
				baos.write(c);
				c = bis.read();
			}
			bis.close();
			res = new JSONObject(new String(baos.toByteArray()));
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}
}