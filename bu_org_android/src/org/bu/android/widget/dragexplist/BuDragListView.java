package org.bu.android.widget.dragexplist;

import org.bu.android.widget.BuSlideView;
import org.bu.android.widget.dragexplist.BuListItemMaster.BuListItem;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class BuDragListView extends ListView {

	private Context mContext;
	private WindowManager mWindowManager;
	private Vibrator vibrator = null;
	// 在滑动的时�?，手的移动要大于这个返回的距离�?才开始移动控�?
	private int mScaledTouchSlop;

	private BuDragView item_temp = null;

	private final float mAlpha = 0.9f;
	// 拖动的view
	private ImageView mDragView;

	private WindowManager.LayoutParams mLayoutParams;
	// 拖动时的位置
	private int mDragStartPosition;
	// 当前的位�?
	private int mDragCurrentPostion;

	// 当前位置距离边界的位?
	private int mDragOffsetX;
	private int mDragOffSetY;
	private int mDragPointX;
	private int mDragPointY;
	// 边界
	private int mUpperBound;
	private int mLowerBound;
	private DropViewListener mDropViewListener;

	public BuDragListView(Context context) {
		this(context, null);
	}

	public BuDragListView(Context context, AttributeSet attr) {
		super(context, attr);
		stopDragging();
		mContext = context;
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		mScaledTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			final int x = (int) ev.getX();// 相对于本身的这个view左上角的x距离
			final int y = (int) ev.getY();
			final int itemNum = pointToPosition(x, y);
			if (itemNum == AdapterView.INVALID_POSITION) {
				break;
			}
			item_temp = (BuDragView) getChildAt(itemNum - getFirstVisiblePosition());
			mDragPointX = x - item_temp.getLeft();
			mDragPointY = y - item_temp.getTop();
			mDragOffsetX = ((int) ev.getRawX()) - x;
			mDragOffSetY = ((int) ev.getRawY()) - y;
			if (item_temp.isCanDrag()) {
				mDragView = null;

				item_temp.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							break;
						case MotionEvent.ACTION_UP:
							stopDragging();
							break;
						}
						// 当返回true时，表示已经consumed消费了这个MotionEvent,导致setOnLongClickListener长按失效
						// 当返回false时，表示没有consumed这个MotionEvent，会继续执行下面的setOnLongClickListener长按效果
						return false;
					}
				});
				// 长按 系统默认的延时时间是500ms，当按下500ms后才触发LongClick事件�?
				item_temp.setOnLongClickListener(new OnLongClickListener() {
					@SuppressLint("ResourceAsColor")
					@Override
					public boolean onLongClick(View v) {
						vibrator.vibrate(new long[] { 100, 70, 70, 100 }, -1);
						// 计算边界
						final int height = getHeight();
						mUpperBound = Math.min(y - mScaledTouchSlop, height / 3);
						mLowerBound = Math.max(y + mScaledTouchSlop, height * 2 / 3);
						mDragCurrentPostion = mDragStartPosition = itemNum;
						item_temp.setVisibility(View.INVISIBLE);
						item_temp.destroyDrawingCache();
						item_temp.setDrawingCacheEnabled(true);
						Bitmap bitmap = Bitmap.createBitmap(item_temp.getDrawingCache());
						item_temp.setDrawingCacheEnabled(false);
						startDragging(bitmap, x, y);
						dragView(x + 15, y - 10);
						return false;// 返回为true时，然后调用onTouchEvent()方法
					}
				});
			}
			break;

		case MotionEvent.ACTION_MOVE:
			break;

		case MotionEvent.ACTION_UP:
			item_temp.setVisibility(View.VISIBLE);
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	private BuSlideView mFocusedItemView = null;

	private void onTouchEvent4BuSlide(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			int y = (int) event.getY();
			int position = pointToPosition(x, y);
			if (position != INVALID_POSITION) {
				try {
					BuListItem data = (BuListItem) getItemAtPosition(position);
					mFocusedItemView = data.getBuSlideView();
				} catch (Exception e) {

				}
			}
			break;
		}
		if (mFocusedItemView != null) {
			mFocusedItemView.onRequireTouchEvent(event);
		}

	}

	/**
	 * 我们在onInterceptTouchEvent方法记录了拖拽的�?��位置和当前位置，并且初始化了windowmanager�?
	 * 接下来我们�?过onTouchEvent方法来实现让windowmanager跟随你的手指移动
	 **/
	@Override
	@SuppressLint("ResourceAsColor")
	public boolean onTouchEvent(MotionEvent ev) {
		onTouchEvent4BuSlide(ev);
		if (mDragView != null && mDragCurrentPostion != INVALID_POSITION && mDropViewListener != null) {
			int x, y;
			switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				x = (int) ev.getX();
				y = (int) ev.getY();
				stopDragging();
				item_temp.setVisibility(View.VISIBLE);
				// mDragView.setBackgroundResource(R.color.white);
				// 数据交换
				mDropViewListener.drop(mDragStartPosition, mDragCurrentPostion);
				break;
			case MotionEvent.ACTION_MOVE:

				x = (int) ev.getX();
				y = (int) ev.getY();

				if (y >= getHeight() / 3) {
					mUpperBound = getHeight() / 3;
				}
				if (y <= getHeight() * 2 / 3) {
					mLowerBound = getHeight() * 2 / 3;
				}
				int speed = 0;
				if (y > mLowerBound) {
					if (getLastVisiblePosition() < getCount() - 1) {
						speed = y > (getHeight() + mLowerBound) / 2 ? 16 : 4;
					} else {
						speed = 1;
					}
				} else if (y < mUpperBound) {
					speed = y < mUpperBound / 2 ? -16 : -4;
					if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= getPaddingTop()) {
						speed = 0;
					}
				}
				if (speed != 0) {
					smoothScrollBy(speed, 30);
				}

				dragView(x, y);
				break;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 这里面我们就记录了拖拽的当前和开始时位置mDragCurrentPostion�?
	 * *mDragStartPosition，并通过startDragging来初始化mWindowManager
	 * ，在startDragging方法里面调用的stopDragging方法其实是一个内存释放的过程，这个方法做的事情就是释放内存空间�?
	 * 
	 * @param bitm
	 * @param x
	 * @param y
	 */
	private void startDragging(Bitmap bitm, int x, int y) {
		stopDragging();

		mLayoutParams = new WindowManager.LayoutParams();
		mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
		mLayoutParams.x = x - mDragPointX + mDragOffsetX;
		mLayoutParams.y = y - mDragPointY + mDragOffSetY;
		mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE //
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON //
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN//
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;//
		mLayoutParams.format = PixelFormat.TRANSLUCENT;
		mLayoutParams.windowAnimations = 0;

		ImageView imageView = new ImageView(mContext);
		imageView.setImageBitmap(bitm);
		if (dragBackResId > -1) {
			imageView.setBackgroundResource(dragBackResId);
		}
		imageView.setPadding(0, 0, 0, 0);
		mWindowManager.addView(imageView, mLayoutParams);
		mDragView = imageView;
	}

	private int dragBackResId = -1;

	public void setDragBackResId(int resId) {
		this.dragBackResId = resId;
	}

	/**
	 * 回收资源
	 */
	private void stopDragging() {
		if (mDragView != null) {
			mWindowManager.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
	}

	/**
	 * 拖拽
	 * 
	 * @param x
	 * @param y
	 */
	private void dragView(int x, int y) {
		if (mDragView != null) {
			mLayoutParams.alpha = mAlpha;
			mLayoutParams.y = y - mDragPointY + mDragOffSetY;
			mLayoutParams.x = x - mDragPointX + mDragOffsetX;
			mWindowManager.updateViewLayout(mDragView, mLayoutParams);
		}
		int tempPosition = pointToPosition(0, y);
		if (tempPosition != INVALID_POSITION) {
			mDragCurrentPostion = tempPosition;
		}

		// 滚动
		int scrollY = 0;
		if (y < mUpperBound) {
			scrollY = 8;
		} else if (y > mLowerBound) {
			scrollY = -8;
		}

		if (scrollY != 0) {
			int top = getChildAt(mDragCurrentPostion - getFirstVisiblePosition()).getTop();
			setSelectionFromTop(mDragCurrentPostion, top + scrollY);
		}
	}

	public void setDropViewListener(DropViewListener listener) {
		this.mDropViewListener = listener;
	}

	public interface DropViewListener {
		void drop(int from, int to);
	}

}
