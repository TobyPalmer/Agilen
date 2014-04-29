package com.example.timemanagement;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.R.color;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.swipetodismiss.*;
import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;

public class TimestampActivity extends ListActivity {
	
	// Contains all blocks in list view
	private List<Block> l = new ArrayList<Block>();
	
	// Handles current "running" block
	Block b;
	boolean started = false;
	boolean stopped = true;
	
	// Contains strings representing all blocks
	ArrayAdapter<String> mAdapter;

	// longs that represents the start- and endtime of the day
	private long start, stop;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timestamp);
		// Show the Up button in the action bar
		setupActionBar();
		
		//Sets stop to the end of this day.
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 23, 59);
		stop = cal.getTimeInMillis();
		
		//Sets start to the start of this day.
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 0, 0);
		start = cal.getTimeInMillis();
		
		//update view
		setDayText(start);
		printBlocks();
		
		//onclick that starts and stops the time.
		//It also changes the color and text on the button
		final Button start = (Button)findViewById(R.id.startButton);
		start.setBackgroundColor(Color.GREEN);
	    start.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	if(stopped){
	        		start.setBackgroundColor(Color.RED);
	        		start.setText(R.string.stop);
	        		startTime(v);	
	        	}
	        	else{
	        		start.setBackgroundColor(Color.GREEN);
	        		start.setText(R.string.start);
	        		stopTime(v);  	
	        	}
	        	
	        }
	    });
		

	}
	
	/**
	 * Helper method that updates the day TextView.
	 * 
	 * @param time The time that the view should be updated with
	 */
	private void setDayText(long time){
		TextView day = (TextView) findViewById(R.id.day);
		day.setText(new SimpleDateFormat("yyyy-MM-dd").format(time));
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
		getMenuInflater().inflate(R.menu.timestamp, menu);
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
	
	public void startTime(View view){
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String startTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		if(stopped){
			
			long startTime = System.currentTimeMillis();
			
			b = new Block(startTime);
			
			MainActivity.db.addBlock(b);
			
			stopped = false;
			started = true;
			
			printBlocks();
		}
	}
	

	
	public void stopTime(View view){
		//String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTimeInMillis());
		//String stopTime = mydate.substring(mydate.length()-8, mydate.length()-3);
		
		if(started){
		
			long stopTime = System.currentTimeMillis();		
			
			b.setStop(stopTime);
			
			MainActivity.db.putBlock(b);			
			
			started = false;
			stopped = true;
			
			printBlocks();
		}
	}
	
	public void printBlocks(){
		 
		final List<Block> blocksOfToday = MainActivity.db.getBlocksBetweenDate(start, stop);
		
		// Fetch blocks from database
		//blocksOfToday = MainActivity.db.getAllBlocks();

		
		// Initial strings for blocks, will be converted to ArrayAdapter
        String[] items = new String[blocksOfToday.size()];
        
        // Define initial block strings
        for (int i = 0; i < items.length; i++) {
        	int orderId = blocksOfToday.get(i).getOrderID();
			final Block block = blocksOfToday.get(i);
			Order order = MainActivity.db.getOrder(orderId);
			
        	String s = "";
			//s = blocksOfToday.get(i).getID() + " " + blocksOfToday.get(i).toStringPublic();
			if(MainActivity.db.getOrder(orderId)==null)
				s = blocksOfToday.get(i).toStringPublic() + " - " + block.getComment();
			else
				s = block.toStringPublic() + " - " + order.getOrderName() + " " + block.getComment();
//				
			items[i] = s;
        }
        
        // Define the data structure for the list strings
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String>(Arrays.asList(items)));        
        setListAdapter(mAdapter);
        
        
        
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        ListView listView = getListView();
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            public void onClick(ListView listView, int position) {
                            	Block block = blocksOfToday.get(position);
                            	Log.w("timemanagement", "GUI: Clicked block:" + block.getID());
            		        	
            		        	Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
            		        	
            		        	i.putExtra("Block", block);
            		        	i.putExtra("String", "editBlock");
            		        	        	
            		        	startActivity(i);
                            	
                            }
                            
                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                	Log.w("timemanagement", "GUI: Swipe-removed block, position = " + position + ", block.getID() = " + blocksOfToday.get(position).getID());
                                	//MainActivity.db.deleteBlock(blocksOfToday.get(position));
                                	deleteBlock(listView,blocksOfToday.get(position));
                                	blocksOfToday.remove(position);
                                	mAdapter.remove(mAdapter.getItem(position));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
		
		
	}
	
	
	/**
	 * Adds one day on the value of start/stop and updates the view. 
	 * 
	 * @param v
	 */
	public void showTomorrow(View v){
		start += 86400000;
		stop += 86400000;
		setDayText(start);
		printBlocks();
	}
	
	/**
	 * Subtracts one day on the value of start/stop and updates the view. 
	 * 
	 * @param v
	 */
	public void showYesterday(View v){
		start -= 86400000;
		stop -= 86400000;
		setDayText(start);
		printBlocks();
	}
		
	
	
	public void addComment(View view, final Block block){
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);

		 builder.setTitle("L�gg till kommentar");
		 
		 String s = "";
		 if(block.getOrderID()!=0){
			 Order o = MainActivity.db.getOrder(block.getOrderID());
			 s="Kommentera '<i>" + block.toStringPublic() + ": <b>" + o.getOrderName() + "</b> </i>' :"; 	 
		 } 
		 else
			 s="Kommentera '<i>" + block.toStringPublic() + "</i>' :";
			 
		 builder.setMessage(Html.fromHtml(s));
		 
		 LayoutInflater inflater = getLayoutInflater();

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    builder.setView(inflater.inflate(R.layout.activity_addcommentpopup, null));
		    
		   
		 

		 builder.setPositiveButton("L�gg till", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	
	        	Dialog d = (Dialog) dialog;

	           	EditText comment = (EditText)d.findViewById(R.id.addCommentPop);
	        	String commentText = comment.getText().toString();
	        	block.setComment(commentText);
	        	MainActivity.db.putBlock(block);
	        	//printBlocks();
	        	
	        	
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
		        	//printBlocks();
	           }	
	       });
		 
		 builder.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	        	   getWindow().setSoftInputMode(
	        			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	        	   printBlocks();
	           }
	       });

		 AlertDialog dialog = builder.create();
		 
		 dialog.show(); 
	 }
	   public void newOrderActivity(View view){
	        Intent intent = new Intent(this, NewOrderActivity.class);
	        
	        startActivity(intent);
	    }
	
}
