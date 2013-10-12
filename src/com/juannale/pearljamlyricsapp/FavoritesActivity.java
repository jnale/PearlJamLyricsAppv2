package com.juannale.pearljamlyricsapp;

import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
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
import com.google.analytics.tracking.android.EasyTracker;
import com.juannale.pearljamlyricsapp.adapters.FavoriteSongAdapter;
import com.juannale.pearljamlyricsapp.dao.PearlJamLyricsAppDAO;
import com.juannale.pearljamlyricsapp.utils.AppUtils;

public class FavoritesActivity extends SherlockActivity {
	
	private TextView listTitle;
	private ListView favoriteListView;
	private FavoriteSongAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites_layout);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		// Get songs from the favorites table
		final List<ContentValues> favoriteList = new PearlJamLyricsAppDAO(this.getBaseContext()).getAllFavorites();

		// If there are favorites stored then...
		if (!favoriteList.isEmpty()) {

			// ListView
			favoriteListView = (ListView) findViewById(R.id.favsList);
			favoriteListView.setFastScrollEnabled(true);
			// Getting adapter by passing data ArrayList
			adapter = new FavoriteSongAdapter(this, favoriteList);
			favoriteListView.setAdapter(adapter);
			favoriteListView.setItemsCanFocus(true);
			
			TextView titleView = (TextView) findViewById(R.id.favsTitle);
			AppUtils.setRobotoLightFont(this, titleView);
			
			//Click event for single list row
			favoriteListView.setOnItemClickListener(new OnItemClickListener() {

	     			@Override
	     			public void onItemClick(AdapterView<?> parent, View view,
	     					int position, long id) {

	     				ContentValues albumMap = (ContentValues) favoriteList
	     						.get(position);

	     				if(!albumMap.get(AppUtils.KEY_SONG_ID).equals("")){
	     					Intent intent = new Intent(FavoritesActivity.this,
		     						SongLyricsActivity.class);
		     				intent.putExtra(AppUtils.KEY_SONG_ID, albumMap.getAsString(AppUtils.KEY_SONG_ID));
		     				startActivity(intent);
	     				}
	     			}
	     		});
			
		} else {
			// Change screen title when no favs were added already
			View lineView = (View) findViewById(R.id.colorLine);
			lineView.setVisibility(View.GONE);
			listTitle = (TextView) findViewById(R.id.favsTitle);
			listTitle.setBackgroundColor(getResources().getColor(android.R.color.white));
			listTitle.setText(R.string.howToAddFav);
			listTitle.setPadding(20, 20, 20, 0);
			listTitle.setTextSize(20);
			
			AppUtils.setRobotoLightFont(this, listTitle);
		}
		
	}
	
	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);  //Analytics start
	  }
	
	@Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);  //Analytics stop
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
		
			case R.id.action_grid:	
				//Go to the song list activity
				Intent albumViewIntent = new Intent(this, 
	            		MainActivity.class);
				startActivity(albumViewIntent);
				return true;    
	            
			case R.id.action_list:	
				//Go to the song list activity
				Intent songListIntent = new Intent(this, 
	            		SongsListActivity.class);
				startActivity(songListIntent);
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
	
}
