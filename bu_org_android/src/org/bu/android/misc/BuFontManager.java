package org.bu.android.misc;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BuFontManager {

	static public class Fonts {

		public static Typeface getHelveticaNeueLTPro_ThEx(Context context1) {
			Context context = new WeakReference<Context>(context1).get();
			return Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTPro-ThEx.otf");
		}

	}

	public static void changeFonts(ViewGroup root, Typeface tf, int style) {
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			setTypeface(v, tf, style);
		}
	}

	public static void setTypeface(View v) {
		setTypeface(v, Fonts.getHelveticaNeueLTPro_ThEx(v.getContext()));
	}

	public static void setTypeface(View v, Typeface tf) {
		if (v instanceof TextView) {// 文本
			((TextView) v).setTypeface(tf);
		} else if (v instanceof Button) {// 按钮
			((Button) v).setTypeface(tf);
		} else if (v instanceof EditText) {// 文本框
			((EditText) v).setTypeface(tf);
		}
	}

	public static void setTypeface(View v, Typeface tf, int style) {
		if (v instanceof TextView) {// 文本
			((TextView) v).setTypeface(tf, style);
		} else if (v instanceof Button) {// 按钮
			((Button) v).setTypeface(tf, style);
		} else if (v instanceof EditText) {// 文本框
			((EditText) v).setTypeface(tf, style);
		}
	}

	public static void changeFonts(ViewGroup root) {
		changeFonts(root, Fonts.getHelveticaNeueLTPro_ThEx(root.getContext()));
	}

	public static void changeFonts(ViewGroup root, Typeface tf) {

		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {// 文本
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {// 按钮
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {// 文本框
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {// 下一级遍历
				changeFonts((ViewGroup) v, tf);
			}
		}
	}

}
