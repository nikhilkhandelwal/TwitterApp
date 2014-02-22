package com.example.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootReciever extends BroadcastReceiver {

	static PendingIntent lastOp;
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
	//	context.startService(new Intent(context, UpdaterService.class) );
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		long interval = Long.parseLong(prefs.getString("delay", "90000"));
		PendingIntent operation = PendingIntent.getService(context, -1, new Intent(YambaApp.ACTION_REFRESH), PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alaramManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
		alaramManager.cancel(lastOp);
		if(interval>0)
		{
		alaramManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, operation);
		}
		
		lastOp=operation;
		Log.d("BootReciever", "On recieve: delay "+interval);
		
	}

}
