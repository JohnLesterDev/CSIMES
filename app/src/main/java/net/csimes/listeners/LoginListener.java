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
				Account acc = new SecurityControl(new Account().setUserName(usrF.getText()).setPasswd(String.valueOf(passF.getPassword())).setAccountType(1)).encryptAccount();
			
				File ownerAcc = new File(Initialize.rootAccPath + File.separator + acc.getUserName());
				
				try {
				ownerAcc.createNewFile();
				} catch (IOException ex) {
					ex.printStackTrace();
				};
				
				String path = Initialize.rootAccPath;
				WriteAccount.write(acc, path);
				
				this.rp.page.getContentPane().removeAll();
				this.rp.page.revalidate();
				this.rp.page.repaint();
				
				new LoginPage(Initialize.pages.get("MAIN"));
			}
		}   
	}
	
	public void insertUpdate(DocumentEvent e) {
		
	}
	
	public void removeUpdate(DocumentEvent e) {
		
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		
	}
	
}
