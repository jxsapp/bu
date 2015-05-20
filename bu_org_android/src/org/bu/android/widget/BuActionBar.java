package org.bu.android.widget;

import java.util.LinkedList;

import org.bu.android.R;
import org.bu.android.log.BuLog;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BuActionBar extends RelativeLayout implements OnClickListener {

	private LayoutInflater mInflater;
	private TextView mTitleView;
	private TextView net_work_tip;
	private LinearLayout mActionsView;
	private Button mHomeBtn, mRightBtn;
	private LinearLayout mHomeLayout;
	private RelativeLayout top_bar_rl;
	private LinearLayout error_bar;
	private ImageView mProgress;

	public BuActionBar(Context context) {
		this(context, null);
	}

	@SuppressWarnings("deprecation")
	public BuActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		mInflater = LayoutInflater.from(context);

		LayoutInflater.from(context).inflate(R.layout.bu_actionbar, this);
		mHomeLayout = (LinearLayout) findViewById(R.id.actionbar_home_bg);
		top_bar_rl = (RelativeLayout) findViewById(R.id.top_bar_rl);
		error_bar = (LinearLayout) findViewById(R.id.error_bar);
		mHomeBtn = (Button) findViewById(R.id.actionbar_home_btn);
		mRightBtn = (Button) findViewById(R.id.actionbar_right_btn);
		mProgress = (ImageView) findViewById(R.id.actionbar_progress_iv);

		net_work_tip = (TextView) findViewById(R.id.net_work_tip);
		mTitleView = (TextView) findViewById(R.id.actionbar_title);
		mActionsView = (LinearLayout) findViewById(R.id.actionbar_actions);
		if (null != attrs) {
			TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.WM_ActionBar);
			if (typeArray.hasValue(R.styleable.WM_ActionBar_wm_title)) {
				setTitle(typeArray.getString(R.styleable.WM_ActionBar_wm_title));
			}
			if (typeArray.hasValue(R.styleable.WM_ActionBar_wm_ac_backgroud)) {
				top_bar_rl.setBackgroundDrawable(typeArray.getDrawable(R.styleable.WM_ActionBar_wm_ac_backgroud));
			}
			typeArray.recycle();
		}
	}

	public TextView getTitleView() {
		return mTitleView;
	}

	public void noNetwork() {
		net_work_tip.setText("(未连接)");
		// mTitleView.noNetwork();
	}

	public void hasNetwork() {
		// mTitleView.hasNetwork();
		net_work_tip.setText("");
	}

	public void addErrorBar(View child) {
		error_bar.addView(child);
	}

	public void hindErrorBar() {
		error_bar.setVisibility(GONE);
	}

	public void showErrorBar() {
		error_bar.setVisibility(VISIBLE);
	}

	public void setHomeAction(Action action) {
		mHomeLayout.setOnClickListener(this);
		mHomeLayout.setTag(action);
		mHomeBtn.setOnClickListener(this);
		mHomeBtn.setTag(action);

		initActionView(mHomeBtn, action);
		mHomeLayout.setVisibility(View.VISIBLE);
	}

	public void setRightAction(Action action) {
		mRightBtn.setOnClickListener(this);
		mRightBtn.setTag(action);

		initActionView(mRightBtn, action);
		mRightBtn.setVisibility(View.VISIBLE);
	}

	private void initActionView(Button mHomeBtn, Action action) {
		if ((Pattern.VALIDATE != action.getText() && Pattern.VALIDATE == action.getDrawable()) || (Pattern.VALIDATE == action.getText() && Pattern.VALIDATE == action.getDrawable())) {
			if (Pattern.VALIDATE != action.getText()) {
				mHomeBtn.setText(getContext().getString(action.getText()));
			} else {
				mHomeBtn.setText(action.getTitle());
			}
		} else if (Pattern.VALIDATE != action.getDrawable() && Pattern.VALIDATE == action.getText()) {
			mHomeBtn.setBackgroundResource(action.getDrawable());
		}
	}

	public void clearHomeAction() {
		mHomeLayout.setVisibility(View.GONE);
	}

	public void showLoading() {
		showProgressBar(true);
	}

	public void dismissLoading() {
		showProgressBar(false);
	}

	public void showProgressBar(boolean show) {
		if (show) {
			mProgress.setVisibility(View.VISIBLE);
			AnimationDrawable animator = (AnimationDrawable) mProgress.getBackground();
			animator.start();
		} else {
			AnimationDrawable animator = (AnimationDrawable) mProgress.getBackground();
			animator.stop();
			mProgress.setVisibility(View.GONE);
		}
	}

	public void setTitle(CharSequence title) {
		mTitleView.setText(title);
	}

	public void setTitle(int resid) {
		mTitleView.setText(resid);
	}

	/**
	 * Function to set a click listener for Title TextView
	 * 
	 * @param listener
	 *            the onClickListener
	 */
	public void setOnTitleClickListener(OnClickListener listener) {
		mTitleView.setOnClickListener(listener);
	}

	@Override
	public void onClick(View view) {
		final Object tag = view.getTag();
		if (tag instanceof Action) {
			final Action action = (Action) tag;
			action.performAction(view);
		}
	}

	/**
	 * Adds a list of {@link Action}s.
	 * 
	 * @param actionList
	 *            the actions to add
	 */
	public void addActions(ActionList actionList) {
		int actions = actionList.size();
		for (int i = 0; i < actions; i++) {
			addAction(actionList.get(i));
		}
	}

	/**
	 * Adds a new {@link Action}.
	 * 
	 * @param action
	 *            the action to add
	 */
	public void addAction(Action action) {
		final int index = mActionsView.getChildCount();
		addAction(action, index);
	}

	/**
	 * Adds a new {@link Action} at the specified index.
	 * 
	 * @param action
	 *            the action to add
	 * @param index
	 *            the position at which to add the action
	 */
	public void addAction(Action action, int index) {
		mActionsView.addView(inflateAction(action), index);
	}

	public View getAction(int index) {
		if (index <= mActionsView.getChildCount()) {
			return mActionsView.getChildAt(index);
		}
		return null;
	}

	public void removeAllActions() {
		mActionsView.removeAllViews();
	}

	public void removeAllActions(Object notMovedTag) {
		int childCount = mActionsView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = mActionsView.getChildAt(i);
			if (view != null) {
				final Object tag = view.getTag();
				if (tag instanceof Action && !tag.equals(notMovedTag)) {
					mActionsView.removeView(view);
				}
			}
		}
	}

	/**
	 * Remove a action from the action bar.
	 * 
	 * @param index
	 *            position of action to remove
	 */
	public void removeActionAt(int index) {
		mActionsView.removeViewAt(index);
	}

	/**
	 * Remove a action from the action bar.
	 * 
	 * @param action
	 *            The action to remove
	 */
	public void removeAction(Action action) {
		int childCount = mActionsView.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View view = mActionsView.getChildAt(i);
			if (view != null) {
				final Object tag = view.getTag();
				if (tag instanceof Action && tag.equals(action)) {
					mActionsView.removeView(view);
				}
			}
		}
	}

	/**
	 * Returns the number of actions currently registered with the action bar.
	 * 
	 * @return action count
	 */
	public int getActionCount() {
		return mActionsView.getChildCount();
	}

	/**
	 * Inflates a {@link View} with the given {@link Action}.
	 * 
	 * @param action
	 *            the action to inflate
	 * @return a view
	 */
	private View inflateAction(Action action) {
		View view = null;
		if (null == action) {
			return view;
		}
		if ((Pattern.VALIDATE != action.getText() && Pattern.VALIDATE == action.getDrawable()) || (Pattern.VALIDATE == action.getText() && Pattern.VALIDATE == action.getDrawable())) {
			view = mInflater.inflate(R.layout.bu_actionbar_item_txt, mActionsView, false);
			BuActionBarTextItem labelView = (BuActionBarTextItem) view.findViewById(R.id.actionbar_item_txt);
			if (Pattern.VALIDATE != action.getText()) {
				labelView.setText(getContext().getString(action.getText()));
			} else {
				labelView.setText(action.getTitle());
			}
			labelView.hasNew(action.hasNew());

		} else if (Pattern.VALIDATE != action.getDrawable() && Pattern.VALIDATE == action.getText()) {
			view = mInflater.inflate(R.layout.bu_actionbar_item_img, mActionsView, false);
			ImageButton labelView = (ImageButton) view.findViewById(R.id.actionbar_item_img);
			labelView.setImageResource(action.getDrawable());
		}
		if (null != view && null != action) {
			view.setTag(action);
			view.setOnClickListener(this);
		}
		return view;
	}

	/**
	 * A {@link LinkedList} that holds a list of {@link Action}s.
	 */
	@SuppressWarnings("serial")
	public static class ActionList extends LinkedList<Action> {
	}

	/**
	 * Definition of an action that could be performed, along with a icon to
	 * show.
	 */
	public interface Action {
		public int getDrawable();

		public int getText();

		public String getTitle();

		public boolean hasNew();

		public void performAction(View view);
	}

	public static abstract class AbstractAction implements Action {
		private int mDrawable = -1;
		private int text = -1;
		private String title = "";

		@Override
		public boolean hasNew() {
			return false;
		}

		public AbstractAction() {
			super();
		}

		public AbstractAction(int drawable) {
			mDrawable = drawable;
			this.text = BuActionBar.Pattern.VALIDATE;
		}

		public AbstractAction(int drawable, int text) {
			mDrawable = drawable;
			this.text = text;
		}

		public AbstractAction(String title) {
			mDrawable = -1;
			this.text = -1;
			this.title = title;
		}

		@Override
		public int getDrawable() {
			return mDrawable;
		}

		@Override
		public int getText() {
			return text;
		}

		@Override
		public String getTitle() {
			return title;
		}

	}

	public static class IntentAction extends AbstractAction {
		private Context mContext;
		private Intent mIntent;

		public IntentAction(Context context, Intent intent, int drawble) {
			this(context, intent, drawble, BuActionBar.Pattern.VALIDATE);
		}

		public IntentAction(Context context, Intent intent, int drawble, int text) {
			super(drawble, text);
			mContext = context;
			mIntent = intent;
		}

		@Override
		public void performAction(View view) {
			try {
				mContext.startActivity(mIntent);
			} catch (ActivityNotFoundException e) {
				BuLog.e(mContext.getClass().getName(), e.getMessage(), e);
			}
		}
	}

	static public interface Pattern {
		public static final int VALIDATE = -1;
	}
}
