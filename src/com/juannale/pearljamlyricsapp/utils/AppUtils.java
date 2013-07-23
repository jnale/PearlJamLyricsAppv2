package com.juannale.pearljamlyricsapp.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class AppUtils {

	private static Typeface robotoRegularTypeFace;
	private static Typeface robotoMediumTypeFace;
	private static Typeface robotoLightTypeFace;
	private static Typeface robotoThinTypeFace;
	
	// XML node keys for albums
 	public static final String KEY_ALBUM = "album"; // parent node
 	public static final String KEY_ALBUM_ID = "id";
 	public static final String KEY_ALBUM_NAME = "name";
 	public static final String KEY_ALBUM_LABEL= "label";
 	public static final String KEY_ALBUM_RELEASE_YEAR = "releaseYear";
 	
 	// XML node keys for songs
 	public static final String KEY_SONG = "song"; // parent node
 	public static final String KEY_SONG_ID = "id";
 	public static final String KEY_TRACK_NUMBER = "trackNumber";
 	public static final String KEY_SONG_TITLE = "title";
 	public static final String KEY_SONG_LENGHT = "lenght";
 	public static final String KEY_SONG_COMPOSER = "composer";
 	
 	public static final String KEY_SONG_VIDEO = "video";
 	public static final String KEY_SONG_LYRICS = "lyrics";
 	
 	//Key for the YoutTube API
 	public static final String YOUTUBE_API_KEY = "AIzaSyB75D6vuIzBqWNBGIEdO_OV6FPKP7iwjBk";

	//Set Roboto Regular Font
    public static void setRobotoRegularFont (Context context, View view)
    {
        if (robotoRegularTypeFace == null)
        {
        	robotoRegularTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        }
        setFont(view, robotoRegularTypeFace);
    }
    
    //Set Roboto Medium Font
    public static void setRobotoMediumFont (Context context, View view)
    {
        if (robotoMediumTypeFace == null)
        {
        	robotoMediumTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        }
        setFont(view, robotoMediumTypeFace);
    }

    //Set Roboto Light Font
    public static void setRobotoLightFont (Context context, View view)
    {
        if (robotoThinTypeFace == null)
        {
        	robotoThinTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        }
        setFont(view, robotoThinTypeFace);
    }
    
    //Set Roboto Thin Font
    public static void setRobotoThinFont (Context context, View view)
    {
        if (robotoLightTypeFace == null)
        {
        	robotoLightTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
        }
        setFont(view, robotoLightTypeFace);
    }
    
    private static void setFont (View view, Typeface robotoTypeFace)
    {
        if (view instanceof ViewGroup)
        {
            for (int i = 0; i < ((ViewGroup)view).getChildCount(); i++)
            {
                setFont(((ViewGroup)view).getChildAt(i), robotoTypeFace);
            }
        }
        else if (view instanceof TextView)
        {
            ((TextView) view).setTypeface(robotoTypeFace);
        }
    }
    
    
     
    // To animate view slide out from bottom to top
    public void slideToTop(View view){
    TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
    animate.setDuration(500);
    animate.setFillAfter(true);
    view.startAnimation(animate);
    view.setVisibility(View.GONE);
    }
	
}
