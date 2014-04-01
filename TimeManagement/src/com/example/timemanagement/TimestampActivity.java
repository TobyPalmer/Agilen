package com.example.timemanagement;

import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.text.format.DateUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class TimestampActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timestamp);
		// Show the Up button in the action bar.
		setupActionBar();
		
		TextView current = (TextView)findViewById(R.id.timestampText);
		current.append("Start \t \t \t \t Stop \n");
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timestamp, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void startTime(View view){
		String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		String startTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		TextView current = (TextView)findViewById(R.id.timestampText);
		//current.setText(System.currentTimeMillis());
		
		current.append(startTime + " \t \t \t \t ");
		
		
	}
	
	public void changeOrder(View view){
		String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		String changeTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		TextView current = (TextView)findViewById(R.id.timestampText);
		
		current.append(changeTime + "\n" + changeTime + " \t \t \t \t ");
		
	}
	
	public void stopTime(View view){
		String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		String stopTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		TextView current = (TextView)findViewById(R.id.timestampText);
		
		current.append(stopTime + "\n");
		
	}

}
