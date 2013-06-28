package com.hts.SharePic;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database 
{	
	public static final String MYDATABASENAME="Myfbpostdb.db";
	public static final String MYDATABASETABLE="Myfbposttb_table1";
	public static final int MYDATABASEVERSION=1;
	public static final String KEY_id="_id";
	public static final String KEY_date="date";
	public static final String KEY_comment="comment";
	public static final String KEY_image="image";
	private static final String SCRIPT_CREATE_DATABASE="create table if not exists "+MYDATABASETABLE+" ("+KEY_id+" integer primary key autoincrement, "+KEY_date+" text not null, "+KEY_comment+ " text not null, "+KEY_image+ " text not null);";
	private datahelper sqliteopenhelper;
	public static String userselectedlang;
	private SQLiteDatabase sqlitedatabase;
	private Context context;
	public Database(Context c)
	{
		context=c;	
	}
	public Database openToread()throws android.database.SQLException
	{
		sqliteopenhelper=new datahelper(context,MYDATABASENAME,null,MYDATABASEVERSION);
		sqlitedatabase=sqliteopenhelper.getReadableDatabase();
		return this;
	}
	public Database openTowrite()throws android.database.SQLException
	{
		sqliteopenhelper=new datahelper(context,MYDATABASENAME,null,MYDATABASEVERSION);
		sqlitedatabase=sqliteopenhelper.getWritableDatabase();
		return this;
	}
	public void close()
	{
		sqliteopenhelper.close();
	}	
	public Cursor retriveall()
	{
		Log.i(SharePicMain.TAG,"Database : retriveall()");
		String[] columns={KEY_id,KEY_date,KEY_comment,KEY_image};
		Cursor cursor=sqlitedatabase.query(MYDATABASETABLE, columns,null,null,null,null,null);
		return cursor;
	}	
	/*	public Cursor retrivelanguages()
	{
		String[] columns={KEY_language};
		Cursor c=sqlitedatabase.query(MYDATABASETABLE, columns,null,null,null,null,null);
		return c;
	}*/
	/*public Cursor retrievealllanguagedescriptions()
	{
		String[] columns={KEY_description};
		Cursor c_alldescription=sqlitedatabase.query(MYDATABASETABLE, columns, null,null,null,null,null);
		return c_alldescription;
	}*/
	public Cursor retrieveselectedlanguagedecription()
	{
		/*userselectedlang=listlang.selectedlanguage;*/
		String query="select languagedescription from Mylanguagedb_table1 where languages ='"+userselectedlang+"';";
		Cursor c1=sqlitedatabase.rawQuery(query,null);
		return c1;
	}
	//retrieve selected language description from service class
	public Cursor retrieveselectedlanguagedescriptionservice(String servicelanguage)
	{
		String query="select languagedescription from Mylanguagedb_table1 where languages ='"+servicelanguage+"';";
		Cursor c_service=sqlitedatabase.rawQuery(query,null);
		return c_service;
	}
	public long insert(String date,String comment,String image)
	{
		Log.i(SharePicMain.TAG,"Database : Insert Values");
		ContentValues contentvalues=new ContentValues();
		contentvalues.put(KEY_date,date);
		contentvalues.put(KEY_comment,comment);
		contentvalues.put(KEY_image,image);
		return sqlitedatabase.insert(MYDATABASETABLE, null,contentvalues);
	}
	public int deleteall()
	{
		return sqlitedatabase.delete(MYDATABASETABLE,null,null);		
	}
	public int deleterow()
	{
		/*String selectedrow=listlang.selectedlanguage;*/
		String selectedrow="";
		return sqlitedatabase.delete(MYDATABASETABLE,selectedrow, null);
	}
	private static class datahelper extends SQLiteOpenHelper
	{
		public datahelper(Context context, String name, CursorFactory factory,int version) 
		{
			super(context, name, factory, version);
		}
		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			db.execSQL(SCRIPT_CREATE_DATABASE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			//db.update(table, values, whereClause, whereArgs)
		}

	}	
}