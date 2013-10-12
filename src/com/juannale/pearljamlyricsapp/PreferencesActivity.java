package com.juannale.pearljamlyricsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.juannale.pearljamlyricsapp.dao.PearlJamLyricsAppDAO;

public class PreferencesActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener{

	public static final String KEY_PREF_OPEN_LIC = "pref_open_source_lic";
	public static final String KEY_RATE_APP = "pref_rate_app";
	public static final String KEY_DEV_BY = "pref_developed_by";
	public static final String KEY_DELETE_FAVS = "pref_clear_favs";
	
	private PearlJamLyricsAppDAO dao;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Remove the app title and show the home as up icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        
        dao = new PearlJamLyricsAppDAO(this);
        
    }
	
	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance(this).activityStart(this);  //Analytics start
	  }
	
	@Override
	  public void onStop() {
	    super.onStop();
	    EasyTracker.getInstance(this).activityStop(this);  //Analytics stop
	  }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()){
		
			case android.R.id.home:
	            // This is called when the Home (Up) button is pressed
	            // in the Action Bar.
	            Intent parentActivityIntent = new Intent(this, 
	            		MainActivity.class);
	            parentActivityIntent.addFlags(
	                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
	                    Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(parentActivityIntent);
	            finish();
	            return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            final Preference preference) {

		//If user click on the 'delete all favorites'
		if(preference.getKey().equals(KEY_DELETE_FAVS)){
			
			AlertDialog.Builder clearAlert = new AlertDialog.Builder(this);
			clearAlert.setTitle(getResources().getString(R.string.pref_clear_favorites_alert_title))
				.setMessage(getResources().getString(R.string.pref_clear_favorites_alert_message))
				.setPositiveButton(getResources().getString(R.string.dialog_yes)
						, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						//delete all favorites from table and show a toast message
						dao.deleteAllFavorites();
						Toast.makeText(getApplicationContext()
								, getResources().getString(R.string.pref_clear_favortites_message)
								, Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton(getResources().getString(R.string.dialog_no)
						, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				dialog.dismiss();
			}
		}).create().show();
		}
		
		//If user click on the 'rate app' preference
		if(preference.getKey().equals(KEY_RATE_APP)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(getResources().getString(R.string.pref_rate_app_url)));
			startActivity(intent);
			return true;
		}
		
		//If user click on the 'developed by' preference
		if(preference.getKey().equals(KEY_DEV_BY)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(getResources().getString(R.string.pref_developed_by_url)));
			startActivity(intent);
			return true;
		}
		
		//If user click on the 'open source licences' preference
		if(preference.getKey().equals(KEY_PREF_OPEN_LIC)) {
			showLicencesDialog();
			return true;
		}
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
	
	
	/**
	 * Show dialog that cites licences sources
	 */
	protected void showLicencesDialog(){
		View view = getLayoutInflater().inflate(R.layout.licences_layout, null);
		
		
		AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(this);
		LicenseDialog.setView(view);
		LicenseDialog.setTitle(getResources().getString(R.string.pref_open_source_lic))
					.setPositiveButton(getResources().getString(R.string.dialog_close), 
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				dialog.dismiss();
			}
		}).create().show();
		
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}
}
