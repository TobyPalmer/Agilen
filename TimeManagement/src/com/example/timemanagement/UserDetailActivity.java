package com.example.timemanagement;

import java.util.Calendar;

import com.example.timemanagement.statistics.DatePickerFragment;
import com.example.timemanagement.statistics.PickerFragment;
import com.example.timemanagement.statistics.TDPassable;
import com.example.timemanagement.statistics.TimePickerFragment;
import com.example.timemanagement.statistics.timeHM;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserDetailActivity extends MainActivity implements TDPassable {
	
	private Button chooseWorkday;
	private TextView username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		Log.e("Hej hioo", "THISKD");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userdetail);
		
		chooseWorkday = (Button)findViewById(R.id.workday);		
		username = (TextView)findViewById(R.id.username);
		
		updateFields();
		
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() 
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	private void updateFields(){
		timeHM workday = new timeHM(MainActivity.currentUser.getWorkday());
		chooseWorkday.setText(workday.toString());
		username.setText(MainActivity.currentUser.getUsername().toString());
	}
	
	public void changeWorkday(View view){
		
	    DialogFragment newFragment = new TimePickerFragment(MainActivity.currentUser.getWorkday(), 0);
	    newFragment.show(getFragmentManager(), "timePicker");	    
	}
	
	@Override
	public void update(PickerFragment p, long o, int ID) {		
		if(p instanceof TimePickerFragment)  {
			long changedWorkday = o;		  
			MainActivity.currentUser.setWorkday(changedWorkday); 
			updateFields();
		}		  
	}
	
}
