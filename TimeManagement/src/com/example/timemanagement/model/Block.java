package com.example.timemanagement.model;

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
	
	@Override
	public String toString() {
		return "Block [ID=" + ID + ", orderID=" + orderID + ", start=" + start
				+ ", stop=" + stop + "]";
	}
	
}
