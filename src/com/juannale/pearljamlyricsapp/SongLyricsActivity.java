package com.juannale.pearljamlyricsapp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.juannale.pearljamlyricsapp.dao.PearlJamLyricsAppDAO;
import com.juannale.pearljamlyricsapp.utils.AppUtils;
import com.juannale.pearljamlyricsapp.utils.XMLParser;

public class SongLyricsActivity extends YouTubeFailureRecoveryActivity {

	private TextView songTitleTxtView;
	private TextView songComposerTxtView;
	private TextView songLyricsTxtView;
	private YouTubePlayerView youTubeView;
	private ImageView addToFavIconImgView;

	private String songId;
	private String songTitle;
	
	private String youTubeVideoCode="";
	
	SharedPreferences myPreferences;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_lyrics_layout);

		//Remove the app title and show the home as up icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        //Get App Preferences
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        //Activity transition
//        overridePendingTransition(R.anim.anim_in, 0);
		
		Bundle bundle = this.getIntent().getExtras();
		songId = bundle.getString(AppUtils.KEY_SONG_ID);

		int resourceId = getResources().getIdentifier(songId, "raw",
				"com.juannale.pearljamlyricsapp");

		songTitleTxtView = (TextView) this.findViewById(R.id.songTitle);
		songComposerTxtView = (TextView) this.findViewById(R.id.songComposer);
		songLyricsTxtView = (TextView) this.findViewById(R.id.songLyrics);
		
		AppUtils.setRobotoLightFont(this, songTitleTxtView);
		AppUtils.setRobotoThinFont(this, songComposerTxtView);
		AppUtils.setRobotoLightFont(this, songLyricsTxtView);
		
		//If there is a change in the font size, set the new size...
		if(myPreferences.getFloat(AppUtils.KEY_SONG_LYRICS_FONT_SIZE
				, getResources().getDimension(R.dimen.font_size_medium_size))!= getResources()
				.getDimension(R.dimen.font_size_medium_size))
			
			songLyricsTxtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, myPreferences.getFloat(AppUtils.KEY_SONG_LYRICS_FONT_SIZE
					, getResources().getDimensionPixelSize(R.dimen.font_size_medium_size)));
		
		try {
			XMLParser parser = new XMLParser();

			String xml = parser.readTextFile(getResources().openRawResource(
					resourceId)); // getting XML content
			Document doc = parser.getDomElement(xml); // getting DOM element
			NodeList nl = doc.getElementsByTagName(AppUtils.KEY_SONG);

			if (nl.getLength() == 1) {

				Element e = (Element) nl.item(0);

				// Set values to each textView
				songTitleTxtView.setText(parser.getValue(e, AppUtils.KEY_SONG_TITLE));
				if (parser.getValue(e, AppUtils.KEY_SONG_COMPOSER) != null)
					songComposerTxtView.setText("(" + parser.getValue(e, AppUtils.KEY_SONG_COMPOSER)
							+ ")");
				songLyricsTxtView.setText(parser.getValue(e, AppUtils.KEY_SONG_LYRICS));
				
				//Get the YouTube video Code
				youTubeVideoCode = parser.getValue(e, AppUtils.KEY_SONG_VIDEO);
				
				//Set the song title if it will be added as favorite
				songTitle = parser.getValue(e, AppUtils.KEY_SONG_TITLE);
				
			}
		} catch (NotFoundException e) {
			Log.e(this.getLocalClassName(), "the " + songId
					+ " XML file was not found");
			songLyricsTxtView.setText(R.string.lyricsError);
			addToFavIconImgView.setVisibility(ViewGroup.GONE);
		}
		
		//If the song has a video code... initialize the Player View
		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		
		if(!youTubeVideoCode.equals("")){
			youTubeView.initialize(AppUtils.YOUTUBE_API_KEY, this);
			
			//Check the preference to show or not the video
			if(myPreferences.getBoolean("pref_show_video", false)){
				youTubeView.setVisibility(View.VISIBLE);
			}
		}
		
		//Call again the onCreateOptionsMenu
		invalidateOptionsMenu();
		
		//set the add to favorites icon
		addToFavIconImgView = (ImageView) findViewById(R.id.songLyricsAddToFav);
		
		final PearlJamLyricsAppDAO dao = new PearlJamLyricsAppDAO(this);
		
		//if the song is in the favorites list, change the image 
		if (dao.isFavorite(songId)) 
			//set the image to full star
			addToFavIconImgView.setImageResource(R.drawable.ic_sel_fav);
		
		addToFavIconImgView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String toastMessage="";
				Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				
				// Search if the song was already saved in the favorites
				if (dao.isFavorite(songId)) {
					
					//removes the song from the favorites table
					if(dao.deleteFavorite(songId)){
						
						//set the image to empty star
						addToFavIconImgView.setImageResource(R.drawable.ic_add_fav);
						
						//vibrate and show the toast message
						vib.vibrate(200);
						
						toastMessage = getResources().getString(
								R.string.songRemovedFromFavs);
					} else{
						toastMessage = getResources().getString(
								R.string.errorRemovingSong);
					}
					
				} else {
					long insertResult;
					//Vibrates when the song is added to the favorites
					vib.vibrate(200);
					
					// Save the song in the favorites
					insertResult = dao.insertFavorite(songId, songTitle);
					
					if(insertResult!=-1){
						//set the image to full star
						addToFavIconImgView.setImageResource(R.drawable.ic_sel_fav);
						
						// set the toast message 
						toastMessage = getResources().getString(
								R.string.songAddedtoFavs);
					} else {
						// set the toast message 
						toastMessage = getResources().getString(
								R.string.errorAddingSong);
					}
				}
				// the song was added to the favorites
				Toast toast = Toast.makeText(getApplicationContext(), toastMessage,
						Toast.LENGTH_SHORT);
				toast.show();
				
			}
		});
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
        getSupportMenuInflater().inflate(R.menu.song_lyrics_menu, menu);
        
        if(youTubeVideoCode.equals("")){
        	menu.getItem(0).setVisible(false);
        } else {
        	 //Check the preference to change the icon and text of the item
    		if(myPreferences.getBoolean("pref_show_video", false)){
    			menu.findItem(R.id.action_video)
    				.setIcon(R.drawable.ic_action_youtube2)
    				.setTitle(getResources().getString(R.string.action_hideVideo));
    		}
        }
        
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
		
			case R.id.action_video:
				//Show and hide de video view of the song
				if(youTubeView.getVisibility()==View.GONE){
					youTubeView.setVisibility(View.VISIBLE);
					item.setIcon(R.drawable.ic_action_youtube2);
					item.setTitle(getResources().getString(R.string.action_hideVideo));
				}
				else{
					youTubeView.setVisibility(View.GONE);
					item.setIcon(R.drawable.ic_action_youtube);
					item.setTitle(getResources().getString(R.string.action_showVideo));
				}
				return true;     
				
			case R.id.action_favorites:	
				//Go to the favorites activity
				Intent favoritesIntent = new Intent(this, 
	            		FavoritesActivity.class);
				startActivity(favoritesIntent);
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

    
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
        boolean wasRestored) {
      if (!wasRestored) {
        player.cueVideo(youTubeVideoCode);
      }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
      return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
	
	/**
	 * To set the lyrics font size
	 */
	public boolean setFontSize(MenuItem item){
		
		float fontSize=14;
		int itemId = item.getItemId();
		SharedPreferences.Editor editor = myPreferences.edit();
		
		//Set the size depending on the menu item selected
		if(itemId == R.id.action_font_size_small)
			fontSize = getResources().getDimensionPixelSize(R.dimen.font_size_small_size);
		else if	(itemId == R.id.action_font_size_medium)	
			fontSize = getResources().getDimensionPixelSize(R.dimen.font_size_medium_size);
		else if	(itemId == R.id.action_font_size_large)
			fontSize = getResources().getDimensionPixelSize(R.dimen.font_size_large_size);
			
		//Set the text size in pixels
		songLyricsTxtView = (TextView) findViewById(R.id.songLyrics);
		songLyricsTxtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
		//Save the change in the preferences
		editor.putFloat(AppUtils.KEY_SONG_LYRICS_FONT_SIZE, fontSize);
		editor.commit();
		return true;
	}
   
}
