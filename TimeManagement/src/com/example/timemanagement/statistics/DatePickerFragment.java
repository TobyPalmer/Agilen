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
	
	/**
	 * Fragment to pick a date
	 * @param time Unix time as Long
	 * @param ID ID of this particular fragment
	 */
	
	public DatePickerFragment(long time, int ID){		
		this.time = time;
		this.ID = ID;
	}	
	
	/**
	 * Set a calander to the time given and build the DatePickerDialog
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {	
		cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		
		return new DatePickerDialog(getActivity(), this, cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				
	}
	/**
	 * When the date has been picked this is run. Which sends the picked time
	 * back to the parent Activity that initiated the fragment
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth){
		 cal.set(year, monthOfYear, dayOfMonth);
		 time = cal.getTimeInMillis();
		 parentActivity.update(this, time, ID);
	}

}
