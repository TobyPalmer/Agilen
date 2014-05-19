package com.example.timemanagement.customadapters;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.timemanagement.R;


/**
 * List Adapter used for the ListActivity. This is special in that it takes a blockStatesList that can manipulate
 * How each row is presented
 * @author Marcus
 *
 */

public class CustomListAdapter1 extends ArrayAdapter<String>{
	private ArrayList<Integer> blockStatesList;
	private StateListDrawable states;
	private Typeface tf;
	
	public CustomListAdapter1(Context context, int resID, ArrayList<String> items, ArrayList<Integer>blockStatesList, String font){
		super(context, resID, items);
		this.blockStatesList = blockStatesList;	
		this.blockStatesList = blockStatesList;		
	    tf = Typeface.createFromAsset(context.getAssets(), font);	
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
		((TextView)v).setTypeface(tf);
		return v;
	}
	
	public void setBlockStatesList(ArrayList<Integer> bList){
		this.blockStatesList = bList;
	}	
	
}