package org.bu.android.misc;

import java.io.File;
import java.lang.ref.WeakReference;

import org.bu.android.boot.BuApplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class BuStartHelper {

	public static Intent getPhotoZoomIntent_NoReturnData(Uri resUri, Uri destUri, int outputX, int outputY) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(resUri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 2);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		intent.putExtra(MediaStore.EXTRA_OUTPUT, destUri);
		return intent;

	}

	public static Intent getPhotoZoomIntent(Uri uri, int outputX, int outputY) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);

		return intent;

	}

	// ////////
	/**
	 * 去是市场评分
	 * 
	 * @param context
	 * @param appId
	 */
	public static void toMarketGrade(Context context, String appId) throws ActivityNotFoundException {
		Context loContext = new WeakReference<Context>(context).get();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?openid=" + appId));
		loContext.startActivity(intent);
	}

	/**
	 * 退出回到桌面
	 * 
	 * @param context
	 */
	public static void toGateoryHome(Context context) {
		Context loContext = new WeakReference<Context>(context).get();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		loContext.startActivity(intent);
	}

	public static Intent getPhotoPickIntent(Uri uri, int widht, int height) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, uri);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", widht);
		intent.putExtra("outputY", height);
		intent.putExtra("return-data", true);
		return intent;
	}

	public static Intent getToDeskIntent() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		return intent;
	}

	public static Intent createShareIntent(String title, String text) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("content/plain");
		intent.putExtra(Intent.EXTRA_TEXT, text);
		return Intent.createChooser(intent, title);
	}

	public static Intent createSmsToIntent(String text) {
		Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
		sendIntent.putExtra("sms_body", text);
		return sendIntent;
	}

	/**
	 * @Des 该方法处理拨打电话
	 * @param phoneNumber
	 */
	public static Intent startDailerCall(String phoneNumber) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:" + phoneNumber));
		return intent;
	}

	public static Intent getCropIntent(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		// intent.putExtra("outputX", 150);
		// intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		return intent;
	}

	public static Bundle getCropImageBundle(Uri uri, int outputX, int outputY) {
		Bundle bundle = new Bundle();
		bundle.putString("crop", "true");
		bundle.putDouble("aspectX", 1);
		bundle.putDouble("aspectY", 2);
		bundle.putInt("outputX", outputX);
		bundle.putInt("outputY", outputY);
		bundle.putBoolean("scale", true);
		bundle.putParcelable(MediaStore.EXTRA_OUTPUT, uri);
		bundle.putBoolean("return-data", false);
		bundle.putString("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		bundle.putBoolean("noFaceDetection", true); // no face detection
		return bundle;
	}

	public static void cropImageUri(Activity activity, Uri uri, int outputX, int outputY, int requestCode) {

		Activity mActivity = new WeakReference<Activity>(activity).get();

		Intent intent = new Intent("com.android.camera.action.CROP");
		// Intent intent = new Intent(mActivity, WeiMiCropper.class);
		intent.setDataAndType(uri, "image/*");
		intent.putExtras(getCropImageBundle(uri, outputX, outputY));
		mActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 拍照选择照片
	 * 
	 * @param filepath
	 * @return
	 */
	public static Intent getTakePhotoIntent(String filepath) {
		/**
		 * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
		 * 文档，you_sdk_path/docs/guide/topics/media/camera.html
		 * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为 官方文档太长了就不看了，其实是错的
		 */
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filepath)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.name());
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, Configuration.ORIENTATION_LANDSCAPE);
		return intent;
	}

	public static void getTakePhotoIntent() {

	}

	/**
	 * 从SDCard中选择照片
	 * 
	 * @return
	 */
	public static Intent getPhotoPickIntent() {
		/**
		 * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
		 * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
		 */
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		/**
		 * 下面这句话，与其它方式写是一样的效果，如果： intent.setData(MediaStore.Images
		 * .Media.EXTERNAL_CONTENT_URI); intent.setType(""image/*");设置数据类型
		 * 如果朋友们要限制上传到服务器的图片类型时可以直接写如 ："image/jpeg 、 image/png等的类型"
		 */
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

		return intent;
	}

	/**
	 * 通过URI 获取到真正的文件地址
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getRealpath(Activity context, Uri uri) {
		Activity activity = new WeakReference<Activity>(context).get();
		// {who=null, request=45313, result=-1, data=Intent {
		// dat=file:///storage/sdcard0/DCIM/Camera/20130617_220736.jpg }} to
		// activity {com.iwxlh.pta/com.iwxlh.pta.report.Report4Congestion}:
		// java.lang.NullPointerException

		String img_path = "";
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
			int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			actualimagecursor.moveToFirst();
			img_path = actualimagecursor.getString(actual_image_column_index);
		} catch (Exception e) {
			img_path = uri.toString();
			if (!BuStringUtils.isEmpety(img_path) && img_path.startsWith("file://")) {
				img_path = img_path.substring(7, img_path.length());
			}
		}
		return img_path;
	}

	public static Intent getIntent(Context context, Class<?> actyClass) {
		Context loContext = new WeakReference<Context>(context).get();
		Intent intent = new Intent(loContext, actyClass);
		return intent;
	}

	public static void startActivity(Context context, Class<?> actyClass) {
		startActivity(context, actyClass, new Bundle());
	}

	public static void startActivity(Context context, Class<?> actyClass, Bundle bundle) {
		Context loContext = new WeakReference<Context>(context).get();
		Intent intent = new Intent(loContext, actyClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

	public static void startService(Context context, Class<?> actyClass) {
		Context loContext = new WeakReference<Context>(context).get();
		Intent intent = new Intent(loContext, actyClass);
		loContext.startService(intent);
	}

	public static void stopService(Context context, Class<?> actyClass) {
		Context loContext = new WeakReference<Context>(context).get();
		Intent intent = new Intent(loContext, actyClass);
		loContext.stopService(intent);
	}

	public static void startActivity(Context context, Intent intent) {
		Context loContext = new WeakReference<Context>(context).get();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		loContext.startActivity(intent);
	}

	public static void start4ResultActivity(Activity activity, Intent intent, int requestCode) {
		Activity localActivity = new WeakReference<Activity>(activity).get();
		localActivity.startActivityForResult(intent, requestCode);
	}

	public static void start4ResultActivity(Activity activity, Class<?> actyClass, int requestCode) {
		Activity localActivity = new WeakReference<Activity>(activity).get();
		Intent intent = new Intent(localActivity, actyClass);
		localActivity.startActivityForResult(intent, requestCode);
	}

	public static void sendBroadCast(String action, Bundle extras) {
		Intent intent = new Intent(action);
		intent.putExtras(extras);
		BuApplication.getApplication().sendBroadcast(intent);
	}

	public static void setResultOK(Activity activity, Bundle data) {
		setResultOK(activity, data, true);
	}

	public static void setResultOK(Activity activity, Bundle data, boolean finish) {
		Activity localActivity = new WeakReference<Activity>(activity).get();
		Intent intent = new Intent();
		intent.putExtras(data);
		localActivity.setResult(Activity.RESULT_OK, intent);
		if (finish) {
			localActivity.finish();
		}
	}

	/**********************************************************************/

	// AndroidExcel
	// android获取一个用于打开PPT文件的intent
	public static Intent getPptFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(String param, boolean paramBoolean) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}

	// android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}

}
