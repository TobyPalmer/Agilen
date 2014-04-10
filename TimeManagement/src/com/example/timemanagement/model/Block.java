package com.example.timemanagement.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Block {

	private int ID; // primary key
	private int orderID; // foreign key
	
	private long start;
	private long stop;
	
	public Block() {
		// Empty constructor
	}
	
	public Block(long s) {
		// Start constructor
		this.start = s;
		this.stop = 0;
		this.orderID = 0;
	}

	public Block(int orderID, long s) {
		// Start constructor
		this.start = s;
		this.stop = 0;
		this.orderID = orderID;
	}

	public Block(int orderID, long start, long stop) {
		// Start constructor
		this.start = start;
		this.stop = stop;
		this.orderID = orderID;
	}

	
	public int getID() {
		return ID;
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
	
	public String toString() {
		Date start = new java.util.Date(this.start);
		String startString = new SimpleDateFormat("HH:mm").format(start);
		
		Date stop = new java.util.Date(this.stop);
		String stopString = new SimpleDateFormat("HH:mm").format(stop);
		
		return "Block, start = " + startString + ", stop =  " + stopString + ", orderID = " + this.orderID + "\n";
	}
	
	public String toStringPublic(){
		Date start = new java.util.Date(this.start);
		String startString = new SimpleDateFormat("yy-MM-dd  HH:mm").format(start);
		
		Date stop = new java.util.Date(this.stop);
		String stopString = new SimpleDateFormat("HH:mm").format(stop);
		
		return startString + " - " + stopString;
	}
	
	
}
