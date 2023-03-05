package net.csimes.init;

import java.io.*;
import java.nio.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.nio.file.*;

import net.csimes.io.*;
import net.csimes.exc.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.splash.*;



public class Initialize {
	private static String lockFilePath = System.getProperty("java.io.tmpdir");
	private static String rootPartition = System.getenv("SystemDrive");
	private static String rootDir = Initialize.getRootPartition() + File.separator + "CSIMES";
	public static String rootAccPath = Initialize.rootDir + File.separator + "ACC";
	public static File accountStat = new File(Initialize.rootDir + File.separator + "ACC");
	public static File firstUser = new File(Initialize.rootDir + File.separator + "TRUE");
	
	public static HashMap<String,Page> pages = Initialize.createPages(new String[]{"MAIN", "credentials", "popup", "free1", "free2", "free3"});	
	public static HashMap<String,ImageIcon> icons = Initialize.loadIcons();
	
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
	
	public static void LockFile(Runnable r, Runnable error) {
		File lockfile = new File(Initialize.lockFilePath, "csimes-inventory.lock");
		
		try {
			if (!lockfile.createNewFile()) {
				error.run();
				return;
				// System.exit(0);
			}
			
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				Initialize.LockFile(true);
			}));
			
			r.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void LockFile(boolean locked) {
		File lockfile = new File(Initialize.lockFilePath, "csimes-inventory.lock");
		
		if (locked) {
			if (!lockfile.delete()) {
			System.err.println("Failed to delete a lock file.");
			}
		}
	}
	
	private static HashMap<String,ImageIcon> loadIcons() {
		HashMap<String,ImageIcon> images = new HashMap<String,ImageIcon>();
		ArrayList<String> imagesList = new ArrayList<String>();
		
		ResourceControl.getResourceList(imagesList, "icons");
		for (int i=0;i < imagesList.size(); i++) {
			images.put(imagesList.get(i), new ImageIcon(ResourceControl.getResourceFile(imagesList.get(i))));
		}
		
		return images;
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
	
	public static HashMap<String,Page> createPages(String[] pagesName) {
		HashMap<String,Page> pages = new HashMap<String,Page>();
		
		for (String pageName : pagesName) {
			pages.put(pageName, new Page(pageName));
		};
		
		return pages;
	}
	
	public void createCoreFiles() {
		Initialize.accountStat.mkdirs();
			
		try {
			Initialize.firstUser.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		};  
		
		this.copyAccounts(Initialize.rootAccPath);
	}
	
	public boolean checkRoot() {
		File firstUser = new File(Initialize.rootDir + File.separator + "TRUE");
		boolean status = false;
		
		
		if ((firstUser.exists() && !firstUser.isDirectory()) && (Initialize.accountStat.exists() && Initialize.accountStat.isDirectory())) {
			status = true;
		}
		
		return status;
		
	}
	
	public void initialize(boolean notfirstuser, HashMap<String,Page> pages) /*throws NotFirstUserException*/ {		
		RegisterPage rp = new RegisterPage(pages.get("credentials"));
		rp.root.clean();
		
		if (notfirstuser) {
			// throw new NotFirstUserException();
			LoginPage lp = new LoginPage(Initialize.pages.get("credentials"));
			lp.paints();
			lp.page.setVisible(true);
		}else{		
			this.createCoreFiles();
			rp = new RegisterPage(pages.get("credentials"));
			rp.root.setVisible(true);
			String msg_ = "First time user detected. Create an Owner account?";
			int stat_ = JOptionPane.showConfirmDialog(null,
						msg_, 
						"CSIMES - First Time User Detected", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE,
						new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
						);
			rp.root.revalidate();
			rp.root.repaint();
			rp.paints();
			if ((stat_ == 1) || (stat_ == -1)) {
				try {
					Files.delete(Initialize.firstUser.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				};  
				
				Initialize.LockFile(true);
				System.exit(0);
			}
			
			rp.root.revalidate();
			rp.root.repaint();
			rp.tf.requestFocus();
		}
		
	}
	
	public Initialize() {
		try {
			this.initialize(this.checkRoot(), Initialize.pages);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getRootPartition() {
		return Initialize.rootPartition;
	}
	
	public static String getRootDir() {
		return Initialize.rootDir;
	}
}
