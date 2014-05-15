package com.example.timemanagement.model;

public class UserDetails {
	private String username;
	private long workday;
	
	public UserDetails(String username, long workday){
		this.username = username;
		this.workday = workday;
	}
	
	public String getUsername(){
		return username;
	}
	
	public long getWorkday(){
		return workday;
	}
	
	public void setWorkday(long workday){
		this.workday = workday;
	}
}
