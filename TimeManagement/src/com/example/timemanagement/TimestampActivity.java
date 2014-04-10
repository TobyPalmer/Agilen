package com.example.timemanagement;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.timemanagement.model.Block;

public class TimestampActivity extends Activity {

	private List<Block> l = new ArrayList<Block>();
	private int listIndex = 0;
	boolean started = false;
	boolean stopped = true;
	Block b;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timestamp);
		// Show the Up button in the action bar.
		setupActionBar();
		
		printBlocks();
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
		
		if(stopped){
			
			long startTime = System.currentTimeMillis();
			
			b = new Block(startTime);
			
			MainActivity.db.addBlock(b);
			
			stopped = false;
			started = true;
			
			printBlocks();
		}
	}
	
	/*
	@SuppressLint("NewApi") public void changeOrder(View view){
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String changeTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		if(started){
		
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
			
			started = true;
			stopped = false;
		}		
	}
*/
	
	public void stopTime(View view){
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String stopTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		if(started){
		
			long stopTime = System.currentTimeMillis();		
			
			b.setStop(stopTime);
			
			MainActivity.db.putBlock(b);			
			
			started = false;
			stopped = true;
			
			printBlocks();
		}
	}
	
	public void printBlocks(){
		l = MainActivity.db.getAllBlocks();
		
		TextView current = (TextView)findViewById(R.id.timestampText);
		
		current.setText(" ");
		
		for(int i=0; i<l.size();i++){
			int orderId = l.get(i).getOrderID();
			
			Log.d("AgilTag",Integer.toString(orderId));
			
			if(MainActivity.db.getOrder(orderId)==null)
				current.append(l.get(i).toStringPublic() + "\n " );
			else
				current.append(l.get(i).toStringPublic() + " " +MainActivity.db.getOrder(orderId).toString() + "\n");
		}	
		
	}
	
	

}
