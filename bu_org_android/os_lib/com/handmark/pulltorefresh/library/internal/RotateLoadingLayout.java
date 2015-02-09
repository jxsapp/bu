/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import org.bu.android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.widget.ImageView.ScaleType;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;

@SuppressLint("ViewConstructor")
public class RotateLoadingLayout extends LoadingLayout {

	static final int ROTATION_ANIMATION_DURATION = 1200;

	private final Matrix mHeaderImageMatrix;

	private GifAnimation mGifAnimation;

	private int[] mGifRes = { R.drawable.loading00, R.drawable.loading05, R.drawable.loading09, };

	public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		mHeaderImage.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
	}

	public void onLoadingDrawableSet(Drawable imageDrawable) {
		System.out.println("RotateLoadingLayout.onLoadingDrawableSet");
		if (null != imageDrawable) {
		}
	}

	int mPrevIndex = -1;

	protected void onPullImpl(float scaleOfLayout) {

		int index = (int) (scaleOfLayout / 1f * 10);
		if (index == mPrevIndex) {
			return;
		} else {
			if (index > 10) {
				index = 10;
			}
			int res = getResources().getIdentifier(String.format("dropdown_anim_%02d", index), "drawable", getContext().getPackageName());
			// Bitmap scaledBitmap = getScaledBitmap(res, index);
			// mHeaderImage.setImageBitmap(scaledBitmap);
			mHeaderImage.setImageResource(res);
			mPrevIndex = index;
		}
	}

	@Override
	protected void refreshingImpl() {
		if (mGifAnimation == null) {
			mGifAnimation = new GifAnimation(mHeaderImage, mGifRes);
		}
		mGifAnimation.start();
	}

	@Override
	protected void resetImpl() {
		mHeaderImage.clearAnimation();
		if (mGifAnimation != null) {
			mGifAnimation.stop();
		}
	}

	@Override
	protected void pullToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.default_ptr_rotate;
	}
}
