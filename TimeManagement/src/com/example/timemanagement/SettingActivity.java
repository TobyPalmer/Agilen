package com.example.timemanagement;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.timemanagement.model.Order;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.util.LogWriter;
import android.text.Editable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class SettingActivity extends MainActivity{
	
	int nrOfNotifications=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
	
		
		 Button addNotification = (Button)findViewById(R.id.addNotification);
		 
		    addNotification.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
				// TODO Auto-generated method stub
		        //Calendar time = Calendar.getInstance();
		        //setNotification(time);
		        notificationPopup();

			}
		 });
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
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void setNotification(Calendar time){
		
		Intent mServiceIntent = new Intent(SettingActivity.this, NotificationHandler.class);
		
		mServiceIntent.putExtra("title", "Chronox");
		mServiceIntent.putExtra("text", "Du har glömt att tidsrapportera!");
		
		PendingIntent pendingIntent = PendingIntent.getService(SettingActivity.this, nrOfNotifications, mServiceIntent, 0);
		
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
		nrOfNotifications++;
		
	}
	
	public void notificationPopup(){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("Notification Time");
		 
		 LayoutInflater inflater = getLayoutInflater();

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.activity_notificationpopup, null));
		    

		   
		 
		 builder.setPositiveButton("Lägg till", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	Calendar cal = Calendar.getInstance();
	        	   
	        	Dialog d = (Dialog) dialog;

				DatePicker date = (DatePicker)d.findViewById(R.id.notificationDate);
	           	TimePicker time = (TimePicker)d.findViewById(R.id.notificationTime);

	           	cal.set(date.getYear(), date.getMonth(), date.getDayOfMonth(), time.getCurrentHour(), time.getCurrentMinute());
	           	
	           	//Call to the function that creates the notification with the selected date/time
	           	setNotification(cal);
	           	
	           	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	           	Date da = new Date(cal.getTimeInMillis());
	           	String datum = sdf.format(da);
	           	TextView t = (TextView)findViewById(R.id.txtView);
	           	t.append(datum + "\n");
	           	
	           	
	           }
	       });
		 
		 builder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });

		 AlertDialog dialog = builder.create();
		 
		 dialog.show(); 
	 }

}
