package net.csimes.temp;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.time.*;
import java.security.*;
import java.time.format.*;



public class OrderProduct implements Serializable {
	
	public int productID;
	public String name;
	public String unit;
	public int quantity;
	public float price;
	
	
	public float getTotal() {
		return ((float) this.quantity) * price; 
	}
}
