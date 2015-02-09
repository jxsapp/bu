package org.bu.android.widget;

import org.bu.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuLabelEditText extends RelativeLayout {

	protected TextView label;
	protected EditText dis;
	protected TextView append;
	protected ImageButton opt_iv;
	protected ImageView icon;

	public BuLabelEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {

		LayoutInflater.from(context).inflate(R.layout.bu_label_edit, this);
		icon = (ImageView) findViewById(R.id.icon);
		label = (TextView) findViewById(R.id.label);
		append = (TextView) findViewById(R.id.append);
		opt_iv = (ImageButton) findViewById(R.id.opt_iv);
		dis = (EditText) findViewById(R.id.dis);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Bu_Label_Dis_Arrow);
		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_label)) {
			label.setText(typedArray.getString(R.styleable.Bu_Label_Dis_Arrow_bu_label));
		}

		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_img)) {
			icon.setImageDrawable(typedArray.getDrawable(R.styleable.Bu_Label_Dis_Arrow_bu_img));
			icon.setVisibility(VISIBLE);
		} else {
			icon.setVisibility(GONE);

			icon.setImageResource(R.drawable.translate1x1);
		}

		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_append)) {
			append.setText(typedArray.getString(R.styleable.Bu_Label_Dis_Arrow_bu_append));
		} else {
			append.setText("");
		}
		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_dis)) {
			dis.setText(typedArray.getString(R.styleable.Bu_Label_Dis_Arrow_bu_dis));
		}
		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_dis_hint)) {
			dis.setHint(typedArray.getString(R.styleable.Bu_Label_Dis_Arrow_bu_dis_hint));
		}

		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_arrow)) {
			opt_iv.setImageDrawable(typedArray.getDrawable(R.styleable.Bu_Label_Dis_Arrow_bu_arrow));
		}
		if (null != typedArray) {
			typedArray.recycle();
		}
	}

	public void addTextChangedListener(TextWatcher watcher) {
		this.dis.addTextChangedListener(watcher);
	}

	public void setDisGrvay(int gravity) {// Gravity
		this.dis.setGravity(gravity);
	}

	public void setOptionIcon(int resId) {
		this.opt_iv.setImageResource(resId);
		this.opt_iv.setVisibility(VISIBLE);
	}

	public void invisibleOptionIcon() {
		this.opt_iv.setVisibility(INVISIBLE);
	}

	public void setDisGrvay(int gravity, int textsize) {// Gravity
		this.dis.setGravity(gravity);
		this.dis.setTextSize(textsize);
	}

	public EditText get() {
		return dis;
	}

	public Editable getText() {
		return dis.getText();
	}

	public int getSelectionStart() {
		return dis.getSelectionStart();
	}

	public String getLabel() {
		return label.getText().toString();
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}

	public String getDis() {
		return dis.getText().toString().trim();
	}

	public void setDis(String dis) {
		this.dis.setText(dis);
	}

	public void setDis(CharSequence text) {
		this.dis.setText(text);
	}

	public void setAppend(CharSequence append) {
		this.append.setText(append);
	}

	public void setDis(int leftId, String dis) {
		this.dis.setText(dis);
		Drawable left = getResources().getDrawable(leftId);
		left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
		this.dis.setCompoundDrawables(left, null, null, null);
	}

	public Object getDisTag() {
		return dis.getTag();
	}

	public void setDisTag(Object tag) {
		this.dis.setTag(tag);
	}

	@Override
	public void setEnabled(boolean enabled) {
		// super.setEnabled(enabled);
		this.dis.setEnabled(enabled);
	}

	public void focus() {
		focus(label.getText().toString() + "有误!");
	}

	public void focus(String msg) {
		this.dis.setFocusable(true);
		this.dis.requestFocus();
		Selection.setSelection(dis.getText(), 0, dis.getText().length());
		dis.setError(msg);
	}

	public void maxLenght(int maxLen) {
		InputFilter[] filters = { new LengthFilter(maxLen) };
		this.dis.setFilters(filters);
	}

	public void inputText() {
		this.dis.setInputType(InputType.TYPE_CLASS_TEXT);
	}

	public void inputVisiblePwd() {
		this.dis.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	}

	public void inputPwd() {
		this.dis.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	}

	public void inputNumber() {
		this.dis.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	public void inputDecimal() {
		this.dis.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
	}

	public void inputNull() {
		this.dis.setInputType(InputType.TYPE_NULL);
	}

	public void inputEMail() {
		this.dis.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	}

	public void inputPhone() {
		this.dis.setInputType(InputType.TYPE_CLASS_PHONE);
	}

}
