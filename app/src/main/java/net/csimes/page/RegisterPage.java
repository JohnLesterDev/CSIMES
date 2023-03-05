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
	
	public Page root;
	public HashMap<String,Component> components = new HashMap<String,Component>();
	public HashMap<String,Account> accounts = new HashMap<String,Account>();
	
	public JTextField tf;
	
	public int rootWidth, rootHeight;
	
	
	public JLabel createTextLabel(boolean isText, String name, int fontSize, Rectangle labelRect) {
		JLabel label = new JLabel(name);
		
		label.setBounds(labelRect);
		
		if (isText == true) {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
			label.setForeground(Color.white);
		}
		
		this.root.add(label);
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
		textField.setForeground(Color.white);
		textField.setCaretColor(Color.white);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setFocusable(true);
		textField.setBorder(BorderFactory.createMatteBorder(0, 0, (int) ((float) fieldRect.height * 0.06), 0, Color.white));
		
		RegisterListener rl = new RegisterListener(this);
		textField.addActionListener(rl);
		((AbstractDocument)textField.getDocument()).addDocumentListener(rl);
		
		this.root.add(textField);
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
		textField.setForeground(Color.white);
		textField.setCaretColor(Color.white);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setFocusable(true);
		textField.setBorder(BorderFactory.createMatteBorder(0, 0, (int) ((float) fieldRect.height * 0.06), 0, Color.white));
		RegisterListener rl = new RegisterListener(this);
		textField.addActionListener(rl);
		((AbstractDocument)textField.getDocument()).addDocumentListener(rl);
		textField.setEchoChar('.');
		
		this.root.add(textField);
		this.components.put(textField.getName(), textField);
		
		return textField;
	}
	
	public void paints() {
		// register rect
		int regisY = (int) (((float) this.rootHeight) * 0.1);
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
		int fpassY = passY - (int) (((float) passY) * 0.12);
		int fpassFont = (int) (((float) this.rootHeight) * 0.05);
		
		
		JLabel register = this.createTextLabel(true, "Register", regisFont, new Rectangle(regisX, regisY, regisW, regisH));
		register.setToolTipText("Register Text Title :>");
		
		JLabel username = this.createTextLabel(true, "Username:", usrFont, new Rectangle(usrX, usrY, usrW, usrH));
		JLabel passwd = this.createTextLabel(true, "Password:", passFont, new Rectangle(passX, passY, passW, passH));
		username.setToolTipText("Username Label :>");
		passwd.setToolTipText("Password Label :>");
		
		JTextField userfield = this.createTextField("usernamefield", fusrFont, new Rectangle(fusrX, fusrY, fusrW, fusrH));
		JTextField passfield = this.createPasswordField("passfield", fpassFont, new Rectangle(fpassX, fpassY, fpassW, fpassH));
		userfield.setToolTipText("<html> Enter your Username here. <br>" +
			"A Username must only consists <br> of 16 alphanumerical " +
			"characters. <br></html>"
		);
		passfield.setToolTipText("<html> Enter your Password here. <br>" +
			"A Password must only consists <br> of 16 alphanumerical " +
			"characters. <br></html>"
		);
		
		this.tf = userfield;
		
	}
	
	
	public RegisterPage(Page page) {
		this.root = page;
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.rootWidth = (int) (((float) dimension.width) * 0.25);
		this.rootHeight = (int) (((float) dimension.height) * 0.35);
		
		System.out.printf("Width: %d, Height: %d \n", this.rootWidth, this.rootHeight);
		this.root.getContentPane().setBackground(new Color(18, 18, 18));
		this.root.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.root.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
					String msg_ = "You have not created your first user account. Do you wish to exit?";
					int stat_ = JOptionPane.showOptionDialog(null,
								msg_, 
								"CSIMES - Exiting Registration", 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE,
								new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35)),
								new String[]{"Exit", "Cancel"},
								"Cancel"
					);
					if (stat_ == 0) {
						JFrame root = (JFrame) we.getSource();
						root.dispose();
				}
			}
			
			public void windowClosed(WindowEvent we) {
				Initialize.LockFile(true);
			}
		});
		
		this.root.setSize(rootWidth, rootHeight);
		this.root.setTitle("CSIMES - Register");
		this.root.setLayout(null);
		this.root.setResizable(false);
		this.root.setIconImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_cropped.png")).getImage());
		
		this.root.setLocationRelativeTo(null);
	}
}
