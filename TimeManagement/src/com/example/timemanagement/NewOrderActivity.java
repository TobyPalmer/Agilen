package com.example.timemanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;

public class NewOrderActivity extends MainActivity {
	
	private List<Order> list = new ArrayList<Order>();
	
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
    	//String[] a = list.toArray(new String[list.size()]); 
    	
    	// Get all orders
    	list = MainActivity.db.getAllOrders();

    	// Write all orders to console
    	for (int i = 0; i < list.size(); i++) {
    	    Order order = list.get(i);
    	}
    	
        Spinner s = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, list);
 
        s.setAdapter(adapter);
        
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

    	EditText comments = (EditText)findViewById(R.id.editTextComments);
    	
    	Date startDate = new Date(b.getStart());
    	Date stopDate = new Date(b.getStop());
    	
    	datePicker.updateDate(startDate.getYear(), startDate.getMonth(), startDate.getDay());
    	
    	timePickerStart.setCurrentHour(startDate.getHours());
    	timePickerStop.setCurrentHour(stopDate.getHours());
    	timePickerStart.setCurrentMinute(startDate.getMinutes());
    	timePickerStop.setCurrentMinute(stopDate.getMinutes());
	}
	
	public void addNewOrderNumber(View view){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("New Task");
		 
		 LayoutInflater inflater = getLayoutInflater();

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.activity_neworderpopup, null));
		 
		 builder.setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	
	        	Dialog d = (Dialog) dialog;

	           	EditText orderName = (EditText)d.findViewById(R.id.orderNamePop);
	        	EditText orderNumber = (EditText)d.findViewById(R.id.orderNumberPop);
	        	String stringOrderName = orderName.getText().toString();
	        	String stringOrderNumber = orderNumber.getText().toString();
	        	
	        	if(isInteger(stringOrderNumber)){
	        		Order order = new Order(stringOrderNumber, stringOrderName);
	        		if(!list.contains(order)){
			        	list.add(order);
			        	
			        	String message = "Your have succesfullt added a new task!";
			        	newPopUp("Task Created",message);	        	
			        	
			        	//Save to dB
			        	MainActivity.db.addOrder(order);
	        		}
	        		else{
	        			newPopUp("Error!","'" + order + "' already exists!");
	        		}

	        	}
	        	else{
	        		newPopUp("Error!","'" + stringOrderNumber + "' is not a valid order number!");
	        	}
	           }
	       });
		 
		 builder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });

		 
		 
		 AlertDialog dialog = builder.create();
		 
		 dialog.show();

		 
	 }
	 
    public void addNewOrder(View view){
    	
    	DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker1);
    	TimePicker timePickerStart = (TimePicker)findViewById(R.id.timePicker1);
    	TimePicker timePickerStop = (TimePicker)findViewById(R.id.timePicker2);

    	EditText editTextComments = (EditText)findViewById(R.id.editTextComments);
    	Spinner spinner = (Spinner)findViewById(R.id.spinner1);
    	
    	int day = datePicker.getDayOfMonth();
    	int month = datePicker.getMonth();
    	int year =  datePicker.getYear()-1900;

    	int startH = timePickerStart.getCurrentHour();
    	int startM = timePickerStart.getCurrentMinute();
    	int stopH = timePickerStop.getCurrentHour();
    	int stopM = timePickerStop.getCurrentMinute();
    	
    	Order selectedOrder = (Order)spinner.getSelectedItem();
    	String comments = editTextComments.getText().toString();
    	
    	Date startDate = new Date(year,month,day,startH,startM);
    	Date stopDate = new Date(year,month,day,stopH,stopM);
    
    	Block b = new Block(startDate.getTime());
    	b.setStop(stopDate.getTime());
    	b.setID(selectedOrder.getID());
    	
    	String message = "Your have succesfullt edited your task! \n\n" + selectedOrder.toString() + "\n" + b.toStringPublic() + "\n " + comments;
    	newPopUp("Task Edited",message);
    	
    	//Save to db
    	MainActivity.db.addBlock(b);
    	
    	
    }
    
    public void newPopUp(String title, String message){
    	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle(title);
		 builder.setMessage(message);
		 
		 builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button

	           }
	     });
		
		 AlertDialog dialog = builder.create();
		 
		 dialog.show();
    	
    }
    
    public static boolean isInteger(String s) {
        try { 
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        // only got here if we didn't return false
        return true;
    }
    

}
