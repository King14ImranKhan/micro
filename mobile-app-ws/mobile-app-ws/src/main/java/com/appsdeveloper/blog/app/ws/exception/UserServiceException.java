package com.appsdeveloper.blog.app.ws.exception;

public class UserServiceException extends RuntimeException{
	
	public static final long serialVersionUID = 1346532467865L;
	
	public UserServiceException(String message) {
	   super(message);	
	}
	
}
