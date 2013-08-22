package com.juannale.pearljamlyricsapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.juannale.pearljamlyricsapp.adapters.FavoriteSongAdapter;
import com.juannale.pearljamlyricsapp.utils.AppUtils;
import android.widget.AdapterView.OnItemClickListener;

public class FavoritesActivity extends SherlockActivity {
	
	private TextView listTitle;
	private ListView myListView;
	private ArrayList<String> elements;
	private FavoriteSongAdapter adapter;
	private int fav2Remove;

	final ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get songs from the favorites
		SharedPreferences appPrefs = getSharedPreferences(
				"com.juannale.pearljamlyricsapp_preferences", MODE_PRIVATE);
		String favs = appPrefs.getString("favorites", "");

		// If there are favorites stored then...
		if (!favs.equals("")) {

			String[] favsArray = favs.split("#");
			elements = new ArrayList<String>();
			Collections.addAll(elements, favsArray);

			// looping through all songs
			for (int i = 0; i < elements.size(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				String song = (String) elements.get(i);
				String[] element = song.split(":", 2);

				// adding each child node to HashMap key => value
				map.put(AppUtils.KEY_SONG_ID, element[1]);
				map.put(AppUtils.KEY_SONG_TITLE, element[0]);

				// adding HashList to ArrayList
				songList.add(map);
			}
			
			// ListView
			myListView = (ListView) findViewById(R.id.favsList);
			myListView.setFastScrollEnabled(true);
			// Getting adapter by passing data ArrayList
			adapter = new FavoriteSongAdapter(this, songList);
			myListView.setAdapter(adapter);

			// Click event for single list row
			myListView.setOnItemClickListener(new OnItemClickListener() {
			
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					HashMap<String, String> songMap = (HashMap<String, String>) songList
							.get(position);

					Intent intent = new Intent(FavoritesActivity.this,
							SongLyricsActivity.class);
					intent.putExtra("songId", songMap.get(AppUtils.KEY_SONG_ID));
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
