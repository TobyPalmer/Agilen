package com.example.timemanagement.statistics;

import android.app.Activity;
import android.app.DialogFragment;

/**
 * Abstract Fragment Class inherited by DatePickerFragment and TimePickerFragment
 * @author Marcus
 *
 */
public class PickerFragment extends DialogFragment {
	
	protected TDPassable parentActivity;
	
	public PickerFragment(){
		
	}
	
	/**
	 * Attach to parent activity in-order to send data to the right activity
	 */
	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		parentActivity = (TDPassable)a;
	}
}
