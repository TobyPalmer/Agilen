package com.example.timemanagement;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.swipetodismiss.SwipeDismissListViewTouchListener;
import com.example.swipetodismiss.*;
import com.example.timemanagement.customadapters.CustomListAdapter2;
import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;

public class TimestampActivity extends MainActivity implements DataPassable {
	
	// Contains all blocks in list view
	private ListView listView;
	private Button next, prev, startB, create;
	private Button day;
	private CustomListAdapter2 mAdapter;
	
	// Calendar that is set to the chosen day.
	private Calendar cal;
	
	// Handles current "running" block
	Block b;
	
	boolean started = false;

	// longs that represents the start- and endtime of the day
	private long start, stop, today;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timestamp);
		
		listView = (ListView) findViewById(android.R.id.list);
		
		
		// Show the Up button in the action bar
		setupActionBar();
	
		
		// Sets the start and stop to that of the current day.
		cal = Calendar.getInstance();
		today = cal.getTimeInMillis();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 23, 59);
		stop = cal.getTimeInMillis();

		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		start = cal.getTimeInMillis();	

		//Adding arrows
		next = (Button)findViewById(R.id.nextDay);
    	Typeface font_for_right = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
    	next.setTypeface(font_for_right);
    	
    	prev = (Button)findViewById(R.id.prevDay);
    	Typeface font_for_left = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
    	prev.setTypeface(font_for_left);
		
		//update view

		// Use block from calling intent (if any)
		if(savedInstanceState == null){
			Bundle extras = getIntent().getExtras();
			if(extras != null){
				Block intentBlock = (Block) extras.get("Block");
				setStartAndStop(intentBlock);
			}
		}
	

		
		//adding arrows to buttons
    	next = (Button)findViewById(R.id.nextDay);
    	Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
    	next.setTypeface(font);
    	
    	prev = (Button)findViewById(R.id.prevDay);
    	prev.setTypeface(font);
    	

    	
    	//use neo sans font on buttons and date
        Typeface font2 = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
        
        //use century gothic on list
    	Typeface font3 = Typeface.createFromAsset(getAssets(), "gothic.ttf");
        
    	
        startB = (Button)findViewById(R.id.startButton);
    	startB.setTypeface(font2);
    	
		// update view
		setDayText(start);
		printBlocks();
    	
        create = (Button)findViewById(R.id.changeOrderButton);
    	create.setTypeface(font2);
    	enableCreateButton();
    	
    	create.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				create.setEnabled(false);
				create.setBackgroundColor(getResources().getColor(R.color.darkGrey));
				create.setTextColor(getResources().getColor(R.color.lightGrey));
				
				Block intentBlock = new Block(start);
				MainActivity.db.addBlock(intentBlock);
				
				intentBlock.setStop(start);
				MainActivity.db.putBlock(intentBlock);	
				
				Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
		    	i.putExtra("Block", intentBlock);
		    	i.putExtra("Caller", "Timestamp");
		    	startActivity(i);	
			}
		});
		
    	day = (Button)findViewById(R.id.day);
    	day.setTypeface(font2);
    	day.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Block temp = new Block(start);
				temp.setStop(stop);
			    DialogFragment newFragment = new DatePickFragment(temp);
			    newFragment.show(getFragmentManager(), "datePicker");
			}
    		
    	});
    	
		

		//onclick that starts and stops the time.
		//It also changes the color and text on the button
		startB.setBackgroundColor(Color.parseColor("#57bf23"));
	    startB.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	if(!started){
	        		startB.setBackgroundColor(getResources().getColor(R.color.red));
	        		startB.setText(R.string.stop);
	        		startTime();	
	        	}
	        	else{
	        		startB.setBackgroundColor(getResources().getColor(R.color.green));
	        		startB.setText(R.string.start);
	        		stopTime();  	
	        	}
	        }
	    });
	    setStartButton();
	}
	
	/**
	 * Sets start and stop to that from a timeBlock.
	 * 
	 * @param intentBlock
	 */
	private void setStartAndStop(Block intentBlock){
		
			cal.setTimeInMillis(intentBlock.getStart());
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
					cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			start = cal.getTimeInMillis();
			
			cal.setTimeInMillis(intentBlock.getStop());
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
					cal.get(Calendar.DAY_OF_MONTH), 23, 59, 0);
			stop = cal.getTimeInMillis();
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
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}

	public void startTime(){
			started = true;
			long startTime = System.currentTimeMillis();

			
			b = new Block(1,startTime);
			String s = b.toString();
			Log.v("orderID", s);
			MainActivity.db.addBlock(b);
			
			printBlocks();
	}
	

	
	public void stopTime(){
			started = false;
			long stopTime = System.currentTimeMillis();		
			
			b.setStop(stopTime);
			
			MainActivity.db.putBlock(b);			
			
			printBlocks();
	}
	
	public void printBlocks(){
		 
		final List<Block> blocksOfToday = MainActivity.db.getBlocksBetweenDate(start, stop);
		
		// Running blocks should only show on today
		for(Block temp : blocksOfToday){
			if(!temp.isStopped() &! startB.isEnabled()){
				blocksOfToday.remove(temp);
			}
		}
		
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
			items[i] = s;
        }
        
        // Define the data structure for the list strings    
        mAdapter = new CustomListAdapter2(this,R.layout.listrow2, new ArrayList<String>(Arrays.asList(items)), "neosanslight.ttf");
        listView.setAdapter(mAdapter);
        
        
        
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        //ListView listView = l_view.getListView();
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
                            	if(!block.isStopped()){
                	    			startB.setBackgroundColor(getResources().getColor(R.color.green));
                	        		startB.setText(R.string.start);
                	        		started = false;
                            	}
                            	Log.w("timemanagement", "GUI: Clicked block:" + block.getID());
            		        	
            		        	Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
            		        	
            		        	i.putExtra("Block", block);
            		        	i.putExtra("Caller", "Timestamp");
            	    			
            	    			printBlocks();
            		        	        	
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
		// Define UI-element to be animated
		final View view = (View)findViewById(R.id.timestampList);
		// Define animation
		final Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_right);
		// Define animation listener
		AnimationListener animListener = new AnimationListener() {
			// onAnimationEnd callback
			public void onAnimationStart(Animation animation) {}
		    public void onAnimationRepeat(Animation animation) {}
		    public void onAnimationEnd(Animation animation) {
		    	start += 86400000;
				stop += 86400000;
		    	setDayText(start);
		    	setStartButton();
		    	printBlocks();
		    }
		};
		// Set animation listener
		anim.setAnimationListener(animListener);
		// Start animation
		view.startAnimation(anim);
	}
	
	/**
	 * Subtracts one day on the value of start/stop and updates the view. 
	 * 
	 * @param v
	 */
	public void showYesterday(View v){
		// Define UI-element to be animated
		final View view = (View)findViewById(R.id.timestampList);
		// Define animation
		final Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_left);
		// Define animation listener
		AnimationListener animListener = new AnimationListener() {
			// onAnimationEnd callback
			public void onAnimationStart(Animation animation) {}
		    public void onAnimationRepeat(Animation animation) {}
		    public void onAnimationEnd(Animation animation) {
		    	start -= 86400000;
				stop -= 86400000;
		    	setDayText(start);
				setStartButton();
				printBlocks();
		    }
		};
		// Set animation listener
		anim.setAnimationListener(animListener);
		// Start animation
		view.startAnimation(anim);
	}
	
	/**
	 * Function that enables the startbutton if the day displayed is today
	 * and disabling the button if another day is displayed.
	 */
	private void setStartButton(){
		Date dateToday = new Date(today);
		Date dateStart = new Date(start);
		Date dateStop = new Date(stop);
		
		if ((dateToday.compareTo(dateStart) == 1) && (dateToday.compareTo(dateStop) == -1)){
			startB.setEnabled(true);
			if(started){
				startB.setBackgroundColor(getResources().getColor(R.color.red));
				startB.setTextColor(Color.WHITE);
			} else{
				startB.setBackgroundColor(getResources().getColor(R.color.green));
				startB.setTextColor(Color.WHITE);
			}
			
		} else{
			startB.setEnabled(false);
			startB.setBackgroundColor(getResources().getColor(R.color.darkGrey));
			startB.setTextColor(getResources().getColor(R.color.lightGrey));
		}
	}
	
	
	private void enableCreateButton(){
    	create.setEnabled(true);
    	create.setBackgroundColor(getResources().getColor(R.color.orange));
		create.setTextColor(Color.WHITE);	
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
		    
		   
		 

		 builder.setPositiveButton("Lägg till", new DialogInterface.OnClickListener() {
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
	
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    enableCreateButton();
	    printBlocks();
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
		        	
		        	// Resets the startbutton if the deleted block was "running"
                  	if(!block.isStopped()){
                		startB.setBackgroundColor(getResources().getColor(R.color.green));
    	        		startB.setText(R.string.start);
    	        		started = false;
                	}
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
	
	/**
	 * Stops any running block if activity is destroyed.
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();
		if(started){
			stopTime();  
		}
	}
			

	@Override
	public void update(PickFragment p, Object o) {
		if(o instanceof Block){
			
			setStartAndStop((Block) o);
			
			// update view
			setDayText(start);
			printBlocks();	
			setStartButton();
		}
	}
}
