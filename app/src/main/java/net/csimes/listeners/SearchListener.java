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


public class SearchListener implements DocumentListener, ActionListener {
	
	public JTable table;
	public MAINPAGE mp_;
	
	public SearchListener(JTable table, MAINPAGE mp_) {
		this.table = table;
		this.mp_ = mp_;
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
	
	public void insertUpdate(DocumentEvent e) {
		JTextField textField = (JTextField) e.getDocument().getProperty("parent");
		this.mp_.filterRows(textField.getText());
	}

	public void removeUpdate(DocumentEvent e) {
		this.insertUpdate(e);
	}

	public void changedUpdate(DocumentEvent e) {
		
	}
}
