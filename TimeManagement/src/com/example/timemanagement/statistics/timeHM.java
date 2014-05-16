package com.example.timemanagement.statistics;

/**
 * Structure that holds hours, minutes and the original time as int, int and Long respectively
 * @author Marcus
 *
 */
public class timeHM {
	protected int minutes;
	protected int hours;	
	protected long origTime;
	
	/**
	 * Constructor
	 * @param time Takes an unix time as Long
	 */
	public timeHM(long time){
		origTime = time;		
		//long wtime = Math.abs(time);
		long wtime = time;
		wtime /= 1000*60;
		hours = (int)wtime / 60;
		minutes = (int)wtime % 60;		
	}
	
	/**
	 * Returns a String in the format # H, # M
	 */
@Override
public String toString(){
	return Integer.toString(hours) + " H, " + Integer.toString(minutes) + " M";	
}

/**
 * Set the time
 * @param time Takes a unix time as argument, as Long
 */
public void setTime(long time){
	long wtime = time;
	wtime /= 1000*60;
	hours = (int)wtime / 60;
	minutes = (int)wtime % 60;
}

/**
 * Get the hours
 * @return returns the hours as String
 */
public String hoursToString(){
	return Integer.toString(hours);
}

/**
 * Get Minutes 
 * @return minutes as String
 */
public String minutesToString(){
	return Integer.toString(minutes);
}
/**
 * Get Hours
 * @return hours as Integer
 */
public int getHours(){
	return hours;
}
/**
 * Get minutes
 * @return minutes as Integer
 */
public int getMinutes(){
	return minutes;
}
/**
 * Function returns hours and minutes as minutes
 * @return Integer as the total number of minutes
 */
public int getTotalMinutes(){
	int min = hours * 60 + minutes;
	return min;
}

}
