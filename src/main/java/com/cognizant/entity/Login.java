package com.cognizant.entity;

public class Login {
	private String userName = null;
	private String password = null;
	private char role = ' ';
	
	public Login(String userName, String password, char role) {
		super();
		this.userName = userName;
		this.password = password;
		this.role = role;
	}

	public Login() {
		// TODO Auto-generated constructor stub
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public char getRole() {
		return role;
	}

	public void setRole(char role) {
		this.role = role;
	}

}
