package org.bu.android.widget;

import org.bu.android.R;
import org.bu.android.misc.BuScreenHolder;
import org.bu.android.misc.BuStringUtils;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class BuAlertDailog {

	public static enum HIND_BTN_TYPE {
		NULL, COMFIRM, CACEL;
	}

	static public abstract class BuAlertlListener {
		public String getConfirmTxt() {
			return null;
		}

		public String getCancleTxt() {
			return null;
		}

		public int getGravity() {
			return Gravity.CENTER;
		}

		public HIND_BTN_TYPE hindBtn() {
			return HIND_BTN_TYPE.NULL;
		}

		public void onConfirm(Context context, Dialog dialog) {
			dialog.dismiss();
		}

		public void onCancel(Dialog dialog) {
			dialog.dismiss();
		}
	}

	static public abstract class AlertLayoutInflaterListener extends BuAlertlListener {

		public int getGravity() {
			return Gravity.LEFT;
		}

		public abstract void onInflater(Dialog dialog, View view);

	}

	static public abstract class BuAlertDailogListenser {
		public String getConfirmTxt() {
			return null;
		}

		public void onConfirm(Dialog dialog) {
			dialog.dismiss();
		}
	}

	public static Dialog builder(final Context context, String title, final String message, final BuAlertlListener listener) {
		final Dialog dialog = new Dialog(context, R.style.IPone_Dialog_Bg);
		dialog.setContentView(R.layout.bu_alert_dailog);
		BuScreenHolder.matchParent(context, dialog.getWindow());

		TextView titleTv = ((TextView) dialog.findViewById(R.id.dialog_title));
		titleTv.setText(title);

		if (BuStringUtils.isEmpety(title)) {
			titleTv.setVisibility(View.GONE);
		}

		TextView msgTv = ((TextView) dialog.findViewById(R.id.dialog_message));
		msgTv.setGravity(listener.getGravity());
		msgTv.setText(message);

		String confirmText = context.getString(R.string.prompt_confirm);
		String cancelText = context.getString(R.string.prompt_cancel);
		if (null != listener) {
			if (!BuStringUtils.isEmpety(listener.getConfirmTxt())) {
				confirmText = listener.getConfirmTxt();
			}
			if (!BuStringUtils.isEmpety(listener.getCancleTxt())) {
				cancelText = listener.getCancleTxt();
			}
		}

		if (listener.hindBtn() == HIND_BTN_TYPE.COMFIRM) {
			dialog.findViewById(R.id.ok_rl).setVisibility(View.GONE);
		} else if (listener.hindBtn() == HIND_BTN_TYPE.CACEL) {
			dialog.findViewById(R.id.cancel_rl).setVisibility(View.GONE);
		}

		final Button confirm = ((Button) dialog.findViewById(R.id.confirm));
		final Button cancel = ((Button) dialog.findViewById(R.id.cancel));
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != listener) {
					if (v.getId() == confirm.getId()) {
						listener.onConfirm(context, dialog);
					} else if (v.getId() == cancel.getId()) {
						listener.onCancel(dialog);
					}
				}
			}
		};

		confirm.setText(confirmText);
		cancel.setText(cancelText);

		confirm.setOnClickListener(clickListener);
		cancel.setOnClickListener(clickListener);
		return dialog;
	}

	/**
	 * 通过 LayoutInflater 扩展UI
	 * 
	 * @param context
	 * @param title
	 * @param layoutId
	 * @param listener
	 * @return
	 */
	public static Dialog builder(final Context context, String title, String message, int layoutId, final AlertLayoutInflaterListener listener) {
		final Dialog dialog = new Dialog(context, R.style.IPone_Dialog_Bg);
		dialog.setContentView(R.layout.bu_alert_dailog);
		DisplayMetrics _dm = BuScreenHolder.matchParent(context, dialog.getWindow());

		TextView titleTv = ((TextView) dialog.findViewById(R.id.dialog_title));
		titleTv.setText(title);

		TextView tv = ((TextView) dialog.findViewById(R.id.dialog_message));
		tv.setGravity(listener.getGravity());
		tv.setText(message);
		LinearLayout dialog_extends = (LinearLayout) dialog.findViewById(R.id.dialog_extends);

		// dialog_extends.removeAllViews();
		final View child = LayoutInflater.from(context).inflate(layoutId, null);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.height = _dm.heightPixels / 4;
		dialog_extends.addView(child, layoutParams);

		if (BuStringUtils.isEmpety(title)) {
			titleTv.setVisibility(View.GONE);
		}

		if (listener.hindBtn() == HIND_BTN_TYPE.COMFIRM) {
			dialog.findViewById(R.id.ok_rl).setVisibility(View.GONE);
		} else if (listener.hindBtn() == HIND_BTN_TYPE.CACEL) {
			dialog.findViewById(R.id.cancel_rl).setVisibility(View.GONE);
		}

		String confirmText = context.getString(R.string.prompt_confirm);
		String cancelText = context.getString(R.string.prompt_cancel);

		if (!BuStringUtils.isEmpety(listener.getConfirmTxt())) {
			confirmText = listener.getConfirmTxt();
		}
		if (!BuStringUtils.isEmpety(listener.getCancleTxt())) {
			cancelText = listener.getCancleTxt();
		}
		listener.onInflater(dialog, child);

		final Button confirm = ((Button) dialog.findViewById(R.id.confirm));
		final Button cancel = ((Button) dialog.findViewById(R.id.cancel));
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != listener) {
					if (v.getId() == confirm.getId()) {
						listener.onConfirm(context, dialog);
					} else if (v.getId() == cancel.getId()) {
						listener.onCancel(dialog);
					}
				}
			}
		};

		confirm.setText(confirmText);
		cancel.setText(cancelText);

		confirm.setOnClickListener(clickListener);
		cancel.setOnClickListener(clickListener);
		return dialog;
	}
}
