package com.juannale.pearljamlyricsapp.adapters;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.juannale.pearljamlyricsapp.R;
import com.juannale.pearljamlyricsapp.dao.PearlJamLyricsAppDAO;
import com.juannale.pearljamlyricsapp.utils.AppUtils;

public class FavoriteSongAdapter extends BaseAdapter {

	private Activity activity;
    private List<ContentValues> data;
    private static LayoutInflater inflater=null;
    private String activityName;
    private PearlJamLyricsAppDAO dao;
    
    static final int ANIMATION_DURATION = 200;
    
    public FavoriteSongAdapter(Activity a, List<ContentValues> d) {
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

    public View getView(final int position, View convertView, ViewGroup parent) {
        
    	View vi=convertView;
    	ViewHolder vh;
      
    	dao = new PearlJamLyricsAppDAO(activity);
    	
        if (convertView==null) {
			vi = inflater.inflate(R.layout.favorites_row, parent, false);
			setViewHolder(vi);
		}
		else if (((ViewHolder)convertView.getTag()).needInflate) {
			vi = inflater.inflate(R.layout.favorites_row, parent, false);
			setViewHolder(vi);
		}
		else {
			vi = convertView;
		}
        
        vh = (ViewHolder)vi.getTag();
        vh.text.setText(data.get(position).getAsString(AppUtils.KEY_SONG_TITLE));//set text
        vh.imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteCell(v, position);
			}
		});
        
        return vi;
	}
    
    private void deleteCell(final View v, final int index) {
		AnimationListener al = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				//Remove the song from the database and the list and the refresh
				dao.deleteFavorite(data.get(index)
						.getAsString(AppUtils.KEY_SONG_ID));
				data.remove(index);
				
//				ViewHolder vh = (ViewHolder)v.getTag();
//				vh.needInflate = true;

				notifyDataSetChanged();
			}
			@Override public void onAnimationRepeat(Animation animation) {}
			@Override public void onAnimationStart(Animation animation) {}
		};

		collapse(v, al);
	}

	private void collapse(final View v, AnimationListener al) {
		final int initialHeight = v.getMeasuredHeight();

		Animation anim = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				}
				else {
					v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		if (al!=null) {
			anim.setAnimationListener(al);
		}
		anim.setDuration(ANIMATION_DURATION);
		v.startAnimation(anim);
	}
    
	private void setViewHolder(View view) {
		ViewHolder vh = new ViewHolder();
		vh.text = (TextView)view.findViewById(R.id.songTitle);
		vh.imageButton = (ImageView) view.findViewById(R.id.deleteFavIcon);
		//apply roboto font
        AppUtils.setRobotoLightFont(activity.getBaseContext(), vh.text);
		vh.needInflate = false;
		view.setTag(vh);
	}	
	
	private class ViewHolder {
		public boolean needInflate;
		public TextView text;
		ImageView imageButton;
	}
	
}
