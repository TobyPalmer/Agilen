package com.example.timemanagement;

import java.util.Calendar;

import com.example.timemanagement.model.Block;

import android.app.Activity;
import android.app.DialogFragment;

public class PickFragment extends DialogFragment {

	protected Block timeBlock;
	protected Calendar cal;
	/**
	 * The activity that has created the fragment
	 */
	protected DataPassable parentActivity;

	public PickFragment(Block timeBlock) {
		this.timeBlock = timeBlock;
	}

	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		parentActivity = (DataPassable)a;
	}

}
