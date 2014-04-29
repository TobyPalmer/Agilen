package com.example.timemanagement;

import java.util.Calendar;

import com.example.timemanagement.model.Block;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;


public class DatePickFragment extends PickFragment
							implements DatePickerDialog.OnDateSetListener {
	
	public DatePickFragment(Block timeBlock){
		super(timeBlock);
	}
	
	/**
	 * Using the date of the current timeBlock to set the DatePicker.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {	
		cal = Calendar.getInstance();
		cal.setTimeInMillis(timeBlock.getStart());
		
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
		 timeBlock.setStart(cal.getTimeInMillis());
		 timeBlock.setStop(cal.getTimeInMillis());
		 
		 //Update the parent activity with the new date.
		 parentActivity.update(this, timeBlock);
	}

}
