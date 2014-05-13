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

import com.example.timemanagement.model.Block;
import com.example.timemanagement.model.Order;
import com.example.timemanagement.sqlite.SQLiteMethods;
import com.example.timemanagement.customadapters.*;
import com.example.timemanagement.statistics.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class StatisticsActivity extends MainActivity implements DatePassable 
{	
	private TextView total;
	private Block startBlock;
	private Block stopBlock;
	
	private long start, stop;
	
	private Button dateStartButton,				   
				   dateStopButton;
	
	private List<Order> oList = new ArrayList<Order>();
	private List<OrderTimeDetails> orderTimeDetailsDirect = new ArrayList<OrderTimeDetails>();
	private List<OrderTimeDetails> orderTimeDetailsIndirect = new ArrayList<OrderTimeDetails>();
	
	private Order o;
	private Block b;
	private long directTime;
	private long indirectTime;
	private long flexTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		setupActionBar();
		
		Calendar cal = Calendar.getInstance();
    	
		start = getStartOfToday();
		
    	stop = getEndOfToday();    	
		
    	dateStartButton = (Button)findViewById(R.id.startDate);
    	dateStartButton.setText(dateAsString(start));
    	
    	dateStopButton = (Button)findViewById(R.id.stopDate);
    	dateStopButton.setText(dateAsString(stop));
		
    	
    	
		
    	
    	// TESTAR TESTAR TESTAR TESTAR TESTAR //
		iterateBlocks();
		createPieChart();
		updateTextFields();
		//total = (TextView) findViewById(R.id.total);
	}
	
	private void updateTextFields(){
		TextView total, direct, indirect, flex;
		total = (TextView)findViewById(R.id.totalTime);
		direct = (TextView)findViewById(R.id.directTime);
		indirect = (TextView)findViewById(R.id.indirectTime);
		flex = (TextView)findViewById(R.id.flexTime);
		
		String t,d,i,f;
		t = "Totaltid: " + new timeHM(directTime+indirectTime).toString();
		d = "Direkttid: " + new timeHM(directTime).toString();
		i = "Indirekttid: " + new timeHM(indirectTime).toString();
		f = "Flextid: " + new timeHM(flexTime).toString();
		
		total.setText(t);
		direct.setText(d);
		indirect.setText(i);
		flex.setText(f);
	}
	
	
	
	private void createPieChart(){
			List<PieDetailsItem> piedata = new ArrayList<PieDetailsItem>(0);		
			PieDetailsItem item;
			  int maxCount = 0;
			  int itemCount = 0;
			  timeHM dT,iT;
			  dT = new timeHM(directTime);
			  iT = new timeHM(indirectTime);
			  int items[] = { dT.getHours(), iT.getHours() };
			  int colors[] = { Color.BLUE, Color.GREEN };
			  
			  String itemslabel[] = { " Direkttid", " Indirekttid" };
			  for (int i = 0; i < items.length; i++) {
				   itemCount = items[i];
				   item = new PieDetailsItem();
				   item.count = itemCount;
				   item.label = itemslabel[i];
				   item.color = colors[i];
				   piedata.add(item);
				   maxCount = maxCount + itemCount;
			  }
			  int size = 255;
			  int BgColor = Color.TRANSPARENT;
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
			  finalLayout.addView(mImageView);
	}
	
	private void updatePieChart(){
		List<PieDetailsItem> piedata = new ArrayList<PieDetailsItem>(0);		
		PieDetailsItem item;
		  int maxCount = 0;
		  int itemCount = 0;
		  timeHM dT,iT;
		  dT = new timeHM(directTime);
		  iT = new timeHM(indirectTime);
		  int items[] = { dT.getHours(), iT.getHours() };
		  int colors[] = { Color.BLUE, Color.GREEN };		  
		  String itemslabel[] = { " Direkttid", " Indirekttid" };
		  for (int i = 0; i < items.length; i++) {
			   itemCount = items[i];
			   item = new PieDetailsItem();
			   item.count = itemCount;
			   item.label = itemslabel[i];
			   item.color = colors[i];
			   piedata.add(item);
			   maxCount = maxCount + itemCount;
		  }
		  int size = 255;
		  int BgColor = Color.TRANSPARENT;
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
	
	private long getStartOfToday(){		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.getInstance().get(Calendar.YEAR), 
					Calendar.getInstance().get(Calendar.MONTH), 
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 0, 0);
		return cal.getTimeInMillis();
	}
	
	private long getStartOfDay(long time){		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0,0);
		return cal.getTimeInMillis();
	}
	
	private long getEndOfToday(){		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.getInstance().get(Calendar.YEAR), 
					Calendar.getInstance().get(Calendar.MONTH), 
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 23, 59);
		return cal.getTimeInMillis();
	}
	
	private long getEndOfDay(long time){		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 23,59);
		return cal.getTimeInMillis();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() 
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	public String dateAsString(long time){
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	
	/**
	 * Sends the timeblock to a DatePickFragment popup.
	 * @param view
	 */
	public void changeStartDate(View view){
	    DialogFragment newFragment = new DatePickerFragment(start, 0);
	    newFragment.show(getFragmentManager(), "datePicker");	    
	}
	
	public void changeStopDate(View view){
	    DialogFragment newFragment = new DatePickerFragment(stop, 1);
	    newFragment.show(getFragmentManager(), "datePicker");	    
	}
	
	@Override
	public void update(DatePickerFragment p, long o, int ID) {		
		  long changedDate = o;		  
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
		  iterateBlocks();
		  updatePieChart();
		  updateTextFields();
	}
	
	public long getCurrentMillis(){
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public int compareDates(long b1, long b2){
		Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(b1);
		Calendar d2 = Calendar.getInstance();
		d2.setTimeInMillis(b2);
		
		return d1.compareTo(d2);
	}
	
	public int compareToFutureDate(long b1){
		Calendar currentDate = Calendar.getInstance();
		Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(b1);
		
		return d1.compareTo(currentDate);		
	}
	
	
	public void iterateBlocks()
	{			
		
		oList = MainActivity.db.getAllOrders();		
		Iterator<Order> oit = oList.iterator();
		int size = getNumberOfDays(start,stop);
		List<Long> overtimeList = new ArrayList<Long>(size);
		
		directTime = 0;
		indirectTime = 0;
		
		
		
		while(oit.hasNext()){
			o = oit.next();
			OrderTimeDetails otd;
			otd = new OrderTimeDetails(o);
			List<Block> bList;
			bList =  MainActivity.db.getBlocksBetweenDate(o.getID(), start, stop);
			Iterator<Block> bit = bList.iterator();
			
			while(bit.hasNext()){
				b = bit.next();
				if(b.getStop() != 0){
					long timeDiff = b.getStop() - b.getStart(); //diff i millisekunder
					otd.totalTime += timeDiff; //L�gg ihop tiden f�r samtliga block som har en stopptid
				}
			}
			
			if(o.getOrderDirectWork() == 1){
				directTime += otd.totalTime;
				orderTimeDetailsDirect.add(otd);
			}
			else{
				indirectTime += otd.totalTime;
				orderTimeDetailsIndirect.add(otd);
			}					
		}
		
		
		// �r inte stolt �ver detta men det var mest tidseffektivt, it works :)
		long s = getStartOfDay(start);
		long e = getEndOfDay(start);
		long step = 1000*60*60*24;
		long workDay = 1000*60*60*8;
		
		List<Block> tempList;
		long overTime = 0;
		while(s < stop){
			tempList =  MainActivity.db.getBlocksBetweenDate(s,e);
			long time = 0;
			Iterator<Block> bit = tempList.iterator();
			while(bit.hasNext()){
				b = bit.next();				
				if(b.getStop() != 0){
					long timeDiff = b.getStop() - b.getStart(); //diff i millisekunder
					time += timeDiff; //L�gg ihop tiden f�r samtliga block som har en stopptid
				}
			}
			if(time > workDay){
				overTime += time - workDay; 
			}
			
			s += step;
			e += step;
		}
		
		flexTime = overTime;
		timeHM a,b,c,d;
		
		a = new timeHM(directTime);
		b = new timeHM(indirectTime);
		c = new timeHM(directTime + indirectTime);
		d = new timeHM(overTime);
		
		Log.e("Direct: ", a.toString());
		Log.e("INDirect: ", b.toString());
		Log.e("TOTALDirect: ", c.toString());
		Log.e("Overtime: ", d.toString());
		Log.e("Number of days: ", Integer.toString(getNumberOfDays(start,stop)));
		
	}
	
	int getNumberOfDays(long start, long stop){
		int count = 0;
		long s = start;		
		while(s < stop){
			s += 1000*60*60*24;
			count++;
		}
		return count;
	}
}

