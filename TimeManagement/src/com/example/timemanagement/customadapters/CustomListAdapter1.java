package com.example.timemanagement.customadapters;

import java.util.ArrayList;

import com.example.timemanagement.R;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;




public class CustomListAdapter1 extends ArrayAdapter<String>{
	private ArrayList<Integer> blockStatesList;
	private StateListDrawable states;
	
	public CustomListAdapter1(Context context, int resID, ArrayList<String> items, ArrayList<Integer>blockStatesList){
		super(context, resID, items);
		this.blockStatesList = blockStatesList;	
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		final View v = super.getView(position, convertView, parent);
		 
		states = new StateListDrawable();
		
		if(blockStatesList.get(position) == 1){
			states.addState(new int[]{android.R.attr.state_pressed},
					getContext().getResources().getDrawable(R.drawable.selected));
			states.addState(new int[]{},
					getContext().getResources().getDrawable(R.drawable.green_normal));
		}
		else if(blockStatesList.get(position) == 2){
			states.addState(new int[]{android.R.attr.state_pressed},
					getContext().getResources().getDrawable(R.drawable.selected));
			states.addState(new int[]{},
					getContext().getResources().getDrawable(R.drawable.yellow_normal));
		}	
		else{
			states.addState(new int[]{android.R.attr.state_pressed},
					getContext().getResources().getDrawable(R.drawable.selected));
			states.addState(new int[]{},
					getContext().getResources().getDrawable(R.drawable.white_normal));
		}
		((TextView)v).setBackground(states);
		return v;
	}
	
	public void setBlockStatesList(ArrayList<Integer> bList){
		this.blockStatesList = bList;
	}	
	
}