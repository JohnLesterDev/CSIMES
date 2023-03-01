package net.csimes.init;

import java.io.*;
import java.net.*;
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



public class Initialize {
	private static String rootPartition = System.getenv("SystemDrive");
	private static String rootDir = Initialize.getRootPartition() + File.separator + "CSIMES";
	public static String rootAccPath = Initialize.rootDir + File.separator + "ACC";
	private static File accountStat = new File(Initialize.rootDir + File.separator + "ACC");
	
	public boolean isInternet;
	public boolean isFirst;
	
	public void checkInternet() {
		try {
			URL url = new URL("http://www.google.com/");
			URLConnection conn = url.openConnection();
			conn.connect();
			isInternet = true;
		}catch (MalformedURLException e) {
			isInternet = false;
		}catch (IOException e) {
			isInternet = false;
		}
	}
	
	public void copyAccounts(String path) {
		ArrayList<String> accsPath = new ArrayList<String>();
		
		ResourceControl.getResourceList(accsPath, "accounts");
		
		for (int i = 0; i < accsPath.size(); i++) {
			try {
				InputStream fl = ResourceControl.getResourceFileStream(accsPath.get(i), true);
				String path_ = accsPath.get(i).split("/")[1];
				new File(path + File.separator + path_).createNewFile();
				OutputStream ofl = new FileOutputStream(path + File.separator + path_, false);
				
				int bytesRead =  -1;
				
				while ((bytesRead = fl.read()) != -1) {
					ofl.write(bytesRead);
				}
				
				ofl.close();
				fl.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	public void checkRoot() {
		File firstUser = new File(Initialize.rootDir + File.separator + "TRUE");
		
		if ((firstUser.exists() && !firstUser.isDirectory()) && (Initialize.accountStat.exists() && Initialize.accountStat.isDirectory())) {
			System.out.println("Not first user?");
			// new LoginPage().new Login();
		}else{
			RegisterPage regPage = new RegisterPage();
			
			String msg_ = "First time user detected. Create an Owner account?";
			int stat_ = JOptionPane.showConfirmDialog(null, msg_, "CSIMES - First Time User Detected", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if ((stat_ == 1) || (stat_ == -1)) {
				System.exit(1);
			}

			// Initialize.accountStat.mkdirs();
			
			/* try {
			// firstUser.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			};  */
			
			
			// this.copyAccounts(Initialize.rootAccPath);
			regPage.root.setVisible(true);

			
		}
		
	}
	
	public Initialize() {
		this.checkRoot();
	}
	
	public static String getRootPartition() {
		return Initialize.rootPartition;
	}
	
	public static String getRootDir() {
		return Initialize.rootDir;
	}
}
