package com.juannale.pearljamlyricsapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.juannale.pearljamlyricsapp.adapters.SongAdapter;
import com.juannale.pearljamlyricsapp.utils.AppUtils;

public class SongsListActivity extends SherlockActivity {

	private ListView myListView;
	private ArrayList<String> elements;
	String[] songListArray;
	private SongAdapter adapter;
	
	private EditText ed;
	private ArrayList<String> arr_sort = new ArrayList<String>();
	private ArrayList<HashMap<String, String>> sortedSongList = new ArrayList<HashMap<String, String>>();
	int textlength = 0;
	boolean listWasFiltered = false;
	private String song2Fav;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.song_list_layout);
		
		//Remove the app title and show the home as up icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        final ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();

		// song list
		songListArray = getResources().getStringArray(R.array.song_list);

		elements = new ArrayList<String>();

		Collections.addAll(elements, songListArray);
		Collections.sort(elements); // sorting...

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
		myListView = (ListView) findViewById(R.id.list);
		myListView.setFastScrollEnabled(true);
		// Getting adapter by passing data ArrayList
		adapter = new SongAdapter(this, songList);
		myListView.setAdapter(adapter);
		
		// Click event for single list row
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String, String> songMap;
				// Check if the list was filtered in order to get the
				// filtered list or the regular one
				if (listWasFiltered) {
					songMap = (HashMap<String, String>) sortedSongList
							.get(position);
				} else {
					songMap = (HashMap<String, String>) songList.get(position);
				}

				Intent intent = new Intent(SongsListActivity.this,
						SongLyricsActivity.class);
				intent.putExtra(AppUtils.KEY_SONG_ID, songMap.get(AppUtils.KEY_SONG_ID));
				startActivity(intent);

			}
		});
		
		ed = (EditText) findViewById(R.id.filterSongsField);

		// to filter the list with the string of the editText
		ed.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				textlength = ed.getText().length();
				arr_sort.clear();

				// refresh elements to add the sorted list
				elements = new ArrayList<String>();

				Collections.addAll(elements, songListArray);
				Collections.sort(elements); // sorting...

				for (int i = 0; i < elements.size(); i++) {
					if (textlength <= elements.get(i).length()) {
						if (ed.getText()
								.toString()
								.equalsIgnoreCase(
										(String) elements.get(i).subSequence(0,
												textlength))) {
							arr_sort.add(elements.get(i));
						}
					}
				}

				sortedSongList.clear();
				sortedSongList = getSongsMapForAdapter(arr_sort);
				myListView.setAdapter(new SongAdapter(
						SongsListActivity.this, sortedSongList));
				listWasFiltered = true;

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}
	
	// Add the songs into a Map
		private ArrayList<HashMap<java.lang.String, java.lang.String>> getSongsMapForAdapter(
				ArrayList<String> songs) {

			ArrayList<HashMap<String, String>> returnList = new ArrayList<HashMap<String, String>>();

			// looping through all songs
			for (int i = 0; i < songs.size(); i++) {
				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				String song = (String) songs.get(i);
				String[] element = song.split(":", 2);

				// adding each child node to HashMap key => value
				map.put(AppUtils.KEY_SONG_ID, element[1]);
				map.put(AppUtils.KEY_SONG_TITLE, element[0]);

				// adding HashList to ArrayList
				returnList.add(map);
			}

			return returnList;
		}
}
