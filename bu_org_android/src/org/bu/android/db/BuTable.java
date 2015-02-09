package org.bu.android.db;

import java.util.Map;

public interface BuTable {
	String getTable();

	Map<String, String> colMaps();

	public static final String _ID = "_id";
	public static final String ID = "_i_id";
	public static final String CUID = "_cuid";
}