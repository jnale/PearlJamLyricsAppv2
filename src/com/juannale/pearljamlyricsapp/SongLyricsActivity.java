package com.juannale.pearljamlyricsapp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.juannale.pearljamlyricsapp.utils.AppUtils;
import com.juannale.pearljamlyricsapp.utils.XMLParser;

public class SongLyricsActivity extends YouTubeFailureRecoveryActivity {

	TextView songTitle;
	TextView songComposer;
	TextView songLyrics;
	YouTubePlayerView youTubeView;
	ImageView addToFavIcon;

	private String youTubeVideoCode="";
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_lyrics_layout);

		//Remove the app title and show the home as up icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle bundle = this.getIntent().getExtras();
		String songId = bundle.getString(AppUtils.KEY_SONG_ID);

		int resourceId = getResources().getIdentifier(songId, "raw",
				"com.juannale.pearljamlyricsapp");

		songTitle = (TextView) this.findViewById(R.id.songTitle);
		songComposer = (TextView) this.findViewById(R.id.songComposer);
		songLyrics = (TextView) this.findViewById(R.id.songLyrics);
		
		AppUtils.setRobotoLightFont(this, songTitle);
		AppUtils.setRobotoThinFont(this, songComposer);
		AppUtils.setRobotoLightFont(this, songLyrics);
		
		try {
			XMLParser parser = new XMLParser();

			String xml = parser.readTextFile(getResources().openRawResource(
					resourceId)); // getting XML content
			Document doc = parser.getDomElement(xml); // getting DOM element
			NodeList nl = doc.getElementsByTagName(AppUtils.KEY_SONG);

			if (nl.getLength() == 1) {

				Element e = (Element) nl.item(0);

				// Set values to each textView
				songTitle.setText(parser.getValue(e, AppUtils.KEY_SONG_TITLE).toUpperCase());
				if (parser.getValue(e, AppUtils.KEY_SONG_COMPOSER) != null)
					songComposer.setText("(" + parser.getValue(e, AppUtils.KEY_SONG_COMPOSER)
							+ ")");
				songLyrics.setText(parser.getValue(e, AppUtils.KEY_SONG_LYRICS));
				
				youTubeVideoCode = parser.getValue(e, AppUtils.KEY_SONG_VIDEO);
				
			}
		} catch (NotFoundException e) {
			Log.e(this.getLocalClassName(), "the " + songId
					+ " XML file was not found");
			songLyrics.setText(R.string.lyricsError);
			addToFavIcon.setVisibility(ViewGroup.GONE);
		}
		
		//If the song has a video code... initialize the Player View
		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		
		if(!youTubeVideoCode.equals("")){
			youTubeView.initialize(AppUtils.YOUTUBE_API_KEY, this);
		}
		
		//Call again the onCreateOptionsMenu
		invalidateOptionsMenu();
		
		//set the add to favorites icon
		addToFavIcon = (ImageView) findViewById(R.id.songLyricsAddToFav);
		addToFavIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToFavIcon.setImageResource(R.drawable.ic_sel_fav);
				
			}
		});
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.song_lyrics_menu, menu);
        
        if(youTubeVideoCode.equals("")){
        	menu.getItem(0).setVisible(false);
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
					item.setTitle("Hide Video");
				}
				else{
					youTubeView.setVisibility(View.GONE);
					item.setIcon(R.drawable.ic_action_youtube);
				}
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
    
    //To animate view slide out from top to bottom
//    public void slideToBottom(View view){
//    	
//	    TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
//	    animate.setDuration(500);
//	    animate.setFillAfter(true);
//	    view.startAnimation(animate);
//	    view.setVisibility(View.VISIBLE);
//	    
//    }
   
}
