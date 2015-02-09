package org.bu.android.widget;

import org.bu.android.R;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuSpinnerTextView extends RelativeLayout {

	private Context mContext;
	/** 下拉PopupWindow */
	private BuSpinnerDropDownItems mPopupWindow;
	/** 下拉布局文件ResourceId */
	private int mResId;
	/** 下拉布局文件创建监听器 */
	private ViewCreatedListener mViewCreatedListener;

	private TextView bu_tv;
	private ImageView bu_iv;

	public BuSpinnerTextView(Context context) {
		super(context);
		initSpinner(context);
	}

	public BuSpinnerTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initSpinner(context);
	}

	public BuSpinnerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSpinner(context);
	}

	public BuSpinnerTextView(Context context, final int resourceId, ViewCreatedListener mViewCreatedListener) {
		super(context);
		setResIdAndViewCreatedListener(resourceId, mViewCreatedListener);
		initSpinner(context);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		if (focused) {
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean focused) {
		if (focused) {
			super.onWindowFocusChanged(focused);
		}
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	private void initSpinner(Context context) {
		this.mContext = context;
		// UMSpinnerButton监听事件
		LayoutInflater.from(context).inflate(R.layout.bu_spinner_view, this);
		bu_tv = (TextView) findViewById(R.id.bu_tv);
		bu_iv = (ImageView) findViewById(R.id.bu_iv);
		setOnClickListener(new UMSpinnerButtonOnClickListener());
	}

	public PopupWindow getPopupWindow() {
		return mPopupWindow;
	}

	public void setPopupWindow(BuSpinnerDropDownItems mPopupWindow) {
		this.mPopupWindow = mPopupWindow;
	}

	public int getResId() {
		return mResId;
	}

	/**
	 * @Description: 隐藏下拉布局
	 */
	public void dismiss() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * @Description: 设置下拉布局文件,及布局文件创建监听器
	 * @param @param mResId 下拉布局文件ID
	 * @param @param mViewCreatedListener 布局文件创建监听器
	 */
	public void setResIdAndViewCreatedListener(int mResId, ViewCreatedListener mViewCreatedListener) {
		this.mViewCreatedListener = mViewCreatedListener;
		// 下拉布局文件id
		this.mResId = mResId;
		// 初始化PopupWindow
		mPopupWindow = new BuSpinnerDropDownItems(mContext);
	}

	/**
	 * UMSpinnerButton的点击事件
	 */
	class UMSpinnerButtonOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (mPopupWindow != null) {
				if (!mPopupWindow.isShowing()) {
					// 设置PopupWindow弹出,退出样式
					mPopupWindow.setAnimationStyle(R.style.Bu_Pop_Title_Anima);
					// 计算popupWindow下拉x轴的位置
					int lx = (BuSpinnerTextView.this.getWidth() - mPopupWindow.getmViewWidth() - 7) / 2;
					// showPopupWindow
					mPopupWindow.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.complete_translate));
					mPopupWindow.showAsDropDown(BuSpinnerTextView.this, lx, -5);
					bu_iv.startAnimation(BuTimeLineMenuAnims.getRotateAnimation(0, -180, 200));
				}
			}
		}
	}

	public class BuSpinnerDropDownItems extends PopupWindow {

		private Context mContext;
		/** 下拉视图的宽度 */
		private int mViewWidth;
		/** 下拉视图的高度 */
		private int mViewHeight;

		public BuSpinnerDropDownItems(Context context) {
			super(context);
			this.mContext = context;
			loadViews();
		}

		@Override
		public void dismiss() {
			bu_iv.startAnimation(BuTimeLineMenuAnims.getRotateAnimation(-180, 0, 300));
			super.dismiss();
		}

		/**
		 * @Description:  加载布局文件
		 * @param
		 * @return void
		 * @throws
		 */
		private void loadViews() {
			// 布局加载器加载布局文件
			LayoutInflater inflater = LayoutInflater.from(mContext);
			final View v = inflater.inflate(mResId, null);
			// 计算view宽高
			onMeasured(v);

			// 必须设置
			setWidth(LayoutParams.WRAP_CONTENT);
			setHeight(LayoutParams.WRAP_CONTENT);
			setContentView(v);
			setFocusable(true);

			// 设置布局创建监听器，以便在实例化布局控件对象
			if (mViewCreatedListener != null) {
				mViewCreatedListener.onViewCreated(v);
			}
		}

		/**
		 * @Description: 计算View长宽
		 * @param @param v
		 */
		private void onMeasured(View v) {
			int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			v.measure(w, h);
			mViewWidth = v.getMeasuredWidth();
			mViewHeight = v.getMeasuredHeight();
		}

		public int getmViewWidth() {
			return mViewWidth;
		}

		public void setmViewWidth(int mViewWidth) {
			this.mViewWidth = mViewWidth;
		}

		public int getmViewHeight() {
			return mViewHeight;
		}

		public void setmViewHeight(int mViewHeight) {
			this.mViewHeight = mViewHeight;
		}

	}

	public interface ViewCreatedListener {
		void onViewCreated(View v);
	}

	public void setText(CharSequence title) {
		this.bu_tv.setText(title);
	}

	public void setText(int resid) {
		this.bu_tv.setText(resid);
	}

	public ImageView getImageView() {
		return bu_iv;
	}
}
