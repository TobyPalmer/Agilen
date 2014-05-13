package com.example.timemanagement.statistics;

import com.example.timemanagement.model.Order;

public class OrderTimeDetails {
	public Order order;	
	public long totalTime;
	public timeHM hmTime; 
	
	public OrderTimeDetails(Order order){
		totalTime = 0;
		hmTime = new timeHM(totalTime);
	}
	
	
	
}
