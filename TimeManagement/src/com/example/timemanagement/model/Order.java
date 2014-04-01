package com.example.timemanagement.model;

public class Order {
	
	private String orderNumber;
	private String orderName;
	
	public Order() {
		// Empty constructor
	}
	
	public Order(String number, String name) {
		this.orderNumber = number;
		this.orderName = name;
	}
	
	public String getOrderNumber() {
		return this.orderNumber;
	}
	
}
