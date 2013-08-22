package com.juannale.pearljamlyricsapp.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.juannale.pearljamlyricsapp.R;
import com.juannale.pearljamlyricsapp.utils.AppUtils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FavoriteSongAdapter extends BaseAdapter {

	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private String activityName;
    
    public FavoriteSongAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        activityName = activity.getClass().getName();       
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        
    	View vi=convertView;
        
        if(convertView==null)
        	vi = inflater.inflate(R.layout.song_list_row, null);
        
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        
        TextView songTitle = (TextView) vi.findViewById(R.id.songTitle); // song title
        songTitle.setText(song.get(AppUtils.KEY_SONG_TITLE)); //set text
        //apply roboto font
        AppUtils.setRobotoLightFont(activity.getBaseContext(), songTitle);
        
        return vi;
	}
    
    

}