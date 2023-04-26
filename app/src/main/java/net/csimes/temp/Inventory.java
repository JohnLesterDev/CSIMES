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
		String payload = "GRP7>SETS|||SETALLID";
		ArrayList<Integer> allID_ = null;
		
		try {
			CLIENT.now.output.writeUTF(payload);
			
			String base64String = CLIENT.now.input.readUTF();
			byte[] objectBytes = Base64.getDecoder().decode(base64String);
			
			// Deserialize the object
			ByteArrayInputStream bais = new ByteArrayInputStream(objectBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			allID_ = (ArrayList<Integer>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return allID_;
	}
	
	private static Integer setMaxID() {
		String payload = "GRP7>SETS|||SETMAXID";
		Integer maxID_ = null;
		
		try {
			CLIENT.now.output.writeUTF(payload);
			
			String base64String = CLIENT.now.input.readUTF();
			byte[] objectBytes = Base64.getDecoder().decode(base64String);
			
			// Deserialize the object
			ByteArrayInputStream bais = new ByteArrayInputStream(objectBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			maxID_ = (Integer) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		
		return maxID_;
	}
	
	private static HashMap<Integer, Product> setProdsToID() {
		String payload = "GRP7>SETS|||SETPRODSTOID";
		HashMap<Integer, Product> prodID_ = null;

		try {
			CLIENT.now.output.writeUTF(payload);

			int objectLength = CLIENT.now.input.readInt();
			byte[] objectBytes = new byte[objectLength];
			CLIENT.now.input.readFully(objectBytes);

			// Deserialize the object
			ByteArrayInputStream bais = new ByteArrayInputStream(objectBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			prodID_ = (HashMap<Integer, Product>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return prodID_;
	}
	
	private static ArrayList<String> setAllProdCategories() {
		String payload = "GRP7>SETS|||SETALLPRODCAT";
		ArrayList<String> prodCat_ = null;
		
		try {
			CLIENT.now.output.writeUTF(payload);
			
			String base64String = CLIENT.now.input.readUTF();
			byte[] objectBytes = Base64.getDecoder().decode(base64String);
			
			// Deserialize the object
			ByteArrayInputStream bais = new ByteArrayInputStream(objectBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			prodCat_ = (ArrayList<String>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return prodCat_;
	}
	
	private static Object[][] setProductToTable() {
		String payload = "GRP7>SETS|||SETPRODTABLE";
		Object[][] prodTable_ = null;
		
		try {
			CLIENT.now.output.writeUTF(payload);
			
			int objectLength = CLIENT.now.input.readInt();
			byte[] objectBytes = new byte[objectLength];
			CLIENT.now.input.readFully(objectBytes);
			// Deserialize the object
			ByteArrayInputStream bais = new ByteArrayInputStream(objectBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			prodTable_ = (Object[][]) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return prodTable_;
	}
	
	public static Integer getMaxID() {
		Inventory.maxID = Inventory.setMaxID();
		
		return Inventory.maxID;
	}
	
	public static HashMap<Integer,Product> getProdsByID() {
		
		return Inventory.prodsByID;
	}
	
	public static ArrayList<Integer> getProdsID() {
		String payload = "GRP7>FETCHES|||ALLIDS|||";
		ArrayList<Integer> prd = null;
		
		try {
			CLIENT.now.output.writeUTF(payload);
			
			String base64String = CLIENT.now.input.readUTF();
			byte[] objectBytes = Base64.getDecoder().decode(base64String);
			
			// Deserialize the object
			ByteArrayInputStream bais = new ByteArrayInputStream(objectBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			prd = (ArrayList<Integer>) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return prd;
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
			prd.quantity += (Float) obj[2];
			prd.price = (Float) obj[3];
			
			prd.totals();
			System.out.println(prd.name);
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