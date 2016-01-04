package controllers;

import models.*;
public class Security extends Secure.Security {
	static boolean authenticate(String username,String password){
		User user = User.find("byUserid",username).first();
		return user !=null && user.password.equals(password);
	}
}
