package com.juannale.pearljamlyricsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.juannale.pearljamlyricsapp.utils.AppUtils;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends SherlockFragmentActivity {
	
	String[] CONTENT; 
	
	ViewPagerFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_tabs);
		
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		CONTENT = getResources().getStringArray(R.array.album_tabs);
		
		mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());
		
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        
        mIndicator = (TabPageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        
	}
	
	class ViewPagerFragmentAdapter extends FragmentPagerAdapter {	    
	    private int mCount = CONTENT.length;

	    public ViewPagerFragmentAdapter(FragmentManager fm) {
	        super(fm);
	    }

	    @Override
	    public Fragment getItem(int position) {
	    	return TabFragment.newInstance(String.valueOf(position), CONTENT[position]); 
	    }

	    @Override
	    public int getCount() {
	        return mCount;
	    }
	    
	    @Override
	    public CharSequence getPageTitle(int position) {
	    	return CONTENT[position].toUpperCase();
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()){
		
			case R.id.action_list:	
				//Go to the song list activity
				Intent songListIntent = new Intent(this, 
	            		SongsListActivity.class);
				startActivity(songListIntent);
				return true;
			
			case R.id.action_favorites:	
				//Go to the favorites activity
				Intent favoritesIntent = new Intent(this, 
	            		FavoritesActivity.class);
				startActivity(favoritesIntent);
				return true;
		
			case R.id.action_settings:
				//Go to the settings activity
				Intent settingsIntent = new Intent(this, 
	            		PreferencesActivity.class);
				startActivity(settingsIntent);
				return true;     
				
			case R.id.action_send_feedback:	
				//Send feedback
				AppUtils.sendFeedback(this);
		        return true;
				
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	}

}
