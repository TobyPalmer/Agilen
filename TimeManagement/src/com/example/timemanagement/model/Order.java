package com.example.timemanagement.model;

public class Order {
	
	private int ID; // primary key
	
	private String orderNumber;
	private String orderName;
	private int directWork;
	
	public Order() {
		// Empty constructor
		this.ID = -1;
		this.orderNumber = "";
		this.orderName = "";
		this.directWork = 0;
	}
	
	public Order(String number, String name) {
		this.orderNumber = number;
		this.orderName = name;
	}
	
	public Order(String number, String name, int isDirectWork){
		this.orderNumber = number;
		this.orderName = name;
		this.directWork = isDirectWork;
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
	
	public int getOrderDirectWork(){
		return directWork;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setOrderDirectWork(int workType){
		this.directWork = workType;
	}
	
	public String getOrderNumber() {
		return this.orderNumber;
	}

	@Override
	public String toString() {
		String dWork;
		if(this.directWork == 1){
			dWork = "Direkttid";
		}
		else{
			dWork = "Interntid";
		}
		//Vill inte visa Direkt-/Indirekttid i Spinner
		return orderNumber + " - " + orderName;// + " - " + dWork;
		//return "Order [ID=" + ID + ", orderNumber=" + orderNumber + ", orderName=" + orderName + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((orderName == null) ? 0 : orderName.hashCode());
		result = prime * result
				+ ((orderNumber == null) ? 0 : orderNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (orderName == null) {
			if (other.orderName != null)
				return false;
		} else if (!orderName.equals(other.orderName))
			return false;
		if (orderNumber == null) {
			if (other.orderNumber != null)
				return false;
		} else if (!orderNumber.equals(other.orderNumber))
			return false;
		return true;
	}

	
}
