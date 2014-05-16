package com.example.timemanagement.statistics;

import com.example.timemanagement.model.Order;

/**
 * Class that holds information about an order such as the order itself, total time as well as a special
 * form of the time that have some extra functionality
 * @author Marcus
 * @see hmTime
 */
public class OrderTimeDetails {
	public Order order;	
	public long totalTime;
	public timeHM hmTime; 
	
	/**
	 * Constructor. Takes an Order object, and initializes the times to 0
	 * @param order Takes an Order 
	 */
	public OrderTimeDetails(Order order){
		this.order = order;
		totalTime = 0;
		hmTime = new timeHM(0);
	}
	
	/**
	 * Updates the special time object hmTime. 
	 */
	public void updateTime(){
		hmTime.setTime(totalTime);
	}
	
	/**
	 * Returns the total time as a String.
	 * @return The total time in the format # H # M
	 */
	public String getTotalTime(){
		updateTime();
		return hmTime.toString();
	}
	
	/**
	 * Get the ordername
	 * @return Ordername as a String
	 */
	public String getOrderName(){
		return order.getOrderName();
	}
	
	/**
	 * Get the Ordernumber
	 * @return Ordernumber as a String
	 */
	public String getOrderNr(){
		return order.getOrderNumber();
	}
}
