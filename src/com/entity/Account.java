package com.entity;

public class Account {
	
	private int id;
	private String username;
	private String password;
	private String place;
	private int validTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	
	public int getValidTime() {
		return validTime;
	}
	public void setValidTime(int validTime) {
		this.validTime = validTime;
	}
	public Account() {
		super();
	}
	public Account(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	
	

}
