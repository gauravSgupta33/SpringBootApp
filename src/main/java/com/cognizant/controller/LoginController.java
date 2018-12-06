package com.cognizant.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.entity.Login;

@RestController
@CrossOrigin
public class LoginController {
	private static List<Login> userList = new ArrayList<Login>(); 
	static {
		Login l1 = new Login("Gaurav", "Gaurav", 'P');
		Login l2 = new Login("User1", "User1", 'L');
		Login l3 = new Login("User2", "User2", 'L');
		userList.add(l1);
		userList.add(l2);
		userList.add(l3);
	}
	
	@PostMapping("/login")
	public Login loginUser(@RequestBody Login loginUser) {
		for(Login user : userList) {
			if(user.getUserName().equals(loginUser.getUserName()) && user.getPassword().equals(loginUser.getPassword())) {
				return user;
			}
		}
		return null;
	}

	@PostMapping("/registerUser")
	public boolean registerUser(@RequestBody Login loginUser) {
		for(Login user : userList) {
			if(user.getUserName().equals(loginUser.getUserName())) {
				return false;
			}
		}
		userList.add(loginUser);
		return true;
	}

}
