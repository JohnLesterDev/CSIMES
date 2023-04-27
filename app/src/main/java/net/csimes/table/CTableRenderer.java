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


public class CTableRenderer extends DefaultTableCellRenderer {
	
	private static final Font CELL_FONT = new Font("Corbel", Font.PLAIN, 13);
	private String tableType;

	public CTableRenderer() {
		this.tableType = "inventory";
	}
	
	public CTableRenderer(String tableType) {
		this.tableType = tableType;
	}

	
	public boolean isCellEditable(int row, int column) {
        return false; 
    }
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
		if (this.tableType.equals("inventory")) {
			if (column == 1 || column == 2) {
				setHorizontalAlignment(SwingConstants.LEFT);
			} else if (column == 0 || column == 3 || column == 5 || column == 6){
				setHorizontalAlignment(SwingConstants.RIGHT);
			} else {
				setHorizontalAlignment(SwingConstants.CENTER);
			}
		} else if (this.tableType.equals("transaction")) {
			if (column == 1) {
				setHorizontalAlignment(SwingConstants.LEFT);
			} else if (column == 0 || column == 3 || column == 4 || column == 5){
				setHorizontalAlignment(SwingConstants.RIGHT);
			} else {
				setHorizontalAlignment(SwingConstants.CENTER);
			}
		}
        
        return c;
    }

}
