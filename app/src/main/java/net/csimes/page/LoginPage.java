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
	public static HashMap<String,Account> accounts = LoginPage.getAccounts();
	
	
	public LoginPage(Page page) {
		this.page = page;
		this.loginDesign();
	}
	
	public static HashMap<String,Account> getAccounts() {
		HashMap<String,Account> accs = new HashMap<String,Account>();
		
		File[] files = new File(Initialize.rootAccPath).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				Account acc = ReadAccount.read(file, true);
				accs.put(acc.getUserName(), acc);
			}
		};
		
		return accs;
	}
	private static HashMap<String,Account> getResourceAccounts() {
		HashMap<String,Account> accs = new HashMap<String,Account>();
		
		ArrayList<String> accsPath = new ArrayList<String>();
		ResourceControl.getResourceList(accsPath, "accounts");
		
		for (Object str : accsPath.toArray()) {
			
		};
		
		return accs;
	}
	
	public JLabel createTextLabel(boolean isText, String name, int fontSize, Rectangle labelRect) {
		JLabel label = new JLabel(name);
		
		label.setBounds(labelRect);
		
		if (isText == true) {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
			label.setForeground(Color.white);
		}
		
		this.page.getContentPane().add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	public JLabel createIconLabel(String name, String iconName, Rectangle labelRect) {
		JLabel label = new JLabel(name);
		label.setName(name);
		
		label.setBounds(labelRect);
		label.setIcon(new ImageIcon(ImageControl.resizeImage(Initialize.icons.get(iconName).getImage(), labelRect.width, labelRect.height)));
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
		textField.setForeground(Color.white);
		textField.setCaretColor(Color.white);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setFocusable(true);
		textField.setBorder(BorderFactory.createMatteBorder(0, 0, (int) ((float) fieldRect.height * 0.06), 0, Color.white));
		
		LoginListener rl = new LoginListener(this);
		textField.addActionListener(rl);
		((AbstractDocument)textField.getDocument()).addDocumentListener(rl);
		
		textField.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (textField.getText().trim().equals("Username")) {
						textField.setText("");
					}
					
					textField.setForeground(Color.white);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (textField.getText().trim().equals("")) {
						textField.setText("Username");
					}
					
					textField.setForeground(Color.lightGray);
				}
			});
		
		this.page.getContentPane().add(textField);
		this.components.put(textField.getName(), textField);
		
		return textField;
	}
	
	public JPasswordField createPasswordField(String name, int fontSize, Rectangle fieldRect) {
		JPasswordField textField = new JPasswordField();
		DocumentFilter dfilter = new MultiDocumentFilter(23);
		
		DocumentFilter oldFilter = ((AbstractDocument)textField.getDocument()).getDocumentFilter();
		if (oldFilter != null) {
			((AbstractDocument)textField.getDocument()).setDocumentFilter(null);
		}
		
		((AbstractDocument)textField.getDocument()).setDocumentFilter(dfilter);
		((AbstractDocument)textField.getDocument()).putProperty("parent", textField);
		
		textField.setName(name);
		textField.setText("Password");
		textField.setPreferredSize(new Dimension(fieldRect.width, fieldRect.height));
		textField.setFont(new Font("Arial", Font.PLAIN, fontSize));
		textField.setBounds(fieldRect);
		textField.setOpaque(false);
		textField.setForeground(Color.white);
		textField.setCaretColor(Color.white);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setFocusable(true);
		textField.setBorder(BorderFactory.createMatteBorder(0, 0, (int) ((float) fieldRect.height * 0.06), 0, Color.white));
		LoginListener rl = new LoginListener(this);
		textField.addActionListener(rl);
		((AbstractDocument)textField.getDocument()).addDocumentListener(rl);
		textField.setEchoChar((char)0);
		
		textField.addFocusListener(new FocusAdapter() {  // Our FocusAdapter class (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (String.valueOf(textField.getPassword()).trim().equals("Password")) {
						textField.setText("");
					}
					
					textField.setEchoChar('.');
					textField.setForeground(Color.white);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (String.valueOf(textField.getPassword()).trim().equals("Password") || String.valueOf(textField.getPassword()).trim().equals("")) {
						textField.setText("Password");
						textField.setEchoChar((char)0);
					}
					
					textField.setForeground(Color.lightGray);
				}
			});
		
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
		
		// username logo rect
		int usrlW = (int) (((float) this.rootWidth) * 0.1);
		int usrlH = (int) (((float) this.rootWidth) * 0.1);
		
		// passwd logo rect
		int passlW = (int) (((float) this.rootWidth) * 0.1);
		int passlH = (int) (((float) this.rootWidth) * 0.1);
		
		// userfield rect
		int usrfW = (int) (((float) this.rootWidth) * 0.45);
		int usrfH = (int) (((float) this.rootWidth) * 0.1);
		int usrfFont = (int) (((float) this.rootHeight) * 0.05);
		
		// passfield rect
		int passfW = (int) (((float) this.rootWidth) * 0.45);
		int passfH = (int) (((float) this.rootWidth) * 0.1);
		int passfFont = (int) (((float) this.rootHeight) * 0.05);
		
		int offset = (int) (((float) this.rootWidth) * 0.04);
		int pairW = usrlW + offset + usrfW;
		
		int usrlX = (this.rootWidth - pairW) / 2;
		int usrfX = usrlX + usrlW + offset;
		int passlX = usrlX;
		int passfX = usrfX;
		
		int usrlY = (int) (((float) this.rootHeight) / 2) - (int)(usrlH * 1.3);
		int usrfY = usrlY - (int) (((float) usrlY) * 0.08);
		int passlY = (int) (((float) this.rootHeight) / 2) - (int) (((float) this.rootHeight) * 0.04);
		int passfY = passlY - (int) (((float) passlY) * 0.08);
		
		
		JLabel login = this.createTextLabel(true, "Login", regisFont, new Rectangle(regisX, regisY, regisW, regisH));
		login.setToolTipText("Login Text Title :>");
		
		JLabel userLogo = this.createIconLabel("usernamelogo", "icons/acc_normal.png", new Rectangle(usrlX, usrlY, usrlW, usrlH));
		userLogo.setToolTipText("<html>Icon that changes whether <br> or not the username is valid.</html>");
		JLabel passLogo = this.createIconLabel("passwdlogo", "icons/pass_normal.png", new Rectangle(passlX, passlY, passlW, passlH));
		passLogo.setToolTipText("<html>Icon that changes whether <br> or not the password is valid.</html>");
		
		JTextField userfield = this.createTextField("usernamefield", usrfFont, new Rectangle(usrfX, usrfY, usrfW, usrfH));
		userfield.setToolTipText("<html> Enter your valid Username here.</html>"
		);
		
		JTextField passfield = this.createPasswordField("passfield", passfFont, new Rectangle(passfX, passfY, passfW, passfH));
		passfield.setToolTipText("<html> Enter your valid Password here.</html>"
		);

	}
	
	
	
	public void loginDesign() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.rootWidth = (int) (((float) dimension.width) * 0.25);
		this.rootHeight = (int) (((float) dimension.height) * 0.35);
		
		this.page.getContentPane().setBackground(new Color(18, 18, 18));
		this.page.setTitle("CSIMES - Login");
		this.page.setSize(this.rootWidth, this.rootHeight);
		this.page.setLocationRelativeTo(null);
		this.page.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.page.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
					String msg_ = "Do you wish to exit?";
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
						Initialize.LockFile(true);
						root.dispose();
				}
			}
			
			public void windowClosed(WindowEvent we) {
				Initialize.LockFile(true);
			}
		});
	}
}