package org.bu.android.image;

import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bu.android.misc.BuFileHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * @String imageUri = "http://site.com/image.png"; // from Web
 * @String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
 * @String imageUri = "content://media/external/audio/albumart/13"; // from
 *         content provider
 * @String imageUri = "assets://image.png"; // from assets
 * @String imageUri = "drawable://" + R.drawable.image; // from drawables (only
 *         images, non-9patch)
 * @author jxs
 * @time 2014-3-11 下午11:56:28
 */
public class ImageLoaderHolder {

	public static enum UriType {
		HTTP("http://"), FILE("file://"), CONTENT("content://"), ASSETS("assets://"), DRAWABLE("drawable://");

		public String agreement = "";

		private UriType(String agreement) {
			this.agreement = agreement;
		}
	}

	public static String formatUrl(UriType uriType, String path) {
		return String.format(uriType.agreement + "%s", path);
	}

	public static void init(Context context, int default_pic) {
		File cacheDir = new File(BuFileHolder.DIR_PIC);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()//
				.showImageForEmptyUri(default_pic)//
				.showImageOnFail(default_pic)//
				.cacheInMemory()//
				.cacheOnDisc()//
				.build();//
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)//
				.defaultDisplayImageOptions(defaultOptions)//
				// 将保存的时候的URI名称用MD5 加密
				.discCacheFileNameGenerator(new FileNameGenerator() {

					@Override
					public String generate(String url) {
						return getFileName(url);
					}
				})//
				.tasksProcessingOrder(QueueProcessingType.LIFO)//
				.threadPoolSize(7)// 线程池内加载的数量
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(500)// 缓存一百张图片
				.discCache(new UnlimitedDiscCache(cacheDir, new FileNameGenerator() {

					@Override
					public String generate(String url) {
						return getFileName(url);
					}
				}))// 自定义缓存路径
				.memoryCacheExtraOptions(480, 800) // max width, max
													// height，即保存的每个缓存文件的最大长宽
				.memoryCacheSize(4 * 1024 * 1024)
				// You can pass your own memory cache implementation/你可
				.memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
				// connectTimeout (8 s), readTimeout (30 s)超时时
				.imageDownloader(new BaseImageDownloader(context, 8 * 1000, 30 * 1000))//
				.build();//

		ImageLoader.getInstance().init(config);

	}

	@SuppressLint("DefaultLocale")
	public static String getFileName(String url) {
		if (null == url) {
			url = "";
		}
		String fid = "";
		Pattern p = null;
		if (url.toLowerCase(Locale.CHINA).indexOf(".jpg") > 0 || url.toLowerCase(Locale.CHINA).indexOf(".png") != -1) {
			p = Pattern.compile("get/(.*).jpg/[A-Za-z0-9]{16}");
			if (url.toLowerCase(Locale.CHINA).indexOf(".png") != -1) {
				p = Pattern.compile("get/(.*).png/[A-Za-z0-9]{16}");
			}
		} else {
			// http://risk.maiziji.cn/v1/debt/images/${imageId}/${accessToken}
			p = Pattern.compile("images/[A-Za-z0-9]{1,50}");
		}
		if (null != p) {
			Matcher m = p.matcher(url);
			while (m.find()) {
				fid = m.group(1);
				break;
			}
		}
		if (null != fid && !"".equals(fid)) {
			fid = fid + ".jpg";
		} else {
			int lastIndex = url.lastIndexOf("/");
			if (lastIndex > 0 && lastIndex < url.length() - 1) {
				fid = url.substring(lastIndex + 1);
			}
		}
		return fid;
	}

	public static void displayImage(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView);
	}

	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(uri, imageView, options);
	}

	public static void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(uri, imageView, listener);
	}

	public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(uri, imageView, options, listener);
	}

}
