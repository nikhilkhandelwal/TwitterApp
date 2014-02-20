package com.example.yamba;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

public class PrefsActivity extends PreferenceActivity {

	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("PrefsActivity","on create");	
		addPreferencesFromResource(R.xml.prefs);
	}
}
