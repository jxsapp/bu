package org.bu.android.widget;

import org.bu.android.R;

import android.content.Context;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.View;

public class BuPasswdEditText extends BuLabelEditText {

	private boolean visbile = false;

	public BuPasswdEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		append.setText("密 码");
		append.setVisibility(View.INVISIBLE);
		opt_iv.setImageResource(R.drawable.v1_pwd_eye);
		setPwdAppend();
	}

	private void setPwdAppend() {
		reset();
		opt_iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				visbile = !visbile;
				reset();
			}
		});
	}

	private void reset() {
		if (visbile) {
			inputVisiblePwd();
			opt_iv.setImageResource(R.drawable.v1_pwd_eye_open);
		} else {
			inputPwd();
			opt_iv.setImageResource(R.drawable.v1_pwd_eye_close);
		}
		Selection.setSelection(dis.getText(), dis.getText().length());
		append.setSelected(visbile);
	}

}
