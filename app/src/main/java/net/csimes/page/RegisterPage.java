package net.csimes.page;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;

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


public class RegisterPage {
	
	public JFrame root;
	public HashMap<String,Component> components = new HashMap<String,Component>();
	public HashMap<String,Account> accounts = new HashMap<String,Account>();
	
	public int rootWidth, rootHeight;
	
	
	public JLabel createTextLabel(boolean isText, String name, int fontSize, Rectangle labelRect) {
		JLabel label = new JLabel(name);
		
		label.setBounds(labelRect);
		
		if (isText == true) {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
		}
		
		this.root.add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	
	public JTextField createTextField(String name, int fontSize, Rectangle fieldRect) {
		JTextField textField = new JTextField();
		((AbstractDocument)textField.getDocument()).setDocumentFilter(new DocumentLimiter(15));
		
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
		textField.addActionListener(new RegisterListener(this));
		
		this.root.add(textField);
		this.components.put(textField.getName(), textField);
		
		return textField;
	}
	
	public JPasswordField createPasswordField(String name, int fontSize, Rectangle fieldRect) {
		JPasswordField textField = new JPasswordField();
		((AbstractDocument)textField.getDocument()).setDocumentFilter(new DocumentLimiter(15));
		
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
		textField.addActionListener(new RegisterListener(this));
		textField.setEchoChar('.');
		
		this.root.add(textField);
		this.components.put(textField.getName(), textField);
		
		return textField;
	}
	
	
	public RegisterPage() {
		this.root = new JFrame();
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.rootWidth = (int) (((float) dimension.width) * 0.25);
		this.rootHeight = (int) (((float) dimension.height) * 0.35);
		
		System.out.printf("Width: %d, Height: %d \n", this.rootWidth, this.rootHeight);
		
		this.root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// register rect
		int regisY = (int) (((float) this.rootHeight) * 0.06);
		int regisW = (int) (((float) this.rootWidth) * 0.35);
		int regisH = (int) (((float) this.rootHeight) * 0.1);
		int regisX = (int) (((float) this.rootWidth) * 0.5) - (regisW /2);
		int regisFont = (int) (((float) this.rootHeight) * 0.09);
		
		// Username rect
		int usrX = (int) (((float) this.rootWidth) * 0.14);
		int usrW = (int) (((float) this.rootWidth) * 0.3);
		int usrH = (int) (((float) this.rootHeight) * 0.08);
		int usrY = (int) (((float) this.rootHeight) / 2) - (usrH * 2);
		int usrFont = (int) (((float) this.rootHeight) * 0.05);
		
		// Password rect
		int passX = (int) (((float) this.rootWidth) * 0.14);
		int passW = (int) (((float) this.rootWidth) * 0.3);
		int passH = (int) (((float) this.rootHeight) * 0.08);
		int passY = (int) (((float) this.rootHeight) / 2) - (int) (((float) this.rootHeight) * 0.04);
		int passFont = (int) (((float) this.rootHeight) * 0.05);
		
		
		// Username field rect
		int fusrX = usrX + usrW - (int) (((float) usrX) * 0.41);
		int fusrW = (int) (((float) this.rootWidth) * 0.45);
		int fusrH = (int) (((float) this.rootHeight) * 0.1);
		int fusrY = usrY - (int) (((float) usrY) * 0.08);
		int fusrFont = (int) (((float) this.rootHeight) * 0.05);
		
		// Password field rect
		int fpassX = passX + passW - (int) (((float) passX) * 0.41);
		int fpassW = (int) (((float) this.rootWidth) * 0.45);
		int fpassH = (int) (((float) this.rootHeight) * 0.1);
		int fpassY = passY - (int) (((float) passY) * 0.08);
		int fpassFont = (int) (((float) this.rootHeight) * 0.05);
		
		
		JLabel register = this.createTextLabel(true, "Register", regisFont, new Rectangle(regisX, regisY, regisW, regisH));
		
		JLabel username = this.createTextLabel(true, "Username:", usrFont, new Rectangle(usrX, usrY, usrW, usrH));
		JLabel passwd = this.createTextLabel(true, "Password:", passFont, new Rectangle(passX, passY, passW, passH));
		
		JTextField userfield = this.createTextField("usernamefield", fusrFont, new Rectangle(fusrX, fusrY, fusrW, fusrH));
		JTextField passfield = this.createPasswordField("passfield", fpassFont, new Rectangle(fpassX, fpassY, fpassW, fpassH));
		
		this.root.setSize(rootWidth, rootHeight);
		this.root.setTitle("CSIMES - Register");
		this.root.setLayout(null);
		this.root.setResizable(false);
		this.root.setIconImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full.png")).getImage());
		
		this.root.setLocationRelativeTo(null);
	}
}
