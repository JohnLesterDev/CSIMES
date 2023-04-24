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
import net.csimes.panels.*;
import net.csimes.splash.*;
import net.csimes.listeners.*;


public class PRDIDListener implements DocumentListener {
	
	public TransactionPanel tpl;
	
	public PRDIDListener(TransactionPanel tpl) {
		this.tpl = tpl;
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	public void insertUpdate(DocumentEvent e) {
		JTextField textField = (JTextField) e.getDocument().getProperty("parent");
		if (!textField.getText().equals("Product Code") && !textField.getText().equals("")) {
			this.tpl.getProductDetails(Integer.valueOf(textField.getText()));
		} else {
			 this.tpl.getProductDetails("hmm");
		}
	}

	public void removeUpdate(DocumentEvent e) {
		this.insertUpdate(e);
	}

	public void changedUpdate(DocumentEvent e) {		
		this.insertUpdate(e);
	}
}
