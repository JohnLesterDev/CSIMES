package net.csimes.temp;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.time.*;
import java.security.*;
import java.time.format.*;



public class Product implements Serializable {
	public int productID;
	public String category;
	public String name;
	public String unit;
	public float quantity;
	public float price;
	public float total;
	public String dateTime;
	
	public File filePath;
	
	public Object[] obj;
		
	public Product(int productID, String category, String name, String unit, float quantity, float price) {
		this.productID = productID;
		this.category = category;
		this.name = name;
		this.unit = unit;
		this.quantity = quantity;
		this.price = Float.parseFloat(String.format("%.2f", price));
		
		this.totals();
		this.dateTimeNow();
		
		this.obj = new Object[] {
			this.productID,
			this.category,
			this.name,
			this.quantity,
			this.price,
			this.total
		};
	}
	
	public Product(int productID, String category, String name, String unit, float quantity, float price, String dateTime) {
		this.productID = productID;
		this.category = category;
		this.name = name;
		this.unit = unit;
		this.quantity = quantity;
		this.price = Float.parseFloat(String.format("%.2f", price));
		
		this.totals();
		this.dateTime = dateTime;
		
		this.obj = new Object[] {
			this.productID,
			this.category,
			this.name,
			this.quantity,
			this.price,
			this.total
		};
	}
	
	public float totals() {
		this.total = (float) this.quantity * this.price;
		this.total = Float.parseFloat(String.format("%.2f", this.total));
		
		this.obj = new Object[] {
			this.productID,
			this.category,
			this.name,
			this.quantity,
			this.price,
			this.total
		};

		return this.total;
	}
	
	public String dateTimeNow() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter ff = DateTimeFormatter.ofPattern("MM/dd/yyyy-hh:mm");
		
		this.dateTime = now.format(ff);
		
		return this.dateTime;
	}
}
