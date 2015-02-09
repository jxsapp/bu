package org.bu.android.misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BuChatUtils {
	public static List<String> collect(String str) {
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("\\[.+?\\]+");
		Matcher m = p.matcher(str);
		while (m.find())
			list.add(m.group());
		return list;
	}

	/*
	 * 从Assets中读取图片
	 */
	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}
}
