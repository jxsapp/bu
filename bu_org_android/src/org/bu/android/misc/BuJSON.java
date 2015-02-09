package org.bu.android.misc;

import java.io.Serializable;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuJSON implements Serializable {

	private static final long serialVersionUID = 7726307173215378829L;
	private JSONObject json;

	public BuJSON(JSONObject json) {
		super();
		this.json = json;
	}

	public JSONObject getJson() {
		return json;
	}

	@SuppressWarnings("unchecked")
	public Iterator<String> keys() {
		return json.keys();
	}

	public void put(String name, String value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {

		}
	}

	public void put(String name, int value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {

		}
	}

	public void put(String name, double value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {

		}
	}

	public void put(String name, JSONArray value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {

		}
	}

	public void put(String name, JSONObject value) {
		try {
			json.put(name, value);
		} catch (JSONException e) {

		}
	}

	/**
	 * Returns an array containing the string names in this object. This method
	 * returns null if this object contains no mappings.
	 */
	public JSONArray names() {
		return json.names();
	}

	public boolean isNull(String name) {

		return json.isNull(name);
	}

	public boolean has(String name) {
		return json.has(name);
	}

	public boolean getBoolean(String name) {
		boolean rst = false;
		try {
			rst = json.getBoolean(name);
		} catch (JSONException e) {
			rst = false;
		}
		return rst;
	}

	public double getDouble(String name) {
		double rst = 0;
		try {
			rst = json.getDouble(name);
		} catch (JSONException e) {
			rst = 0;
		}
		return rst;
	}

	public int getInt(String name) {
		int rst = 0;
		try {
			rst = json.getInt(name);
		} catch (JSONException e) {
			rst = 0;
		}
		return rst;
	}

	public long getLong(String name) {
		long rst = 0;
		try {
			rst = json.getLong(name);
		} catch (JSONException e) {
			rst = 0;
		}
		return rst;
	}

	public String getString(String name) {

		String rst = "";
		try {
			rst = json.getString(name);
		} catch (JSONException e) {
			rst = "";
		}
		if (BuStringUtils.isEmpety(rst) || "null".equals(rst)) {
			rst = "";
		}
		return rst;
	}

	public JSONArray getJSONArray(String name) {

		JSONArray rst = new JSONArray();
		try {
			rst = json.getJSONArray(name);
		} catch (JSONException e) {
			rst = new JSONArray();
		}
		return rst;
	}

	public JSONObject getJSONObject(String name) {
		JSONObject rst = new JSONObject();
		try {
			rst = json.getJSONObject(name);
		} catch (JSONException e) {
			rst = new JSONObject();
		}
		return rst;
	}

	@Override
	public String toString() {
		return json.toString();
	}

}
