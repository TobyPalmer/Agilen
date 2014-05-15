package com.example.timemanagement.statistics;

import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;


public class PickerFragment extends DialogFragment {
	
	protected TDPassable parentActivity;	
	
	
	public PickerFragment(){
		
	}
	
	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		parentActivity = (TDPassable)a;
	}
}
