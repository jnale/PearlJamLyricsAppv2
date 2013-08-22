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

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.internal.ActionBarSherlockNative;
import com.actionbarsherlock.view.MenuItem;

public class PreferencesActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener{

	public static final String KEY_PREF_OPEN_LIC = "pref_open_source_lic";
	public static final String KEY_RATE_APP = "pref_rate_app";
	public static final String KEY_DEV_BY = "pref_developed_by";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Remove the app title and show the home as up icon
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
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
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_PREF_OPEN_LIC)) {
			
		}
		
	}
	
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            final Preference preference) {

		//If user click on the 'rate app' preference
		if(preference.getKey().equals(KEY_RATE_APP)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=com.juannale.pearljamlyricsapp"));
			startActivity(intent);
			return true;
		}
		
		//If user click on the 'developed by' preference
		if(preference.getKey().equals(KEY_DEV_BY)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("http://www.twitter.com/nachoNale"));
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
		
		AlertDialog.Builder LicenseDialog = new AlertDialog.Builder(this);
		LicenseDialog.setTitle("Legal Notices").setPositiveButton("OK", 
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				dialog.dismiss();
			}
		}).setMessage("software licences").create();
		
		LicenseDialog.show();
		
	}
}
