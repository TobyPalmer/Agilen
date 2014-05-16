package com.example.timemanagement;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.timemanagement.model.Block;
import com.example.timemanagement.statistics.DatePassable;
import com.example.timemanagement.statistics.DatePickerFragment;

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

public class BackupActivity extends MainActivity implements DatePassable{
	
	private TextView arrow, dateText;
	private Button exportStart, exportStop;
	private long start, stop, today;
	private Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);
		setupActionBar();
		
		/***** SÄTTA DET HÄR I FUNKTION KANSKE ******/
		cal = Calendar.getInstance();
		cal.set(Calendar.getInstance().get(Calendar.YEAR), 
					Calendar.getInstance().get(Calendar.MONTH)-1, 
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 0, 0);
		start = cal.getTimeInMillis(); 
		
		cal = Calendar.getInstance();
		cal.set(Calendar.getInstance().get(Calendar.YEAR), 
					Calendar.getInstance().get(Calendar.MONTH), 
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 23, 59);
		stop = cal.getTimeInMillis(); 
		
		arrow = (TextView)findViewById(R.id.arrow);
		dateText = (TextView)findViewById(R.id.dateText);
		
		exportStart = (Button)findViewById(R.id.dateStartButton);
		exportStart.setText(dateAsString(start));
		
		exportStop = (Button)findViewById(R.id.dateStopButton);
		exportStop.setText(dateAsString(stop));
		
		// EXPORTERAR BACKUP DATA SOM JSON //
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
	
		// EXPORTERAR TIDSRAPPORT MELLAN SPECIFIKA DATUM SOM CSV //
		Button exportTimeReportCSVButton = (Button)findViewById(R.id.exportTimeReportCSVButton);
		exportTimeReportCSVButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	String message = MainActivity.db.exportCSV(start, stop);
	        	AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
	        	builder.setCancelable(true);
	        	builder.setTitle("Exportera som CSV");
	        	builder.setMessage(message);
	        	
	        	// User clicked OK button
	   		 	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	   	           public void onClick(DialogInterface dialog, int id) {	   	        	   
	   	           }
	   		 	});
	        
	   		 	builder.show();
	        }
	    });
		
		Typeface font2 = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
		Typeface font_for_icon = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
		
		arrow.setTypeface(font_for_icon);
		dateText.setTypeface(font2);
		exportStart.setTypeface(font2);
		exportStop.setTypeface(font2);
		exportAllAsJSONButton.setTypeface(font2);
		exportTimeReportCSVButton.setTypeface(font2);
			
	}

	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() 
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}
	
	public String dateAsString(long time)
	{
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	
	public void changeStartDate(View view){
	    DialogFragment newFragment = new DatePickerFragment(start, 0);
	    newFragment.show(getFragmentManager(), "datePicker");	    
	}
	public void changeStopDate(View view){
	    DialogFragment newFragment = new DatePickerFragment(stop, 1);
	    newFragment.show(getFragmentManager(), "datePicker");	    
	}
	
	public long getCurrentMillis(){
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public int compareDates(long b1, long b2){
		Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(b1);
		Calendar d2 = Calendar.getInstance();
		d2.setTimeInMillis(b2);
		
		return d1.compareTo(d2);
	}
	
	public int compareToFutureDate(long b1){
		Calendar currentDate = Calendar.getInstance();
		Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(b1);
		
		return d1.compareTo(currentDate);		
	}

	@Override
	public void update(DatePickerFragment p, long o, int ID) 
	{		
		  long changedDate = o;		  
		  if(ID == 1)
		  {	
			  
			  stop = changedDate;
			 		  
			  if((compareDates(stop, start)  >= 0) && compareToFutureDate(stop) < 1)
			  {				  
				  exportStop.setText(dateAsString(stop));
			  }
			  else
			  {
				  if(compareToFutureDate(stop) < 1)
				  {
					  stop = start;					  
				  }
				  else
				  {					  
					  stop = getCurrentMillis();					  
				  }
				  exportStop.setText(dateAsString(stop));
			  }			 
		  }			  
		  else
		  {
			  start = changedDate;			  
			  		  
			  if((compareDates(start, stop) < 1) && compareToFutureDate(start) < 1)
			  {				  
				  exportStart.setText(dateAsString(start));
			  }
			  else
			  {
				  if(compareToFutureDate(start) < 1)
				  {
					  start = stop;					  
				  }
				  else
				  {					  
					  start = getCurrentMillis();					  
				  }
				  exportStart.setText(dateAsString(start));
			  }
		  }	
	}
}