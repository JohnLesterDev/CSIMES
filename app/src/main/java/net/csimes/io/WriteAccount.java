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



public class WriteAccount {
	
	public static void write(Account acc, String path) {		
		try {
			new File(path + File.separator + acc.getUserName()).createNewFile();
			System.out.println(path + File.separator + acc.getUserName());
			FileOutputStream flOut = new FileOutputStream(path + File.separator + acc.getUserName(), false);
			ObjectOutputStream outObj = new ObjectOutputStream(flOut);
			outObj.writeObject(acc);
			outObj.close();
			flOut.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}	
