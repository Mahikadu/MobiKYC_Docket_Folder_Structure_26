package org.exto.dbConfig;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Dbcon {

	private Context context;
	private SQLiteDatabase database;
	private Dbhelper dbHelper;

	public Dbcon(Context context) {
		this.context = context;
	}

	public Dbcon open() throws SQLException {
		dbHelper = new Dbhelper(context);		
		try
		{				
		database = dbHelper.getWritableDatabase();	
		}
		catch (Exception e)
		{
			dbHelper.onCreate(database);
			
		}
		return this;
	}

	public void close() {
		dbHelper.close();
	}


	public long insert(String values[],String names[],String tbl) {
		ContentValues initialValues = createContentValues(values,names);
		
		return database.insert(tbl, null, initialValues);
	}


	public boolean update(String rowId, String values[],String names[],String tbl,String id) {
		ContentValues updateValues = createContentValues(values,names);
		
		
		return database.update(tbl, updateValues, id + "="
				+ rowId, null) > 0;
	}
	
	public boolean updateMulti(String rowId, String values[],String names[],String tbl,String id[]) {
		ContentValues updateValues = createContentValues(values,names);
	
		return database.update(tbl, updateValues, rowId, id) > 0;
	}
	
	public void rawQueryDefined(String query) {

		database.rawQuery(query, null);
	}


	public boolean delete(String tbl,String fName, String fValue) {

		return database.delete(tbl, fName + "=" + fValue, null) > 0;
	}
	public boolean delete(String tbl) {

		return database.delete(tbl, null, null) > 0;
	}
	public boolean deleteMulti(String tbl,String where, String fValue[]) {
		return database.delete(tbl, where, fValue) > 0;
	}

	
	public Cursor fetchallOrder(String tbl,String names[],String order) {
		return database.query(tbl,names , null, null, null,
				null, order);
	}
	
	public Cursor fetchallSpecify(String tbl,String names[],String fName, String fValue,String order) {
		
		Cursor mCursor = database.query(true, tbl, names,
				fName + "= '" + fValue +"'", null, null, null, order, null);
			
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
		}
		
	public Cursor fetchallSpecifyMSelect(String tbl,String names[],String select, String args[],String order) {
		
		Cursor mCursor = database.query(true, tbl, names,
				select , args, null, null, null, null);
	
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
		}

	
	public Cursor fetchone(String fValue,String tbl,String names[],String fName) throws SQLException {
		Cursor mCursor = database.query(true, tbl, names,
				fName + "='" + fValue+"'", null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	private ContentValues createContentValues(String values[],String names[]) {
		ContentValues values1 = new ContentValues();
		
		for(int i=0;i<values.length;i++)
		{
		values1.put(names[i], values[i]);		
		}
		
		return values1;
	}
	
	
	public String fetchVal()
	{
		String value = null;
		
		Cursor c = database.query("claim_auto", new String[]{"claimId"}, null, null, null, null, "claimId desc", "1");
		if(c!=null && c.getCount()>0)
		{
			c.moveToFirst();
			value = String.valueOf(c.getInt(0));
		}
		return value;
		
		
	}

}