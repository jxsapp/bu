package org.bu.android.misc;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BuGson<T> {

	public T get(String sta) {
		T list = null;
		java.lang.reflect.Type tt = new TypeToken<T>() {
		}.getType();
		Gson gson = new Gson();
		list = gson.fromJson(sta, tt);
		return list;
	}

	public List<T> getList(String sta) {
		List<T> list = null;
		java.lang.reflect.Type tt = new TypeToken<List<T>>() {
		}.getType();
		Gson gson = new Gson();
		list = gson.fromJson(sta, tt);
		return list;
	}

}
