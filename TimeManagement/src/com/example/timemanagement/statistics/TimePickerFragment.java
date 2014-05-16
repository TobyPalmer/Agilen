package com.example.timemanagement.statistics;


import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;


@SuppressLint("ValidFragment")
public class TimePickerFragment extends PickerFragment
							implements TimePickerDialog.OnTimeSetListener {
		
	protected int ID;
	protected Calendar cal;
	protected long time;
	
	public TimePickerFragment(long time, int ID){
		this.time = time;
		this.ID = ID;
	}
	
	
	/**
	 * Creates a timepicker fragment
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {	
		cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		
		return new TimePickerDialog(getActivity(), this, cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
				
	}
	
	/**
	 * When time is set return to parent activity
	 */
	@Override
	public void onTimeSet(TimePicker view, int hour, int minute){
		 
		 time = ((hour*60*60*1000) + (minute*60*1000));
		 //Update the parent activity with the new date.
		 parentActivity.update(this, time, ID);
	}

}
