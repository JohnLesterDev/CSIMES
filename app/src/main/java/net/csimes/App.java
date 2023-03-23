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
    
    public static void main(String[] args) {
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
		
		/*ArrayList<String> categoryList = new ArrayList<String>();
        ArrayList<String> descriptionList = new ArrayList<String>();
        ArrayList<Integer> quantityList = new ArrayList<Integer>();
        ArrayList<Float> priceList = new ArrayList<Float>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\CSIMES\\inventory.txt"));
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
			}  */
    }
}
