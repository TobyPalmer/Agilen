package com.example.timemanagement.model;

public class Order {
	
	private int ID; // primary key
	
	private String orderNumber;
	private String orderName;
	
	public Order() {
		// Empty constructor
		this.ID = -1;
		this.orderNumber = "";
		this.orderName = "";
	}
	
	public Order(String number, String name) {
		this.orderNumber = number;
		this.orderName = name;
	}
	
	public int getID() {
		return this.ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}
	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderNumber() {
		return this.orderNumber;
	}

	@Override
	public String toString() {
		return orderNumber + " - " + orderName;
		//return "Order [ID=" + ID + ", orderNumber=" + orderNumber + ", orderName=" + orderName + "]";
	}
	
}
