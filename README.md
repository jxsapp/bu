# bu
Bu 一个Android开发的生产库，主要有以下几个部分构成：

 一、UI组件
 
  1.Application--BuApplication
      示例：
  2.Activity   --BuActivity
      示例：
  3.Fragment   --BuFragment
  
二、数据库组件

  1.DataBase   --BuInfoDB,BuAuthorityProvider
      示例：
      /**
 * 
 * the provider's stuff : BuAuthority.AUTHORITY.mzj
 * 
 * @Des org.bu.android.providers.mzj.*
 * 
 * @author jxs
 * @time 2014-9-15 下午11:17:20
 */
public abstract class ATProvider extends BuAuthorityProvider {

	@Override
	protected String getAuthority() {
		return "mzj_at";
	}

	@Override
	protected String getNullColumnHack() {
		return BuTable._ID;
	}

	@Override
	protected String getItemId() {
		return BuTable._ID;
	}

}
public class UserInfoProvider extends ATProvider {

	@Override
	protected String getTableName() {
		return UserInfoHolder.Table.TABLE_NAME;
	}

	@Override
	protected String getNullColumnHack() {
		return UserInfoHolder.Table._ID;
	}

	@Override
	protected String getItemId() {
		return UserInfoHolder.Table._ID;
	}

	@Override
	public SQLiteOpenHelper getSQLiteOpenHelper() {
		return new UserInfoDB(getContext());
	}

}

创建数据库 UserInfoHolder 方法creater(DB);
public synchronized void creater(SQLiteDatabase db) {
		creater(db, new Table() {

			@Override
			public String getTable() {
				return TABLE_NAME;
			}

			@Override
			public Map<String, String> colMaps() {

				Map<String, String> cols = new LinkedHashMap<String, String>();

				cols.put(USER_ID, "varchar(30)");
				cols.put(USER_NAME, "varchar(30)");
				cols.put(USER_PWD, "varchar(30)");
				cols.put(ACCESS_TOKEN, "varchar(30)");
				cols.put(REFRESH_TOKEN, "varchar(30)");
				cols.put(EXPIRES_IN, "varchar(30)");
				cols.put(CREATE_TIME, "varchar(30)");
				cols.put(UPDATE_TIME, "varchar(30)");
				return cols;
			}
		});
	}
	
	public class UserInfoDB extends BuInfoDB {

	public UserInfoDB(Context context) {
		super(context, new DB() {

			@Override
			public String dbName() {
				return "mzj_user_info_db";
			}

			@Override
			public int dbVersion() {
				return 1;
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				UserInfoHolder.getInstance().creater(db);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXISTS " + UserInfoHolder.Table.TABLE_NAME);
				onCreate(db);
			}

		});
	}

}


三、网络通信组件

    Http      --BuHttp
    
四、Widget 组件

  
