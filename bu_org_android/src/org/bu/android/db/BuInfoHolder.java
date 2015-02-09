package org.bu.android.db;

import java.util.Map;

import org.bu.android.boot.BuApplication;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;

public abstract class BuInfoHolder extends AbstractBuInfoHolder<BuAuthorityProvider> {

	protected ContentResolver mContentResolver = null;

	protected BuInfoHolder(BuAuthorityProvider v) {
		super(v);
		mContentResolver = BuApplication.getApplication().getContentResolver();
	}

	

	/**
	 * CREATE TABLE IF NOT EXISTS mzj_user_tb ( _id INTEGER PRIMARY KEY
	 * AUTOINCREMENT, _user_id varchar(30) ,_user_name varchar(30) ,_password
	 * varchar(30) , _cuid varchar(30))
	 * 
	 * @param db
	 * @param table
	 */
	protected void creater(SQLiteDatabase db, BuTable table) {

		Map<String, String> colMaps = table.colMaps();

		StringBuffer createSQL = new StringBuffer(" CREATE TABLE IF NOT EXISTS ");
		createSQL.append(table.getTable());
		createSQL.append(" ( _id INTEGER PRIMARY KEY AUTOINCREMENT, ");
		for (String col : colMaps.keySet()) {
			createSQL.append(col + "  " + colMaps.get(col) + "   ,");
		}
		createSQL.append(BuTable.ID + " varchar(30), "); // 键值ID
		createSQL.append(BuTable.CUID + " varchar(30) "); // 多用户ID
		createSQL.append(" )");
		db.execSQL(createSQL.toString());
	}

}