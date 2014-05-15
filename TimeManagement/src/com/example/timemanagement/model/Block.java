package com.example.timemanagement.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Block implements Serializable, Comparable<Block>{

	private int ID; // primary key
	private int orderID; // foreign key
	
	private long start;
	private long stop;
	
	private String comment;
	private int checked;
	
	public Block() {
		// Empty constructor
	}
	
	public Block(long s) {
		// Start constructor
		this.start = s;
		this.stop = 0;
		this.orderID = 0;
		this.comment = "";
		this.checked = 0;
	}

	public Block(int orderID, long s) {
		// Start constructor
		this.start = s;
		this.stop = 0;
		this.orderID = orderID;
		this.comment = "";
		this.checked = 0;
	}

	public Block(int orderID, long start, long stop, String comment) {
		// Start constructor
		this.start = start;
		this.stop = stop;
		this.orderID = orderID;
		this.comment = comment;
		this.checked = 0;
	}

	
	public int getID() {
		return ID;
	}

	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getStop() {
		return stop;
	}
	
	public void setStop(long s) {
		this.stop = s;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String toString() {
		Date start = new java.util.Date(this.start);
		String startString = new SimpleDateFormat("HH:mm").format(start);
		
		Date stop = new java.util.Date(this.stop);
		String stopString = new SimpleDateFormat("HH:mm").format(stop);
		
		return "Block, start = " + startString + ", stop =  " + stopString + ", orderID = " + this.orderID 
				+ ", comment = " + this.comment + ", checked = " + this.checked + "\n";
	}
	
	public String toStringPublic(){
		Date start = new java.util.Date(this.start);
		String startString = new SimpleDateFormat("HH:mm").format(start);
		
		Date stop = new java.util.Date(this.stop);
		String stopString = new SimpleDateFormat("HH:mm").format(stop);
		
		if(this.stop!=0)
			return startString + " - " + stopString;
		else
			return startString + " - xx:xx";
	}
	
	public String toDateString(){
		return new SimpleDateFormat("yyyy-MM-dd").format(start);
	}
	
	public String toTimeString(boolean isStart){
		if(isStart)
			return new SimpleDateFormat("HH:mm").format(start);
		else
			return new SimpleDateFormat("HH:mm").format(stop);
	}
	
	public String printTime(){
		
		Long diff;
		
		if(stop!=0)
			diff = stop-start;
		else
			diff = System.currentTimeMillis() - start;
		
		diff -= 1000*60*60;
		
		Date date = new java.util.Date(diff);
		
		return date.getHours() + "h" + date.getMinutes() + "m";
	}
	
	 
	@Override
	public int compareTo(Block b) {
        if(b.getStart()<getStart())
        	return -1;
        else if(b.getStart()>getStart())
        	return 1;
        else
        	return 0;
	}
	
}
