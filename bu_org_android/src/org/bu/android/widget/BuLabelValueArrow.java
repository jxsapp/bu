package org.bu.android.widget;

import org.bu.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextPaint;
import android.text.InputFilter.LengthFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuLabelValueArrow extends RelativeLayout {

	private ImageView icon;
	private ImageView arrow;
	private TextView label;
	private TextView dis;

	public BuLabelValueArrow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {

		LayoutInflater.from(context).inflate(R.layout.bu_label_value_arrow, this);
		icon = (ImageView) findViewById(R.id.icon);
		arrow = (ImageView) findViewById(R.id.arrow);
		label = (TextView) findViewById(R.id.label);
		dis = (TextView) findViewById(R.id.dis);

		label.setText("");
		dis.setText("");

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

		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_dis)) {
			dis.setText(typedArray.getString(R.styleable.Bu_Label_Dis_Arrow_bu_dis));
		}

		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_dis_hint)) {
			dis.setHint(typedArray.getString(R.styleable.Bu_Label_Dis_Arrow_bu_dis_hint));
		}
		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_dis_gravity)) {
			setDisGrvay(typedArray.getInt(R.styleable.Bu_Label_Dis_Arrow_bu_dis_gravity, -1));
		}

		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_arrow)) {
			arrow.setImageDrawable(typedArray.getDrawable(R.styleable.Bu_Label_Dis_Arrow_bu_arrow));
		}
		if (null != typedArray) {
			typedArray.recycle();
		}

	}

	public void focus() {
		focus(label.getText().toString() + "有误!");
	}

	public void focus(String msg) {
		this.dis.setFocusable(true);
		this.dis.requestFocus();
		dis.setError(msg);
	}

	public void maxLenght(int maxLen) {
		InputFilter[] filters = { new LengthFilter(maxLen) };
		this.dis.setFilters(filters);
	}

	public void maxLines(int maxlines) {
		this.dis.setMaxLines(maxlines);
	}

	public void setLabel(int color, String text) {
		this.label.setTextColor(color);
		this.label.setText(text);
	}

	public void setDis(int color, String text) {
		this.dis.setTextColor(color);
		this.dis.setText(text);
	}

	public void setDisGrvay(int gravity) {// Gravity
		this.dis.setGravity(gravity);
	}

	public void setDisGrvay(int gravity, int textsize) {// Gravity
		this.dis.setGravity(gravity);
		this.dis.setTextSize(textsize);
	}

	public void setStyle(int textsize) {// Gravity
		this.setDisStyle(textsize);
		this.setLabelStyle(textsize);
	}

	public void setDisStyle(int textsize) {// Gravity
		this.dis.setTextSize(textsize);
		TextPaint tp = dis.getPaint();
		tp.setFakeBoldText(true);
	}

	public void setLabelStyle(int textsize) {// Gravity
		this.label.setTextSize(textsize);
		TextPaint tp = label.getPaint();
		tp.setFakeBoldText(true);
	}

	public void setColor(int color) {
		this.label.setTextColor(color);
		this.dis.setTextColor(color);
	}

	public void invisibleArrow() {
		this.arrow.setVisibility(INVISIBLE);
	}

	public void setArrow(int resId) {
		this.arrow.setImageResource(resId);
		this.arrow.setVisibility(VISIBLE);
	}

	public void setIcon(int resId) {
		this.icon.setImageResource(resId);
	}

	public String getLabel() {
		return label.getText().toString();
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}

	public void setLabel(CharSequence text) {
		this.label.setText(text);
	}

	public String getDis() {
		return dis.getText().toString();
	}

	public void setDis(String dis) {
		this.dis.setText(dis);
	}

	public void setDis(CharSequence text) {
		this.dis.setText(text);
	}

	public void setTagDis(int tag, String text) {
		this.dis.setTag(tag + "");
		this.setDis(text);
	}

	public void setDisDraw(int leftId) {
		Drawable left = getResources().getDrawable(leftId);
		left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
		this.dis.setCompoundDrawables(left, null, null, null);
	}

	public void setLabelDraw(int leftId) {
		Drawable left = getResources().getDrawable(leftId);
		left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
		this.label.setCompoundDrawables(left, null, null, null);
	}

	public Object getDisTag() {
		return dis.getTag();
	}

	public void setDisTag(Object tag) {
		this.dis.setTag(tag);
	}

}
