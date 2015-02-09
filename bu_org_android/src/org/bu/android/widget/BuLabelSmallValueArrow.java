package org.bu.android.widget;

import org.bu.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuLabelSmallValueArrow extends RelativeLayout {

	private ImageView arrow;
	private TextView label;
	private TextView dis;

	public BuLabelSmallValueArrow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {

		LayoutInflater.from(context).inflate(R.layout.bu_label_small_value_arrow, this);
		arrow = (ImageView) findViewById(R.id.arrow);
		label = (TextView) findViewById(R.id.label);
		dis = (TextView) findViewById(R.id.dis);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Bu_Label_Dis_Arrow);
		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_label)) {
			label.setText(typedArray.getString(R.styleable.Bu_Label_Dis_Arrow_bu_label));
		}
		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_dis)) {
			dis.setText(typedArray.getString(R.styleable.Bu_Label_Dis_Arrow_bu_dis));
		}
		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_arrow)) {
			arrow.setImageDrawable(typedArray.getDrawable(R.styleable.Bu_Label_Dis_Arrow_bu_arrow));
		}
		if (null != typedArray) {
			typedArray.recycle();
		}
	}

	public void invisibleArrow() {
		this.arrow.setVisibility(INVISIBLE);
	}

	public void setArrow(int resId) {
		this.arrow.setImageResource(resId);
		this.arrow.setVisibility(VISIBLE);
	}

	public String getLabel() {
		return label.getText().toString();
	}

	public void setLabel(String label) {
		this.label.setText(label);
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

}
