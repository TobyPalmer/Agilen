package com.example.timemanagement.statistics;


import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;


@SuppressLint("ValidFragment")
public class DatePickerFragment extends PickerFragment
							implements DatePickerDialog.OnDateSetListener {
	protected int ID;
	protected Calendar cal;
	protected long time;
	
	
	public DatePickerFragment(long time, int ID){		
		this.time = time;
		this.ID = ID;
	}	
	
	/**
	 * Using the date of the current timeBlock to set the DatePicker.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {	
		cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		
		return new DatePickerDialog(getActivity(), this, cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				
	}
	/**
	 * Gets called when the new date is set.
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth){
		 cal.set(year, monthOfYear, dayOfMonth);
		 time = cal.getTimeInMillis();
		 //Update the parent activity with the new date.
		 Log.e("GET TO HERE IN DATEFRAGMENT", "TUDELS");
		 parentActivity.update(this, time, ID);
	}

}
