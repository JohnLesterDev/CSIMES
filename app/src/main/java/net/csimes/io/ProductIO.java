package net.csimes.io;

import java.io.*;
import java.util.*;
import javax.swing.*;

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.splash.*;



public class ProductIO {
	
	public static void write(Product  prod, String path) {		
		try {
			new File(path + File.separator + prod.name).createNewFile();
			FileOutputStream flOut = new FileOutputStream(path + File.separator + prod.name, false);
			ObjectOutputStream outObj = new ObjectOutputStream(flOut);
			outObj.writeObject(prod);
			outObj.close();
			flOut.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Product read(String path) {
		Product prod = null;
		
		try {
				FileInputStream flIn = new FileInputStream(path);
				ObjectInputStream inObj = new ObjectInputStream(flIn);
				prod = new SecurityControl((Product) inObj.readObject()).decryptProduct();
				inObj.close();
				flIn.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		
		return prod;
	}
	
}	
