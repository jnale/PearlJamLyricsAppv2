package com.juannale.pearljamlyricsapp;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.juannale.pearljamlyricsapp.adapters.FavoriteSongAdapter;
import com.juannale.pearljamlyricsapp.utils.AppUtils;

public class FavoritesActivity extends SherlockActivity {
	
	private TextView listTitle;
	private ListView myListView;
	private FavoriteSongAdapter adapter;
	private int fav2Remove;

	ArrayList<HashMap<String, String>> favsList = new ArrayList<HashMap<String, String>>();


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		// Get songs from the favorites
		SharedPreferences appPrefs = getSharedPreferences(
				"com.juannale.pearljamlyricsapp_preferences", MODE_PRIVATE);
		String favs = appPrefs.getString("favorites", "");

		// If there are favorites stored then...
		if (!favs.equals("")) {

			favsList = AppUtils.getFavList(favs);
			
			// ListView
			myListView = (ListView) findViewById(R.id.favsList);
			myListView.setFastScrollEnabled(true);
			// Getting adapter by passing data ArrayList
			adapter = new FavoriteSongAdapter(this, favsList);
			myListView.setAdapter(adapter);

			// Click event for single list row
			myListView.setOnItemClickListener(new OnItemClickListener() {
			
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					HashMap<String, String> songMap = (HashMap<String, String>) favsList
							.get(position);

					Intent intent = new Intent(FavoritesActivity.this,
							SongLyricsActivity.class);
					intent.putExtra(AppUtils.KEY_SONG_ID, songMap.get(AppUtils.KEY_SONG_ID));
					startActivity(intent);

				}

			});
			
		} else {
			// Change screen title when no favs were added already
			listTitle = (TextView) findViewById(R.id.favsTitle);
			listTitle.setText(R.string.howToAddFav);
		}
	}
	
	/**
	 * fav2Remove getter
	 * 
	 * @return String
	 */
	public int getFav2Remove() {
		return fav2Remove;
	}

	/**
	 * fav2Remove setter
	 * 
	 * @param fav2Remove
	 */
	public void setFav2Remove(int fav2Remove) {
		this.fav2Remove = fav2Remove;
	}
	
}
