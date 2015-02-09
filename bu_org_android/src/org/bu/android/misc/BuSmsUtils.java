package org.bu.android.misc;

import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

public class BuSmsUtils {

	private static Intent sentIntent = new Intent(BuHandlerHolder.Action.SMS_SEND_ACTIOIN);
	private static Intent deliveryIntent = new Intent(BuHandlerHolder.Action.SMS_DELIVERED_ACTIOIN);

	public static void sendMessage(Context context, String number, String body) {
		List<String> texts = SmsManager.getDefault().divideMessage(body);
		for (String text : texts) {
			BuSmsUtils.natureSendSms(context, number, text);
		}
	}

	/**
	 * @Des 真正的发送短信的地方，将其集中式收回发送，方便测试
	 * @param context
	 * @param number
	 * @param body
	 */
	public static void natureSendSms(Context context, String number, String body) {
		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent, 0);
		PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0, deliveryIntent, 0);
		SmsManager.getDefault().sendTextMessage(number, null, body, sentPI, deliverPI);
		// 暂且注释掉正真发送短信的服务，以便测试
	}

	public static void sendtoMessage(Context context, String number, String body) {
		Uri uri = Uri.parse("smsto:" + number);
		Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
		sendIntent.putExtra("sms_body", body);
		context.startActivity(sendIntent);
	}
}
