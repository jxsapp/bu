package org.bu.android.db;

import java.util.ArrayList;
import java.util.List;

import org.bu.android.boot.BuApplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SystemParamCommonHolder {

	static public interface Table {
		public static final String PTAS_COUNTRY_TB = "ptas_country_tb";
		public static final String PTAS_PROVINE_TB = "ptas_provine_tb";
		public static final String PTAS_CITY_TB = "ptas_city_tb";

		public static final String ID = "cutemer_id";
		public static final String P_ID = "parent_id";
		public static final String NAME = "cutemer_name";
		public static final String KEY = "cutemer_key";
		public static final String AREA_CODE = "area_code";// 区号
		public static final String GEO_POINT = "geo_point";

		public static final String PTAS_GENERY_TB = "ptas_genery_tb";

		public static final String param_type = "param_type";
		public static final String param_type_desp = "param_type_desp";
		public static final String param_id = "param_id";
		public static final String param_value = "param_value";// 区号
		public static final String param_desp = "param_desp";

		static String GENERY_COLUMNS[] = { param_type, param_type_desp, param_id, param_value, param_desp };
	}

	public static void createCountryTable(SQLiteDatabase db) {
		StringBuffer createSQL = new StringBuffer(" CREATE TABLE IF NOT EXISTS ");
		createSQL.append(Table.PTAS_COUNTRY_TB);
		createSQL.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
		createSQL.append(Table.ID + " varchar(5) , ");
		createSQL.append(Table.NAME + " varchar(20),");
		createSQL.append(Table.KEY + " varchar(5)");
		createSQL.append(" )");
		db.execSQL(createSQL.toString());
	}

	public static void createProvineTable(SQLiteDatabase db) {
		StringBuffer createSQL = new StringBuffer(" CREATE TABLE IF NOT EXISTS ");
		createSQL.append(Table.PTAS_PROVINE_TB);
		createSQL.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
		createSQL.append(Table.ID + " varchar(5) , ");
		createSQL.append(Table.P_ID + " varchar(5) , ");// 国家的ID
		createSQL.append(Table.NAME + " varchar(20),");
		createSQL.append(Table.KEY + " varchar(5)");
		createSQL.append(" )");
		db.execSQL(createSQL.toString());
	}

	public static void createCityTable(SQLiteDatabase db) {
		StringBuffer createSQL = new StringBuffer(" CREATE TABLE IF NOT EXISTS ");
		createSQL.append(Table.PTAS_CITY_TB);
		createSQL.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
		createSQL.append(Table.ID + " varchar(5) , ");// 自身ID
		createSQL.append(Table.P_ID + " varchar(5) , ");// 省ID
		createSQL.append(Table.NAME + " varchar(25),");// 名称
		createSQL.append(Table.AREA_CODE + " varchar(10),");// 区号
		createSQL.append(Table.GEO_POINT + " varchar(50)");// 经纬度
		createSQL.append(" )");
		db.execSQL(createSQL.toString());
	}

	public static void createGeneryTable(SQLiteDatabase db) {
		StringBuffer createSQL = new StringBuffer(" CREATE TABLE IF NOT EXISTS ");
		createSQL.append(Table.PTAS_GENERY_TB);
		createSQL.append(" (_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
		createSQL.append(Table.param_type + " varchar(10), ");
		createSQL.append(Table.param_type_desp + " varchar(200), ");
		createSQL.append(Table.param_value + " varchar(200), ");
		createSQL.append(Table.param_desp + " varchar(200), ");
		createSQL.append(Table.param_id + " varchar(5)");
		createSQL.append(" )");
		db.execSQL(createSQL.toString());
	}

	public static boolean needInitData() {
		return getSystemParamCountrys().size() == 0;
	}

	public static void doInit() {
		SystemParamDB sdb = new SystemParamDB(BuApplication.getApplication());
		SQLiteDatabase db = sdb.getWritableDatabase();
		SystemParamCommonHolder.insertCoutry(db);
		SystemParamCommonHolder.insertIntoProvine(db);
		SystemParamCommonHolder.insertCity(db);
		SystemParamCommonHolder.insertGenery(db);
		db.close();
	}

	public static List<SystemParamCountry> getSystemParamCountrys() {
		SystemParamDB dbHolder = new SystemParamDB(BuApplication.getApplication());
		SQLiteDatabase db = dbHolder.getWritableDatabase();
		Cursor cursor = db.query(Table.PTAS_COUNTRY_TB, new String[] { "_id", Table.ID, Table.NAME, Table.KEY }, null, null, null, null, "_id asc");
		List<SystemParamCountry> results = new ArrayList<SystemParamCountry>();
		if (null != cursor && cursor.moveToFirst()) {
			do {
				SystemParamCountry coutry = new SystemParamCountry();
				coutry.setId(cursor.getString(1));
				coutry.setName(cursor.getString(2));
				coutry.setCode(cursor.getString(3));
				results.add(coutry);
			} while (cursor.moveToNext());
		}
		if (null != cursor)
			cursor.close();
		db.close();
		return results;
	}

	public static List<SystemParamProvine> getSystemParamProvines(String coutryId) {
		SystemParamDB dbHolder = new SystemParamDB(BuApplication.getApplication());
		SQLiteDatabase db = dbHolder.getWritableDatabase();
		Cursor cursor = db.query(Table.PTAS_PROVINE_TB, new String[] { "_id", Table.ID, Table.NAME }, Table.P_ID + "=?", new String[] { coutryId }, null, null, "_id asc");
		List<SystemParamProvine> results = new ArrayList<SystemParamProvine>();
		if (null != cursor && cursor.moveToFirst()) {
			do {
				SystemParamProvine provine = new SystemParamProvine();
				provine.setId(cursor.getString(1));
				provine.setName(cursor.getString(2));
				results.add(provine);
			} while (cursor.moveToNext());
		}
		if (null != cursor)
			cursor.close();
		db.close();
		return results;
	}

	public static List<SystemParamCity> getSystemParamCitys(String provineId) {
		SystemParamDB dbHolder = new SystemParamDB(BuApplication.getApplication());
		SQLiteDatabase db = dbHolder.getWritableDatabase();
		Cursor cursor = db.query(Table.PTAS_CITY_TB, new String[] { "_id", Table.ID, Table.NAME, Table.AREA_CODE, Table.GEO_POINT }, Table.P_ID + "=?", new String[] { provineId + "" }, null, null,
				"_id asc");
		List<SystemParamCity> results = new ArrayList<SystemParamCity>();
		if (null != cursor && cursor.moveToFirst()) {
			do {
				SystemParamCity city = new SystemParamCity();
				city.setId(cursor.getString(1));
				city.setPid(provineId);
				city.setName(cursor.getString(2));
				city.setAreaCode(cursor.getString(3));
				city.setGeoPoint(cursor.getString(4));
				results.add(city);
			} while (cursor.moveToNext());
		}
		if (null != cursor)
			cursor.close();
		db.close();
		return results;
	}

	public static List<SystemParamsGenery> getSystemParamGenerys(SystemParamsGenery.ParamType param_type) {
		SystemParamDB dbHolder = new SystemParamDB(BuApplication.getApplication());
		SQLiteDatabase db = dbHolder.getWritableDatabase();
		Cursor cursor = db.query(Table.PTAS_GENERY_TB, Table.GENERY_COLUMNS, Table.param_type + "=? ", new String[] { param_type.name }, null, null, "_id asc");
		List<SystemParamsGenery> results = new ArrayList<SystemParamsGenery>();
		if (null != cursor && cursor.moveToFirst()) {
			do {
				SystemParamsGenery coutry = new SystemParamsGenery();
				coutry.setParam_desp(cursor.getString(cursor.getColumnIndex(Table.param_desp)));
				coutry.setParam_id(cursor.getString(cursor.getColumnIndex(Table.param_id)));
				coutry.setParam_type(cursor.getString(cursor.getColumnIndex(Table.param_type)));
				coutry.setParam_type_desp(cursor.getString(cursor.getColumnIndex(Table.param_type_desp)));
				coutry.setParam_value(cursor.getString(cursor.getColumnIndex(Table.param_value)));
				results.add(coutry);
			} while (cursor.moveToNext());
		}
		if (null != cursor)
			cursor.close();
		db.close();
		return results;
	}

	public static void insertCoutry(SQLiteDatabase db) {
		String Columns = " (" + Table.ID + "," + Table.NAME + "," + Table.KEY + ") ";
		db.beginTransaction();
		try {
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"1\", \"中国\", \"CHN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"2\", \"阿尔巴尼亚\", \"ALB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"3\", \"阿尔及利亚\", \"DZA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"4\", \"阿富汗\", \"AFG\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"5\", \"阿根廷\", \"ARG\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"6\", \"阿拉伯联合酋长国\", \"ARE\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"7\", \"阿鲁巴\", \"ABW\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"8\", \"阿曼\", \"OMN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"9\", \"阿塞拜疆\", \"AZE\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"10\", \"阿森松岛\", \"ASC\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"11\", \"埃及\", \"EGY\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"12\", \"埃塞俄比亚\", \"ETH\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"13\", \"爱尔兰\", \"IRL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"14\", \"爱沙尼亚\", \"EST\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"15\", \"安道尔\", \"AND\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"16\", \"安哥拉\", \"AGO\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"17\", \"安圭拉\", \"AIA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"18\", \"安提瓜岛和巴布达\", \"ATG\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"19\", \"奥地利\", \"AUT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"20\", \"奥兰群岛\", \"ALA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"21\", \"澳大利亚\", \"AUS\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"22\", \"巴巴多斯岛\", \"BRB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"23\", \"巴布亚新几内亚\", \"PNG\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"24\", \"巴哈马\", \"BHS\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"25\", \"巴基斯坦\", \"PAK\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"26\", \"巴拉圭\", \"PRY\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"27\", \"巴勒斯坦\", \"PSE\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"28\", \"巴林\", \"BHR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"29\", \"巴拿马\", \"PAN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"30\", \"巴西\", \"BRA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"31\", \"白俄罗斯\", \"BLR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"32\", \"百慕大\", \"BMU\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"33\", \"保加利亚\", \"BGR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"34\", \"北马里亚纳群岛\", \"MNP\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"35\", \"贝宁\", \"BEN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"36\", \"比利时\", \"BEL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"37\", \"冰岛\", \"ISL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"38\", \"波多黎各\", \"PRI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"39\", \"波兰\", \"POL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"40\", \"波斯尼亚和黑塞哥维那\", \"BIH\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"41\", \"玻利维亚\", \"BOL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"42\", \"伯利兹\", \"BLZ\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"43\", \"博茨瓦纳\", \"BWA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"44\", \"不丹\", \"BTN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"45\", \"布基纳法索\", \"BFA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"46\", \"布隆迪\", \"BDI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"47\", \"布韦岛\", \"BVT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"48\", \"朝鲜\", \"PRK\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"49\", \"丹麦\", \"DNK\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"50\", \"德国\", \"DEU\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"51\", \"东帝汶\", \"TLS\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"52\", \"多哥\", \"TGO\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"53\", \"多米尼加\", \"DMA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"54\", \"多米尼加共和国\", \"DOM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"55\", \"俄罗斯\", \"RUS\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"56\", \"厄瓜多尔\", \"ECU\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"57\", \"厄立特里亚\", \"ERI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"58\", \"法国\", \"FRA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"59\", \"法罗群岛\", \"FRO\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"60\", \"法属波利尼西亚\", \"PYF\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"61\", \"法属圭亚那\", \"GUF\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"62\", \"法属南部领地\", \"ATF\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"63\", \"梵蒂冈\", \"VAT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"64\", \"菲律宾\", \"PHL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"65\", \"斐济\", \"FJI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"66\", \"芬兰\", \"FIN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"67\", \"佛得角\", \"CPV\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"68\", \"弗兰克群岛\", \"FLK\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"69\", \"冈比亚\", \"GMB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"70\", \"刚果\", \"COG\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"71\", \"刚果民主共和国\", \"COD\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"72\", \"哥伦比亚\", \"COL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"73\", \"哥斯达黎加\", \"CRI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"74\", \"格恩西岛\", \"GGY\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"75\", \"格林纳达\", \"GRD\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"76\", \"格陵兰\", \"GRL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"77\", \"古巴\", \"CUB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"78\", \"瓜德罗普\", \"GLP\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"79\", \"关岛\", \"GUM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"80\", \"圭亚那\", \"GUY\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"81\", \"哈萨克斯坦\", \"KAZ\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"82\", \"海地\", \"HTI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"83\", \"韩国\", \"KOR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"84\", \"荷兰\", \"NLD\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"85\", \"荷属安地列斯\", \"ANT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"86\", \"赫德和麦克唐纳群岛\", \"HMD\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"87\", \"洪都拉斯\", \"HND\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"88\", \"基里巴斯\", \"KIR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"89\", \"吉布提\", \"DJI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"90\", \"吉尔吉斯斯坦\", \"KGZ\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"91\", \"几内亚\", \"GIN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"92\", \"几内亚比绍\", \"GNB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"93\", \"加拿大\", \"CAN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"94\", \"加纳\", \"GHA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"95\", \"加蓬\", \"GAB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"96\", \"柬埔寨\", \"KHM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"97\", \"捷克共和国\", \"CZE\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"98\", \"津巴布韦\", \"ZWE\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"99\", \"喀麦隆\", \"CMR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"100\", \"卡塔尔\", \"QAT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"101\", \"开曼群岛\", \"CYM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"102\", \"科科斯群岛\", \"CCK\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"103\", \"科摩罗\", \"COM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"104\", \"科特迪瓦\", \"CIV\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"105\", \"科威特\", \"KWT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"106\", \"克罗地亚\", \"HRV\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"107\", \"肯尼亚\", \"KEN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"108\", \"库克群岛\", \"COK\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"109\", \"拉脱维亚\", \"LVA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"110\", \"莱索托\", \"LSO\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"111\", \"老挝\", \"LAO\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"112\", \"黎巴嫩\", \"LBN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"113\", \"立陶宛\", \"LTU\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"114\", \"利比里亚\", \"LBR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"115\", \"利比亚\", \"LBY\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"116\", \"列支敦士登\", \"LIE\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"117\", \"留尼旺岛\", \"REU\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"118\", \"卢森堡\", \"LUX\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"119\", \"卢旺达\", \"RWA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"120\", \"罗马尼亚\", \"ROU\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"121\", \"马达加斯加\", \"MDG\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"122\", \"马尔代夫\", \"MDV\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"123\", \"马耳他\", \"MLT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"124\", \"马拉维\", \"MWI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"125\", \"马来西亚\", \"MYS\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"126\", \"马里\", \"MLI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"127\", \"马其顿\", \"MKD\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"128\", \"马绍尔群岛\", \"MHL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"129\", \"马提尼克\", \"MTQ\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"130\", \"马约特岛\", \"MYT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"131\", \"曼岛\", \"IMN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"132\", \"毛里求斯\", \"MUS\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"133\", \"毛里塔尼亚\", \"MRT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"134\", \"美国\", \"USA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"135\", \"美属萨摩亚\", \"ASM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"136\", \"美属外岛\", \"UMI\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"137\", \"蒙古\", \"MNG\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"138\", \"蒙特塞拉特\", \"MSR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"139\", \"孟加拉\", \"BGD\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"140\", \"秘鲁\", \"PER\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"141\", \"密克罗尼西亚\", \"FSM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"142\", \"缅甸\", \"MMR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"143\", \"摩尔多瓦\", \"MDA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"144\", \"摩洛哥\", \"MAR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"145\", \"摩纳哥\", \"MCO\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"146\", \"莫桑比克\", \"MOZ\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"147\", \"墨西哥\", \"MEX\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"148\", \"纳米比亚\", \"NAM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"149\", \"南非\", \"ZAF\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"150\", \"南极洲\", \"ATA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"151\", \"南乔治亚和南桑德威奇群岛\", \"SGS\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"152\", \"瑙鲁\", \"NRU\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"153\", \"尼泊尔\", \"NPL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"154\", \"尼加拉瓜\", \"NIC\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"155\", \"尼日尔\", \"NER\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"156\", \"尼日利亚\", \"NGA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"157\", \"纽埃\", \"NIU\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"158\", \"挪威\", \"NOR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"159\", \"诺福克\", \"NFK\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"160\", \"帕劳群岛\", \"PLW\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"161\", \"皮特凯恩\", \"PCN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"162\", \"葡萄牙\", \"PRT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"163\", \"乔治亚\", \"GEO\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"164\", \"日本\", \"JPN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"165\", \"瑞典\", \"SWE\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"166\", \"瑞士\", \"CHE\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"167\", \"萨尔瓦多\", \"SLV\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"168\", \"萨摩亚\", \"WSM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"169\", \"塞尔维亚,黑山\", \"SCG\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"170\", \"塞拉利昂\", \"SLE\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"171\", \"塞内加尔\", \"SEN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"172\", \"塞浦路斯\", \"CYP\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"173\", \"塞舌尔\", \"SYC\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"174\", \"沙特阿拉伯\", \"SAU\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"175\", \"圣诞岛\", \"CXR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"176\", \"圣多美和普林西比\", \"STP\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"177\", \"圣赫勒拿\", \"SHN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"178\", \"圣基茨和尼维斯\", \"KNA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"179\", \"圣卢西亚\", \"LCA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"180\", \"圣马力诺\", \"SMR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"181\", \"圣皮埃尔和米克隆群岛\", \"SPM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"182\", \"圣文森特和格林纳丁斯\", \"VCT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"183\", \"斯里兰卡\", \"LKA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"184\", \"斯洛伐克\", \"SVK\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"185\", \"斯洛文尼亚\", \"SVN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"186\", \"斯瓦尔巴和扬马廷\", \"SJM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"187\", \"斯威士兰\", \"SWZ\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"188\", \"苏丹\", \"SDN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"189\", \"苏里南\", \"SUR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"190\", \"所罗门群岛\", \"SLB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"191\", \"索马里\", \"SOM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"192\", \"塔吉克斯坦\", \"TJK\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"193\", \"泰国\", \"THA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"194\", \"坦桑尼亚\", \"TZA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"195\", \"汤加\", \"TON\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"196\", \"特克斯和凯克特斯群岛\", \"TCA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"197\", \"特里斯坦达昆哈\", \"TAA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"198\", \"特立尼达和多巴哥\", \"TTO\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"199\", \"突尼斯\", \"TUN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"200\", \"图瓦卢\", \"TUV\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"201\", \"土耳其\", \"TUR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"202\", \"土库曼斯坦\", \"TKM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"203\", \"托克劳\", \"TKL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"204\", \"瓦利斯和福图纳\", \"WLF\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"205\", \"瓦努阿图\", \"VUT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"206\", \"危地马拉\", \"GTM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"207\", \"维尔京群岛，美属\", \"VIR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"208\", \"维尔京群岛，英属\", \"VGB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"209\", \"委内瑞拉\", \"VEN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"210\", \"文莱\", \"BRN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"211\", \"乌干达\", \"UGA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"212\", \"乌克兰\", \"UKR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"213\", \"乌拉圭\", \"URY\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"214\", \"乌兹别克斯坦\", \"UZB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"215\", \"西班牙\", \"ESP\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"216\", \"希腊\", \"GRC\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"217\", \"新加坡\", \"SGP\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"218\", \"新喀里多尼亚\", \"NCL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"219\", \"新西兰\", \"NZL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"220\", \"匈牙利\", \"HUN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"221\", \"叙利亚\", \"SYR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"222\", \"牙买加\", \"JAM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"223\", \"亚美尼亚\", \"ARM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"224\", \"也门\", \"YEM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"225\", \"伊拉克\", \"IRQ\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"226\", \"伊朗\", \"IRN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"227\", \"以色列\", \"ISR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"228\", \"意大利\", \"ITA\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"229\", \"印度\", \"IND\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"230\", \"印度尼西亚\", \"IDN\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"231\", \"英国\", \"GBR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"232\", \"英属印度洋领地\", \"IOT\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"233\", \"约旦\", \"JOR\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"234\", \"越南\", \"VNM\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"235\", \"赞比亚\", \"ZMB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"236\", \"泽西岛\", \"JEY\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"237\", \"乍得\", \"TCD\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"238\", \"直布罗陀\", \"GIB\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"239\", \"智利\", \"CHL\")");
			db.execSQL("insert   into   " + Table.PTAS_COUNTRY_TB + Columns + "     values(\"240\", \"中非共和国\", \"CAF\")");

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static void insertIntoProvine(SQLiteDatabase db) {
		String Columns = " (" + Table.P_ID + "," + Table.ID + "," + Table.NAME + "," + Table.KEY + ") ";
		db.beginTransaction();

		try {

			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"1\",\"北京市\",\"B\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"2\",\"天津市\",\"T\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"3\",\"上海市\",\"S\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"4\",\"重庆市\",\"C\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"5\",\"河北省\",\"H\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"6\",\"山西省\",\"S\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"7\",\"台湾省\",\"T\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"8\",\"辽宁省\",\"L\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"9\",\"吉林省\",\"J\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"10\",\"黑龙江省\",\"H\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"11\",\"江苏省\",\"J\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"12\",\"浙江省\",\"Z\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"13\",\"安徽省\",\"A\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"14\",\"福建省\",\"F\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"15\",\"江西省\",\"J\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"16\",\"山东省\",\"S\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"17\",\"河南省\",\"H\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"18\",\"湖北省\",\"H\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"19\",\"湖南省\",\"H\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"20\",\"广东省\",\"G\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"21\",\"甘肃省\",\"G\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"22\",\"四川省\",\"S\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"23\",\"贵州省\",\"G\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"24\",\"海南省\",\"H\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"25\",\"云南省\",\"Y\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"26\",\"青海省\",\"Q\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"27\",\"陕西省\",\"S\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"28\",\"广西壮族自治区\",\"G\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"29\",\"西藏自治区\",\"X\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"30\",\"宁夏回族自治区\",\"N\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"31\",\"新疆维吾尔自治区\",\"X\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"32\",\"内蒙古自治区\",\"N\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"33\",\"澳门特别行政区\",\"A\")");
			db.execSQL("insert   into   " + Table.PTAS_PROVINE_TB + Columns + "     values(\"1\",\"34\",\"香港特别行政区\",\"X\")");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static void insertCity(SQLiteDatabase db) {
		String Columns = " (" + Table.ID + "," + Table.P_ID + "," + Table.NAME + "," + Table.GEO_POINT + "," + Table.AREA_CODE + ") ";
		db.beginTransaction();
		try {

			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"1\",\"1\",\"北京市\",\"39.929983,116.395636\",\"010\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"2\",\"2\",\"天津市\",\"39.143928,117.210813\",\"022\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"3\",\"3\",\"上海市\",\"31.249158,121.487897\",\"021\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"4\",\"4\",\"重庆市\",\"29.544604,106.530634\",\"023\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"5\",\"5\",\"石家庄市\",\"38.048958,114.522076\",\"0311\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"6\",\"5\",\"唐山市\",\"39.650527,118.183444\",\"0315\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"7\",\"5\",\"秦皇岛市\",\"39.945459,119.604366\",\"0335\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"8\",\"5\",\"邯郸市\",\"36.609303,114.482686\",\"0310\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"9\",\"5\",\"邢台市\",\"37.069526,114.520477\",\"0319\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"10\",\"5\",\"保定市\",\"38.886561,115.494807\",\"0312\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"11\",\"5\",\"张家口市\",\"40.811181,114.893777\",\"0313\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"12\",\"5\",\"承德市\",\"40.992517,117.933814\",\"0314\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"13\",\"5\",\"沧州市\",\"38.297614,116.863797\",\"0317\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"14\",\"5\",\"廊坊市\",\"39.518609,116.703593\",\"0316\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"15\",\"5\",\"衡水市\",\"37.746928,115.686226\",\"0318\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"16\",\"6\",\"太原市\",\"37.890275,112.550862\",\"0351\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"17\",\"6\",\"大同市\",\"40.113743,113.2905\",\"0352\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"18\",\"6\",\"阳泉市\",\"37.869526,113.569235\",\"0353\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"19\",\"6\",\"长治市\",\"36.201663,113.120289\",\"0355\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"20\",\"6\",\"晋城市\",\"35.499828,112.867326\",\"0356\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"21\",\"6\",\"朔州市\",\"39.337671,112.479923\",\"0349\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"22\",\"6\",\"晋中市\",\"37.693358,112.738509\",\"0354\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"23\",\"6\",\"运城市\",\"35.038853,111.006845\",\"0359\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"24\",\"6\",\"忻州市\",\"38.461029,112.727936\",\"0350\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"25\",\"6\",\"临汾市\",\"36.099743,111.538786\",\"0357\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"26\",\"6\",\"吕梁市\",\"37.527313,111.143154\",\"0358\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"27\",\"8\",\"沈阳市\",\"41.808638,123.432782\",\"024\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"28\",\"8\",\"大连市\",\"38.948706,121.593474\",\"0411\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"29\",\"8\",\"鞍山市\",\"41.118741,123.007758\",\"0412\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"30\",\"8\",\"抚顺市\",\"41.877297,123.929814\",\"0413\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"31\",\"8\",\"本溪市\",\"41.325831,123.778055\",\"0414\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"32\",\"8\",\"丹东市\",\"40.129019,124.338534\",\"0415\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"33\",\"8\",\"锦州市\",\"41.130873,121.147744\",\"0416\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"34\",\"8\",\"营口市\",\"40.668647,122.233382\",\"0417\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"35\",\"8\",\"阜新市\",\"42.019243,121.66082\",\"0418\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"36\",\"8\",\"辽阳市\",\"41.273334,123.172444\",\"0419\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"37\",\"8\",\"盘锦市\",\"41.141244,122.073224\",\"0427\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"38\",\"8\",\"铁岭市\",\"42.299752,123.854842\",\"0410\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"39\",\"8\",\"朝阳市\",\"41.571824,120.446159\",\"0421\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"40\",\"8\",\"葫芦岛市\",\"40.743027,120.860754\",\"0429\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"41\",\"9\",\"长春市\",\"43.898335,125.313636\",\"0431\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"42\",\"9\",\"吉林市\",\"43.871982,126.564535\",\"0432\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"43\",\"9\",\"四平市\",\"43.175519,124.391373\",\"0434\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"44\",\"9\",\"辽源市\",\"42.923296,125.133679\",\"0437\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"45\",\"9\",\"通化市\",\"41.736392,125.942648\",\"0435\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"46\",\"9\",\"白山市\",\"41.945855,126.43579\",\"0439\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"47\",\"9\",\"松原市\",\"45.136047,124.832989\",\"0438\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"48\",\"9\",\"白城市\",\"45.62108,122.840772\",\"0436\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"49\",\"9\",\"延边朝鲜族自治州\",\"42.896413,129.485897\",\"0433\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"50\",\"10\",\"哈尔滨市\",\"45.773219,126.657708\",\"0451\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"51\",\"10\",\"齐齐哈尔市\",\"47.347695,123.987288\",\"0452\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"52\",\"10\",\"鹤岗市\",\"47.338664,130.292467\",\"0468\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"53\",\"10\",\"双鸭山市\",\"46.655096,131.171396\",\"0469\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"54\",\"10\",\"鸡西市\",\"45.321534,130.941763\",\"0467\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"55\",\"10\",\"大庆市\",\"46.596702,125.021831\",\"0459\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"56\",\"10\",\"伊春市\",\"47.734682,128.910757\",\"0458\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"57\",\"10\",\"牡丹江市\",\"44.58852,129.60803\",\"0453\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"58\",\"10\",\"佳木斯市\",\"46.813777,130.284733\",\"0454\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"59\",\"10\",\"七台河市\",\"45.775005,131.019044\",\"0464\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"60\",\"10\",\"黑河市\",\"50.250688,127.500821\",\"0456\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"61\",\"10\",\"绥化市\",\"46.646058,126.989093\",\"0455\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"62\",\"10\",\"大兴安岭地区\",\"51.991788,124.196099\",\"0457\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"63\",\"11\",\"南京市\",\"32.057233,118.778068\",\"025\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"64\",\"11\",\"无锡市\",\"31.57003,120.305448\",\"0510\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"65\",\"11\",\"徐州市\",\"34.271548,117.188103\",\"0516\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"66\",\"11\",\"常州市\",\"31.771395,119.981861\",\"0519\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"67\",\"11\",\"苏州市\",\"31.317986,120.6199\",\"0512\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"68\",\"11\",\"南通市\",\"32.014663,120.873797\",\"0513\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"69\",\"11\",\"连云港市\",\"34.601547,119.173871\",\"0518\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"70\",\"11\",\"淮安市\",\"33.606508,119.030178\",\"0517\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"71\",\"11\",\"盐城市\",\"33.379857,120.148864\",\"0515\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"72\",\"11\",\"扬州市\",\"32.408505,119.427777\",\"0514\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"73\",\"11\",\"镇江市\",\"32.204402,119.455831\",\"0511\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"74\",\"11\",\"泰州市\",\"32.476053,119.919599\",\"0523\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"75\",\"11\",\"宿迁市\",\"33.952045,118.296891\",\"0527\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"76\",\"12\",\"杭州市\",\"30.259243,120.219372\",\"0571\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"77\",\"12\",\"宁波市\",\"29.885256,121.579003\",\"0574\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"78\",\"12\",\"温州市\",\"28.00283,120.690633\",\"0577\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"79\",\"12\",\"嘉兴市\",\"30.773988,120.760422\",\"0573\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"80\",\"12\",\"湖州市\",\"30.877921,120.13724\",\"0572\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"81\",\"12\",\"绍兴市\",\"30.002362,120.592466\",\"0575\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"82\",\"12\",\"金华市\",\"29.102898,119.652569\",\"0579\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"83\",\"12\",\"衢州市\",\"28.956904,118.87584\",\"0570\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"84\",\"12\",\"舟山市\",\"30.036002,122.169863\",\"0580\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"85\",\"12\",\"台州市\",\"28.668278,121.44061\",\"0576\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"86\",\"12\",\"丽水市\",\"28.456299,119.92957\",\"0578\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"87\",\"13\",\"合肥市\",\"31.86694,117.282695\",\"0551\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"88\",\"13\",\"芜湖市\",\"31.366014,118.384107\",\"0553\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"89\",\"13\",\"蚌埠市\",\"32.929491,117.357075\",\"0552\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"90\",\"13\",\"淮南市\",\"32.642809,117.018638\",\"0554\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"91\",\"13\",\"马鞍山市\",\"31.688525,118.51588\",\"0555\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"92\",\"13\",\"淮北市\",\"33.960017,116.791439\",\"0561\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"93\",\"13\",\"铜陵市\",\"30.940927,117.819424\",\"0562\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"94\",\"13\",\"安庆市\",\"30.537891,117.058729\",\"0556\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"95\",\"13\",\"黄山市\",\"29.734428,118.293567\",\"0559\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"96\",\"13\",\"滁州市\",\"32.317344,118.324568\",\"0550\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"97\",\"13\",\"阜阳市\",\"32.901205,115.820927\",\"0558\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"98\",\"13\",\"宿州市\",\"33.636766,116.988689\",\"0557\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"99\",\"13\",\"巢湖市\",\"31.608726,117.880481\",\"0565\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"100\",\"13\",\"六安市\",\"31.755553,116.505248\",\"0564\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"101\",\"13\",\"亳州市\",\"33.871205,115.787924\",\"0558\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"102\",\"13\",\"池州市\",\"30.660016,117.494471\",\"0566\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"103\",\"13\",\"宣城市\",\"30.951635,118.752089\",\"0563\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"104\",\"14\",\"福州市\",\"26.047125,119.330212\",\"0591\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"105\",\"14\",\"厦门市\",\"24.489228,118.103881\",\"0592\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"106\",\"14\",\"莆田市\",\"25.448448,119.077725\",\"0594\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"107\",\"14\",\"三明市\",\"26.270827,117.642188\",\"0598\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"108\",\"14\",\"泉州市\",\"24.901648,118.600356\",\"0595\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"109\",\"14\",\"漳州市\",\"24.517061,117.676198\",\"0596\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"110\",\"14\",\"南平市\",\"26.643623,118.181881\",\"0599\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"111\",\"14\",\"龙岩市\",\"25.078681,117.017991\",\"0597\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"112\",\"14\",\"宁德市\",\"26.656522,119.542077\",\"0593\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"113\",\"15\",\"南昌市\",\"28.689573,115.893519\",\"0791\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"114\",\"15\",\"景德镇市\",\"29.303557,117.186513\",\"0798\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"115\",\"15\",\"萍乡市\",\"27.63954,113.859908\",\"0799\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"116\",\"15\",\"九江市\",\"29.719632,115.999843\",\"0792\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"117\",\"15\",\"新余市\",\"27.822317,114.94711\",\"0790\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"118\",\"15\",\"鹰潭市\",\"28.241304,117.035445\",\"0701\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"119\",\"15\",\"赣州市\",\"25.84529,114.935908\",\"0797\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"120\",\"15\",\"吉安市\",\"27.113842,114.992034\",\"0796\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"121\",\"15\",\"宜春市\",\"27.811126,114.400033\",\"0795\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"122\",\"15\",\"抚州市\",\"27.954539,116.360917\",\"0794\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"123\",\"15\",\"上饶市\",\"28.457617,117.955463\",\"0793\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"124\",\"16\",\"济南市\",\"36.68278,117.024962\",\"0531\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"125\",\"16\",\"青岛市\",\"36.10521,120.384427\",\"0532\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"126\",\"16\",\"淄博市\",\"36.804681,118.059127\",\"0533\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"127\",\"16\",\"枣庄市\",\"34.807875,117.279299\",\"0632\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"128\",\"16\",\"东营市\",\"37.487115,118.583917\",\"0546\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"129\",\"16\",\"烟台市\",\"37.536557,121.309547\",\"0535\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"130\",\"16\",\"潍坊市\",\"36.716114,119.142628\",\"0536\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"131\",\"16\",\"济宁市\",\"35.402121,116.600791\",\"0537\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"132\",\"16\",\"泰安市\",\"36.188076,117.089407\",\"0538\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"133\",\"16\",\"威海市\",\"37.52878,122.093956\",\"0631\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"134\",\"16\",\"日照市\",\"35.42022,119.507178\",\"0633\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"135\",\"16\",\"莱芜市\",\"36.233649,117.68466\",\"0634\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"136\",\"16\",\"临沂市\",\"35.072405,118.340764\",\"0539\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"137\",\"16\",\"德州市\",\"37.460824,116.328156\",\"0534\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"138\",\"16\",\"聊城市\",\"36.455825,115.986862\",\"0635\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"139\",\"16\",\"滨州市\",\"37.405312,117.968291\",\"0543\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"140\",\"16\",\"菏泽市\",\"35.26244,115.463357\",\"0530\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"141\",\"17\",\"郑州市\",\"34.756607,113.649642\",\"0371\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"142\",\"17\",\"开封市\",\"34.801851,114.351641\",\"0378\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"143\",\"17\",\"洛阳市\",\"34.657366,112.447521\",\"0379\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"144\",\"17\",\"平顶山市\",\"33.745294,113.300848\",\"0375\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"145\",\"17\",\"安阳市\",\"36.110262,114.351803\",\"0372\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"146\",\"17\",\"鹤壁市\",\"35.755419,114.297761\",\"0392\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"147\",\"17\",\"新乡市\",\"35.307255,113.912684\",\"0373\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"148\",\"17\",\"焦作市\",\"35.234606,113.211835\",\"0391\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"149\",\"17\",\"濮阳市\",\"35.753296,115.026619\",\"0393\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"150\",\"17\",\"许昌市\",\"34.026734,113.835304\",\"0374\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"151\",\"17\",\"漯河市\",\"33.576277,114.046055\",\"0395\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"152\",\"17\",\"三门峡市\",\"34.783316,111.18126\",\"0398\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"153\",\"17\",\"南阳市\",\"33.011418,112.54284\",\"0377\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"154\",\"17\",\"商丘市\",\"34.438588,115.641877\",\"0370\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"155\",\"17\",\"信阳市\",\"32.128577,114.085482\",\"0376\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"156\",\"17\",\"周口市\",\"33.623735,114.6541\",\"0394\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"157\",\"17\",\"驻马店市\",\"32.983151,114.049146\",\"0396\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"158\",\"17\",\"济源市\",\"35.10536,112.405264\",\"0391\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"159\",\"18\",\"武汉市\",\"30.581078,114.316194\",\"027\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"160\",\"18\",\"黄石市\",\"30.216123,115.050675\",\"0714\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"161\",\"18\",\"十堰市\",\"32.636987,110.801223\",\"0719\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"162\",\"18\",\"荆州市\",\"30.332584,112.241863\",\"0716\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"163\",\"18\",\"宜昌市\",\"30.73275,111.310976\",\"0717\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"164\",\"18\",\"襄樊市\",\"32.094927,112.176322\",\"0710\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"165\",\"18\",\"鄂州市\",\"30.384431,114.895592\",\"0711\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"166\",\"18\",\"荆门市\",\"31.042605,112.217321\",\"0724\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"167\",\"18\",\"孝感市\",\"30.927948,113.935725\",\"0712\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"168\",\"18\",\"黄冈市\",\"30.446104,114.906614\",\"0713\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"169\",\"18\",\"咸宁市\",\"29.880653,114.300051\",\"0715\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"170\",\"18\",\"随州市\",\"31.717856,113.379351\",\"0722\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"171\",\"18\",\"仙桃市\",\"30.293964,113.387445\",\"0728\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"172\",\"18\",\"天门市\",\"30.649042,113.126226\",\"0728\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"173\",\"18\",\"潜江市\",\"30.343112,112.768764\",\"0728\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"174\",\"18\",\"神农架林区\",\"31.595762,110.487229\",\"0719\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"175\",\"18\",\"恩施土家族苗族自治州\",\"30.285887,109.491916\",\"0718\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"176\",\"19\",\"长沙市\",\"28.213472,112.979344\",\"0731\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"177\",\"19\",\"株洲市\",\"27.827428,113.131688\",\"0733\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"178\",\"19\",\"湘潭市\",\"27.835087,112.935552\",\"0732\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"179\",\"19\",\"衡阳市\",\"26.898163,112.583812\",\"0734\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"180\",\"19\",\"邵阳市\",\"27.236803,111.461523\",\"0739\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"181\",\"19\",\"岳阳市\",\"29.378004,113.146187\",\"0730\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"182\",\"19\",\"常德市\",\"29.012141,111.653715\",\"0736\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"183\",\"19\",\"张家界市\",\"29.124885,110.481615\",\"0744\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"184\",\"19\",\"益阳市\",\"28.58808,112.366538\",\"0737\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"185\",\"19\",\"郴州市\",\"25.782256,113.037698\",\"0735\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"186\",\"19\",\"永州市\",\"26.435967,111.614639\",\"0746\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"187\",\"19\",\"怀化市\",\"27.55748,109.986954\",\"0745\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"188\",\"19\",\"娄底市\",\"27.741067,111.996392\",\"0738\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"189\",\"19\",\"湘西土家族苗族自治州\",\"28.317944,109.745741\",\"0743\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"190\",\"20\",\"广州市\",\"23.120048,113.307648\",\"020\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"191\",\"20\",\"深圳市\",\"22.54605,114.025969\",\"0755\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"192\",\"20\",\"珠海市\",\"22.25691,113.562444\",\"0756\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"193\",\"20\",\"汕头市\",\"23.3839,116.728647\",\"0754\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"194\",\"20\",\"韶关市\",\"24.802957,113.594459\",\"0751\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"195\",\"20\",\"佛山市\",\"23.035093,113.134024\",\"0757\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"196\",\"20\",\"江门市\",\"22.575112,113.078122\",\"0759\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"197\",\"20\",\"湛江市\",\"21.25746,110.36506\",\"0759\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"198\",\"20\",\"茂名市\",\"21.66822,110.931244\",\"0668\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"199\",\"20\",\"肇庆市\",\"23.078662,112.479644\",\"0758\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"200\",\"20\",\"惠州市\",\"23.113533,114.410651\",\"0752\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"201\",\"20\",\"梅州市\",\"24.304569,116.126396\",\"0753\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"202\",\"20\",\"汕尾市\",\"22.778727,115.372915\",\"0660\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"203\",\"20\",\"河源市\",\"23.757244,114.713721\",\"0762\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"204\",\"20\",\"阳江市\",\"21.87151,111.977006\",\"0662\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"205\",\"20\",\"清远市\",\"23.698464,113.040771\",\"0763\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"206\",\"20\",\"东莞市\",\"23.043016,113.76343\",\"0769\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"207\",\"20\",\"中山市\",\"22.545174,113.422056\",\"0760\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"208\",\"20\",\"潮州市\",\"23.661804,116.630067\",\"0768\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"209\",\"20\",\"揭阳市\",\"23.547999,116.379494\",\"0663\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"210\",\"20\",\"云浮市\",\"22.937969,112.050937\",\"0766\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"211\",\"21\",\"兰州市\",\"36.064222,103.823303\",\"0931\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"212\",\"21\",\"金昌市\",\"38.516067,102.208122\",\"0935\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"213\",\"21\",\"白银市\",\"36.54668,104.171234\",\"0943\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"214\",\"21\",\"天水市\",\"34.584319,105.736927\",\"0938\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"215\",\"21\",\"嘉峪关市\",\"39.802393,98.281629\",\"0937\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"216\",\"21\",\"武威市\",\"37.933171,102.640145\",\"0935\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"217\",\"21\",\"张掖市\",\"38.939319,100.459885\",\"0936\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"218\",\"21\",\"平凉市\",\"35.550105,106.688906\",\"0933\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"219\",\"21\",\"酒泉市\",\"39.741467,98.508406\",\"0937\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"220\",\"21\",\"庆阳市\",\"35.726797,107.644218\",\"0934\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"221\",\"21\",\"定西市\",\"35.586049,104.62663\",\"0932\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"222\",\"21\",\"陇南市\",\"33.394477,104.934569\",\"0939\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"223\",\"21\",\"临夏回族自治州\",\"35.598508,103.215249\",\"0930\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"224\",\"21\",\"甘南藏族自治州\",\"34.99221,102.917433\",\"0941\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"225\",\"22\",\"成都市\",\"30.679942,104.06792\",\"028\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"226\",\"22\",\"自贡市\",\"29.35915,104.776063\",\"0813\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"227\",\"22\",\"攀枝花市\",\"26.587567,101.722418\",\"0812\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"228\",\"22\",\"泸州市\",\"28.895922,105.443963\",\"0830\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"229\",\"22\",\"德阳市\",\"31.131137,104.402395\",\"0838\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"230\",\"22\",\"绵阳市\",\"31.504699,104.705511\",\"0816\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"231\",\"22\",\"广元市\",\"32.44104,105.819679\",\"0839\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"232\",\"22\",\"遂宁市\",\"30.557485,105.564884\",\"0825\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"233\",\"22\",\"内江市\",\"29.599459,105.073052\",\"0832\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"234\",\"22\",\"乐山市\",\"29.60095,103.760817\",\"0833\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"235\",\"22\",\"南充市\",\"30.800963,106.105547\",\"0817\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"236\",\"22\",\"眉山市\",\"30.061113,103.841422\",\"028\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"237\",\"22\",\"宜宾市\",\"28.769669,104.633017\",\"0831\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"238\",\"22\",\"广安市\",\"30.463982,106.635718\",\"0826\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"239\",\"22\",\"达州市\",\"31.214197,107.494965\",\"0818\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"240\",\"22\",\"雅安市\",\"29.999712,103.009348\",\"0835\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"241\",\"22\",\"巴中市\",\"31.869186,106.757914\",\"0827\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"242\",\"22\",\"资阳市\",\"30.132183,104.635928\",\"028\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"243\",\"22\",\"阿坝藏族羌族自治州\",\"31.905755,102.228559\",\"0837\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"244\",\"22\",\"甘孜藏族自治州\",\"30.055143,101.969227\",\"0836\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"245\",\"22\",\"凉山彝族自治州\",\"27.892387,102.259586\",\"0834\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"246\",\"23\",\"贵阳市\",\"26.629906,106.709172\",\"0851\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"247\",\"23\",\"六盘水市\",\"26.591864,104.852078\",\"0858\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"248\",\"23\",\"遵义市\",\"27.699959,106.931251\",\"0852\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"249\",\"23\",\"安顺市\",\"26.228589,105.928266\",\"0853\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"250\",\"23\",\"铜仁地区\",\"27.726263,109.196158\",\"0856\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"251\",\"23\",\"毕节地区\",\"27.302606,105.300485\",\"0857\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"252\",\"23\",\"黔西南布依族苗族自治州\",\"25.095141,104.900551\",\"0859\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"253\",\"23\",\"黔东南苗族侗族自治州\",\"26.583989,107.98535\",\"0855\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"254\",\"23\",\"黔南布依族苗族自治州\",\"26.264534,107.523199\",\"0854\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"255\",\"24\",\"海口市\",\"20.022068,110.330798\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"256\",\"24\",\"三亚市\",\"18.257775,109.522764\",\"0899\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"257\",\"24\",\"五指山市\",\"18.831299,109.517742\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"258\",\"24\",\"琼海市\",\"19.21483,110.41435\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"259\",\"24\",\"儋州市\",\"19.571147,109.41397\",\"0890\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"260\",\"24\",\"文昌市\",\"19.750939,110.780903\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"261\",\"24\",\"万宁市\",\"18.839879,110.292504\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"262\",\"24\",\"东方市\",\"18.998157,108.851002\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"263\",\"24\",\"澄迈县\",\"19.693133,109.996728\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"264\",\"24\",\"定安县\",\"19.49099,110.320082\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"265\",\"24\",\"屯昌县\",\"19.347746,110.063355\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"266\",\"24\",\"临高县\",\"19.805918,109.724101\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"267\",\"24\",\"白沙黎族自治县\",\"19.21605,109.35858\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"268\",\"24\",\"昌江黎族自治县\",\"19.222476,109.011295\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"269\",\"24\",\"乐东黎族自治县\",\"18.658613,109.062697\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"270\",\"24\",\"陵水黎族自治县\",\"18.575977,109.948659\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"271\",\"24\",\"保亭黎族苗族自治县\",\"18.597591,109.656108\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"272\",\"24\",\"琼中黎族苗族自治县\",\"19.039768,109.861847\",\"0898\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"273\",\"25\",\"昆明市\",\"25.04915,102.714596\",\"0871\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"274\",\"25\",\"曲靖市\",\"25.52075,103.782538\",\"0874\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"275\",\"25\",\"玉溪市\",\"24.370443,102.545059\",\"0877\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"276\",\"25\",\"保山市\",\"25.120482,99.177994\",\"0875\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"277\",\"25\",\"昭通市\",\"27.340631,103.725019\",\"0870\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"278\",\"25\",\"丽江市\",\"26.87535,100.229623\",\"0888\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"279\",\"25\",\"思茅市\",\"22.739127,100.855247\",\"0879\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"280\",\"25\",\"临沧市\",\"23.887799,100.092604\",\"0883\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"281\",\"25\",\"文山壮族苗族自治州\",\"23.374082,104.246288\",\"0876\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"282\",\"25\",\"红河哈尼族彝族自治州\",\"23.367712,103.384058\",\"0873\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"283\",\"25\",\"西双版纳傣族自治州\",\"22.009424,100.803029\",\"0691\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"284\",\"25\",\"楚雄彝族自治州\",\"25.066351,101.529381\",\"0878\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"285\",\"25\",\"大理白族自治州\",\"25.596894,100.223667\",\"0872\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"286\",\"25\",\"德宏傣族景颇族自治州\",\"24.441232,98.589433\",\"0692\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"287\",\"25\",\"迪庆藏族自治州\",\"27.831022,99.713681\",\"0887\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"288\",\"26\",\"西宁市\",\"36.640737,101.767917\",\"0971\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"289\",\"26\",\"海东地区\",\"36.517608,102.085198\",\"0972\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"290\",\"26\",\"海北藏族自治州\",\"36.960653,100.87979\",\"0970\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"291\",\"26\",\"黄南藏族自治州\",\"35.522848,102.00759\",\"0973\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"292\",\"26\",\"海南藏族自治州\",\"36.28436,100.62405\",\"0974\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"293\",\"26\",\"果洛藏族自治州\",\"34.480481,100.22372\",\"0975\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"294\",\"26\",\"玉树藏族自治州\",\"33.006233,97.013312\",\"0976\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"295\",\"26\",\"海西蒙古族藏族自治州\",\"37.373794,97.342621\",\"0977\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"296\",\"27\",\"西安市\",\"34.277798,108.953094\",\"029\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"297\",\"27\",\"铜川市\",\"34.908362,108.96806\",\"0919\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"298\",\"27\",\"宝鸡市\",\"34.36408,107.170641\",\"0917\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"299\",\"27\",\"咸阳市\",\"34.345372,108.707507\",\"0910\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"300\",\"27\",\"渭南市\",\"34.502357,109.48393\",\"0913\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"301\",\"27\",\"延安市\",\"36.603313,109.500504\",\"0911\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"302\",\"27\",\"汉中市\",\"33.081562,107.045471\",\"0916\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"303\",\"27\",\"榆林市\",\"38.279432,109.745921\",\"0912\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"304\",\"27\",\"安康市\",\"32.704369,109.038038\",\"0915\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"305\",\"27\",\"商洛市\",\"33.873903,109.934206\",\"0914\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"306\",\"28\",\"南宁市\",\"22.806489,108.297232\",\"0771\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"307\",\"28\",\"柳州市\",\"24.329053,109.422396\",\"0772\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"308\",\"28\",\"桂林市\",\"25.262898,110.260919\",\"0773\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"309\",\"28\",\"梧州市\",\"23.485387,111.305469\",\"0774\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"310\",\"28\",\"北海市\",\"21.472712,109.122622\",\"0779\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"311\",\"28\",\"防城港市\",\"21.617398,108.351787\",\"0770\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"312\",\"28\",\"钦州市\",\"21.973348,108.638795\",\"0777\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"313\",\"28\",\"贵港市\",\"23.10337,109.613699\",\"0775\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"314\",\"28\",\"玉林市\",\"22.643965,110.151667\",\"0775\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"315\",\"28\",\"百色市\",\"23.901511,106.631819\",\"0776\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"316\",\"28\",\"贺州市\",\"24.411046,111.552593\",\"0774\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"317\",\"28\",\"河池市\",\"24.699517,108.069943\",\"0778\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"318\",\"28\",\"来宾市\",\"23.74116,109.231812\",\"0772\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"319\",\"28\",\"崇左市\",\"22.415451,107.357317\",\"0771\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"320\",\"29\",\"拉萨市\",\"29.662553,91.111884\",\"0891\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"321\",\"29\",\"那曲地区\",\"31.480674,92.067017\",\"0896\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"322\",\"29\",\"昌都地区\",\"31.140572,97.18558\",\"0895\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"323\",\"29\",\"山南地区\",\"29.229025,91.750643\",\"0893\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"324\",\"29\",\"日喀则地区\",\"29.269015,88.89148\",\"0892\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"325\",\"29\",\"阿里地区\",\"30.404555,81.107663\",\"0897\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"326\",\"29\",\"林芝地区\",\"29.666939,94.349979\",\"0894\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"327\",\"30\",\"银川市\",\"38.50262,106.206471\",\"0951\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"328\",\"30\",\"石嘴山市\",\"39.020222,106.379332\",\"0952\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"329\",\"30\",\"吴忠市\",\"37.993554,106.20825\",\"0953\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"330\",\"30\",\"固原市\",\"36.02152,106.285262\",\"0954\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"331\",\"30\",\"中卫市\",\"37.521117,105.196749\",\"0955\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"332\",\"31\",\"乌鲁木齐市\",\"43.840377,87.564979\",\"0991\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"333\",\"31\",\"克拉玛依市\",\"45.594327,84.881175\",\"0990\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"334\",\"31\",\"石河子市\",\"44.308253,86.041857\",\"0993\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"335\",\"31\",\"阿拉尔市\",\"40.615675,81.291734\",\"0997\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"336\",\"31\",\"图木舒克市\",\"39.889221,79.198153\",\"0998\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"337\",\"31\",\"五家渠市\",\"44.368896,87.565446\",\"0994\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"338\",\"31\",\"吐鲁番市\",\"42.678924,89.266019\",\"0995\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"339\",\"31\",\"阿克苏市\",\"40.349439,81.156009\",\"0997\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"340\",\"31\",\"喀什市\",\"39.513105,76.014342\",\"0998\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"341\",\"31\",\"哈密市\",\"42.344464,93.529368\",\"0902\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"342\",\"31\",\"和田市\",\"37.153344,79.915809\",\"0903\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"343\",\"31\",\"阿图什市\",\"40.131225,76.867149\",\"0908\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"344\",\"31\",\"库尔勒市\",\"41.705493,85.709412\",\"0996\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"345\",\"31\",\"昌吉市　\",\"44.175082,87.073615\",\"0994\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"346\",\"31\",\"阜康市\",\"44.4241,88.305946\",\"0994\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"347\",\"31\",\"米泉市\",\"43.959144,87.666856\",\"0994\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"348\",\"31\",\"博乐市\",\"44.844207,81.874277\",\"0909\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"349\",\"31\",\"伊宁市\",\"44.02035,81.289039\",\"0999\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"350\",\"31\",\"奎屯市\",\"44.559554,85.013926\",\"0992\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"351\",\"31\",\"塔城市\",\"46.811365,83.190123\",\"0901\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"352\",\"31\",\"乌苏市\",\"44.407682,84.277873\",\"0992\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"353\",\"31\",\"阿勒泰市\",\"47.890131,87.926206\",\"0906\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"354\",\"32\",\"呼和浩特市\",\"40.828318,111.660345\",\"0471\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"355\",\"32\",\"包头市\",\"40.647117,109.846235\",\"0472\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"356\",\"32\",\"乌海市\",\"39.683172,106.831997\",\"0473\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"357\",\"32\",\"赤峰市\",\"42.297111,118.930753\",\"0476\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"358\",\"32\",\"通辽市\",\"43.633755,122.260359\",\"0475\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"359\",\"32\",\"鄂尔多斯市\",\"39.816486,109.9937\",\"0477\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"360\",\"32\",\"呼伦贝尔市\",\"49.20163,119.760814\",\"0470\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"361\",\"32\",\"巴彦淖尔市\",\"40.769177,107.423801\",\"0478\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"362\",\"32\",\"乌兰察布市\",\"41.022358,113.112842\",\"0474\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"363\",\"32\",\"锡林郭勒盟\",\"43.939699,116.027331\",\"0479\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"364\",\"32\",\"兴安盟\",\"46.083757,122.048161\",\"0482\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"365\",\"32\",\"阿拉善盟\",\"38.843072,105.695677\",\"0483\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"366\",\"33\",\"澳门特别行政区\",\"22.204117,113.557512\",\"853\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"367\",\"34\",\"香港特别行政区\",\"22.293582,114.186119\",\"852\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"368\",\"7\",\"台北市\",\"25.061608,121.512968\",\"002\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"369\",\"7\",\"高雄市\",\"22.622803,120.303346\",\"007\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"370\",\"7\",\"基隆市\",\"25.118155,121.749833\",\"0032\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"371\",\"7\",\"台中市\",\"24.19761,120.593103\",\"0042\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"372\",\"7\",\"台南市\",\"23.019339,120.220558\",\"0062\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"373\",\"7\",\"新竹市\",\"24.820467,120.942652\",\"0035\")");
			db.execSQL("insert   into  " + Table.PTAS_CITY_TB + Columns + "     values(\"374\",\"7\",\"嘉义市\",\"23.486841,120.450524\",\"0052\")");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public static void insertGenery(SQLiteDatabase db) {

		String Columns = " (" + Table.param_type + "," + Table.param_type_desp + "," + Table.param_id + "," + Table.param_value + "," + Table.param_desp + ") ";
		db.beginTransaction();
		try {

			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"PT\", \"网别\", \"1\", \"GSM\", \"GSM 网路\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"PT\", \"网别\", \"2\", \"WCDMA\", \"WCDMA 网络\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"PT\", \"网别\", \"3\", \"CDMA\", \"CDMA 网络\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"PT\", \"网别\", \"4\", \"TDCDMA\", \"TDCDMA 网络\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"PT\", \"网别\", \"5\", \"CDMA2000\", \"CDMA2000 网络\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"PT\", \"网别\", \"6\", \"其它\", \"其它\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"OS\", \"操作系统类型\", \"1\", \"iPhone OS\", \"iPhone OS 操作系统\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"OS\", \"操作系统类型\", \"2\", \"Android OS\", \"Android 操作系统\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"OS\", \"操作系统类型\", \"3\", \"Windows Phone OS\", \"Windows Phone 操作系统\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"OS\", \"操作系统类型\", \"4\", \"Symbian OS\", \"Symbian 操作系统\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"OS\", \"操作系统类型\", \"5\", \"BlackBerry OS\", \"BlackBerry 操作系统\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"OS\", \"操作系统类型\", \"6\", \"Palm OS\", \"Palm 操作系统\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"OS\", \"操作系统类型\", \"7\", \"MeeGo OS\", \"MeeGo 操作系统\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"1\", \"apple\", \"苹果\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"2\", \"MOTOROLA\", \"摩托罗拉\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"3\", \"NOKIA\", \"诺基亚\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"4\", \"MICROSOFT\", \"微软\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"5\", \"BlackBerry\", \"黑莓\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"6\", \"PHILIPS\", \"飞利浦\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"7\", \"DELL\", \"戴尔\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"8\", \"HP\", \"惠普\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"9\", \"PALM\", \"PALM\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"10\", \"ALCATEL\", \"阿尔卡特\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"11\", \"GOOGLE\", \"谷歌\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"12\", \"VERTU\", \"奢侈品\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"13\", \"SAMSUNG\", \"三星\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"14\", \"SONY\", \"索尼\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"15\", \"LG\", \"LG\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"16\", \"SONY ERICSSON\", \"索爱\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"17\", \"SHARP\", \"夏普\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"18\", \"NEC\", \"NEC\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"19\", \"FUJITSU\", \"富士通\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"20\", \"TOSHIBA\", \"东芝\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"21\", \"KYOCERA\", \"京瓷\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"22\", \"PANTECH\", \"泛泰\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"23\", \"联想 LENOVO\", \"联想 LENOVO\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"24\", \"酷派 Coolpad\", \"酷派 Coolpad\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"25\", \"步步高 VIVO\", \"步步高 VIVO\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"26\", \"魅族 MEIZU\", \"魅族 MEIZU\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"27\", \"OPPO\", \"OPPO OPPO\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"28\", \"金立 GIONEE\", \"金立 GIONEE\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"29\", \"小米\", \"小米\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"30\", \"化为 HUAWEI\", \"化为 HUAWEI\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"31\", \"中兴 ZTE\", \"中兴 ZTE\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"32\", \"TCL 王牌\", \"TCL 王牌\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"33\", \"天语 K-TOUCH\", \"天语 K-TOUCH\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"34\", \"北斗\", \"北斗\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"35\", \"大可乐 DAKELE\", \"大可乐 DAKELE\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"36\", \"盛大\", \"盛大\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"37\", \"欧恩\", \"欧恩\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"38\", \"多普达\", \"多普达\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"39\", \"海尔 Haier\", \"海尔 Haier\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"40\", \"长虹 CHANGHONG\", \"长虹 CHANGHONG\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"41\", \"康佳 KONKA\", \"康佳 KONKA\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"42\", \"海信 HISENSE\", \"海信 HISENSE\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"43\", \"纽曼 Newsmy\", \"纽曼 Newsmy\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"44\", \"夏新 AMOI\", \"夏新 AMOI\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"45\", \"朵为 DOOV\", \"朵为 DOOV\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"46\", \"英华达 OKWAP\", \"英华达 OKWAP\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"47\", \"万利达\", \"万利达\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"48\", \"波导 DIRD\", \"波导 DIRD\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"49\", \"摩西 MOSES\", \"摩西 MOSES\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"50\", \"七喜 HEDY\", \"七喜 HEDY\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"51\", \"多家乐 DJL\", \"多家乐 DJL\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"52\", \"THL\", \"THL\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"53\", \"宏达 HTC\", \"宏达 HTC\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"54\", \"宏基 acer\", \"宏基 acer\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"55\", \"华硕 ASUS\", \"华硕 ASUS\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"56\", \"技嘉 GIGABYTE\", \"技嘉 GIGABYTE\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"57\", \"华晶 altek\", \"华晶 altek\")");
			db.execSQL("insert   into  " + Table.PTAS_GENERY_TB + Columns + "     values(\"BRAND\", \"品牌\", \"-1\", \"其它\", \"其它\")");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

}