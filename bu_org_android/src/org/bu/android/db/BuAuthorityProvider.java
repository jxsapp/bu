package org.bu.android.db;

import java.util.Locale;

import org.bu.android.misc.BuStringUtils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public abstract class BuAuthorityProvider extends ContentProvider implements BuAuthority {

	private String tableName = "";
	private String provider = "";
	private String authority = "";

	protected SQLiteOpenHelper dbHolder = null;
	private final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

	protected void initData() {
		this.tableName = getTableName();
		this.provider = getProvider();
		this.authority = getAuthority();
		URI_MATCHER.addURI(AUTHORITY + "." + authority + "." + provider, tableName + "/#", SINGLE);
		URI_MATCHER.addURI(AUTHORITY + "." + authority + "." + provider, tableName, ALL);
	}

	public Uri getContentURI() {
		return Uri.parse("content://" + AUTHORITY + "." + authority + "." + provider + "/" + tableName);
	}

	protected abstract String getAuthority();

	protected String getProvider() {
		String simpleName = this.getClass().getSimpleName();
		String frst = simpleName.substring(0, 1);
		return simpleName.replace(frst, frst.toLowerCase(Locale.getDefault()));
	}

	protected abstract String getTableName();

	protected abstract String getNullColumnHack();

	protected abstract String getItemId();

	public abstract SQLiteOpenHelper getSQLiteOpenHelper();

	@Override
	public boolean onCreate() {
		initData();
		dbHolder = getSQLiteOpenHelper();
		if (null != dbHolder) {
			return true;
		}
		return false;
	}

	@Override
	public String getType(Uri uri) {
		// 返回当前操作数据的mineType (images/type)

		String result = null;
		switch (URI_MATCHER.match(uri)) {
		case ALL:
			result = DIR + tableName;
			break;
		case SINGLE:
			result = DIR + ITEM;
			break;
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}

		return result;
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {
		if (URI_MATCHER.match(uri) != ALL) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		Uri inserUri = null;
		SQLiteDatabase db = dbHolder.getWritableDatabase();
		switch (URI_MATCHER.match(uri)) {
		case ALL:
			long rowId = db.insert(tableName, getNullColumnHack(), values);
			inserUri = ContentUris.withAppendedId(uri, rowId);
			this.getContext().getContentResolver().notifyChange(uri, null);
			break;
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
		return inserUri;
	}

	@Override
	public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHolder.getWritableDatabase();
		int count;
		switch (URI_MATCHER.match(uri)) {
		case ALL:
			count = db.delete(tableName, selection, selectionArgs);
			break;

		case SINGLE:
			String key = uri.getPathSegments().get(1);
			count = db.delete(tableName, getItemId() + "=" + key + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbHolder.getReadableDatabase();
		Cursor cursor = null;
		switch (URI_MATCHER.match(uri)) {
		case ALL:
			cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case SINGLE:
			long key = ContentUris.parseId(uri);
			String where = getItemId() + "= " + key;
			if (!BuStringUtils.isEmpety(selection)) {
				where = selection + " and " + where;
			}
			cursor = db.query(tableName, projection, where, selectionArgs, null, null, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unknow Uri:" + uri);
		}
		return cursor;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHolder.getWritableDatabase();
		int count;
		switch (URI_MATCHER.match(uri)) {
		case ALL:
			count = db.update(tableName, values, selection, selectionArgs);
			break;

		case SINGLE:
			String noteId = uri.getPathSegments().get(1);
			count = db.update(tableName, values, getItemId() + "=" + noteId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
