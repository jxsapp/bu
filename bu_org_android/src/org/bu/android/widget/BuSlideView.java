package org.bu.android.widget;

import org.bu.android.R;
import org.bu.android.widget.dragexplist.BuDragView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class BuSlideView extends BuDragView {

	private Context mContext;
	private LinearLayout mViewContent;
	private Scroller mScroller;
	private Button option;
	private View option_bg;
	private OnSlideListener mOnSlideListener;

	private int mHolderWidth = 90;

	private int mLastX = 0;
	private int mLastY = 0;
	private static final int TAN = 2;

	private boolean canSlide = true;

	public static interface OnSlideListener {
		public static final int SLIDE_STATUS_OFF = 0;
		public static final int SLIDE_STATUS_START_SCROLL = 1;
		public static final int SLIDE_STATUS_ON = 2;

		/**
		 * @param view
		 *            current SlideView
		 * @param status
		 *            SLIDE_STATUS_ON or SLIDE_STATUS_OFF
		 */
		public void onSlide(View view, int status);

	}

	public BuSlideView(Context context) {
		super(context);
		initView();
	}

	public BuSlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		mContext = getContext();
		mScroller = new Scroller(mContext);

		setOrientation(LinearLayout.HORIZONTAL);
		View.inflate(mContext, R.layout.bu_slide_view_merge, this);
		option_bg = findViewById(R.id.option_bg);
		option = (Button) findViewById(R.id.option);
		mViewContent = (LinearLayout) findViewById(R.id.view_content);
		mHolderWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources().getDisplayMetrics()));

	}

	public void addOnClearListener(View.OnClickListener clickListener) {
		option.setOnClickListener(clickListener);
	}

	public void setButtonText(CharSequence text) {
		option.setText(text);
	}

	public void setButtonText(int resId, CharSequence text) {
		Drawable drawable = getContext().getResources().getDrawable(resId);
		setButtonText(drawable, text);
	}

	public void setButtonText(Drawable drawable, CharSequence text) {
		Drawable left = drawable;
		left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
		option.setCompoundDrawables(left, null, null, null);
		option.setText(text);
	}

	public void setContentView(View view) {
		mViewContent.addView(view);
	}

	public Button getDelete() {
		return option;
	}

	public void setOptionStyle(int bgColor, int textColor, String text) {
		option_bg.setBackgroundResource(bgColor);
		option.setTextColor(getContext().getResources().getColor(textColor));
		option.setText(text);
	}

	public void setOnSlideListener(OnSlideListener onSlideListener) {
		mOnSlideListener = onSlideListener;
	}

	public void shrink() {
		if (getScrollX() != 0) {
			this.smoothScrollTo(0, 0);
		}
	}

	public void onRequireTouchEvent(MotionEvent event) {
		if (!canSlide) {
			return;
		}
		int x = (int) event.getX();
		int y = (int) event.getY();
		int scrollX = getScrollX();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			if (mOnSlideListener != null) {
				mOnSlideListener.onSlide(this, OnSlideListener.SLIDE_STATUS_START_SCROLL);
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			int deltaX = x - mLastX;
			int deltaY = y - mLastY;
			if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
				break;
			}

			int newScrollX = scrollX - deltaX;
			if (deltaX != 0) {
				if (newScrollX < 0) {
					newScrollX = 0;
				} else if (newScrollX > mHolderWidth) {
					newScrollX = mHolderWidth;
				}
				this.scrollTo(newScrollX, 0);
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			int newScrollX = 0;
			if (scrollX - mHolderWidth * 0.75 > 0) {
				newScrollX = mHolderWidth;
			}
			this.smoothScrollTo(newScrollX, 0);
			if (mOnSlideListener != null) {
				mOnSlideListener.onSlide(this, newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF : OnSlideListener.SLIDE_STATUS_ON);
			}
			break;
		}
		default:
			break;
		}

		mLastX = x;
		mLastY = y;
	}

	public void open() {
		this.smoothScrollTo(mHolderWidth, 0);
	}

	public boolean isOpend() {
		return mScroller.getCurrX() > 0;
	}

	public void setCanSlide(boolean canSlide) {
		this.canSlide = canSlide;
	}

	public boolean isCanSlide() {
		return canSlide;
	}

	public boolean isCanDrag() {
		return canSlide;
	}

	private void smoothScrollTo(int destX, int destY) {
		if (!canSlide) {
			destX = 0;
		}
		// 缓慢滚动到指定位置
		int scrollX = getScrollX();
		int delta = destX - scrollX;
		mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
		invalidate();
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

}
