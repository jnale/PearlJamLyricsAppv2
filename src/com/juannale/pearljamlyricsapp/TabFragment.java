package com.juannale.pearljamlyricsapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.juannale.pearljamlyricsapp.adapters.AlbumAdapter;
import com.juannale.pearljamlyricsapp.utils.AppUtils;
import com.juannale.pearljamlyricsapp.utils.XMLParser;

public class TabFragment extends SherlockFragment{

	private static final String KEY_TAB_NUM = "key.tab.num";
	private static final String KEY_TAB_NAME="key.tab.name";
	
	private String mTabNum = "???";
	private String mTabTitle = "???";
	
	public static TabFragment newInstance(String tabNum, String tabName) {
		TabFragment fragment = new TabFragment();
        
        fragment.mTabNum = tabNum;
        fragment.mTabTitle = tabName;
   
        return fragment;
    }

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
		
		XMLParser parser = new XMLParser();
		
		String resourceName = mTabTitle;
		
		//Check if the tab title is in ES-es
		if(resourceName.equals("estudio"))
			resourceName = "studio";
		else if(resourceName.equals("otros"))
			resourceName = "others";
		
		int resourceId = getResources().getIdentifier(resourceName, "raw",
				"com.juannale.pearljamlyricsappv2");

		String xml = parser.readTextFile(getResources().openRawResource(
				resourceId)); // getting XML content
		
		Document doc = parser.getDomElement(xml); // getting DOM element

		NodeList nl = doc.getElementsByTagName(AppUtils.KEY_ALBUM);
		// looping through all album nodes <album>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			// adding each child node to HashMap key => value
			map.put(AppUtils.KEY_ALBUM_ID, parser.getValue(e, AppUtils.KEY_ALBUM_ID));
			map.put(AppUtils.KEY_ALBUM_NAME, parser.getValue(e, AppUtils.KEY_ALBUM_NAME));
			map.put(AppUtils.KEY_ALBUM_LABEL, parser.getValue(e, AppUtils.KEY_ALBUM_LABEL));
			map.put(AppUtils.KEY_ALBUM_RELEASE_YEAR, parser.getValue(e, AppUtils.KEY_ALBUM_RELEASE_YEAR));

			// adding HashList to ArrayList
			albumList.add(map);
		}
		
		View view = inflater.inflate(R.layout.mainactivity_layout, null);
       
		GridView gridview = (GridView) view.findViewById(R.id.gridview);
	    gridview.setAdapter(new AlbumAdapter(getActivity(), albumList));
       
	    gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String, String> albumMap = (HashMap<String, String>) albumList
						.get(position);

				Intent intent = new Intent(getActivity().getBaseContext(),
						SongsByAlbumActivity.class);
				intent.putExtra(AppUtils.KEY_ALBUM_ID, albumMap.get(AppUtils.KEY_ALBUM_ID));
				intent.putExtra(AppUtils.KEY_ALBUM_NAME, albumMap.get(AppUtils.KEY_ALBUM_NAME));
				intent.putExtra(AppUtils.KEY_ALBUM_RELEASE_YEAR, albumMap.get(AppUtils.KEY_ALBUM_RELEASE_YEAR));
				intent.putExtra(AppUtils.KEY_ALBUM_LABEL, albumMap.get(AppUtils.KEY_ALBUM_LABEL));
				startActivity(intent);
			}
		});
	    
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_TAB_NUM)) {
        	mTabNum =  savedInstanceState.getString(KEY_TAB_NUM) != null ? savedInstanceState.getString(KEY_TAB_NUM) : "???";
        	mTabTitle =  savedInstanceState.getString(KEY_TAB_NAME) != null ? savedInstanceState.getString(KEY_TAB_NAME) : "???";
        }
		
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TAB_NUM, mTabNum);
        outState.putString(KEY_TAB_NAME, mTabTitle);
    }
	
}
