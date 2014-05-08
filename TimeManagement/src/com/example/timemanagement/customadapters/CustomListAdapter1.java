package com.example.timemanagement.customadapters;

import java.util.ArrayList;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;




public class CustomListAdapter1 extends ArrayAdapter<String>{
	private ArrayList<Integer> blockStatesList;
	
	public CustomListAdapter1(Context context, int resID, ArrayList<String> items, ArrayList<Integer>blockStatesList){
		super(context, resID, items);
		this.blockStatesList = blockStatesList;		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = super.getView(position, convertView, parent);
		
		if(blockStatesList.get(position)== 1){
			((TextView)v).setBackgroundColor(Color.parseColor("#57bf23"));
		}
		else if(blockStatesList.get(position)== 2){
			((TextView)v).setBackgroundColor(Color.YELLOW);
		}	
		else{
			((TextView)v).setBackgroundColor(Color.WHITE);
		}
		return v;
	}
	
	public void setBlockStatesList(ArrayList<Integer> bList){
		this.blockStatesList = bList;
	}	
	
}