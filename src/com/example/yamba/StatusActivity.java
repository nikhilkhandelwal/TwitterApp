package com.example.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener {

	Button buttonUpdate;
	EditText editStatus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	Debug.startMethodTracing("Yamba.trace");
		setContentView(R.layout.status);
		buttonUpdate= (Button) findViewById(R.id.button_update);
		editStatus = (EditText) findViewById(R.id.edit_status);
		
		buttonUpdate.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	//	Debug.stopMethodTracing();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String statusText=editStatus.getText().toString();
		
		new PostToTwiter().execute(statusText);
		Log.d("Status Update","onCLick" + statusText);
		
	}
	
	class PostToTwiter extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
			Twitter twitter = new Twitter("student", "password");
			twitter.setAPIRootUrl("http://yamba.marakana.com/api");
			twitter.setStatus(params[0]);
			Log.d("Status Update","Succesfully Posted: "+params[0]);
			
			return "Successfully Posted: "+params[0];
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Status Update", "died"+ e);
			return "Failed Posting: "+params[0];
		}
		
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
		
	}
	

	
	}
	

