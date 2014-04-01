package com.example.timemanagement.model;

public class Block {

	private int ID; // primary key
	private int orderID; // foreign key
	
	private int start;
	private int stop;
	
	public Block() {
		// Empty constructor
	}
	
	public Block(int s) {
		// Start constructor
		this.start = s;
		this.stop = 0;
		this.orderID = 0;
	}
	
	public void setStop(int s) {
		this.stop = s;
	}
	
	public String toString() {
		return "Block, start = " + this.start + ", stop =  " + this.stop + ", orderID = " + this.orderID;
	}
	
}
