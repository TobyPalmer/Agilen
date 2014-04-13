package com.example.timemanagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.timemanagement.model.Block;

public class NewOrderActivity extends Activity implements DataPassable{
	
	private List<String> list = new ArrayList<String>();
	private Block timeBlock;
	private Button dateButton,
				   startButton,
				   stopButton;
	private TextView arrow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_order);
    	
    	//Gets the current date.
    	//ToDo: Get date from existing task instead.
    	Calendar cal = Calendar.getInstance();
    	timeBlock = new Block(9, cal.getTimeInMillis(), cal.getTimeInMillis());
    	dateButton = (Button)findViewById(R.id.taskDate);
    	dateButton.setText(timeBlock.toDateString());
    	
    	startButton = (Button)findViewById(R.id.taskStart);
    	startButton.setText(timeBlock.toTimeString(true));
    	
    	arrow = (TextView)findViewById(R.id.arrow);
    	Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
    	arrow.setTypeface(font);
    	
    	stopButton = (Button)findViewById(R.id.taskStop);
    	stopButton.setText(timeBlock.toTimeString(false));
    	
        list.add("02042304809 - Utveckling");
        list.add("02041514809 - Möte");
        list.add("02041519209 - Design");
        list.add("01341514809 - Lek");
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
	/**
	 * Sends the timeblock to a DatePickFragment popup.
	 * @param view
	 */
	public void changeDate(View view){
	    DialogFragment newFragment = new DatePickFragment(timeBlock);
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	/**
	 * Sends the timeblock to a TimePickFragment popup as start.
	 * @param view
	 */
	public void changeStart(View view){
	    DialogFragment newFragment = new TimePickFragment(timeBlock, true);
	    newFragment.show(getFragmentManager(), "startPicker");
	}
	
	/**
	 * Sends the timeblock to a DatePickFragment popup as stop.
	 * @param view
	 */
	public void changeStop(View view){
	    DialogFragment newFragment = new TimePickFragment(timeBlock, false);
	    newFragment.show(getFragmentManager(), "stopPicker");
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
	        		String order = stringOrderNumber + " - " + stringOrderName;
	        		if(!list.contains(order)){
			        	list.add(order);
			        	
			        	String message = "Your have succesfullt added a new task!";
			        	newPopUp("Task Created",message);	        			
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

	@Override
	public void update(Object o) {
		if(o instanceof Block){
			 timeBlock = (Block)o;
			 
			 if(timeBlock.getStart() > timeBlock.getStop()){
				 timeBlock.setStop(timeBlock.getStart());
				 timeBlock.setStart(timeBlock.getStop());
			 }
			 
			 dateButton.setText(timeBlock.toDateString());
			 startButton.setText(timeBlock.toTimeString(true));
			 stopButton.setText(timeBlock.toTimeString(false));
		}
		else return;	
	}
    
}
