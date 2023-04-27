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



public class TransactionIO {
	
	public static void write(Transaction trans, String path) {		
		try {
			trans.filePath = new File(path + File.separator + trans.name);
			trans.filePath.createNewFile();
			FileOutputStream flOut = new FileOutputStream(path + File.separator + trans.name, false);
			ObjectOutputStream outObj = new ObjectOutputStream(flOut);
			outObj.writeObject(trans);
			outObj.close();
			flOut.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Transaction read(String path) {
		Transaction trans = null;
		
		try {
				FileInputStream flIn = new FileInputStream(path);
				ObjectInputStream inObj = new ObjectInputStream(flIn);
				trans = new SecurityControl((Transaction) inObj.readObject()).decryptTransaction();
				inObj.close();
				flIn.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		
		return trans;
	}
	
}	
