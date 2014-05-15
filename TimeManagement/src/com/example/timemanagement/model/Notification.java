package com.example.timemanagement.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.format.DateFormat;

public class Notification {
	
	private int ID; // primary key
	
	private long time;
	private boolean everyDay;
	private boolean everyWeek;
	private boolean everyMonth;
	
	public Notification(long c, boolean day, boolean week, boolean month){
		time = c;
		everyDay = day;
		everyWeek = week;
		everyMonth = month;
	}
	
	public Notification(){
		time = System.currentTimeMillis();
		everyDay = false;
		everyWeek = false;
		everyMonth = false;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int id) {
		ID = id;
	}
	
	public void setTime(long c){
		time = c;
	}
	
	public void setNoRepeat(){
		everyDay = false;
		everyWeek = false;
		everyMonth = false;
	}
	
	public void setEveryDay(boolean day){
		everyDay = day;
	}
	
	public void setEveryWeek(boolean week){
		everyWeek = week;
	}
	
	public void setEveryMonth(boolean month){
		everyMonth = month;
	}
	
	public long getTime(){
		return time;
	}
	
	public boolean everyDay(){
		return everyDay;
	}
	
	public boolean everyWeek(){
		return everyWeek;
	}
	
	public boolean everyMonth(){
		return everyMonth;
	}
	
	public String toString(){
		Date date = new Date(time);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String dateFormatted = formatter.format(date);
		SimpleDateFormat form = new SimpleDateFormat("dd");
		String datef = form.format(date);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int d = c.DAY_OF_WEEK;
		String s;
		
		if(this.everyDay)
			s = "Varje dag";
		else if(this.everyWeek)
			s = getWeekDay(d) + "ar varje vecka";
		else if(this.everyMonth)
			s = "Den " + datef + ":e varje månad";
		else
			s="Repeteras inte";
		
		return s + " - " + dateFormatted;
	}
	
	public String getWeekDay(int i){
		if(i==1)
			return "Söndag";
		if(i==2)
			return "Måndag";
		if(i==3)
			return "Tisdag";
		if(i==4)
			return "Onsdag";
		if(i==5)
			return "Torsdag";
		if(i==6)
			return "Fredag";
		if(i==7)
			return "Lördag";

		return "nåt gick fel";
		
		
	}

}
