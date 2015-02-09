package org.bu.android.widget;

import org.bu.android.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuActionBarFilterItem extends RelativeLayout {

	private TextView list_title;
	private ImageView list_has_new;

	public BuActionBarFilterItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public BuActionBarFilterItem(Context context) {
		super(context);
		init(context, null);
	}

	private void init(Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.bu_actionbar_filter_item, this);
		this.list_title = (TextView) findViewById(R.id.list_title);
		this.list_has_new = (ImageView) findViewById(R.id.list_has_new);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Bu_Label_Dis_Arrow);
		if (typedArray.hasValue(R.styleable.Bu_Label_Dis_Arrow_bu_label)) {
			list_title.setText(typedArray.getString(R.styleable.Bu_Label_Dis_Arrow_bu_label));
		} else {
			list_title.setText("");
		}

		if (null != typedArray) {
			typedArray.recycle();
		}
		setNoHasNew();
	}

	public void setHasNew() {
		list_has_new.setVisibility(VISIBLE);
	}

	public void setNoHasNew() {
		list_has_new.setVisibility(INVISIBLE);
	}

	public void setText(CharSequence text) {
		list_title.setText(text);
	}

	public void setText(int rst) {
		list_title.setText(rst);
	}

	public CharSequence getText() {
		return list_title.getText();
	}

}
