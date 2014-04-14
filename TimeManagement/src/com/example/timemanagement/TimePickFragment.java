package com.example.timemanagement;

import java.util.Calendar;

import com.example.timemanagement.model.Block;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickFragment extends PickFragment
							implements TimePickerDialog.OnTimeSetListener {
	
	private boolean isStart;
	/**
	 * @param timeBlock
	 * @param isStart determines if the timeBlock represents the start- or endtime.
	 */
	public TimePickFragment(Block timeBlock, boolean isStart){
		super(timeBlock);
		
		cal = Calendar.getInstance();
		this.isStart = isStart;
		
		if(isStart) 
			cal.setTimeInMillis(timeBlock.getStart());
		else 
			cal.setTimeInMillis(timeBlock.getStop());
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
	}
	
	
	/**
	 * Updates the timeBlock with the new time and informs the parent activity.
	 */
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		
		 cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
				 hourOfDay, minute);
		 
		 if(isStart)
			timeBlock.setStart(cal.getTimeInMillis());
		 else
		 	timeBlock.setStop(cal.getTimeInMillis());
		 
		 //Update the parent activity with the new time.
		 parentActivity.update(timeBlock);
	}

}
