package net.csimes.temp;

import java.io.*;
import java.util.*;

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.util.*;
import net.csimes.mouse.*;
import net.csimes.table.*;
import net.csimes.audio.*;
import net.csimes.client.*;
import net.csimes.splash.*;
import net.csimes.listeners.*;



public class Inventory {
	
	private static File inventoryFile = new File(Initialize.rootDir + File.separator + "INVENTORY");
	public static String inventoryPath = Initialize.rootDir + File.separator + "INVENTORY";
	
	private static HashMap<Integer,Product> prodsByID = Inventory.setProdsToID();
	private static Object[][] prodsByTable = Inventory.setProductToTable();
	private static ArrayList<Integer> allID = Inventory.setAllID();
	private static ArrayList<String> allProdCategories = Inventory.setAllProdCategories();
	private static Integer maxID = Inventory.setMaxID();
	
	
	private static ArrayList<Integer> setAllID() {
		ArrayList<Integer> allID_ = new ArrayList<Integer>();
		allID_.add(0);
		
		if (Inventory.inventoryFile.listFiles() != null) {
			for (File file : Inventory.inventoryFile.listFiles()) {
				Product prd = ProductIO.read(file.getAbsolutePath());
				allID_.add(prd.productID);
			}
			
			Inventory.allID = allID_;
			
			return allID_;
		} else {
			allID_.add(0);
			Inventory.allID = allID_;
			return allID_;
		}
	}
	
	private static Integer setMaxID() {
		try {
			Inventory.allID = Inventory.setAllID();
			
			if (Inventory.allID != null) {
				Integer maxID_ = (Integer) Collections.max(Inventory.allID);
				Inventory.maxID = maxID_;
				
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
	
	private static HashMap<Integer,Product> setProdsToID() {
		HashMap<Integer,Product> prodsByID_ = new HashMap<Integer,Product>();
		
		if (Inventory.inventoryFile.listFiles() != null) {
			for (File file : Inventory.inventoryFile.listFiles()) {
				Product prd = ProductIO.read(file.getAbsolutePath());
				
				prodsByID_.put((Integer) prd.productID, prd);
			}
			
			Inventory.prodsByID = prodsByID_;
			
			return prodsByID_;
		} else {
			return null;
		}
	}
	
	private static ArrayList<String> setAllProdCategories() {
		ArrayList<String> allProdCategories_ = new ArrayList<String>();
		allProdCategories_.add("Categories");
		
		if (Inventory.inventoryFile.listFiles() != null) {
			for (File file : Inventory.inventoryFile.listFiles()) {
				Product prd = ProductIO.read(file.getAbsolutePath());
				
				if (!allProdCategories_.contains(prd.category)) {
					allProdCategories_.add(prd.category);
				}
			}
			
			Inventory.allProdCategories = allProdCategories_;
			
			return allProdCategories_;
		} else {
			return null;
		}
	}
	
	private static Object[][] setProductToTable() {
		Inventory.prodsByID = Inventory.setProdsToID();
		Inventory.maxID = Inventory.setMaxID();
		
		ArrayList<ArrayList<Object>> prdArrays = new ArrayList<ArrayList<Object>>();
		
		if (Inventory.inventoryFile.listFiles() != null) {
			for (Product prd : Inventory.prodsByID.values()) {
				ArrayList<Object> prdArray = new ArrayList<Object>();
				
				prdArray.add(String.format("%06d", prd.productID));
				prdArray.add(prd.category);
				prdArray.add(prd.name);
				prdArray.add(prd.quantity);
				prdArray.add(prd.unit);
				prdArray.add(String.format("%.2f", prd.price));				
				prdArray.add(String.format("%.2f", prd.totals()));	
				prdArray.add(prd.dateTime);

				prdArrays.add(prdArray);
			}
			
			Object[][] obj = new Object[prdArrays.size()][10];
			
			for (int k = 0; k < prdArrays.size(); k++) {
				for (int j = 0; j < prdArrays.get(0).size(); j++) {
					obj[k][j] = prdArrays.get(k).get(j);
				}
			}
			
			Arrays.sort(obj, Comparator.comparingInt(a -> Integer.parseInt((String) a[0])));
			
			Inventory.prodsByTable = obj;
			return obj;
		} else {
			return null;
		}
	}
	
	public static Integer getMaxID() {
		Inventory.maxID = Inventory.setMaxID();
		
		return Inventory.maxID;
	}
	
	public static HashMap<Integer,Product> getProdsByID() {
		
		return Inventory.prodsByID;
	}
	
	public static ArrayList<String> getAllProdCategories() {
		Inventory.allProdCategories = Inventory.setAllProdCategories();
		
		return Inventory.allProdCategories;
	}
	
	public static Object[][] getProdsByTable() {
		Inventory.prodsByTable = Inventory.setProductToTable();
		
		return Inventory.prodsByTable;
	}
	
	public static Product getProductByID(Integer ID) {
		String payload = "GRP7>FETCHES|||BYID|||" + Integer.toString(ID);
		Product prd = null;
		
		try {
			CLIENT.now.output.writeUTF(payload);
			
			String base64String = CLIENT.now.input.readUTF();
			byte[] objectBytes = Base64.getDecoder().decode(base64String);
			
			// Deserialize the object
			ByteArrayInputStream bais = new ByteArrayInputStream(objectBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			prd = (Product) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return prd;
	}
	
	public static void insertProduct(Object... obj) {
		Inventory.refresh();
		
		Product prd = new SecurityControl(new Product(
			Inventory.getMaxID() + 1,
			(String) obj[0],
			(String) obj[1],
			(String) obj[2],
			(Float) obj[3],
			(Float) obj[4]
		)).encryptProduct();
		
		ProductIO.write(prd, Inventory.inventoryPath);
	}
	
	public static boolean removeProductByID(Integer ProductID) {
		Inventory.refresh();
		
		if (Inventory.prodsByID.get(ProductID).filePath.delete()) {
			Inventory.refresh();
			
			return true;
		} else {
			Inventory.refresh();
			return false;
		}
	}
	
	public static void modifyProductByID(Integer ID, Object... obj) {
		Inventory.refresh();
		
		
		if (Inventory.prodsByID.get(ID) != null) {
			Product prd = Inventory.prodsByID.get(ID);
			
			prd.filePath.delete();
			
			prd.category = (String) obj[0];
			prd.name = (String) obj[1];
			prd.quantity += (Integer) obj[2];
			prd.price = (Float) obj[3];
			
			prd.totals();
			
			Product newPrd = new SecurityControl(prd).encryptProduct();
			ProductIO.write(newPrd, Inventory.inventoryPath);
			
			return;
		} else {
			return;
		}
	}
	
	public static void refresh() {
		Inventory.prodsByID = Inventory.setProdsToID();
		Inventory.allProdCategories = Inventory.setAllProdCategories();
		Inventory.prodsByTable = Inventory.setProductToTable();
		Inventory.allID = Inventory.setAllID();
		Inventory.maxID = Inventory.setMaxID();
	}
}