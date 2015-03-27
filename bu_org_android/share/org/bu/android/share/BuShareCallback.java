package org.bu.android.share;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;

public interface BuShareCallback {

	public void onShare(Platform platform, ShareParams paramsToShare);

}
