package net.csimes.temp;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.security.*;



public class Product implements Serializable {
	public int productID;
	public String category;
	public String name;
	public int quantity;
	public float price;
	public float total;
	
	public Object[] obj;
	
	public NumberFormat nf = NumberFormat.getInstance();
	
	public Product(int productID, String category, String name, int quantity, float price) {
		this.nf.setMaximumFractionDigits(2);
		this.productID = productID;
		this.category = category;
		this.name = name;
		this.quantity = quantity;
		this.price = Float.parseFloat(this.nf.format(price));
		
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
		this.total = Float.parseFloat(this.nf.format(this.total));
		
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
}
