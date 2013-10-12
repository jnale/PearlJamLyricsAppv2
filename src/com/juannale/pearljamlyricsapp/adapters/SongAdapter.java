package com.juannale.pearljamlyricsapp.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juannale.pearljamlyricsapp.R;
import com.juannale.pearljamlyricsapp.dao.PearlJamLyricsAppDAO;
import com.juannale.pearljamlyricsapp.utils.AppUtils;

public class SongAdapter extends ArrayAdapter<HashMap<String, String>> {

	Context mContext;
    ArrayList<HashMap<String, String>> data;
    int layoutResourceId;
    Activity activity;
    
    private PearlJamLyricsAppDAO dao;
    
    public SongAdapter(Context context, int layoutResourceId, ArrayList<HashMap<String, String>> d) {
    	super(context,layoutResourceId,d);
    	mContext = context;
    	this.layoutResourceId = layoutResourceId;
        this.data=d;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        
    	View row=convertView;
		ViewHolder holder;
		activity = (Activity) mContext;
		
		if(row==null){
			LayoutInflater mInflate = ((Activity) mContext).getLayoutInflater();
			row = mInflate.inflate(layoutResourceId, null);
		
			holder = new ViewHolder();
			holder.songTitle = (TextView) row.findViewById(R.id.songTitle); // song title
	        holder.songAddToFavImgView = (ImageView) row.findViewById(R.id.addToFavIcon); //add to fav icon
	        row.setTag(holder);
	        
		} else {
	        holder = (ViewHolder) row.getTag();
	    }   
    	
        HashMap<String, String> songMap = new HashMap<String, String>();
        songMap = data.get(position);
        
        holder.songTitle.setText(songMap.get(AppUtils.KEY_SONG_TITLE)); //set text
        //apply roboto font
        AppUtils.setRobotoLightFont(mContext, holder.songTitle);
        
        dao = new PearlJamLyricsAppDAO(mContext);
		
		//If the song is already in the favorite's list, change the star image
		if (dao.isFavorite(songMap.get(AppUtils.KEY_SONG_ID)))
			holder.songAddToFavImgView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_sel_fav_small));
		else 
			holder.songAddToFavImgView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_add_fav_small));
		
		
		final int mPosition = position;
			
		holder.songAddToFavImgView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageView icon = (ImageView) v;
				//Check if the song is already a favorite
				if(!dao.isFavorite(data.get(mPosition).get(AppUtils.KEY_SONG_ID))){
					dao.insertFavorite(data.get(mPosition).get(AppUtils.KEY_SONG_ID), data.get(mPosition).get(AppUtils.KEY_SONG_TITLE));
					icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_sel_fav_small));
				} else {
					dao.deleteFavorite(data.get(mPosition).get(AppUtils.KEY_SONG_ID));
					icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_add_fav_small));
				}	
			}
		}); 
        
        return row;
	}
		
	private static class ViewHolder {
		public TextView songTitle;
        public ImageView songAddToFavImgView;
	}	

}
