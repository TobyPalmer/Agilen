package com.example.timemanagement.model;
/**
 * Class that holds information about the current user
 * @author Marcus
 *
 */
public class UserDetails {
	private String username;
	private long workday;
	
	/**
	 * Consturctor
	 * @param username Name of the user
	 * @param workday How long their workday is as an unix time 
	 */
	public UserDetails(String username, long workday){
		this.username = username;
		this.workday = workday;
	}
	
	/**
	 * Get the current users username
	 * @return Username as a string
	 */
	public String getUsername(){
		return username;
	}
	
	/**
	 * Return the current users workday
	 * @return workday as a unix time 
	 */
	public long getWorkday(){
		return workday;
	}
	
	/**
	 * Sets the current users workday
	 * @param workday Workday as an unix time. Long
	 */
	public void setWorkday(long workday){
		this.workday = workday;
	}
}
