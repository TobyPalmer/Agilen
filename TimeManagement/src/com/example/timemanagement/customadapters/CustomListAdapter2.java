package com.example.timemanagement.customadapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;




public class CustomListAdapter2 extends ArrayAdapter<String>{
	private ArrayList<Integer> blockStatesList;
	
	public CustomListAdapter2(Context context, int resID, ArrayList<String> items){
		super(context, resID, items);
		this.blockStatesList = blockStatesList;		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View v = super.getView(position, convertView, parent);
		//((TextView)v).();
		return v;
	}
	
	/*public void setBlockStatesList(ArrayList<Integer> bList){
		this.blockStatesList = bList;
	}	*/
	
}