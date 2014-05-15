package com.example.timemanagement.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.timemanagement.MainActivity;

import android.text.format.DateFormat;
import android.util.Log;

public class Notification {
	
	private int ID; // primary key
	
	private long time;
	private boolean everyDay;
	private boolean everyWeek;
	private boolean everyMonth;
	private boolean spareTime;
	
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
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String dateFormat = format.format(date);
		
		SimpleDateFormat form = new SimpleDateFormat("dd");
		String datef = form.format(date);
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int d = c.DAY_OF_WEEK;
		String s;
		
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		String weekDay = dayFormat.format(c.getTime());
		String spare = "";
		if(spareTime)
			spare = "Fritid";
		else
			spare = "Ej fritid";
		
		if(this.everyDay)
			s = "Varje dag";
		else if(this.everyWeek)
			s = getWeekDay(weekDay) + "ar varje vecka";
		else if(this.everyMonth)
			s = "Den " + datef + ":e varje månad";
		else
			s = dateFormat;
		
		return s + " - " + dateFormatted + " - " + spare;
	}
	
	public String getWeekDay(String i){

		if(i.equals("Sunday"))
			return "Söndag";
		else if(i.equals("Monday"))
			return "Måndag";
		else if(i.equals("Tuesday"))
			return "Tisdag";
		else if(i.equals("Wednesday"))
			return "Onsdag";
		else if(i.equals("Thursday"))
			return "Torsdag";
		else if(i.equals("Friday"))
			return "Fredag";
		else if(i.equals("Saturday"))
			return "Lördag";
		else
			return "Någonting gick fel!";

	}

	public boolean spareTime() {
		return spareTime;
	}

	public void setSpareTime(boolean s) {
		spareTime = s;
	}
	

}
