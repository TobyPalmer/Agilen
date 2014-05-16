package com.example.timemanagement.statistics;
/**
 * Interface for sending data between fragments and activities
 * @author Marcus
 *
 */
public interface TDPassable {
	public void update(PickerFragment p, long o, int ID);
}
