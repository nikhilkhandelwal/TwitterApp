package com.example.yamba;

import winterwell.jtwitter.Twitter.Status;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class StatusProvider extends ContentProvider {
	
	public static final String AUTHORITY = "content://com.example.yamba.provider";
	public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);
	public static final String DB_NAME="timeline.db";
	public static final String TABLE_NAME="status";
	public static final int DB_VERSION=1;
	public static final String C_ID= "_id";
	public static final String C_CREATED_AT="created_at";
	public static final String C_USER="user_name";
	public static final String C_TEXT="status_text";
	
	DBHelper dbHelper;
	SQLiteDatabase db;

	@Override
	public boolean onCreate() {

		dbHelper = new DBHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		
		if(uri.getLastPathSegment()==null)
		{
			return "vnd.android.cursor.item/vnd.example.yamba.status";
		}
		else
		{
			return "vnd.android.cursor.dir/vnd.example.yamba.status";
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		db = dbHelper.getWritableDatabase();
		long id= db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		if(id!=-1)
		{
		return Uri.withAppendedPath(uri	, Long.toString(id));
		}
		else
		{
			return uri;
		}
		
	}

	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		db = dbHelper.getReadableDatabase();
		Cursor cursor=db.query(StatusProvider.TABLE_NAME, projection, selection	, selectionArgs , null, null, sortOrder);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static ContentValues statusToValues(Status status)
	{
		ContentValues values = new ContentValues();
		values.put(C_ID, status.id);
		values.put(C_CREATED_AT, status.createdAt.getTime());
		values.put(C_TEXT, status.text);
		values.put(C_USER,status.user.name);
		return values;
	}

	class DBHelper extends SQLiteOpenHelper{

		static final String TAG= "DBHelper";
		public DBHelper(Context context) {
			super(context, StatusProvider.DB_NAME, null, StatusProvider.DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
			String sql= String.format("create table %s "+"(%s int primary key, %s int, %s text, %s text)", StatusProvider.TABLE_NAME, StatusProvider.C_ID,
					StatusProvider.C_CREATED_AT,StatusProvider.C_USER,StatusProvider.C_TEXT);
			Log.d(TAG,"On create in SQl");
			
			db.execSQL(sql);
			
			
			
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("drop if exists "+StatusProvider.TABLE_NAME);
			onCreate(db);
			}
		
	}
}


