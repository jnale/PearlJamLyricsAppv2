package com.juannale.pearljamlyricsapp.dao;

import java.util.ArrayList;
import java.util.List;

import com.juannale.pearljamlyricsapp.utils.AppUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PearlJamLyricsAppDAO extends SQLiteOpenHelper {

	private static int DATABASE_VERSION = 2;
	private static String DATABASE_NAME = "pearljamlyricsapp_db";
	private static String DATABASE_FAVORITES_TABLE = "FAVORITES";	
	
	private static String FAVORITE_TABLE_ID = "_id";
	private static String FAVORITE_TABLE_SONG_ID = "SONG_ID";
	private static String FAVORITE_TABLE_SONG_TITLE = "SONG_TITLE";
	
	static final String TAG = "DBAdapter";
	
	// Table creation scripts --------------------------------------------------------------------
	private static String FAVORITES_TABLE_CREATE = "create table " +
			DATABASE_FAVORITES_TABLE + " ("+ FAVORITE_TABLE_ID +
			" INTEGER PRIMARY KEY autoincrement," +
			FAVORITE_TABLE_SONG_ID + " TEXT, " +
			FAVORITE_TABLE_SONG_TITLE + " TEXT)";
	
	public PearlJamLyricsAppDAO(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			createTables(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		//Delete the old version of the table
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_FAVORITES_TABLE);
        createTables(db);
	}
		
	/**
	 * Creates the data model
	 * @param db
	 */
	private void createTables(SQLiteDatabase db) throws SQLException
	{
		db.execSQL(FAVORITES_TABLE_CREATE);
	}

    //---insert a favorite into the database---
    public long insertFavorite(String songId, String songTitle)
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	long result;
    	
        ContentValues initialValues = new ContentValues();
        initialValues.put(FAVORITE_TABLE_SONG_ID, songId);
        initialValues.put(FAVORITE_TABLE_SONG_TITLE, songTitle);
        result = db.insert(DATABASE_FAVORITES_TABLE, null, initialValues);
        db.close();
        
        return result;
    }

    //---retrieves all the favorites---
    public List<ContentValues> getAllFavorites()
    {
    	List<ContentValues> returnList = new ArrayList<ContentValues>();
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
        Cursor cursor = db.query(DATABASE_FAVORITES_TABLE, new String[] {FAVORITE_TABLE_SONG_ID,
                FAVORITE_TABLE_SONG_TITLE}, null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
			do {
				ContentValues base = new ContentValues();
				base.put(AppUtils.KEY_SONG_ID,cursor.getString(0));
				base.put(AppUtils.KEY_SONG_TITLE, cursor.getString(1));
				returnList.add(base);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
        
		
		
        return returnList;
    }
    
    //---retrieves a particular favorite---
    public boolean isFavorite(String songId)
    {
    	
    	boolean result = false;
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	try{
	        Cursor mCursor =
	                db.query(true, DATABASE_FAVORITES_TABLE, new String[] {FAVORITE_TABLE_ID, FAVORITE_TABLE_SONG_ID}, 
	                		FAVORITE_TABLE_SONG_ID + "='" + songId +"'", null,
	                null, null, null, null);
	        if (mCursor != null && mCursor.getCount() > 0) {
	            result = true;
	        }
    	} catch(SQLException e){
    		e.printStackTrace();
    	}
        db.close();
        
        return result;
    }
    
    //---deletes all favorites from table---
    public void deleteAllFavorites()
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
        db.execSQL("DELETE FROM " + DATABASE_FAVORITES_TABLE);
        db.close();
        
    }
    
    
  //---deletes a particular favorite---
    public boolean deleteFavorite(String songId)
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	boolean result = false;
    	
        result = db.delete(DATABASE_FAVORITES_TABLE, FAVORITE_TABLE_SONG_ID + "='" + songId +"'", null) > 0;
        db.close();
        
        return result;
    }
	
}