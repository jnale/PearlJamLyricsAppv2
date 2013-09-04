package com.juannale.pearljamlyricsapp.adapters;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.juannale.pearljamlyricsapp.R;
import com.juannale.pearljamlyricsapp.SongsByAlbumActivity;
import com.juannale.pearljamlyricsapp.utils.AppUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AlbumSongAdapter extends BaseAdapter {
	
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
	
	public AlbumSongAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
    }

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		 return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		
		if(convertView==null)
            vi = inflater.inflate(R.layout.albumsong_row, null);
		
		TextView songNumberView = (TextView) vi.findViewById(R.id.songNumber);
		TextView songTitleView = (TextView) vi.findViewById(R.id.songTitle);
		TextView songComposerView = (TextView) vi.findViewById(R.id.songComposer);
		
		ImageView songAddToFavView = (ImageView) vi.findViewById(R.id.addToFavIcon);
		
		HashMap<String, String> songMap = new HashMap<String, String>();
		songMap = data.get(position);
		
		songNumberView.setText(songMap.get(AppUtils.KEY_TRACK_NUMBER));
		songTitleView.setText(songMap.get(AppUtils.KEY_SONG_TITLE));
		if(!songMap.get(AppUtils.KEY_SONG_LENGHT).equals(""))
			songTitleView.setText(songTitleView.getText() + " (" + songMap.get(AppUtils.KEY_SONG_LENGHT) + ")");
		songComposerView.setText(songMap.get(AppUtils.KEY_SONG_COMPOSER));
		
		AppUtils.setRobotoLightFont(activity.getBaseContext(), songNumberView);
		AppUtils.setRobotoLightFont(activity.getBaseContext(), songTitleView);
		AppUtils.setRobotoLightFont(activity.getBaseContext(), songComposerView);
		
//		songAddToFavView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				ImageView icon = (ImageView) v;
//				icon.setImageResource(R.drawable.ic_add_fav);
//			}
//		}); 		
		return vi;
	}

}
