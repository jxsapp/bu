package org.bu.android.app;

import android.os.Bundle;
import android.view.View.OnClickListener;

public interface IBuUI extends OnClickListener {

	public void initUI(Bundle savedInstanceState, Object... params);

}
