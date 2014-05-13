package com.example.timemanagement;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.timemanagement.model.Block;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupActivity extends MainActivity implements DataPassable{
	
	// ANNA TESTAR LITE
	private Button exportStart, exportStop;
	private long start, stop, today;
	private Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);
		// Show the Up button in the action bar.
		setupActionBar();

		Button exportAllAsJSONButton = (Button)findViewById(R.id.exportAllAsJSONButton);
		exportAllAsJSONButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	String message = MainActivity.db.exportJSON();
	        	AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
	        	builder.setCancelable(true);
	        	builder.setTitle("Exportera som JSON");
	        	builder.setMessage(message);
	        	
	        	
	        	// User clicked OK button
	   		 builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	   	           public void onClick(DialogInterface dialog, int id) {
	   	        	   
	   	           }
	   	     });
	        	
	        	
	        	builder.show();
	        }
	    });
		

		
		// ANNA TESTAR LITE
		cal = Calendar.getInstance();
		today = cal.getTimeInMillis();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 23, 59);
		stop = cal.getTimeInMillis();

		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		start = cal.getTimeInMillis();	
		
		setExportStartText(start);
		setExportStopText(start);
		
		exportStart = (Button)findViewById(R.id.exportStart);
		exportStart.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Block temp = new Block(start);
				temp.setStop(stop);
				
			    DialogFragment newFragment = new DatePickFragment(temp);
			    newFragment.show(getFragmentManager(), "datePicker");
			}
    		
    	});
		
		exportStop = (Button)findViewById(R.id.exportStop);
		exportStop.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Block temp = new Block(start);
				temp.setStop(stop);
				
			    DialogFragment newFragment = new DatePickFragment(temp);
			    newFragment.show(getFragmentManager(), "datePicker");
			}
    		
    	});
		
		Typeface font2 = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
		exportAllAsJSONButton.setTypeface(font2);
	}
	
	private void setExportStartText(long time){
		TextView exportStart = (TextView) findViewById(R.id.exportStart);
		Log.d("Start", "Start");
		exportStart.setText(new SimpleDateFormat("yyyy-MM-dd").format(time));
	}
	private void setExportStopText(long time){
		TextView exportStop = (TextView) findViewById(R.id.exportStop);
		Log.e("Stop", "Stop");
		exportStop.setText(new SimpleDateFormat("yyyy-MM-dd").format(time));
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}


		getActionBar().setDisplayHomeAsUpEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setStartAndStop(Block intentBlock){
		
		cal.setTimeInMillis(intentBlock.getStart());
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		start = cal.getTimeInMillis();
		
		cal.setTimeInMillis(intentBlock.getStop());
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 23, 59, 0);
		stop = cal.getTimeInMillis();
}

	@Override
	public void update(PickFragment p, Object o) {
		// TODO Auto-generated method stub
		Block timeBlock = (Block)o;
		 
		 if(p instanceof TimePickFragment){
			 
			 //Changes the time if the user used invalid values.
			 if(((TimePickFragment) p).isStart() && (timeBlock.getStart() > timeBlock.getStop())){
				 timeBlock.setStop(timeBlock.getStart());
				 timeBlock.setStart(timeBlock.getStop());
			 }
			 else if((timeBlock.getStart() > timeBlock.getStop())){
				 timeBlock.setStart(timeBlock.getStop());
				 timeBlock.setStop(timeBlock.getStart());
			 }
		 }	 
		 
		 setExportStartText(timeBlock.getStart());
		 setExportStopText(timeBlock.getStop());
	}
			
		

}