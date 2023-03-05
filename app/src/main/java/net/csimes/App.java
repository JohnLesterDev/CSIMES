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
import net.csimes.splash.*;



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
    }
}
