package com.example.timemanagement.model;

public class Order {
	
	private int ID; // primary key
	
	private String orderNumber;
	private String orderName;
	
	public Order() {
		// Empty constructor
	}
	
	public Order(String number, String name) {
		this.orderNumber = number;
		this.orderName = name;
	}
	
	public int getID() {
		return this.ID;
	}
	
	public String getOrderNumber() {
		return this.orderNumber;
	}
	
}
