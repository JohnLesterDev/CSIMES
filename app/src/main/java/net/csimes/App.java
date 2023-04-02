package net.csimes;

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
import net.csimes.util.*;
import net.csimes.mouse.*;
import net.csimes.table.*;
import net.csimes.audio.*;
import net.csimes.panels.*;
import net.csimes.splash.*;
import net.csimes.listeners.*;




public class App {
    
	public static void test() {
		
	}
	
	
	public static void start() {
		System.out.println(System.getProperty("java.io.tmpdir"));	

		Initialize.LockFile(() -> {
			new Initialize();
		}, () -> {
			String msg_ = "Another instance of CSIMES is already running. Please close it and try again.";
			JOptionPane.showMessageDialog(null,
						msg_, 
						"CSIMES - Another CSIMES Instance Running", 
						JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
						);
		});
	}
	
	
	public static void fetchInventory(String path) {
		ArrayList<String> categoryList = new ArrayList<String>();
        ArrayList<String> descriptionList = new ArrayList<String>();
        ArrayList<Integer> quantityList = new ArrayList<Integer>();
        ArrayList<Float> priceList = new ArrayList<Float>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split("\\|");
                categoryList.add(row[0].trim());
                descriptionList.add(row[1].trim());
                quantityList.add(Integer.parseInt(row[2].trim()));
                priceList.add(Float.parseFloat(row[3].replace("$", "").trim()));
            }
            reader.close();
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
			
			for (int i = 0; i < categoryList.size(); i++) {
				ProductIO.write(new SecurityControl(
					new Product(
						i,
						categoryList.get(i),
						descriptionList.get(i),
						quantityList.get(i),
						priceList.get(i)
					)
				).encryptProduct(), Inventory.inventoryPath);
			}  
	}

	
	public static void printDeviceInfo() {
		String os = System.getProperty("os.name");
		String osVersion = System.getProperty("os.version");
		String deviceName = System.getProperty("user.name");
		String deviceBrand = System.getenv("USERDOMAIN");
		String cpuArch = System.getProperty("os.arch");		
		String cpuInfo = System.getenv("PROCESSOR_IDENTIFIER");
		
		Runtime rt = Runtime.getRuntime();
		String pid = String.valueOf(ProcessHandle.current().pid());
		
		System.out.println("\n\nDEVELOPER INFORMATION:\n--------------------------\n");
		System.out.println("OS:        " + os + " (" + osVersion + ")");
		System.out.println("Device:    " + deviceName + " (" + deviceBrand + ")");
		System.out.println("CPU:       " + cpuArch + " | " + cpuInfo);
		System.out.println("PID:       " + pid);
		System.out.println("\n--------------------------\n\n");
	}
	
	
    public static void main(String[] args) {	
		printDeviceInfo();
		
		if (args.length == 0) {
			start();
			return;
		}
		
		String filePath = "";
		boolean fileMode = false, testMode = false, startMode = false;
		
		for (String arg : args) {
			if (arg.endsWith(".txt")) {
				fileMode = true;
				filePath = arg;
			}
			if (arg.equals("test")) {
				testMode = true;
			}
			if (arg.equals("run")) {
				startMode = true;
			}
		}
		
		if (!fileMode && !testMode && !startMode) {
			System.out.println("[]> Argument Error: Invalid argument/s provided.");
		}
		
		if (testMode) {
			test();
			System.out.println("\n\n--------------------------\nTest/s Completed :> \t\n--------------------------\n\n");
		}
		if (fileMode) {
			fetchInventory(filePath);
			System.out.println("\n\n--------------------------\nFetching Inventory Completed :> \t\n--------------------------\n\n");
		}
		if (startMode) {
			start();
		}
    }
}
