package org.bu.android.db;

import java.io.Serializable;

import org.bu.android.misc.BuGsonHolder;
import org.json.JSONObject;

public abstract class ModleInfo implements Serializable {
	private static final long serialVersionUID = 408294155661043861L;

	public JSONObject getJson(boolean all) {
		return BuGsonHolder.getJson(this, all);
	}

	@Override
	public String toString() {
		return getJson(true).toString();
	}

	
	
}