package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements OnSharedPreferenceChangeListener {
	
	static final String TAG = "Yamba App";
	
	static final String ACTION_NEW_STATUS = "com.example.yamba.NEW_STATUS";
	static final String ACTION_REFRESH = "com.example.yamba.REFRESH_SERVICE";
	static final String ACTION_REFRESH_ALARM = "com.example.yamba.REFRESH_SERVICE";
	private Twitter twitter;
	
	
	SharedPreferences prefs;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		//prefs
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
	
	}
	
	public Twitter getTwitter() {
		if (twitter==null) {
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			String server = prefs.getString("server", "");
			//twitter
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(server);
		}
		
		return twitter;
	}
	static final Intent refreshAlram = new Intent(ACTION_REFRESH_ALARM);

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
		twitter=null;
		
		sendBroadcast(refreshAlram);
	}
	
	long lastTimeSeen=-1;
	int count=0;
	
	public void pullAndInsert()
	{
		try {
			List<Status> timeline = getTwitter().getUserTimeline();
			for (Status status : timeline) {
				//statusData.insert(status);
				
				getContentResolver().insert(StatusProvider.CONTENT_URI, StatusProvider.statusToValues(status));
				
				if(status.createdAt.getTime()>lastTimeSeen)
				{
					count++;
					lastTimeSeen = status.createdAt.getTime();
				}
				Log.d(TAG,String.format("%s: %s", status.getUser(),
						status.getText()));
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,"Failed to pull and insert"+e);
		}
		if(count>0)
		{
			//Log.d(TAG, "New tweets: "+count);
			sendBroadcast(new Intent(ACTION_NEW_STATUS).putExtra("count", count) );
		}
	}
	
}
