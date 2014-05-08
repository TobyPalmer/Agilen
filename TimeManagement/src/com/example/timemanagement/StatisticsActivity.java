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

public class StatisticsActivity extends MainActivity 
{	
	private TextView total;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		setupActionBar();
		
		// TESTAR TESTAR TESTAR TESTAR TESTAR //
		iterateBlocks();
		
		//total = (TextView) findViewById(R.id.total);
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
	
	
	// TESTAR TESTAR TESTAR TESTAR TESTAR // 
	private long start, stop;
	private int h, m, directH, directM, indirectH, indirectM, totalH, totalM;
	private String s;
	
	public void iterateBlocks()
	{	
		Calendar cal1 = Calendar.getInstance();
		cal1.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH)-1, 
				cal1.get(Calendar.DAY_OF_MONTH), 0, 0);
		start = cal1.getTimeInMillis();
		
		stop = System.currentTimeMillis();
		
		/*
		Calendar cal2 = Calendar.getInstance();
		cal2.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), 
				cal2.get(Calendar.DAY_OF_MONTH), 23, 59);
		stop = cal2.getTimeInMillis();
		
		String temp = "start: " + cal1.getTime() + "  stop: " + cal2.getTime();
		Log.d("cal.getTime()", temp);
		*/	
		
		final List<Block> blocks = MainActivity.db.getBlocksBetweenDate(start, stop);
		
		String[] items = new String[blocks.size()];
		
		/*
		int tempi = items.length;
		String temps = "items.length: " + tempi;
		Log.d("TEST", temps);
		*/
		
		long timediff;
		
		for (int i = 0; i < items.length; i++) 
		{
			final Block block = blocks.get(i);
			int orderId = blocks.get(i).getOrderID();
			Order order = MainActivity.db.getOrder(orderId);
			
			if(block.getStop()!=0)
    			timediff = block.getStop()-block.getStart();
    		else
    			timediff = System.currentTimeMillis() - block.getStart();
    		
    		timediff -= 1000*60*60;
    		
    		Date date = new java.util.Date(timediff);
    	    
    	    h = date.getHours();
    	    m = date.getMinutes();
    	    if(m > 60)
    	    {
    	    	h += m/60;
    	    	m = m % 60;    	    	
    	    }
    	    
    	    totalH += h;
    	    totalM += m;
    	    
    	    if(order.getOrderDirectWork() == 1)
    	    {
    	    	directH += h;
    	    	directM += m;
    	    }
    	    else
    	    {
    	    	indirectH += h;
    	    	indirectM += m;
    	    }
    	    
    	    /*
			String s = "Direct: " + directH + "h " + directM + "m  Indirect: " + indirectH + "h " + indirectM + "m  ";
			Log.d("IN loop", s);
			*/
			
    	    s = "Arbetat tid: " + totalH + "h " + totalM + "m";
    	    //total.setText(s);
		}
	}	    	
}