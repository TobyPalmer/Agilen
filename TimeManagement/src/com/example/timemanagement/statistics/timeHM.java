package com.example.timemanagement.statistics;


public class timeHM {
	protected int minutes;
	protected int hours;	
	protected long origTime;
	
	public timeHM(long time){
		origTime = time;		
		//long wtime = Math.abs(time);
		long wtime = time;
		wtime /= 1000*60;
		hours = (int)wtime / 60;
		minutes = (int)wtime % 60;		
	}
		
@Override
public String toString(){
	return Integer.toString(hours) + " H, " + Integer.toString(minutes) + " M";	
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
