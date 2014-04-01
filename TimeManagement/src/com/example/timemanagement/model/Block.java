package com.example.timemanagement.model;

public class Block {

	private int start;
	private int stop;
	private String orderNumber; // foreign key
	
	public Block() {
		// Empty constructor
	}
	
	public Block(int s) {
		// Start constructor
		this.start = s;
		this.stop = 0;
		this.orderNumber = null;
	}
	
	public void setStop(int s) {
		this.stop = s;
	}
	
	public String toString() {
		return "Block, start = " + this.start + ", stop =  " + this.stop + ", orderNumber = " + this.orderNumber;
	}
	
}
