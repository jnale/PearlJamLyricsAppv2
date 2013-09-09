package com.juannale.pearljamlyricsapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PearlJamLyricsAppDAO extends SQLiteOpenHelper {
	
	// Table creation scripts --------------------------------------------------------------------
	String createFavorites	= "create table FAVORITES (ID INTEGER, SONG_ID TEXT, SONG_NAME TEXT)";

	public PearlJamLyricsAppDAO(Context context, String name,
			CursorFactory factory, int version) {
		
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		createTables(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		//Delete the old version of the table
        db.execSQL("DROP TABLE IF EXISTS CIUDADES");
        createTables(db);
	}
	
	/**
	 * Creates the data model
	 * @param db
	 */
	private void createTables(SQLiteDatabase db)
	{
		db.execSQL(createFavorites);
	}

}
