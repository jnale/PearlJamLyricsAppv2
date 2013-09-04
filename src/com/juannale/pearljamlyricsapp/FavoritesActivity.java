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
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
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
			
			TextView titleView = (TextView) findViewById(R.id.favsTitle);
			AppUtils.setRobotoLightFont(this, titleView);

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.favorites_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()){
			
			case android.R.id.home:
	            // This is called when the Home (Up) button is pressed
	            // in the Action Bar.
	            Intent parentActivityIntent = new Intent(this, 
	            		MainActivity.class);
	            parentActivityIntent.addFlags(
	                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                    Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(parentActivityIntent);
	            finish();
	            return true;
		
			case R.id.action_settings:
				//Go to the settings activity
				Intent settingsIntent = new Intent(this, 
	            		PreferencesActivity.class);
				startActivity(settingsIntent);
				return true;     
				
			case R.id.action_send_feedback:	
				//Send feedback
				AppUtils.sendFeedback(this);
		        return true;
				
		}
		
		return super.onOptionsItemSelected(item);
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
