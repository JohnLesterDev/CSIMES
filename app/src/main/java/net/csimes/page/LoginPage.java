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
	
	public Page page;
	public int rootWidth, rootHeight;
	public HashMap<String,Component> components = new HashMap<String,Component>();
	public HashMap<String,Account> accounts = new HashMap<String,Account>();
	
	
	public LoginPage(Page page) {
		this.page = page;
		this.loginDesign();
	}
	
	public JLabel createTextLabel(boolean isText, String name, int fontSize, Rectangle labelRect) {
		JLabel label = new JLabel(name);
		
		label.setBounds(labelRect);
		
		if (isText == true) {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
		}
		
		this.page.getContentPane().add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	public JTextField createTextField(String name, int fontSize, Rectangle fieldRect) {
		JTextField textField = new JTextField();
		DocumentFilter dfilter = new MultiDocumentFilter(16);
		
		DocumentFilter oldFilter = ((AbstractDocument)textField.getDocument()).getDocumentFilter();
		if (oldFilter != null) {
			((AbstractDocument)textField.getDocument()).setDocumentFilter(null);
		}
		
		((AbstractDocument)textField.getDocument()).setDocumentFilter(dfilter);
		((AbstractDocument)textField.getDocument()).putProperty("parent", textField);
		
		textField.setName(name);
		textField.setPreferredSize(new Dimension(fieldRect.width, fieldRect.height));
		textField.setFont(new Font("Arial", Font.PLAIN, fontSize));
		textField.setBounds(fieldRect);
		textField.setOpaque(false);
		textField.setForeground(Color.black);
		textField.setCaretColor(Color.black);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setFocusable(true);
		textField.setBorder(BorderFactory.createMatteBorder(0, 0, (int) ((float) fieldRect.height * 0.06), 0, Color.black));
		
		LoginListener rl = new LoginListener(this);
		textField.addActionListener(rl);
		((AbstractDocument)textField.getDocument()).addDocumentListener(rl);
		
		this.page.getContentPane().add(textField);
		this.components.put(textField.getName(), textField);
		
		return textField;
	}
	
	public JPasswordField createPasswordField(String name, int fontSize, Rectangle fieldRect) {
		JPasswordField textField = new JPasswordField();
		DocumentFilter dfilter = new MultiDocumentFilter(16);
		
		DocumentFilter oldFilter = ((AbstractDocument)textField.getDocument()).getDocumentFilter();
		if (oldFilter != null) {
			((AbstractDocument)textField.getDocument()).setDocumentFilter(null);
		}
		
		((AbstractDocument)textField.getDocument()).setDocumentFilter(dfilter);
		((AbstractDocument)textField.getDocument()).putProperty("parent", textField);
		
		textField.setName(name);
		textField.setPreferredSize(new Dimension(fieldRect.width, fieldRect.height));
		textField.setFont(new Font("Arial", Font.PLAIN, fontSize));
		textField.setBounds(fieldRect);
		textField.setOpaque(false);
		textField.setForeground(Color.black);
		textField.setCaretColor(Color.black);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setFocusable(true);
		textField.setBorder(BorderFactory.createMatteBorder(0, 0, (int) ((float) fieldRect.height * 0.06), 0, Color.black));
		LoginListener rl = new LoginListener(this);
		textField.addActionListener(rl);
		((AbstractDocument)textField.getDocument()).addDocumentListener(rl);
		textField.setEchoChar('.');
		
		this.page.getContentPane().add(textField);
		this.components.put(textField.getName(), textField);
		
		return textField;
	}
	
	public void paints() {
		// register rect
		int regisY = (int) (((float) this.rootHeight) * 0.1);
		int regisW = (int) (((float) this.rootWidth) * 0.23);
		int regisH = (int) (((float) this.rootHeight) * 0.1);
		int regisX = (int) (((float) this.rootWidth) * 0.5) - (regisW /2);
		int regisFont = (int) (((float) this.rootHeight) * 0.09);
		
		JLabel register = this.createTextLabel(true, "Login", regisFont, new Rectangle(regisX, regisY, regisW, regisH));
		register.setToolTipText("Register Text Title :>");
		
	}
	
	
	
	public void loginDesign() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.rootWidth = (int) (((float) dimension.width) * 0.25);
		this.rootHeight = (int) (((float) dimension.height) * 0.35);
		
		this.page.setTitle("CSIMES - Login");
		this.page.setSize(this.rootWidth, this.rootHeight);
		this.page.setLocationRelativeTo(null);
	}
}