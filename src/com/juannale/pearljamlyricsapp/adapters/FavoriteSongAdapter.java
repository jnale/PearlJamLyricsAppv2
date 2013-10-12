package com.juannale.pearljamlyricsapp.adapters;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.juannale.pearljamlyricsapp.R;
import com.juannale.pearljamlyricsapp.dao.PearlJamLyricsAppDAO;
import com.juannale.pearljamlyricsapp.utils.AppUtils;

public class FavoriteSongAdapter extends BaseAdapter {

	Activity activity;
    List<ContentValues> data;
    private static LayoutInflater inflater=null;
    private PearlJamLyricsAppDAO dao;
    
    public FavoriteSongAdapter(Activity a, List<ContentValues> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        
    	View vi=convertView;
      
    	dao = new PearlJamLyricsAppDAO(activity);
    	
        if(convertView==null)
        	vi = inflater.inflate(R.layout.favorites_row, null);
        
        TextView songTitle = (TextView) vi.findViewById(R.id.songTitle); // song title
        songTitle.setText(data.get(position).getAsString(AppUtils.KEY_SONG_TITLE)); //set text
        //apply roboto font
        AppUtils.setRobotoLightFont(activity.getBaseContext(), songTitle);
        
        final ImageView deleteIcon = (ImageView) vi.findViewById(R.id.deleteFavIcon);
        
        deleteIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Remove the song from the database and the list and the refresh
				dao.deleteFavorite(data.get(position)
						.getAsString(AppUtils.KEY_SONG_ID));
				data.remove(position);
				
				Toast.makeText(activity, activity.getResources().getString(R.string.songRemovedFromFavs)
						, Toast.LENGTH_SHORT).show();
				
				notifyDataSetChanged();
			}
		});
        
        deleteIcon.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					
				case MotionEvent.ACTION_DOWN:
					deleteIcon.setBackgroundColor(activity.getResources().getColor(R.color.light_blue));
					break;
					
				case MotionEvent.ACTION_UP:
					deleteIcon.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
					break;
					
				case MotionEvent.ACTION_OUTSIDE:
					deleteIcon.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
					break;
				}
				return false;
			}
		});
        
        return vi;
	}
    
    

}
