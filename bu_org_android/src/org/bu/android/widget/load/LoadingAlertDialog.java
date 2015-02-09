package org.bu.android.widget.load;

import org.bu.android.R;
import org.bu.android.misc.BuScreenHolder;
import org.bu.android.misc.BuStringUtils;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class LoadingAlertDialog {

	private Context context;
	private Dialog dialog;

	public LoadingAlertDialog(Context context) {
		this.context = context;
		this.dialog = builder4Loading(context, "");
	}

	public static Dialog builder4Loading(Context context, String msg) {
		final Dialog dialog = new Dialog(context, R.style.IPone_Dialog_Bg);
		dialog.setContentView(R.layout.bu_loading_alert);
		BuScreenHolder.matchParent(context, dialog.getWindow());
		TextView message = ((TextView) dialog.findViewById(R.id.alert_message));
		if (!BuStringUtils.isEmpety(msg)) {
			message.setText(msg);
		}
		return dialog;
	}

	public boolean isShowing() {
		if (null != dialog) {
			return dialog.isShowing();
		}
		return false;
	}

	public void show() {
		if (null != dialog)
			dialog.show();
	}

	public void showLoading(int msgId) {//
		showLoading(context.getString(msgId));
	}

	public void showLoading(String msg) {//
		this.dialog = builder4Loading(context, msg);
		this.show();
	}

	public void dismiss() {
		if (null != dialog)
			dialog.dismiss();
	}

	public void cancel() {
		if (null != dialog)
			dialog.cancel();
	}

}