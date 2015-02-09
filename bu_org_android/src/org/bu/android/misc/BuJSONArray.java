package org.bu.android.misc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuJSONArray {

	private JSONArray jsonArray;

	public BuJSONArray(JSONArray jsonArray) {
		super();
		this.jsonArray = jsonArray;
	}

	public JSONArray getJSONArray(int index) {
		JSONArray array = new JSONArray();
		try {
			array = jsonArray.getJSONArray(index);
		} catch (JSONException e) {
			array = new JSONArray();
		}
		return array;
	}

	public int length() {
		return jsonArray.length();
	}

	public String getString(int index) {
		String rst = "";
		try {
			rst = jsonArray.getString(index);
		} catch (JSONException e) {
			rst = "";
		}

		return rst;
	}

	public JSONObject getJSONObject(int index) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = jsonArray.getJSONObject(index);
		} catch (JSONException e) {
			jsonObject = new JSONObject();
		}
		return jsonObject;
	}

}
