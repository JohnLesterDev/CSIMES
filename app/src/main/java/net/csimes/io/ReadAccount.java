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



public class ReadAccount {
	
	public static Account read(String path, boolean isSecured) {
		Account acc = null;
		
		if (isSecured == true) {
			try {
				FileInputStream flIn = new FileInputStream(path);
				ObjectInputStream inObj = new ObjectInputStream(flIn);
				acc = new SecurityControl((Account) inObj.readObject()).decryptAccount();
				inObj.close();
				flIn.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				FileInputStream flIn = new FileInputStream(path);
				ObjectInputStream inObj = new ObjectInputStream(flIn);
				acc = (Account) inObj.readObject();
				inObj.close();
				flIn.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return acc;
	}
	
	public static Account read(InputStream path, boolean isSecured) {
		Account acc = null;
		
		if (isSecured == true) {
			try {
				ObjectInputStream inObj = new ObjectInputStream(path);
				acc = new SecurityControl((Account) inObj.readObject()).decryptAccount();
				inObj.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				ObjectInputStream inObj = new ObjectInputStream(path);
				acc = (Account) inObj.readObject();
				inObj.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return acc;
	}
	
	public static Account read(File path, boolean isSecured) {
		Account acc = null;
		
		if (isSecured == true) {
			try {
				FileInputStream flIn = new FileInputStream(path);
				ObjectInputStream inObj = new ObjectInputStream(flIn);
				acc = new SecurityControl((Account) inObj.readObject()).decryptAccount();
				inObj.close();
				flIn.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				FileInputStream flIn = new FileInputStream(path);
				ObjectInputStream inObj = new ObjectInputStream(flIn);
				acc = (Account) inObj.readObject();
				inObj.close();
				flIn.close();
			}catch(FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return acc;
	}
}
