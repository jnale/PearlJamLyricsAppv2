package com.juannale.pearljamlyricsapp.adapters;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
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
    
    private LruCache<Integer, Bitmap> mMemoryCache;
 
    public AlbumAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/8th of the available memory for this memory cache.
		final int cacheSize = maxMemory;
		mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(Integer key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
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
    	ViewHolder viewHolder;
        View vi=convertView;
        
        if(convertView==null)
            vi = inflater.inflate(R.layout.album_row, null);
       
        viewHolder = new ViewHolder();
        viewHolder.albumTitle = (TextView)vi.findViewById(R.id.albumTitle); // album name 
        viewHolder.yearAndLabel = (TextView)vi.findViewById(R.id.albumYearAndLabel); // year and label
        viewHolder.albumCover=(SquareImageView)vi.findViewById(R.id.albumCover); // thumb image
 
        AppUtils.setRobotoMediumFont(activity.getBaseContext(), viewHolder.albumTitle);
        AppUtils.setRobotoLightFont(activity.getBaseContext(), viewHolder.yearAndLabel);
        
        HashMap<String, String> album = new HashMap<String, String>();
        album = data.get(position);
 
        // Setting values of list view
        viewHolder.albumTitle.setText(album.get(AppUtils.KEY_ALBUM_NAME));
        viewHolder.yearAndLabel.setText( "(" + album.get(AppUtils.KEY_ALBUM_RELEASE_YEAR) + ") " + album.get(AppUtils.KEY_ALBUM_LABEL));
        
        try {
            Class<com.juannale.pearljamlyricsapp.R.drawable> res = R.drawable.class;
            Field field = res.getField(album.get(AppUtils.KEY_ALBUM_ID));
            int drawableId = field.getInt(null);
            setImageView(viewHolder, drawableId);
        
        }
        catch (NoSuchFieldException e) {
        	//If the cover image is not found, set the generic one
        	viewHolder.albumCover.setImageResource(R.drawable.pearljam);
			Log.i("AlbumAdapter", "Failure to get drawable id: " + album.get(AppUtils.KEY_ALBUM_ID));
        } catch (IllegalArgumentException e) {
        	Log.e("AlbumAdapter", "Failure", e);
		} catch (IllegalAccessException e) {
			Log.e("AlbumAdapter", "Failure", e);
		}
        
        return vi;
    }
    
    private void setImageView(ViewHolder viewHolder, int imageResId) {
		

		Bitmap bitmap = getBitmapFromMemCache(imageResId);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(activity.getBaseContext().getResources(), imageResId);
			addBitmapToMemoryCache(imageResId, bitmap);
		}
		viewHolder.albumCover.setImageBitmap(bitmap);
	}

	private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	private Bitmap getBitmapFromMemCache(int key) {
		return mMemoryCache.get(key);
	}
	
	private static class ViewHolder {
		TextView albumTitle;
		TextView yearAndLabel;
		SquareImageView albumCover;
	}
}