package com.juannale.pearljamlyricsapp.adapters;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juannale.pearljamlyricsapp.R;
import com.juannale.pearljamlyricsapp.utils.AppUtils;
import com.juannale.pearljamlyricsapp.widget.SquareImageView;
 
/**
 * @author juan.nale
 * Adapter for the Album type
 */
public class AlbumAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    String XmlId;
 
    public AlbumAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
 
    /**
     * Gets the view to inflate with the data
     * 
     * @param position int
     * @param convertView view
     * @param parent viewGroup
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        
        if(convertView==null)
            vi = inflater.inflate(R.layout.album_row, null);
       
        TextView albumTitle = (TextView)vi.findViewById(R.id.albumTitle); // album name 
        TextView yearAndLabel = (TextView)vi.findViewById(R.id.albumYearAndLabel); // year and label
        SquareImageView albumCover=(SquareImageView)vi.findViewById(R.id.albumCover); // thumb image
 
        AppUtils.setRobotoMediumFont(activity.getBaseContext(), albumTitle);
        AppUtils.setRobotoLightFont(activity.getBaseContext(), yearAndLabel);
        
        HashMap<String, String> album = new HashMap<String, String>();
        album = data.get(position);
 
        // Setting values of list view
        albumTitle.setText(album.get(AppUtils.KEY_ALBUM_NAME));
        yearAndLabel.setText( "(" + album.get(AppUtils.KEY_ALBUM_RELEASE_YEAR) + ") " + album.get(AppUtils.KEY_ALBUM_LABEL));
        
        try {
            Class<com.juannale.pearljamlyricsappv2.R.drawable> res = R.drawable.class;
            Field field = res.getField(album.get(AppUtils.KEY_ALBUM_ID));
            int drawableId = field.getInt(null);
            albumCover.setImageResource(drawableId);
        
        }
        catch (NoSuchFieldException e) {
        	//If the cover image is not found, set the generic one
		    albumCover.setImageResource(R.drawable.pearljam);
			Log.i("AlbumAdapter", "Failure to get drawable id: " + album.get(AppUtils.KEY_ALBUM_ID));
        } catch (IllegalArgumentException e) {
        	Log.e("AlbumAdapter", "Failure", e);
		} catch (IllegalAccessException e) {
			Log.e("AlbumAdapter", "Failure", e);
		}
        
        return vi;
    }
}