package org.exto.dbConfig;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbhelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "sudesi";

	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_CREATE_SCAN = "create table scan (scannedId integer primary key autoincrement,_scanId text,resolution text,quality text,savedServer integer);";
	private static final String TABLE_CREATE_IMAGE = "create table image (imageId integer primary key autoincrement,scannedId integer,imagePath text not null,savedServer integer,saveTime text,docType text);";
	private static final String TABLE_CLAIM_AUTO="create table claim_auto(claimId integer);";
	
	public Dbhelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(TABLE_CREATE_SCAN);
			database.execSQL(TABLE_CREATE_IMAGE);
			database.execSQL(TABLE_CLAIM_AUTO);
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion,
				int newVersion) {
			database.execSQL("DROP TABLE IF EXISTS scan");
			database.execSQL("DROP TABLE IF EXISTS image");
			database.execSQL("DROP TABLE IF EXISTS claim_auto");
			onCreate(database);
		}
		
		public void dropandcreate(SQLiteDatabase database)
		{
			database.execSQL("DROP TABLE IF EXISTS scan");
			database.execSQL("DROP TABLE IF EXISTS image");
			database.execSQL("DROP TABLE IF EXISTS claim_auto");
			onCreate(database);
		}
}