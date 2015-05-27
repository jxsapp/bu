package org.bu.android.widget;

import org.bu.android.R;
import org.bu.android.boot.BuApplication;
import org.bu.android.misc.BuHandlerHolder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class BuToast {

	public static void show(String msg, Integer... duration) {
		Context context = BuApplication.getApplication();
		final Toast toast = new Toast(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.bu_toast, null);
		((TextView) contentView.findViewById(R.id.alert_message)).setText(msg);
		toast.setView(contentView);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}

	public static void show(int msgId, Integer... duration) {
		show(BuApplication.getApplication().getString(msgId), duration);
	}

	public static void sendBoradCastMsg(String message, String number, boolean floatViewNavi) {
		Context context = BuApplication.getApplication();
		if (floatViewNavi) {
			Intent intent = new Intent(BuHandlerHolder.Action.LISTERN_PHONE_STATE);
			Bundle bundle = new Bundle();
			bundle.putString("msg", message);
			bundle.putString("number", number);
			intent.putExtras(bundle);
			context.sendBroadcast(intent);
		} else {
			Message msg = new Message();
			msg.what = BuHandlerHolder.What.MESSAGE;
			msg.obj = message;
			BuApplication.getApplication().getHandler().sendMessage(msg);
		}
	}

	public static void sendBoradCastMsg(int resId, String number) {
		sendBoradCastMsg(BuApplication.getApplication().getString(resId), number, false);
	}

	public static void sendBoradCastMsg(String message, String number) {
		sendBoradCastMsg(message, number, false);
	}

	public static void sendBoradCastMsg(int resId) {
		sendBoradCastMsg(BuApplication.getApplication().getString(resId), "", false);
	}

	public static void sendBoradCastMsg(String message) {
		sendBoradCastMsg(message, "", false);
	}
}