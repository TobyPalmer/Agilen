package com.example.timemanagement;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;
import com.example.timemanagement.model.UserDetails;
import com.example.timemanagement.statistics.DatePickerFragment;
import com.example.timemanagement.statistics.OrderListStatisticsFragment;
import com.example.timemanagement.statistics.OrderTimeDetails;
import com.example.timemanagement.statistics.PickerFragment;
import com.example.timemanagement.statistics.PieChartFragment;
import com.example.timemanagement.statistics.PieDetailsItem;
import com.example.timemanagement.statistics.TDPassable;
import com.example.timemanagement.statistics.View_PieChart;
import com.example.timemanagement.statistics.timeHM;
import com.example.timemanagement.sqlite.SQLiteMethods;
import com.example.timemanagement.customadapters.*;
import com.example.timemanagement.statistics.*;


public class StatisticsActivity extends FragmentActivity implements TDPassable 
{	

	public static final String DIRECT_TIME = "Direkttid: ";
	public static final String INDIRECT_TIME = "Interntid: ";
	public static final String TOTAL_TIME = "Totaltid: ";
	public static final String FLEX_TIME = "Flexbank: ";
	public static final String FLEX_TIME_MINUS = " Minus";
	public static final String FLEX_TIME_PLUS = " Plus";
	public static final String DIRECT_TIME_PIE_LABEL = " Direkttid";
	public static final String INDIRECT_TIME_PIE_LABEL = " Interntid";
	public static final int DIRECT_TIME_PIE_CHART_COLOR =  Color.parseColor("#57BF23");
	public static final int INDIRECT_TIME_PIE_CHART_COLOR =  Color.parseColor("#F88621");
	public static final int PIE_CHART_SIZE =  255;
	public static final int PIE_CHART_COLOR =  Color.TRANSPARENT;

	
	//Variables for the paging function, PieChart and Orderlist statistics
	private static final int NUM_PAGES = 2;	
	private ViewPager mPager;
	private ScreenSlidePagerAdapter mPagerAdapter;	
	
	//Buttons to pick the date. These will ultimately set start and stop that are in milliseconds Unixdate
	private Button dateStartButton, dateStopButton;	
	private long start, stop;		
	
	//Sets the printed precision of percentages
	public static DecimalFormat df = new DecimalFormat("#0.00");
	
	/**
	 * Public variables that are used to calculate the different times.
	 * As well as to store the orders for the Order Statistics.
	 * These are accessed from both the pie chart and order list fragments	 
	 */
	public static long directTime = 0;
	public static long indirectTime = 0;
	public static long flexTime = 0;
	public ArrayList<OrderTimeDetails> orderTimeDetailsDirect = new ArrayList<OrderTimeDetails>();
	public ArrayList<OrderTimeDetails> orderTimeDetailsIndirect = new ArrayList<OrderTimeDetails>();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);		
		
		/**
		 * Retrieve where the pager will go, and initiate it with a custom 
		 * ScreenSlidePagerAdapter
		 * This is what handles the fragment pages.
		 */
		mPager = (ViewPager)findViewById(R.id.statistics_pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		
		/**
		 * This will take todays date, figure out which week it is, and then
		 * set the starting date to the first day of the week. In this case
		 * monday.
		 */
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
    	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);		
		    	   	
		start = getStartOfDay(cal.getTimeInMillis());				
    	stop = getEndOfToday(); 
    	
    	
		
    	/**
    	 * As a precaution if, for some reason start becomes bigger than stop,
    	 * it becomes the same as stop
    	 */
    	if(start > stop){
    		start = getStartOfDay(stop);
    	}
    	
    	/**
    	 * Bind the date buttons and set their texts
    	 */
    	dateStartButton = (Button)findViewById(R.id.startDate);
    	dateStartButton.setText(dateAsString(start));
    	
    	dateStopButton = (Button)findViewById(R.id.stopDate);
    	dateStopButton.setText(dateAsString(stop));
    	
    	TextView arrow = (TextView)findViewById(R.id.arrow);
    	
    	Typeface font2 = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
    	Typeface font_for_icon = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
    	
    	dateStartButton.setTypeface(font2);
    	dateStopButton.setTypeface(font2);
    	arrow.setTypeface(font_for_icon);
    	
    	/**
    	 * As a precaution if a user doesn't log in but instead
    	 * goes directly to this activity, currentUser will be initiated with the 
    	 * default user
    	 */
    	if(MainActivity.currentUser == null){
    		MainActivity.currentUser = new UserDetails("Test", MainActivity.DEFAULT_WORKDAY);
    	}
    	
    	/**
    	 * Make the calculations 
    	 */
		calculateStatistics();		
	}
	
		
	/**
	 * This function disables the back button when swiping to next view, if we are standing
	 * on the first fragment the back button works as expected	
	 */
	@Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
	
	/**
	 * This function updates the Orders List Statistics View
	 */
	private void updateOrderLists(){
		
		// Bind to the ListViews in the Layout
		ListView directList = (ListView)findViewById(R.id.statistics_direct_time_list);
		ListView indirectList = (ListView)findViewById(R.id.statistics_indirect_time_list);
		
		// Create Custom List Adapters for the ListViews and set them to the two ListViews
		CustomListAdapterStatistics directAdapter = new CustomListAdapterStatistics(this, R.layout.order_list_item, orderTimeDetailsDirect);        
        directList.setAdapter(directAdapter);
        
        CustomListAdapterStatistics indirectAdapter = new CustomListAdapterStatistics(this, R.layout.order_list_item, orderTimeDetailsIndirect);        
        indirectList.setAdapter(indirectAdapter);
	}
	
	/**
	 * This function updates the information in the Pie Chart View, namely the text,
	 * such as Direct & Indirect time, flex time and total time along with percentages if applicable
	 */
	private void updateTextFields(){
		
		TextView total, direct, indirect, flex;		
		
		total = (TextView)findViewById(R.id.totalTime);		
		direct = (TextView)findViewById(R.id.directTime);
		indirect = (TextView)findViewById(R.id.indirectTime);
		flex = (TextView)findViewById(R.id.flexTime);
		
		String t,d,i,f;
		t = TOTAL_TIME + new timeHM(directTime+indirectTime).toString();
		d = DIRECT_TIME + new timeHM(directTime).toString() + " " + df.format(procent(directTime + indirectTime, directTime)) + " %";
		i = INDIRECT_TIME + new timeHM(indirectTime).toString() + " " + df.format(procent(directTime + indirectTime, indirectTime)) + " %";
		if(flexTime >= 0){
			f = FLEX_TIME + new timeHM(Math.abs(flexTime)).toString() + FLEX_TIME_PLUS;
		}
		else{
			f = FLEX_TIME + new timeHM(Math.abs(flexTime)).toString() + FLEX_TIME_MINUS;
		}
				

		total.setText(t);
		direct.setText(d);
		indirect.setText(i);
		flex.setText(f);
		
		Typeface font2 = Typeface.createFromAsset(getAssets(), "neosanslight.ttf");
		total.setTypeface(font2);
		direct.setTypeface(font2);
		indirect.setTypeface(font2);
		flex.setTypeface(font2);
	}
	

	/**
	 * This function creates and updates the Pie Chart
	 */
	private void updatePieChart() {
		List<PieDetailsItem> piedata = new ArrayList<PieDetailsItem>(0);		
		PieDetailsItem item;
		  int maxCount = 0;
		  int itemCount = 0;
		  timeHM dT,iT;
		  dT = new timeHM(directTime);
		  iT = new timeHM(indirectTime);
		  int items[] = { dT.getTotalMinutes(), iT.getTotalMinutes() };
		  int colors[] = { DIRECT_TIME_PIE_CHART_COLOR, INDIRECT_TIME_PIE_CHART_COLOR };		  
		  String itemslabel[] = { DIRECT_TIME_PIE_LABEL, INDIRECT_TIME_PIE_LABEL };
		  for (int i = 0; i < items.length; i++) {
			   itemCount = items[i];
			   item = new PieDetailsItem();
			   item.count = itemCount;
			   item.label = itemslabel[i];
			   item.color = colors[i];
			   piedata.add(item);
			   maxCount = maxCount + itemCount;
		  }
		  int size = PIE_CHART_SIZE;
		  int BgColor = PIE_CHART_COLOR;
		  Bitmap mBaggroundImage = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		  View_PieChart piechart = new View_PieChart(this);
		  piechart.setLayoutParams(new LayoutParams(size, size));
		  piechart.setGeometry(size, size, 2, 2, 2, 2, 2130837504);
		  piechart.setSkinparams(BgColor);
		  piechart.setData(piedata, maxCount);
		  piechart.invalidate();
		  piechart.draw(new Canvas(mBaggroundImage));
		  piechart = null;
		  ImageView mImageView = new ImageView(this);
		  mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
		    LayoutParams.WRAP_CONTENT));
		  mImageView.setBackgroundColor(BgColor);
		  mImageView.setImageBitmap(mBaggroundImage);
		  LinearLayout finalLayout = (LinearLayout) findViewById(R.id.pie_container);
		  finalLayout.removeAllViews();
		  finalLayout.addView(mImageView);
}
	
	/**
	 * This function returns the beginning of today, hence at hour 00:00
	 * @return Returns a Unix time in Milliseconds as a Long
	 */
	private long getStartOfToday(){		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.getInstance().get(Calendar.YEAR), 
					Calendar.getInstance().get(Calendar.MONTH), 
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 0, 0);
		return cal.getTimeInMillis();
	}
	
	/**
	 * This function returns the start of a provided day. Hence hour 00:00
	 * @param time The date as unix time (Milliseconds). As a Long.
	 * @return Returns a Unix time date as a Long
	 */
	private long getStartOfDay(long time){		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(cal.get(Calendar.YEAR), 
					cal.get(Calendar.MONTH), 
						cal.get(Calendar.DAY_OF_MONTH), 0, 0);
		return cal.getTimeInMillis();
	}
	
	/**
	 * Returns the end of today at 23:59 in Unix Time (Milliseconds) As a Long
	 * @return Unix time, as Long
	 */
	private long getEndOfToday(){		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.getInstance().get(Calendar.YEAR), 
					Calendar.getInstance().get(Calendar.MONTH), 
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 23, 59);
		return cal.getTimeInMillis();
	}
	
	/**
	 * Returns the end of the provided date at 23:59. As a Long
	 * @param time Unix time as a Long
	 * @return End of today as a Unix time Long
	 */
	private long getEndOfDay(long time){		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(cal.get(Calendar.YEAR), 
					cal.get(Calendar.MONTH), 
						cal.get(Calendar.DAY_OF_MONTH), 23, 59);
		return cal.getTimeInMillis();
	}

	
	/**
	 * Returns a formated date as yyyy-MM-dd from a unix time
	 * @param time unix time as a Long
	 * @return Date formated as yyyy-MM-dd
	 */

	public String dateAsString(long time){
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	
	/**
	 * This functions starts a fragment pop-up in-order to change the start date
	 * @param view 
	 */
	public void changeStartDate(View view){
		// DatePickerFragment gets the start time in unix time as a Long as well as an ID to be used later
	    DialogFragment newFragment = new DatePickerFragment(start, 0);
	    newFragment.show(getFragmentManager(), "datePicker");	    
	}
	
	/**
	 * This functions starts a fragment pop-up in-order to change the stop date
	 * @param view 
	 */
	public void changeStopDate(View view){
	    DialogFragment newFragment = new DatePickerFragment(stop, 1);
	    newFragment.show(getFragmentManager(), "datePicker");	    
	}
	
	/**
	 * This function updates the times chosen and also does corrections for weird selections
	 */
	@Override
	public void update(PickerFragment p, long o, int ID) {
		
		// If we get an instance of our fragment we perform the following
		if(p instanceof DatePickerFragment)  {			
			long changedDate = o;
				// ID identifies if it was the start or stop that was triggered
			  if(ID == 1){				  
				  stop = getEndOfDay(changedDate);			  
				  if((compareDates(stop, start)  >= 0) && compareToFutureDate(stop) < 1){				  
					  dateStopButton.setText(dateAsString(stop));
				  }
				  else{
					  if(compareToFutureDate(stop) < 1){
						  stop = start;					  
					  }
					  else{					  
						  stop = getCurrentMillis();					  
					  }
					  dateStopButton.setText(dateAsString(stop));
				  }			  
			  }			  
			  else{
				  start = getStartOfDay(changedDate);			  
				  if((compareDates(start, stop) < 1) && compareToFutureDate(start) < 1){				  
					  dateStartButton.setText(dateAsString(start));
				  }
				  else{
					  if(compareToFutureDate(start) < 1){
						  start = stop;					  
					  }
					  else{					  
						  start = getCurrentMillis();					  
					  }
					  dateStartButton.setText(dateAsString(start));
				  }
			  }
			  // After the selection has been made update everything
			  calculateStatistics();
			  updatePieChart();
			  updateTextFields();
			  updateOrderLists();
		}		  
	}
	/**
	 * This will return the current time instance in milliseconds as a Long
	 * @return Unix time Long
	 */
	public long getCurrentMillis(){
		return Calendar.getInstance().getTimeInMillis();
	}
	
	/**
	 * This function checks if one unix time is before, after or equal
	 * @param b1 Unix time as Long
	 * @param b2 Unix time as Long
	 * @return 0 if equal, -1 if b2 is before b1 and 1 if b2 is after b1
	 */
	public int compareDates(long b1, long b2){
		Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(b1);
		Calendar d2 = Calendar.getInstance();
		d2.setTimeInMillis(b2);
		
		return d1.compareTo(d2);
	}
	
	/**
	 * This function compares a date to the current time instance
	 * @param b1 unix time as a Long
	 * @return 0 if equal, -1 if b1 is before current time instance, 1 if after current time instance
	 */
	public int compareToFutureDate(long b1){
		Calendar currentDate = Calendar.getInstance();
		Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(b1);
		
		return d1.compareTo(currentDate);		
	}
	
	/**
	 * This function calculates the different statistics as total times,
	 * Direct and Indirect times, flex time and also populates two lists
	 * containing OrderTimeDetails to be used in the Order List Statistics
	 * View
	 */
	public void calculateStatistics()
	{	
		
		Order o;
		Block b;
		List<Order> oList = new ArrayList<Order>();	
		
		// Fetch all orders 
		oList = MainActivity.db.getAllOrders();				
				
		directTime = 0;
		indirectTime = 0;
		
		// Clear the lists
		orderTimeDetailsDirect.clear();
		orderTimeDetailsIndirect.clear();
				
		Iterator<Order> oit = oList.iterator();
		while(oit.hasNext()){
			o = oit.next();
			/**
			 * Create an OrderTImeDetails object that will store
			 * the order & totalTime.
			 */
			OrderTimeDetails otd;
			otd = new OrderTimeDetails(o);
			/**
			 * Create a list of blocks between the selected dates for a specific order
			 */
			List<Block> bList;
			bList =  MainActivity.db.getBlocksBetweenDate(o.getID(), start, stop);
			
			Iterator<Block> bit = bList.iterator();			
			while(bit.hasNext()){
				b = bit.next();
				if(b.getStop() != 0){
					long timeDiff = b.getStop() - b.getStart(); 	// Calculate the difference
					otd.totalTime += timeDiff; 	// Add the difference to a total for this specific order
				}
			}
			
			/**
			 * Separate the orders in either Direct or Indirect work
			 */
			if(o.getOrderDirectWork() == 1){				
				directTime += otd.totalTime;	// Add Every orders total time to a global total
				otd.updateTime();	// Update the internal time in object OrderTimeDetails
				orderTimeDetailsDirect.add(otd); 	// Add the object to a list of direct work orders
			}
			else{
				indirectTime += otd.totalTime;
				otd.updateTime();
				orderTimeDetailsIndirect.add(otd);
			}					
		}
				
		/**
		 * This next part calculates the users flex time
		 * If the user has worked less than a normal workday hours is taken 
		 * off the flex time, if the user has worked more than a normal workday
		 * time will be added to the flex time 
		 */
		long s = getStartOfDay(start);
		long e = getEndOfDay(start);
		long step = 1000*60*60*24; // One full day in unix time (Milliseconds)			
		long overTime = 0;	// Initialize the overTime to zero before calculations
		List<Block> blockList;
		
		// As long as start is less than stop continue counting flex time
		while(s < stop){
			blockList =  MainActivity.db.getBlocksBetweenDate(s,e);	// Get all time blocks
			long time = 0;
			Iterator<Block> bit = blockList.iterator();
			while(bit.hasNext()){
				b = bit.next();				
				if(b.getStop() != 0){
					long timeDiff = b.getStop() - b.getStart(); 
					time += timeDiff;
				}
			}
			
			overTime += time - MainActivity.currentUser.getWorkday();				
			
			// Jump to next day
			s += step;
			e += step;
		}		
		flexTime = overTime;
	}
	
	/**
	 * Function that takes two times in unix time (milliseconds) returns a percentage
	 * @param total is the total time
	 * @param part is a part of the total time
	 * @return Function returns the percentage as a double 
	 */
	double procent(long total, long part){
		double t = total;
		double p = part;
		
		if(total != 0){			
			return ((p/t) * 100);
		}
		return 0;		
	}
	
	/**
	 * Function that counts how many full days are between two unix times
	 * @param start Start time in unix time as a Long. (Milliseconds)
	 * @param stop Stop time in unix time as a Long. (Milliseconds)
	 * @return Returns the number of days as an Int
	 */
	int getNumberOfDays(long start, long stop){
		int count = 0;
		long s = start;		
		while(s < stop){
			s += 1000*60*60*24;
			count++;
		}
		return count;
	}
	
	/**
	 * This is a Screen Slider Adapter that makes it possible to slide in
	 * screens. For this Activity there are two screens the Pie Chart Screen
	 * with the Direct and Indirect total times with percentages,
	 * and the Order List Statistics Screen which shows two lists of the different
	 * orders and their total times.
	 * @author Marcus
	 *
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        
		private PieChartFragment pieChart;
		private OrderListStatisticsFragment orderlist;
		
		public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm); 
            pieChart = new PieChartFragment();
            orderlist = new OrderListStatisticsFragment();
        }
				
        @Override
        public Fragment getItem(int position) {
        	if(position == 1){        		
        		return orderlist;
        	}
        	else{        		
        		return pieChart;
        	}
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

