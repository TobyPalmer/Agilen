package com.example.timemanagement.customadapters;

import java.util.ArrayList;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * List Adapter used for the ListActivity. This is special in that it takes a blockStatesList that can manipulate
 * How each row is presented
 * @author Marcus
 *
 */

public class CustomListAdapter1 extends ArrayAdapter<String>{
	private ArrayList<Integer> blockStatesList;
	Context context; 
	Typeface tf;
	
	public CustomListAdapter1(Context context, int resID, ArrayList<String> items, ArrayList<Integer>blockStatesList, String font){
		super(context, resID, items);
		this.blockStatesList = blockStatesList;		
	    this.context = context;
	    tf = Typeface.createFromAsset(context.getAssets(), font);	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = super.getView(position, convertView, parent);
		
		if(blockStatesList.get(position)== 1){
			((TextView)v).setBackgroundColor(Color.parseColor("#57bf23"));
		}
		else if(blockStatesList.get(position)== 2){
			((TextView)v).setBackgroundColor(Color.parseColor("#fcfdb4"));
		}	
		else{
			((TextView)v).setBackgroundColor(Color.parseColor("#cff9b9"));
		}
		((TextView)v).setTypeface(tf);
		return v;
	}
	
	public void setBlockStatesList(ArrayList<Integer> bList){
		this.blockStatesList = bList;
	}	
	
}