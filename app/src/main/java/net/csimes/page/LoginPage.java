package net.csimes.page;

// Importing the necessary packages
import java.io.*;													 // Importing the java I/O package
import java.awt.*;										 	         // Importing the Abstract Window Toolkit package
import java.util.*;													 // Importing the java Utility package
import javax.swing.*;												 // Importing the java Swing package
import java.awt.image.*;
import java.awt.event.*;											 // Importing the AWT Event package
import javax.swing.text.*;											 // Importing the Text package from java Swing
import javax.swing.border.*;										 // Importing the Border package from java Swing

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.util.*;
import net.csimes.splash.*;
import net.csimes.listeners.*;


public class LoginPage {
	
	public RegisterPage rp;
	public int rootWidth, rootHeight;
	public HashMap<String,Component> components = new HashMap<String,Component>();
	public HashMap<String,Account> accounts = new HashMap<String,Account>();
	
	
	public LoginPage(RegisterPage rp) {
		this.rp = rp;
		this.loginDesign();
	}
	
	public JLabel createTextLabel(boolean isText, String name, int fontSize, Rectangle labelRect) {
		JLabel label = new JLabel(name);
		
		label.setBounds(labelRect);
		
		if (isText == true) {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
		}
		
		this.rp.root.add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	
	public void loginDesign() {
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.rootWidth = (int) (((float) dimension.width) * 0.25);
		this.rootHeight = (int) (((float) dimension.height) * 0.35);
		
		this.rp.root.setTitle("CSIMES - Login");
		this.rp.root.setSize(this.rootWidth, this.rootHeight);
		this.rp.root.setLocationRelativeTo(null);
	}
}