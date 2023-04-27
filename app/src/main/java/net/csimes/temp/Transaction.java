package net.csimes.temp;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.time.*;
import java.security.*;
import java.time.format.*;



public class Transaction implements Serializable {
	public String dateTime = this.dateTimeNow();
	public int orderID;
	public String name =  "Unknown Customer";
	public ArrayList<OrderProduct> products = new ArrayList<OrderProduct>();
	public float amountDue = 0.00f;
	public float cash = 0.00f;
	public float change = 0.00f;
	public int STATUS = 1; 
	
	public File filePath;
	
	public String dateTimeNow() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter ff = DateTimeFormatter.ofPattern("MM/dd/yyyy-hh:mm");
		
		this.dateTime = now.format(ff);
		
		return this.dateTime;
	}
	
}
