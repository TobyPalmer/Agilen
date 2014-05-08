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
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.timemanagement.R.color;
import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;

public class NewOrderActivity extends MainActivity implements DataPassable{
	
	private List<Order> list = new ArrayList<Order>();
	private Spinner s;
	private ArrayAdapter adapter;
	private Block timeBlock;
	private Button dateButton,
				   startButton,
				   stopButton;
	private Button d;
	private TextView arrow;
	private EditText comment;
	private boolean newTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_order);
    	
		newTask = true;
		
    	//Gets the current date.
    	//ToDo: Get date from existing task instead.
    	Calendar cal = Calendar.getInstance();
    	timeBlock = new Block(9, cal.getTimeInMillis(), cal.getTimeInMillis(), "comment");
    	dateButton = (Button)findViewById(R.id.taskDate);
    	dateButton.setText(timeBlock.toDateString());
    	
    	startButton = (Button)findViewById(R.id.taskStart);
    	startButton.setText(timeBlock.toTimeString(true));
    	
    	comment = (EditText)findViewById(R.id.editTextComments);
    	
    	arrow = (TextView)findViewById(R.id.arrow);
    	Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
    	arrow.setTypeface(font);
    	
    	stopButton = (Button)findViewById(R.id.taskStop);
    	stopButton.setText(timeBlock.toTimeString(false));
    	
    	
    	
    	// Get all orders
    	list = MainActivity.db.getAllOrders();
    	
        s = (Spinner) findViewById(R.id.spinner1);
		adapter = new ArrayAdapter(this, R.layout.spinner_item, list);
 
        s.setAdapter(adapter);
        
        if(getIntent().getExtras() != null){
        	Log.w("Time Management", "fick från Timestamp");
        	newTask = false;
        	timeBlock = (Block) getIntent().getSerializableExtra("Block");
        	update(timeBlock);
        }
        
        d = (Button)findViewById(R.id.deleteButton);
	       if(getIntent().getExtras()!=null){
	        	d.setVisibility(View.VISIBLE);
	       }
	       else{
	        	d.setVisibility(View.INVISIBLE);
	       }
	       d.setOnClickListener(new View.OnClickListener() {

	    	
	        public void onClick(View v) {
            	Log.w("AgilTag", timeBlock.toString());
	        	deleteBlock(v, timeBlock);
	        }
	    });

		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
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
	
	public void setBlock(Block b){

    	timeBlock = b;

	}
	
	public void addNewOrderNumber(View view){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("New Task");
		 
		 LayoutInflater inflater = getLayoutInflater();

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.activity_neworderpopup, null));
		 
		 builder.setPositiveButton("L�gg till", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	
	        	Dialog d = (Dialog) dialog;

	           	EditText orderName = (EditText)d.findViewById(R.id.orderNamePop);
	        	EditText orderNumber = (EditText)d.findViewById(R.id.orderNumberPop);
	        	CheckBox orderDirectWork = (CheckBox)d.findViewById(R.id.orderDirectWorkPop);
	        	String stringOrderName = orderName.getText().toString();
	        	String stringOrderNumber = orderNumber.getText().toString();
	        	int integerOrderDirectWork = 0; 
	        	if(orderDirectWork.isChecked()){
	        		integerOrderDirectWork = 1;
	        	}	        	
	        	
	        	if(isInteger(stringOrderNumber)){
	        		Order order = new Order(stringOrderNumber, stringOrderName, integerOrderDirectWork);
	        		if(!list.contains(order)){
			        	list.add(order);
			        	
			        	String message = "You have succesfully added a new task!";
			        	newPopUp("Task Created",message);
			        	
			            s.setSelection(list.size()-1);
			            adapter.notifyDataSetChanged();
			        	
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
		 
		 builder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });

		 AlertDialog dialog = builder.create();
		 
		 dialog.show(); 
	 }
	 
    public void addNewOrder(View view){
    
    	EditText editTextComments = (EditText)findViewById(R.id.editTextComments);
    	Spinner spinner = (Spinner)findViewById(R.id.spinner1);
    	Order selectedOrder = null;
    	String orderString = "";
    	
    	if(spinner.getAdapter().getCount() > 0){
    		selectedOrder = (Order)spinner.getSelectedItem();
    		timeBlock.setOrderID(selectedOrder.getID());
    		orderString =  selectedOrder.toString();
    	}
    		
    	String comments = editTextComments.getText().toString();
    
    	timeBlock.setComment(comments);
    	
    	String message = "You have succesfully edited your task! \n\n" +
    					 orderString + "\n" + timeBlock.toStringPublic() +
    					 "\n " + comments;
    	newPopUp("Task Edited",message);
    	
    	//Save to db
    	if(getIntent().getExtras()!=null)
    		MainActivity.db.putBlock(timeBlock);
    	else
    		MainActivity.db.addBlock(timeBlock);
    	
    	 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("Uppgift sparad");
		 builder.setMessage(message);
		 
		 // User clicked OK button
		 builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   
	        	    Intent i = new Intent(getApplicationContext(), TimestampActivity.class);
		        	i.putExtra("Block", timeBlock);  	
		        	startActivity(i);
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
	public void update(PickFragment p, Object o) {		
		  timeBlock = (Block)o;
			 
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
	
			 //Refresh date and time
			 dateButton.setText(timeBlock.toDateString());
			 startButton.setText(timeBlock.toTimeString(true));
			 stopButton.setText(timeBlock.toTimeString(false));
			 
			 
			 if(!newTask){
				 Log.w("AgilTag","kk");
				 updateSpinner();
				 updateComment();
				 newTask = true;
			 } 	
	}
	
	public void update(Block timeBlock){
		
		 if(timeBlock.getStart() > timeBlock.getStop()){
			 timeBlock.setStop(timeBlock.getStart());
			 timeBlock.setStart(timeBlock.getStop());
		 }
		 else if((timeBlock.getStart() > timeBlock.getStop())){
			 timeBlock.setStart(timeBlock.getStop());
			 timeBlock.setStop(timeBlock.getStart());
		 }

		 //Refresh date and time
		 dateButton.setText(timeBlock.toDateString());
		 startButton.setText(timeBlock.toTimeString(true));
		 stopButton.setText(timeBlock.toTimeString(false));
		 
		 if(!newTask){
			 Log.w("AgilTag","kk");
			 updateSpinner();
			 updateComment();
			 newTask = true;
		 }
	}
	
	public void updateSpinner(){
	   //Update spinner
	   Order order = MainActivity.db.getOrder(timeBlock.getOrderID());
       s.setSelection(list.indexOf(order));
       adapter.notifyDataSetChanged();		
	}
	
	public void updateComment(){
        //Update comments
        String comments = MainActivity.db.getBlock(timeBlock.getID()).getComment();
        comment.setText(comments);	 
	}
	
	public void deleteBlock(View view, final Block block){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);


		 builder.setTitle("Ta bort");
		 
		 String s = "";
		 if(block.getOrderID()!=0){
			 Order o = MainActivity.db.getOrder(block.getOrderID());
			 s="Vill du ta bort '<i>" + block.toStringPublic() + ": <b>" + o.getOrderName() + "</b> </i> '?"; 	 
		 } 
		 else
			 s="Vill du ta bort '<i>" + block.toStringPublic() + "</i>' ?";
			 
		 builder.setMessage(Html.fromHtml(s));

	
		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		 
		 builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button

		        	MainActivity.db.deleteBlock(block);
		        	startActivity(new Intent(getApplicationContext(), TimestampActivity.class));
	           }	
	       });
		 
		 builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	        	   getWindow().setSoftInputMode(
	        			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	           }
	       });

		 AlertDialog dialog = builder.create();
		 
		 dialog.show(); 
	 }

}
