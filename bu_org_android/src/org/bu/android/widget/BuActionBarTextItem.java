package org.bu.android.widget;

import org.bu.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuActionBarTextItem extends RelativeLayout {

	private TextView item_text;
	private ImageView has_new;

	public BuActionBarTextItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public BuActionBarTextItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BuActionBarTextItem(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.bu_actionbar_item_txt_obj, this);
		this.item_text = (TextView) findViewById(R.id.item_txt);
		this.has_new = (ImageView) findViewById(R.id.has_new);
	}

	public void setText(String text) {
		item_text.setText(text);
	}
	
	public CharSequence getText(){
		return item_text.getText();
	}

	public void hasNew(boolean hasNew) {
		if (hasNew) {
			has_new.setVisibility(VISIBLE);
		} else {
			has_new.setVisibility(GONE);
		}
	}

}
