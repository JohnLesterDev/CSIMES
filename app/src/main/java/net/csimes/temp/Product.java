package net.csimes.temp;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.security.*;



public class Product implements Serializable {
	public int productID;
	public String category;
	public String name;
	public int quantity;
	public float price;
	public float total;
	
	public Product(int productID, String category, String name, int quantity, float price) {
		this.productID = productID;
		this.category = category;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
	
	public float totals() {
		this.total = (float) this.quantity * this.price;
		return this.total;
	}
}
