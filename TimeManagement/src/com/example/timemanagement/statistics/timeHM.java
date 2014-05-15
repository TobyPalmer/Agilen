package com.example.timemanagement.statistics;


public class timeHM {
	protected int minutes;
	protected int hours;	
	
	public timeHM(long time){
		long wtime = Math.abs(time);		
		wtime /= 1000*60;
		hours = (int)wtime / 60;
		minutes = (int)wtime % 60;		
	}
	
@Override
public String toString(){
	return Integer.toString(hours) + "h, " + Integer.toString(minutes) + "m";	
}

public String hoursToString(){
	return Integer.toString(hours);
}

public String minutesToString(){
	return Integer.toString(minutes);
}

public int getHours(){
	return hours;
}
public int getMinutes(){
	return minutes;
}

}
