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
	
	public void setStart(long s) {
		this.start = s;
	}
	
	public void setStop(long s) {
		this.stop = s;
	}
	
	public String toString() {
		Date start = new java.util.Date(this.start);
		String startString = new SimpleDateFormat("hh:mm").format(start);
		
		Date stop = new java.util.Date(this.stop);
		String stopString = new SimpleDateFormat("hh:mm").format(stop);
		
		return "Block, start = " + startString + ", stop =  " + stopString + ", orderID = " + this.orderID + "\n";
	}
	
}
