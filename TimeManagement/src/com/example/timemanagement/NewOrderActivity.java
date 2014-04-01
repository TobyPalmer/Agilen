package com.example.timemanagement;

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

public class NewOrderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_order);
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
    	
    	current.setText("");
    	current.append(year + "-" + month + "-" + day + "\n" + startH + ":" + startM + " - " + stopH + ":" + stopM + "\n");
    	current.append("Order Number: " + _orderNumber + "\n" + "Comments: " + _comments + "\n \n");
    	

    }

}
