package com.word.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WordContents extends SQLiteOpenHelper{
	private String db_name;
	private static final int version = 1;
	private static final String default_name = "words";
	private static final String ID = "_id";
	private static final String WORD = "word";
	private static final String PHONETIC = "phonetic";
	private static final String DETAIL = "detail";
	private static final String ISDELETED = "is_deleted";
	private static final String ISSTAR = "is_star";
	private static final String TABLE = "notes";
	

	public WordContents(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.db_name = name;
	}
	
	public WordContents(Context context, String name){
		this(context, name, null, version);
	}
	
	public WordContents(Context context){
		this(context, default_name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + 
				"(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				WORD + " TEXT UNIQUE," + PHONETIC + " TEXT," + DETAIL + " TEXT," + ISDELETED +" INTEGER," +
				ISSTAR + " INTEGER )");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("test", "Nothing to do.");
		
	}
	
	public String getDBName(){
		return this.db_name;
	}

}
