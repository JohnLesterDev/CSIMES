package net.csimes.exc;

public class NotFirstUserException extends Exception {
	
	public NotFirstUserException() {
		super("User already exists and is not a first user.");
	}
}
