
package com.example.timemanagement;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import android.content.*;
import android.annotation.TargetApi;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.timemanagement.R.color;
import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;
import com.example.timemanagement.sqlite.SQLiteMethods;
import com.example.timemanagement.R;
import com.example.timemanagement.customadapters.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;






	public class ListActivity extends MainActivity {
		
		
		private List<Block> bList = new ArrayList<Block>();
		private Date d;
		private ListView l_view;
		private TextView day, total;
		private CustomListAdapter1 listAdapter;
		private Date currentDate;
		private Button next, prev;

		private ArrayList<String> blockList;
		private ArrayList<Integer> blockStatesList;
		
		private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		private String s, dateString;
		private Block b;
		private int n = 0;

		private int hoursDay, minutesDay;
		private long today, day_next, timeDiff, stop, start;
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
		
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_list);
			// Show the Up button in the action bar.
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
			
			//Create a list of all the blocks
	    	//BList = MainActivity.db.getAllBlocks();
			bList = MainActivity.db.getBlocksBetweenDate(start, stop);
			
			l_view = (ListView) findViewById(R.id.l_view);
			day = (TextView) findViewById(R.id.day);
			total = (TextView) findViewById(R.id.total);
			
			blockList = new ArrayList<String>();
			blockStatesList = new ArrayList<Integer>();
			
			//Calendar cal = new GregorianCalendar();
			today = System.currentTimeMillis();
			currentDate = new Date(today);
			day_next = 1000 * 60 * 60 * 24;
			
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			
			d = new Date(today);
			dateString = (dateFormat.format(d));
			iterateBlocks(dateString);
			
			//Adding arrows to buttons
	    	next = (Button)findViewById(R.id.nextDay);
	    	Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
	    	next.setTypeface(font);
	    	
	    	prev = (Button)findViewById(R.id.prevDay);
	    	prev.setTypeface(font);

	    	final Button nextButton = (Button) findViewById(R.id.nextDay);
	    	nextButton.setEnabled(false);
	    	//nextButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));		
	    	nextButton.setOnClickListener(new View.OnClickListener() {

		        public void onClick(View v) {
		            // TODO Auto-generated method stub
		        	
		        	if(d.compareTo(currentDate) == 0)
		        	{
		        		nextDate();
		        		iterateBlocks(dateString);
		        		nextButton.setEnabled(false);
		        	}	
		        }
		    });
		    
		    Button prevButton = (Button) findViewById(R.id.prevDay);
		    //prevButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));		
		    prevButton.setOnClickListener(new View.OnClickListener() {

		        public void onClick(View v) {
		            // TODO Auto-generated method stub
		        	Log.e("KOmmer in hit","Noh");		        	
		        	if(d.compareTo(currentDate) != 0)
		        	{		        		
		        		nextButton.setEnabled(true);
		        	}		        	
		        	prevDate();
		        	iterateBlocks(dateString);	
		        }
		    });
		    
		    Button selectAllButton = (Button) findViewById(R.id.selectAll);	    			
	    	selectAllButton.setOnClickListener(new View.OnClickListener() {
		        public void onClick(View v) {
		            // TODO Auto-generated method stub
		        	Log.e("Button","selectAllButton");
		        	Iterator<Block> it = bList.iterator(); 
			    	while(it.hasNext())
			    	{
			    		b = it.next();
			    		if(b.getOrderID() != 0){
			    			b.setChecked(1);
			    		}			    		
			    	}
			    	setAllChecks(1);
			    	iterateBlocks(dateString);
		        }
		    });
	    	
	    	Button deselectAllButton = (Button) findViewById(R.id.deselectAll);
	    	deselectAllButton.setOnClickListener(new View.OnClickListener() {

		        public void onClick(View v) {
		            // TODO Auto-generated method stub

		        	Log.e("Button","deselectAllButton");
		        	Iterator<Block> it = bList.iterator(); 
			    	while(it.hasNext())
			    	{
			    		b = it.next();
			    		if(b.getOrderID() != 0){
			    			b.setChecked(0);
			    		}			    		
			    	}
			    	setAllChecks(0);
			    	iterateBlocks(dateString);
		        }
		    });
		}
		
		private void setAllChecks(int value){
			for(int i = 0; i < blockStatesList.size();i++){
				blockStatesList.set(i, value);				
			}
			listAdapter.setBlockStatesList(blockStatesList);
		}
		
		public void nextDate()
		{
			today += day_next;
			d = new Date(today);
			Log.d("Datum", d.toString());
			dateString = (dateFormat.format(d));
		}
	
		public void prevDate()
		{
			today -= day_next;
			d = new Date(today);
			Log.d("Datum", d.toString());
			dateString = (dateFormat.format(d));
		}
		
		public void iterateBlocks(String dateString)
		{
			blockList.clear();
			//Iterate through the blocks
			hoursDay =  minutesDay = 0;
	    	
			Iterator<Block> it = bList.iterator(); 
	    	while(it.hasNext())
	    	{
	    		b = it.next();
	    		if(b.getStop()!=0)
	    		{
		    		s = new SimpleDateFormat("HH:mm").format(b.getStart());
		    		s += " - ";
		    		s += new SimpleDateFormat("HH:mm").format(b.getStop());	    			
		    		if(b.getOrderID()!=0){
		    			Order order = MainActivity.db.getOrder(b.getOrderID());
		    			s += " - " + order.getOrderName();
		    		}
		    		
		    		s += " - " + b.printTime();
		    		
		    		if(b.getChecked() == 1){
		    			s += " - Checked! ";
		    		}
		    		
		    	    //adds the hours and minutes of a block to hoursDay and minutesDay
		    		if(b.getStop()!=0)
		    			timeDiff = b.getStop()-b.getStart();
		    		else
		    			timeDiff = System.currentTimeMillis() - b.getStart();
		    		
		    		timeDiff -= 1000*60*60;
		    		
		    		Date date = new java.util.Date(timeDiff);
		    	    
		    	    hoursDay += date.getHours();
		    	    minutesDay += date.getMinutes();
		    	    if(minutesDay > 60){
		    	    	hoursDay += minutesDay/60;
		    	    	minutesDay = minutesDay % 60;    	    	
		    	    }
	    	    
	    	
		    	    //s += "Total time: " + hoursDay + "h " + minutesDay + "m";
		    		
		    		
		    	    if(b.toDateString().equals(dateString))
		    	    {
		    	    	blockList.add(s);
	
		    	    	if(!(b.getOrderID() == 0)){
		    	    		blockStatesList.add(b.getChecked());
		    	    	}
		    	    	else{
		    	    		blockStatesList.add(2);
		    	    	}
		    	    	
		    	    }
	    		}
	    	}
	    	
	    	//listAdapter = new ArrayAdapter<String>(this,R.layout.listrow, blockList);
	    	listAdapter = new CustomListAdapter1(this,R.layout.listrow, blockList, blockStatesList);
	    	l_view.setAdapter(listAdapter);	    	
	    	l_view.setOnItemClickListener(new OnItemClickListener()
	    	{
	    		
	    		@Override 
	    		public void onItemClick(AdapterView<?> adapter, View v, final int position, long arg3)
	    		{
	    			String value = (String)adapter.getItemAtPosition(position);
	    			try{
	    				if(!(bList.get(position).getOrderID() == 0)){
	    					bList.get(position).setChecked(switchChecked(bList.get(position).getChecked()));	    				
		    				blockStatesList.set(position, bList.get(position).getChecked());
		    				listAdapter.setBlockStatesList(blockStatesList);
		    				Log.e("Changed item checked to: ", Integer.toString(bList.get(position).getChecked()));
		    				MainActivity.db.putBlock(bList.get(position));
	    				}
	    				else{
	    					AlertDialog.Builder noOrderNrDialogBuilder = new AlertDialog.Builder(l_view.getContext());
	    					noOrderNrDialogBuilder.setMessage("Denna posten saknar order nr!\nVill du redigera detta?");
	    					noOrderNrDialogBuilder.setCancelable(true);
	    					noOrderNrDialogBuilder.setPositiveButton("Ja", 
	    							new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Block block = bList.get(position);
											Intent i = new Intent(getApplicationContext(), NewOrderActivity.class);
			            		        	
			            		        	i.putExtra("Block", block);
			            		        	i.putExtra("String", "editBlock");
			            		        	        	
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
     
	    	day.setText(dateString);
	    	
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
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
	}

