package com.example.timemanagement.customadapters;

import java.util.ArrayList;
import java.util.List;

import com.example.timemanagement.model.Notification;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;




public class CustomListAdapter2 extends ArrayAdapter<String>{
	Context context; 
	Typeface tf;
	
	public CustomListAdapter2(Context context, int resID, ArrayList<String> items, String font){
		super(context, resID, items);
	    this.context = context;
	    tf = Typeface.createFromAsset(context.getAssets(), font);	
	}
	
	
	
	public CustomListAdapter2(Context context2, int resID, String[] orderList, String font) {
		// TODO Auto-generated constructor stub
		super(context2, resID, orderList);
	    this.context = context2;
	    tf = Typeface.createFromAsset(context.getAssets(), font);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = super.getView(position, convertView, parent);
		//TextView text = (TextView)convertView.findViewById(R.id.rowTextView2);
		((TextView)v).setTypeface(tf);
		return v;
		
	}
	
	/*public void setBlockStatesList(ArrayList<Integer> bList){
		this.blockStatesList = bList;
	}	*/
	
}