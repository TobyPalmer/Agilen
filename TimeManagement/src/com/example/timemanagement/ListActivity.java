
package com.example.timemanagement;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timemanagement.customadapters.CustomListAdapter1;
import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;
import com.example.timemanagement.R;
import android.app.DialogFragment;



	public class ListActivity extends MainActivity implements DataPassable {
		
		
		private List<Block> bList = new ArrayList<Block>();
		private ListView l_view;
		private TextView total;
		private CustomListAdapter1 listAdapter;
		private Date currentDate;
		private Button next, prev, day;

		private ArrayList<String> blockList;
		private ArrayList<Integer> blockStatesList;
		
		private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		private String s, dateString;
		private Block b;
		private int minutesDay, hoursDay;
		private long today, timeDiff, stop, start;
		int height, width;
		private Calendar cal;

		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_list);
			// Show the Up button in the action bar.
			setupActionBar();

			WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
	        height = wm.getDefaultDisplay().getHeight();
	        width = wm.getDefaultDisplay().getWidth();
			
			ListView l = (ListView)findViewById(R.id.l_view);
			l.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, (height-480)));

			cal = Calendar.getInstance();

			today = cal.getTimeInMillis();
			// Sets start and stop either from the calling activity or from the
			// date of today.
			Bundle extras = getIntent().getExtras();
			if(extras != null){
				Block intentBlock = (Block) extras.get("Block");
				cal.setTimeInMillis(intentBlock.getStart());
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
						cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
				start = cal.getTimeInMillis();
				
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
						cal.get(Calendar.DAY_OF_MONTH), 23, 59, 0);
				stop = cal.getTimeInMillis();
			} else{
				
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
						cal.get(Calendar.DAY_OF_MONTH), 23, 59, 0);
				stop = cal.getTimeInMillis();
				
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
						cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
				start = cal.getTimeInMillis();
			}
			
			
			//Create a list of all the blocks
	    	//BList = MainActivity.db.getAllBlocks();
			bList = MainActivity.db.getBlocksBetweenDate(start, stop);
			
			Typeface font3 = Typeface.createFromAsset(getAssets(), "gothic.ttf");
			Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
			Typeface font2 = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
			
			l_view = (ListView) findViewById(R.id.l_view);
			day = (Button) findViewById(R.id.day);
			total = (TextView) findViewById(R.id.total);
			total.setTypeface(font2);
			
			blockList = new ArrayList<String>();
			blockStatesList = new ArrayList<Integer>();
			
			cal.setTimeInMillis(today);
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
					cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			today = cal.getTimeInMillis();
			currentDate = new Date(today);
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			dateString = (dateFormat.format(start));
			iterateBlocks(dateString);
			
			//Adding arrows to buttons
	    	next = (Button)findViewById(R.id.nextDay); 	
	    	next.setTypeface(font);
	    	
	    	prev = (Button)findViewById(R.id.prevDay);
	    	prev.setTypeface(font);

	    	day.setTypeface(font2);
	    	
	    	day.setOnClickListener(new View.OnClickListener() {
				
				@Override
			public void onClick(View v) {
					Block temp = new Block(start);
					temp.setStop(stop);
				    DialogFragment newFragment = new DatePickFragment(temp);
				    newFragment.show(getFragmentManager(), "datePicker");
				}
			});
	    		   
	    	next.setOnClickListener(new View.OnClickListener() {

		        public void onClick(View v) {
		        	if(new Date(start).before(currentDate)){
		        		nextDate();
		        	};
		        }
	    	});
		        		
		        	
		    
		    prev = (Button) findViewById(R.id.prevDay);		
		    prev.setOnClickListener(new View.OnClickListener() {

		        public void onClick(View v) {
		        	prevDate();
		        }
		    });
		    
		    Button selectAllButton = (Button) findViewById(R.id.selectAll);	  
		    selectAllButton.setTypeface(font2);
	    	selectAllButton.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		        	Log.e("Button","selectAllButton");
		        	Iterator<Block> it = bList.iterator(); 
			    	while(it.hasNext())
			    	{
			    		b = it.next();
			    		if(b.getOrderID() > 1 ){
			    			b.setChecked(1);
			    			MainActivity.db.putBlock(b);
			    		}			    		
			    	}
			    	setAllChecks(1);
			    	iterateBlocks(dateString);
		        }
		    });
	    	
	    	Button deselectAllButton = (Button) findViewById(R.id.deselectAll);
	    	deselectAllButton.setTypeface(font2);
	    	deselectAllButton.setOnClickListener(new View.OnClickListener() {

		        public void onClick(View v) {

		        	Iterator<Block> it = bList.iterator(); 
			    	while(it.hasNext())
			    	{
			    		b = it.next();
			    		if(b.getOrderID() > 1){
			    			b.setChecked(0);
			    			MainActivity.db.putBlock(b);
			    		}			    		
			    	}
			    	setAllChecks(0);
			    	iterateBlocks(dateString);
		        }
		    });
		}
		
		private void setAllChecks(int value){
			for(int i = 0; i < blockStatesList.size();i++){
				
				if(!(bList.get(i).getOrderID() <= 1)){
					blockStatesList.set(i, value);
					bList.get(i).setChecked(value);
				}
				else
					bList.get(i).setChecked(0);
									
			}
			
			listAdapter.setBlockStatesList(blockStatesList);
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
		 * Sets the day to one day in the future.
		 * Also resets some variables to the new day.
		 */
		public void nextDate() {
			// Define UI-element to be animated
			final View view = (View)findViewById(R.id.checkList);
			// Define animation
			final Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_left);
			// Define animation listener
			AnimationListener animListener = new AnimationListener() {
				// onAnimationEnd callback
				public void onAnimationStart(Animation animation) {}
			    public void onAnimationRepeat(Animation animation) {}
			    public void onAnimationEnd(Animation animation) {
					start += 86400000;
					stop += 86400000;
					dateString = (dateFormat.format(start));
	        		iterateBlocks(dateString);
			    }
			};
			// Set animation listener
			anim.setAnimationListener(animListener);
			// Start animation
			view.startAnimation(anim);
		}
	
		/**
		 * Sets the day to one day in the past.
		 * Also resets some variables to the new day.
		 */
		public void prevDate() {
			// Define UI-element to be animated
			final View view = (View)findViewById(R.id.checkList);
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
					dateString = (dateFormat.format(start));
	        		iterateBlocks(dateString);
			    }
			};
			// Set animation listener
			anim.setAnimationListener(animListener);
			// Start animation
			view.startAnimation(anim);
		}
		
		public void iterateBlocks(String dateString)
		{
			blockList.clear();
			//Iterate through the blocks
			bList = MainActivity.db.getBlocksBetweenDate(start, stop);
			blockStatesList.clear();
			hoursDay=0;
			minutesDay=0;
	    	
			Iterator<Block> it = bList.iterator(); 
	    	while(it.hasNext())
	    	{
	    			
	    		b = it.next();
	    		if(b.getStop()!=0)
	    		{
	    			s="";
	    			if(b.getChecked() == 1) {
	    				s += Html.fromHtml((String)"&#10003;").toString();
	    				s += " ";
	    			}
	    			
		    		s += new SimpleDateFormat("HH:mm").format(b.getStart());
		    		s += " - ";
		    		s += new SimpleDateFormat("HH:mm").format(b.getStop());	    			
		    		if(b.getOrderID()!=0){
		    			Order order = MainActivity.db.getOrder(b.getOrderID());
		    			s += " - " + order.getOrderName();
		    		}
		    		
		    		s += " - " + b.printTime();

		    		
		    	    //adds the hours and minutes of a block to hoursDay and minutesDay
		    		
		    		if(b.getChecked()==1){
			    		if(b.getStop()!=0)
			    			timeDiff = b.getStop()-b.getStart();
			    		else
			    			timeDiff = System.currentTimeMillis() - b.getStart();
			    		
			    		timeDiff -= 1000*60*60;
			    		
			    		Date date = new java.util.Date(timeDiff);
			    	    
			    	    hoursDay += date.getHours();
			    	    minutesDay += date.getMinutes();
			    	    if(minutesDay > 60){
			    	    	minutesDay = minutesDay % 60;
			    	    	hoursDay++;
			    	    }
		    		}

		    	    if(b.toDateString().equals(dateString))
		    	    {
		    	    	blockList.add(s);
	
		    	    	if(!(b.getOrderID() <= 1)){
		    	    		blockStatesList.add(b.getChecked());
		    	    	}
		    	    	else{
		    	    		blockStatesList.add(2);
		    	    	}
		    	    	
		    	    }
	    		}
	    	}	    	

	    	listAdapter = new CustomListAdapter1(this,R.layout.listrow, blockList, blockStatesList, "neosanslight.ttf");
	    	l_view.setAdapter(listAdapter);	    	
	    	l_view.setOnItemClickListener(new OnItemClickListener()
	    	{
	    		@Override 
	    		public void onItemClick(AdapterView<?> adapter, View v, final int position, long arg3)
	    		{
	    			String value = (String)adapter.getItemAtPosition(position);
	    			try{
	    				if(!(bList.get(position).getOrderID() <= 1)){
	    					bList.get(position).setChecked(switchChecked(bList.get(position).getChecked()));	    				
		    				blockStatesList.set(position, bList.get(position).getChecked());
		    				listAdapter.setBlockStatesList(blockStatesList);
		    				Log.e("Changed item checked to: ", Integer.toString(bList.get(position).getChecked()));
		    				MainActivity.db.putBlock(bList.get(position));
	    				}
	    				else{
	    					AlertDialog.Builder noOrderNrDialogBuilder = new AlertDialog.Builder(l_view.getContext());
	    					noOrderNrDialogBuilder.setMessage("Denna post saknar ordernummer, vill du åtgärda detta?");
	    					noOrderNrDialogBuilder.setCancelable(true);
	    					noOrderNrDialogBuilder.setPositiveButton("Ja", 
	    							new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Block block = bList.get(position);
											Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
			            		        	
			            		        	i.putExtra("Block", block);
			            		        	i.putExtra("Caller", "Checkview");
			            		        	        	
			            		        	startActivity(i);											
										}
									});
	    					
	    					noOrderNrDialogBuilder.setNegativeButton("Nej", 
	    							new DialogInterface.OnClickListener() {										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.cancel();
										}
									});
	    					AlertDialog alertNoOrder = noOrderNrDialogBuilder.create();
	    					alertNoOrder.show();
	    				}
	    				
	    				
	    			}
	    			catch (Exception e){
	    				Log.e("Något blev fel! At position:",Integer.toString(position));
	    			}
	    			Log.e("Värde i listan: ",value);
	    			Log.e("Position i listan: ",Integer.toString(position));
	    			iterateBlocks(getDateString());
	    			
	    		}
	    	});
	    	
	    	// Send the user to NewOrderActivity on longclick.
	    	l_view.setLongClickable(true);
	    	l_view.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int pos, long id) {
					Block block = bList.get(pos);
					Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
		        	
		        	i.putExtra("Block", block);
		        	i.putExtra("Caller", "Checkview");
		        	        	
		        	startActivity(i);
					return false;
				}
	        }); 
     
	    	day.setText(dateString);
	    	s = " Sammanlagd tid: " + hoursDay + "h " + minutesDay + "m";
	    	total.setText(s);
		}
		
		private String getDateString(){
			return dateString;			
		}
		
		private int switchChecked (int value){
			if(value > 0){
				return 0;
			}
			else{
				return 1;
			}
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
		 * Updates values depending on what's chosen in the datepickerfragment. 
		 */
		@Override
		public void update(PickFragment p, Object o) {
			if(o instanceof Block){
				Block temp = (Block) o;
				long startOfDay = temp.getStart() - (temp.getStart() % 86400000);
				if(startOfDay < today){
					setStartAndStop(temp);
					dateString = (dateFormat.format(start));
					iterateBlocks(dateString);
				} else
					showWrongtimeToast(temp);
			}
		}
		/**
		 * Showing a toast when the user tried to input future dates with the picker.
		 * @param b The timeblock
		 */
		private void showWrongtimeToast(Block b){
			String text = "Invalid date! " + b.toDateString() + " is in the future!";
			
			Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
			toast.show();
		}
		
	}

