package com.example.timemanagement;

import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.timemanagement.model.Block;

public class NewOrderActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_order);
		
    	final TimePicker timePickerStart = (TimePicker)findViewById(R.id.timePicker1);
    	final TimePicker timePickerStop = (TimePicker)findViewById(R.id.timePicker2);
    	
    	timePickerStart.onFinishTemporaryDetach();
		
    	timePickerStart.setIs24HourView(true);
    	timePickerStop.setIs24HourView(true);
    	
    	timePickerStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

	         public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
	        	 if(timePickerStart.getCurrentHour()>timePickerStop.getCurrentHour() || (timePickerStart.getCurrentHour()==timePickerStop.getCurrentHour() && timePickerStart.getCurrentMinute()>timePickerStop.getCurrentMinute())){
		        		 timePickerStart.setCurrentHour(timePickerStart.getCurrentHour());
		        		 timePickerStart.setCurrentMinute(timePickerStart.getCurrentMinute());
		        		 timePickerStop.setCurrentHour(timePickerStart.getCurrentHour());
		        		 timePickerStop.setCurrentMinute(timePickerStart.getCurrentMinute());
		         }
	         }
        });
    	
    	timePickerStop.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

	         public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
	        	 if(timePickerStart.getCurrentHour()>timePickerStop.getCurrentHour() || (timePickerStart.getCurrentHour()==timePickerStop.getCurrentHour() && timePickerStart.getCurrentMinute()>timePickerStop.getCurrentMinute())){
		        		 timePickerStart.setCurrentHour(timePickerStop.getCurrentHour());
		        		 timePickerStart.setCurrentMinute(timePickerStop.getCurrentMinute());
		        		 timePickerStop.setCurrentHour(timePickerStop.getCurrentHour());
		        		 timePickerStop.setCurrentMinute(timePickerStop.getCurrentMinute());
		         }
	         }
       });
    	
    	/*
    	Block b = new Block();
    	//sek - min - tim - dag - år
    	b.setStart(1000*60*60*24*365*5);
    	b.setStop(1000*60*60*24*365*6);
    	
    	setBlock(b);
    	*/
		// Show the Up button in the action bar.
		setupActionBar();
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
		getMenuInflater().inflate(R.menu.new_order, menu);
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
	
	@SuppressWarnings("deprecation")
	public void setBlock(Block b){
    	DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);
    	TimePicker timePickerStart = (TimePicker)findViewById(R.id.timePicker1);
    	TimePicker timePickerStop = (TimePicker)findViewById(R.id.timePicker2);
    	EditText orderNumber = (EditText)findViewById(R.id.editTextOrder);
    	EditText comments = (EditText)findViewById(R.id.editTextComments);
    	
    	Date startDate = new Date(b.getStart());
    	Date stopDate = new Date(b.getStop());
    	
    	datePicker.updateDate(1900+startDate.getYear(), startDate.getMonth(), startDate.getDay());
    	
    	timePickerStart.setCurrentHour(startDate.getHours());
    	timePickerStop.setCurrentHour(stopDate.getHours());
    	timePickerStart.setCurrentMinute(startDate.getMinutes());
    	timePickerStop.setCurrentMinute(stopDate.getMinutes());
	}
	
    public void addNewOrder(View view){
    	
    	TextView current = (TextView)findViewById(R.id.TextViewAddedOrder);
    	DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);
    	TimePicker timePickerStart = (TimePicker)findViewById(R.id.timePicker1);
    	TimePicker timePickerStop = (TimePicker)findViewById(R.id.timePicker2);
    	EditText orderNumber = (EditText)findViewById(R.id.editTextOrder);
    	EditText comments = (EditText)findViewById(R.id.editTextComments);
    	
    	int day = datePicker.getDayOfMonth();
    	int month = datePicker.getMonth();
    	int year =  datePicker.getYear();

    	int startH = timePickerStart.getCurrentHour();
    	int startM = timePickerStart.getCurrentMinute();
    	int stopH = timePickerStop.getCurrentHour();
    	int stopM = timePickerStop.getCurrentMinute();
    	
    	String _comments = orderNumber.getText().toString();
    	String _orderNumber = comments.getText().toString();
    	
    	Date startDate = new Date(year,month,day,startH,startM);
    	Date stopDate = new Date(year,month,day,stopH,stopM);
    
    	Block b = new Block(startDate.getTime());
    	b.setStop(stopDate.getTime());
    	
    	current.setText("");
    	current.append(b.toString());
    }

}
