package com.juannale.pearljamlyricsapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.juannale.pearljamlyricsapp.adapters.AlbumSongAdapter;
import com.juannale.pearljamlyricsapp.utils.AppUtils;
import com.juannale.pearljamlyricsapp.utils.XMLParser;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

public class SongsByAlbumActivity extends SherlockActivity {
	
	private String albumName;
	private String albumId;	
	private String albumInfo;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove the app title and show the home as up icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        FadingActionBarHelper helper = new FadingActionBarHelper()
            .actionBarBackground(R.drawable.ab_solid_app)
            .headerLayout(R.layout.songsbyalbum_header)
            .contentLayout(R.layout.songsbyalbum_layout);
        
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        //Get the intent parameters
        Bundle bundle = getIntent().getExtras();
        albumName = bundle.getString(AppUtils.KEY_ALBUM_NAME);
        albumId = bundle.getString(AppUtils.KEY_ALBUM_ID);
        
        TextView albumTitle = (TextView) findViewById(R.id.albumCoverTitle); // album name 
        
        AppUtils.setRobotoLightFont(this, albumTitle);
        
        // Setting values of list view
        albumTitle.setText(albumName);
        
        ImageView albumCover = (ImageView) findViewById(R.id.image_header);
        
        try {
            Class<com.juannale.pearljamlyricsapp.R.drawable> res = R.drawable.class;
            Field field = res.getField(albumId + "_cover");
            int drawableId = field.getInt(null);
            albumCover.setImageResource(drawableId);
        
        }
        catch (NoSuchFieldException e) {
        	//If the cover image is not found, set the generic one
		    albumCover.setImageResource(R.drawable.pearljam);
			Log.i("SongsByAlbumActivity", "Failure to get drawable id: " + albumId);
        } catch (IllegalArgumentException e) {
        	Log.e("SongsByAlbumActivity", "Failure", e);
		} catch (IllegalAccessException e) {
			Log.e("SongsByAlbumActivity", "Failure", e);
		}
        
        int resourceId = getResources().getIdentifier(albumId, "raw",
				"com.juannale.pearljamlyricsapp");
        
        final ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();

		XMLParser parser = new XMLParser();
		String xml = parser.readTextFile(getResources().openRawResource(
				resourceId)); // getting XML content
		Document doc = parser.getDomElement(xml); // getting DOM element

		NodeList nl = doc.getElementsByTagName(AppUtils.KEY_SONG);

		// looping through all song nodes <songs>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			// adding each child node to HashMap key => value
			map.put(AppUtils.KEY_SONG_ID, parser.getValue(e, AppUtils.KEY_SONG_ID));
			map.put(AppUtils.KEY_TRACK_NUMBER, parser.getValue(e, AppUtils.KEY_TRACK_NUMBER));
			map.put(AppUtils.KEY_SONG_TITLE, parser.getValue(e, AppUtils.KEY_SONG_TITLE));
			map.put(AppUtils.KEY_SONG_COMPOSER, parser.getValue(e, AppUtils.KEY_SONG_COMPOSER));
			map.put(AppUtils.KEY_SONG_LENGHT, parser.getValue(e, AppUtils.KEY_SONG_LENGHT));

			// adding HashList to ArrayList
			songList.add(map);
		}
        
        ListView listView = (ListView) findViewById(android.R.id.list);
        AlbumSongAdapter adapter = new AlbumSongAdapter(this, songList);
        listView.setAdapter(adapter);
        
     // Click event for single list row
        listView.setOnItemClickListener(new OnItemClickListener() {

     			@Override
     			public void onItemClick(AdapterView<?> parent, View view,
     					int position, long id) {

     				HashMap<String, String> albumMap = (HashMap<String, String>) songList
     						.get(position-1);

     				if(!albumMap.get(AppUtils.KEY_SONG_ID).equals("")){
     					Intent intent = new Intent(SongsByAlbumActivity.this,
	     						SongLyricsActivity.class);
	     				intent.putExtra(AppUtils.KEY_SONG_ID, albumMap.get(AppUtils.KEY_SONG_ID));
	     				startActivity(intent);
     				}
     			}
     		});
        
        //Check if the album info is available to show the icon and build the dialog
        int albumInfoResourceId = getResources().getIdentifier(albumId + "_info", "raw",
				"com.juannale.pearljamlyricsapp");
        
        albumInfo = loadAlbumInfo(albumInfoResourceId);
        
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.songs_by_album_menu, menu);
        
        if(albumInfo!= null){
        	//Hide the action icon if there is no info for the album
        	MenuItem item = (MenuItem) menu.findItem(R.id.action_album_info);
        	item.setVisible(true);
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
	            
			case R.id.action_album_info:
				showAlbumInfoDialog(albumInfo);
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
    
    /**
     * @return A list of Strings read from the specified resource
     */
    private String loadAlbumInfo(int rawResourceId) {
        try {
            InputStream inputStream = getResources().openRawResource(rawResourceId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //Read text from file
            StringBuilder text = new StringBuilder();
            
            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            
            reader.close();
            return text.toString();
        } catch (Exception e) {
            return null;
        }
    }

    
    /**
	 * Show the album info in an alert dialog
	 */
	protected void showAlbumInfoDialog(String albumInfo){
		ScrollView scroll = new ScrollView(this);
		TextView messageView = new TextView(this);
		messageView.setText(albumInfo);
		messageView.setPadding(10, 10, 10, 10);
		AppUtils.setRobotoLightFont(this, messageView);
		scroll.addView(messageView);
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(scroll);
		builder.setTitle(albumName + " " + getString(R.string.dialog_title_albumInfo)).setPositiveButton(R.string.dialog_close, 
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				dialog.dismiss();
			}
		}).show();
		
	}
	
}
