package org.bu.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 熟人动态 数据库
 * 
 * The DataBase Of Acquaintance Actions
 * 
 * @author jiangxs
 * @date 2014年6月4日 上午10:38:34
 */
public abstract class BuInfoDB extends SQLiteOpenHelper {

	public static interface DB {
		public String dbName();

		public int dbVersion();

		void onCreate(SQLiteDatabase db);

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

	}

	private DB _db;

	public BuInfoDB(Context context, DB db) {
		super(context, db.dbName(), null, db.dbVersion());
		this._db = db;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		_db.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		_db.onUpgrade(db, oldVersion, newVersion);
	}

}
