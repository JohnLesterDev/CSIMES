package net.csimes.temp;

import java.io.*;
import java.util.*;
import javax.swing.*;

import net.csimes.io.*;
import net.csimes.sec.*;
import net.csimes.init.*;
import net.csimes.temp.*;
import net.csimes.util.*;



public class Order {
	
	private static File orderFile = new File(Initialize.rootDir + File.separator + "ORDER");
	public static String orderPath = Initialize.rootDir + File.separator + "ORDER";
	
	private static HashMap<Integer,Transaction> transByID = Order.setOrderToID();
	private static Object[][] orderByTable = Order.setOrderToTable();
	private static ArrayList<Integer> allID = Order.setAllID();
	private static Integer maxID = Order.setMaxID();
	
	
	public static ArrayList<Integer> setAllID() {
		ArrayList<Integer> allID_ = new ArrayList<Integer>();
		allID_.add(0);
		
		if (Order.orderFile.listFiles() != null) {
			for (File file : Order.orderFile.listFiles()) {
				Transaction trans = TransactionIO.read(file.getAbsolutePath());
				allID_.add(trans.orderID);
			}
			
			Order.allID = allID_;
			
			return allID_;
		} else {
			allID_.add(0);
			Order.allID = allID_;
			return allID_;
		}
	}
	
	public static Integer setMaxID() {
		try {
			Order.allID = Order.setAllID();
			
			if (Order.allID != null) {
				Integer maxID_ = (Integer) Collections.max(Order.allID);
				Order.maxID = maxID_;
				
				return maxID_;
			} else {
				return null;
			}
		}catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
			Throwable cause = e.getCause();
			if (cause != null) {
				System.err.println("Cause: " + cause.getMessage());
				cause.printStackTrace();
			} else {
				e.printStackTrace();
			}
		}
		
		return 1;
	}
	
	public static HashMap<Integer,Transaction> setOrderToID() {
		HashMap<Integer,Transaction> transByID_ = new HashMap<Integer,Transaction>();
		
		if (Order.orderFile.listFiles() != null) {
			for (File file : Order.orderFile.listFiles()) {
				Transaction trans = TransactionIO.read(file.getAbsolutePath());
				
				transByID_.put((Integer) trans.orderID, trans);
			}
			
			Order.transByID = transByID_;
			
			return transByID_;
		} else {
			return null;
		}
	}
	
	public static Object[][] setOrderToTable() {
		// Order.prodsByID = Order.setProdsToID();
		// Order.maxID = Order.setMaxID();
		
		// ArrayList<ArrayList<Object>> prdArrays = new ArrayList<ArrayList<Object>>();
		
		// if (Order.OrderFile.listFiles() != null) {
			// for (Product prd : Order.prodsByID.values()) {
				// ArrayList<Object> prdArray = new ArrayList<Object>();
				
				// prdArray.add(String.format("%06d", prd.productID));
				// prdArray.add(prd.category);
				// prdArray.add(prd.name);
				// prdArray.add(String.format("%.1f", prd.quantity));
				// prdArray.add(prd.unit);
				// prdArray.add(String.format("%,.2f", prd.price));				
				// prdArray.add(String.format("%,.2f", prd.totals()));	
				// prdArray.add(prd.dateTime);

				// prdArrays.add(prdArray);
			// }
			
			// Object[][] obj = new Object[prdArrays.size()][10];
			
			// for (int k = 0; k < prdArrays.size(); k++) {
				// for (int j = 0; j < prdArrays.get(0).size(); j++) {
					// obj[k][j] = prdArrays.get(k).get(j);
				// }
			// }
			
			// Arrays.sort(obj, Comparator.comparingInt(a -> Integer.parseInt((String) a[0])));
			
			// Order.prodsByTable = obj;
			// return obj;
		// } else {
			// return null;
		// }
		
		return null;
	}
	
	public static Integer getMaxID() {
		Order.maxID = Order.setMaxID();
		
		return Order.maxID;
	}
	
	public static HashMap<Integer,Transaction> getTransByID() {
		
		return Order.transByID;
	}
	
	public static ArrayList<Integer> getOrderID() {
		
		return new ArrayList<>(Order.transByID.keySet());
	}
	
	public static Object[][] getOrderByTable() {
		return Order.setOrderToTable();
	}
	
	public static Transaction getOrderByID(Integer ID) {
		return Order.transByID.get(ID);
	}
	
	public static void refresh() {
		Order.transByID = Order.setOrderToID();
		Order.allID = Order.setAllID();
		Order.maxID = Order.setMaxID();
	}
	
	public static void createOrder(JLabel label) {
		Transaction transac = new Transaction();
		transac.orderID = Order.getMaxID() + 1;
		label.setText(String.valueOf(transac.orderID));
		transac.dateTimeNow();
		
		TransactionIO.write(new SecurityControl(transac).encryptTransaction(), Order.orderPath);
		
		Order.refresh();
	}
	
	public static void addProductToOrder(Integer orderID, Integer id, String name, String unit, int quantity, float price) {
		Transaction trans = Order.getOrderByID(orderID);
		
		trans.filePath.delete();
		
		OrderProduct ordprd = new OrderProduct();
		ordprd.productID = id;
		ordprd.name = name;
		ordprd.unit = unit;
		ordprd.quantity = quantity;
		ordprd.price = price;
		
		trans.products.add(ordprd);
		
		TransactionIO.write(new SecurityControl(trans).encryptTransaction(), Order.orderPath);
		
		Order.refresh();
	}
	
	public static void addCustomerToOrder(Integer orderID, String customer) {
		Transaction trans = Order.getOrderByID(orderID);
		
		trans.filePath.delete();
		trans.name = customer;
		
		TransactionIO.write(new SecurityControl(trans).encryptTransaction(), Order.orderPath);
		
		Order.refresh();
	}
	
	public static void finalOrder(Integer orderID, float amountDue, float cash, float change) {
		Transaction trans = Order.getOrderByID(orderID);
		
		trans.filePath.delete();
		trans.amountDue = amountDue;
		trans.cash = cash;
		trans.change = change;
		trans.STATUS = 0;
		
		TransactionIO.write(new SecurityControl(trans).encryptTransaction(), Order.orderPath);
		
		Order.refresh();
	}
	
	public static void voidOrder(Integer orderID) {
		Transaction trans = Order.getOrderByID(orderID);
		
		trans.filePath.delete();
		trans.STATUS = -1;
		
		TransactionIO.write(new SecurityControl(trans).encryptTransaction(), Order.orderPath);
		
		Order.refresh();
	}
}