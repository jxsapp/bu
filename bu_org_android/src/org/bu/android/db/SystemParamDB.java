package org.bu.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SystemParamDB extends SQLiteOpenHelper {

	static interface DB {
		public static String NAME = "sptas_system.db";
		public static int VERSION = 1;
	}

	public SystemParamDB(Context context) {
		super(context, DB.NAME, null, DB.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		SystemParamCommonHolder.createCountryTable(db);
		SystemParamCommonHolder.createProvineTable(db);
		SystemParamCommonHolder.createCityTable(db);
		SystemParamCommonHolder.createGeneryTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + SystemParamCommonHolder.Table.PTAS_CITY_TB);
		db.execSQL("DROP TABLE IF EXISTS " + SystemParamCommonHolder.Table.PTAS_PROVINE_TB);
		db.execSQL("DROP TABLE IF EXISTS " + SystemParamCommonHolder.Table.PTAS_COUNTRY_TB);
		db.execSQL("DROP TABLE IF EXISTS " + SystemParamCommonHolder.Table.PTAS_GENERY_TB);
		onCreate(db);
	}

}
