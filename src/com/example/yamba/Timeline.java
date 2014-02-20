package com.example.yamba;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class Timeline extends ListActivity{

	static final String TAG = "TimelineActivity";
	


	static final String FROM[] ={ StatusData.C_USER,StatusData.C_CREATED_AT, StatusData.C_TEXT};
	static final int TO[] = {R.id.text_user , R.id.text_created_at, R.id.text_text};
	ListView list;
	Cursor cursor;
	SimpleCursorAdapter adapter;
	
	TimelineReciever receiver;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		cursor = ((YambaApp) getApplication()).statusData.query();
		
		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
		
		adapter.setViewBinder(VIEW_BINDER);
		
		setTitle(R.string.timeline);
		
		setListAdapter(adapter);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(receiver==null){
			receiver = new TimelineReciever(); 
			
		}
		registerReceiver(receiver, new IntentFilter(YambaApp.ACTION_NEW_STATUS));
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
		
	}


	
	static final ViewBinder VIEW_BINDER = new ViewBinder(){

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			// TODO Auto-generated method stub
			
			if(view.getId()!= R.id.text_created_at) return false;
			
			long time = cursor.getLong(cursor.getColumnIndex(StatusData.C_CREATED_AT));
			CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(time);
			
			((TextView)view).setText(relativeTime);
			
			return true;
		}

		
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		Intent intentUpdater = new Intent(this, UpdaterService.class);
		Intent intentRefresher = new Intent(this, RefreshService.class);
		Intent intentPreferences = new Intent(this, PrefsActivity.class);
		Intent intentStatus = new Intent(this, StatusActivity.class);
		switch(item.getItemId()){
		
		case R.id.item_start_service:
			
			startService(intentUpdater);
			return true;
		case R.id.item_stop_service:
			stopService(intentUpdater);
			return true;
			
		case R.id.item_refresh_service:
			startService(intentRefresher);
			return true;
		case R.id.item_preferences:
			startActivity(intentPreferences);
			return true;
			
		case R.id.item_update_status:
			startActivity(intentStatus);
			return true;
			
			default:
				
				return false;
				
		}

}
	
	
	public class TimelineReciever extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
			cursor = ((YambaApp) getApplication()).statusData.query();
			
			adapter.changeCursor(cursor);
			
			Log.d(TAG, "on recieve in timeline reciever"+ intent.getIntExtra("count", 0));
			
			
		}
		
	}

}