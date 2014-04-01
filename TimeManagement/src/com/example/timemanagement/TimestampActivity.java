package com.example.timemanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.timemanagement.model.Block;

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

	private List<Block> l = new ArrayList<Block>();
	private int listIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timestamp);
		// Show the Up button in the action bar.
		setupActionBar();
		
		TextView current = (TextView)findViewById(R.id.timestampText);
		current.setText("");
		current.append("Start \t \t \t \t Stop \n");
		for(int i=0; i<l.size();i++){
			current.append(l.get(i).toString());
		}
		
		
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
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String startTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		long startTime = System.currentTimeMillis();
		TextView current = (TextView)findViewById(R.id.timestampText);
		
		Date df = new java.util.Date(startTime);
		String vv = new SimpleDateFormat("hh:mm").format(df);
		Block b = new Block(startTime);
		l.add(b);
		
		current.setText("");
		for(int i=0; i<l.size();i++){
			current.append(l.get(i).toString());
		}
		
	}
	
	public void changeOrder(View view){
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String changeTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		long changeTime = System.currentTimeMillis();

		TextView current = (TextView)findViewById(R.id.timestampText);
		
		l.get(listIndex).setStop(changeTime);
		listIndex++;
		Block b = new Block(changeTime);
		l.add(b);
		
		current.setText("");
		for(int i=0; i<l.size();i++){
			current.append(l.get(i).toString());
		}
		
	}
	
	public void stopTime(View view){
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String stopTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		long stopTime = System.currentTimeMillis();
		
		TextView current = (TextView)findViewById(R.id.timestampText);
		
		l.get(listIndex).setStop(stopTime);
		
		current.setText("");
		for(int i=0; i<l.size();i++){
			current.append(l.get(i).toString());
		}
	}

}
