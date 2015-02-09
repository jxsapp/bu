package org.bu.android.widget;

import org.bu.android.R;
import org.bu.android.boot.BuApplication;
import org.bu.android.misc.HandlerHolder;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BuToast {

	enum BuToastStyle {
		NONE,INFO,ERROR,WARN;
	}

	public static void show(String msg) {
		Context context = BuApplication.getApplication();
		final Toast toast = new Toast(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.bu_toast, null);
		SpannableString spannableString = new SpannableString(msg);
		((TextView) contentView.findViewById(R.id.alert_message)).setText(spannableString);
		toast.setView(contentView);
		// toast.setGravity(Gravity.TOP, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void show(String msg, BuToastStyle type) {
		Context context = BuApplication.getApplication();
		final Toast toast = new Toast(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.bu_toast, null);
		SpannableString spannableString = new SpannableString(msg);
		((TextView) contentView.findViewById(R.id.alert_message)).setText(spannableString);
		toast.setView(contentView);
		// toast.setGravity(Gravity.TOP, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void show(int msgId) {
		show(BuApplication.getApplication().getString(msgId));
	}

	public static void sendBoradCastMsg(String message, Bundle data) {
		Message msg = new Message();
		msg.setData(data);
		msg.what = HandlerHolder.WHAT_MSG;
		msg.obj = message;
		BuApplication.getApplication().getHandler().sendMessage(msg);
	}

	public static void sendBoradCastMsg(int resId) {
		sendBoradCastMsg(BuApplication.getApplication().getString(resId), null);
	}

	public static void sendBoradCastMsg(String message) {
		sendBoradCastMsg(message, null);
	}
}
