package net.csimes.listeners;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.event.*;

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


public class LoginListener implements DocumentListener, ActionListener {
	private LoginPage rp;
	
	public LoginListener(LoginPage rp) {
		this.rp = rp;
	}
	
	public void actionPerformed(ActionEvent e) {
		JTextField txt = (JTextField) e.getSource();
		
		if (txt.getName().equals("usernamefield")) {
			if (!txt.getText().equals("")) {
				txt.transferFocus();
			}
		} else {
			JTextField usrF = (JTextField) rp.components.get("passfield");
			JPasswordField passF = (JPasswordField) e.getSource();
			
			if (!String.valueOf(passF.getPassword()).equals("")) {
				
			}
		}   
	}
	
	public void insertUpdate(DocumentEvent e) {
		JTextField textField = (JTextField) e.getDocument().getProperty("parent");
		if (textField.getName().equals("usernamefield")) {
			if (textField.getText().equals("")) {
				this.changeIcon("normal", true, (JLabel) rp.components.get("usernamelogo"));
				return;
			}
			
			if (LoginPage.accounts.containsKey(textField.getText())) {
				this.changeIcon("checked", true, (JLabel) rp.components.get("usernamelogo"));
				textField.transferFocus();
				return;
			} else if (textField.getText().equals("Username")){
				this.changeIcon("normal", true, (JLabel) rp.components.get("usernamelogo"));
				return;
			} else {
				this.changeIcon("wrong", true, (JLabel) rp.components.get("usernamelogo"));
				return;
			}
		} else {
			JPasswordField txt = (JPasswordField) e.getDocument().getProperty("parent");
			textField = (JTextField) rp.components.get("usernamefield");
			String nPass = SecurityControl.toMD5(String.valueOf(txt.getPassword()));

			if (String.valueOf(txt.getPassword()).equals("")) {
				this.changeIcon("normal", false, (JLabel) rp.components.get("passwdlogo"));
				return;
			}
			if (LoginPage.accounts.containsKey(textField.getText())) {
				if (LoginPage.accounts.get(textField.getText()).getPasswd().equals(nPass)) {
					this.changeIcon("checked", false, (JLabel) rp.components.get("passwdlogo"));
					return;
				} else if (String.valueOf(txt.getPassword()).equals("Password")){
					this.changeIcon("normal", true, (JLabel) rp.components.get("passwdlogo"));
					return;
				} else {
					this.changeIcon("wrong", false, (JLabel) rp.components.get("passwdlogo"));
					return;
				}
			}
		}
	}
	
	public void removeUpdate(DocumentEvent e) {
		JTextField textField = (JTextField) e.getDocument().getProperty("parent");
		if (textField.getName().equals("usernamefield")) {
			if (textField.getText().equals("")) {
				this.changeIcon("normal", true, (JLabel) rp.components.get("usernamelogo"));
				return;
			}
			
			if (LoginPage.accounts.containsKey(textField.getText())) {
				this.changeIcon("checked", true, (JLabel) rp.components.get("usernamelogo"));
				textField.transferFocus();
				return;
			} else if (textField.getText().equals("Username")){
				this.changeIcon("normal", true, (JLabel) rp.components.get("usernamelogo"));
				return;
			} else {
				this.changeIcon("wrong", true, (JLabel) rp.components.get("usernamelogo"));
				return;
			}
		} else {
			JPasswordField txt = (JPasswordField) e.getDocument().getProperty("parent");
			textField = (JTextField) rp.components.get("usernamefield");
			String nPass = SecurityControl.toMD5(String.valueOf(txt.getPassword()));

			if (String.valueOf(txt.getPassword()).equals("")) {
				this.changeIcon("normal", false, (JLabel) rp.components.get("passwdlogo"));
				return;
			}
			if (LoginPage.accounts.containsKey(textField.getText())) {
				if (LoginPage.accounts.get(textField.getText()).getPasswd().equals(nPass)) {
					this.changeIcon("checked", false, (JLabel) rp.components.get("passwdlogo"));
					return;
				} else if (String.valueOf(txt.getPassword()).equals("Password")){
					this.changeIcon("normal", true, (JLabel) rp.components.get("passwdlogo"));
					return;
				} else {
					this.changeIcon("wrong", false, (JLabel) rp.components.get("passwdlogo"));
					return;
				}
			}
		}
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		JTextField textField = (JTextField) e.getDocument().getProperty("parent");
		if (textField.getName().equals("usernamefield")) {
			if (textField.getText().equals("")) {
				this.changeIcon("normal", true, (JLabel) rp.components.get("usernamelogo"));
				return;
			}
			
			if (LoginPage.accounts.containsKey(textField.getText())) {
				this.changeIcon("checked", true, (JLabel) rp.components.get("usernamelogo"));
				textField.transferFocus();
				return;
			} else if (textField.getText().equals("Username")){
				this.changeIcon("normal", true, (JLabel) rp.components.get("usernamelogo"));
				return;
			} else {
				this.changeIcon("wrong", true, (JLabel) rp.components.get("usernamelogo"));
				return;
			}
		} else {
			JPasswordField txt = (JPasswordField) e.getDocument().getProperty("parent");
			textField = (JTextField) rp.components.get("usernamefield");
			String nPass = SecurityControl.toMD5(String.valueOf(txt.getPassword()));

			if (String.valueOf(txt.getPassword()).equals("")) {
				this.changeIcon("normal", false, (JLabel) rp.components.get("passwdlogo"));
				return;
			}
			if (LoginPage.accounts.containsKey(textField.getText())) {
				if (LoginPage.accounts.get(textField.getText()).getPasswd().equals(nPass)) {
					this.changeIcon("checked", false, (JLabel) rp.components.get("passwdlogo"));
					return;
				} else if (String.valueOf(txt.getPassword()).equals("Password")){
					this.changeIcon("normal", true, (JLabel) rp.components.get("passwdlogo"));
					return;
				} else {
					this.changeIcon("wrong", false, (JLabel) rp.components.get("passwdlogo"));
					return;
				}
			}
		}
	}
	
	public void changeIcon(String icontype, boolean isUser, JLabel label) {
		Icon img = label.getIcon();
		int w = img.getIconWidth();
		int h = img.getIconHeight();
		
		if (isUser) {
				switch (icontype) {
				case "checked":
					label.setIcon(new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/acc_checked.png").getImage(), w, h)));
					break;
				case "wrong":
					label.setIcon(new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/acc_wrong.png").getImage(), w, h)));
					break;
				case "normal":
					label.setIcon(new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/acc_normal.png").getImage(), w, h)));
					break;
				
				}
		} else {
			switch (icontype) {
			case "checked":
				label.setIcon(new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/pass_checked.png").getImage(), w, h)));
				break;
			case "wrong":
				label.setIcon(new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/pass_wrong.png").getImage(), w, h)));
				break;
			case "normal":
				label.setIcon(new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/pass_normal.png").getImage(), w, h)));
				break;
			
			}
		}
	}
	
}
