package com.example.timemanagement;

import com.example.timemanagement.statistics.PickerFragment;
import com.example.timemanagement.statistics.TDPassable;
import com.example.timemanagement.statistics.TimePickerFragment;
import com.example.timemanagement.statistics.timeHM;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity to handle the configuration of the currentUser
 * @author Marcus
 *
 */
public class UserDetailActivity extends MainActivity implements TDPassable {
	
	private Button chooseWorkday;
	private TextView username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		Log.e("Hej hioo", "THISKD");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userdetail);
		
		Typeface font2 = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
		
		chooseWorkday = (Button)findViewById(R.id.workday);		
		username = (TextView)findViewById(R.id.username);
		
		TextView usernameLabel, workdayLabel;
		
		usernameLabel = (TextView)findViewById(R.id.usernameLabel);
		workdayLabel = (TextView)findViewById(R.id.txtWorkday);
		
		chooseWorkday.setTypeface(font2);
		username.setTypeface(font2);
		usernameLabel.setTypeface(font2);
		workdayLabel.setTypeface(font2);
		
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
	/**
	 * Updates the workday to be used in the Statistics calculations
	 */
	private void updateFields(){
		timeHM workday = new timeHM(MainActivity.currentUser.getWorkday());
		chooseWorkday.setText(workday.toString());
		username.setText(MainActivity.currentUser.getUsername().toString());
	}
	
	/**
	 * Function to get a new time by using a time picker dialog fragment
	 * @param view
	 */
	public void changeWorkday(View view){
		
	    DialogFragment newFragment = new TimePickerFragment(MainActivity.currentUser.getWorkday(), 0);
	    newFragment.show(getFragmentManager(), "timePicker");	    
	}
	
	/**
	 * Update the new workday for the current user
	 */
	@Override
	public void update(PickerFragment p, long o, int ID) {		
		if(p instanceof TimePickerFragment)  {
			long changedWorkday = o;		  
			MainActivity.currentUser.setWorkday(changedWorkday); 
			if(currentUser.getUsername().compareTo(MainActivity.DEFAULT_USER_NAME) != 0){
				try{
					MainActivity.db.putUser(MainActivity.currentUser);
				}catch (Exception e){
					Log.w("DB EXCEPTION", "COULD NOT UPDATE USER DETAILS");
				}
			}			
			updateFields();
		}		  
	}
	
}
