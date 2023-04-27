package net.csimes.table;

import java.io.*;													 // Importing the java I/O package
import java.awt.*;										 	         // Importing the Abstract Window Toolkit package
import java.util.*;													 // Importing the java Utility package
import javax.swing.*;												 // Importing the java Swing package
import java.awt.image.*;
import java.awt.event.*;											 // Importing the AWT Event package
import javax.swing.text.*;											 // Importing the Text package from java Swing
import javax.swing.table.*;										 // Importing the Border package from java Swing
import javax.swing.border.*;										 // Importing the Border package from java Swing

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.util.*;
import net.csimes.mouse.*;
import net.csimes.audio.*;
import net.csimes.splash.*;
import net.csimes.listeners.*;


public class CHeaderRenderer extends DefaultTableCellRenderer {
	
	private String tableType;

	public CHeaderRenderer() {
		this.tableType = "inventory";
	}
	
	public CHeaderRenderer(String tableType) {
		this.tableType = tableType;
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
			if (this.tableType.equals("inventory")) {
				component.setBackground(new Color(144, 142, 151));
				component.setForeground(Color.black);
				component.setFont(new Font("Arial", Font.BOLD, 13));
				((DefaultTableCellRenderer) component).setHorizontalAlignment(SwingConstants.CENTER);
			} else if (this.tableType.equals("transaction")) {
				component.setBackground(new Color(144, 142, 151));
				component.setForeground(Color.black);
				component.setFont(new Font("Arial", Font.BOLD, 12));
				((DefaultTableCellRenderer) component).setHorizontalAlignment(SwingConstants.CENTER);
			} 
        
		return component;
	}
}
