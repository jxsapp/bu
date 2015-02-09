package org.bu.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class BuLineEditText extends EditText {
	// 画笔 用来画下划线
	private Paint paint;

	public BuLineEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
//		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.parseColor("#533215"));
		// 开启抗锯齿 较耗内存
		paint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 得到总行数
		int lineCount = getLineCount();
		// 得到每行的高度
		int lineHeight = getLineHeight();
		// 根据行数循环画线
		for (int i = 0; i < lineCount; i++) {
			int lineY = (i + 1) * lineHeight;
			canvas.drawLine(0, lineY, this.getWidth(), lineY, paint);
		}

	}

}