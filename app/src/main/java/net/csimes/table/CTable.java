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



public class CTable extends JTable {
	public MAINPAGE mainp;
	private String tableType;
	
    public CTable(MAINPAGE mainp) {
        super();
        
		this.mainp = mainp;
		this.tableType = "inventory";
    }
    
	public CTable(MAINPAGE mainp, String tableType) {
        super();
        
		this.mainp = mainp;
		this.tableType = tableType;
    }
    
    public void setColumnWidths() {
        if (this.tableType.equals("inventory")) {
			getColumnModel().getColumn(0).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.059));
			getColumnModel().getColumn(1).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.070));
			getColumnModel().getColumn(2).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.256));
			getColumnModel().getColumn(3).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.056));
			getColumnModel().getColumn(4).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.056));
			getColumnModel().getColumn(5).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.065));
			getColumnModel().getColumn(6).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.086));
			getColumnModel().getColumn(7).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.087));
			
			for (int i = 0; i < getColumnCount(); i++) {
				TableColumn column = getColumnModel().getColumn(i);
				column.setResizable(false);
			}
		} else if (this.tableType.equals("transaction")) {
			getColumnModel().getColumn(0).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.058));
			getColumnModel().getColumn(1).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.191));
			getColumnModel().getColumn(2).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.057));
			getColumnModel().getColumn(3).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.055));
			getColumnModel().getColumn(4).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.070));
			getColumnModel().getColumn(5).setPreferredWidth((int) (((float) this.mainp.rootWidth) * 0.069));
			
			for (int i = 0; i < getColumnCount(); i++) {
				TableColumn column = getColumnModel().getColumn(i);
				column.setResizable(false);
			}
		}
    }
}
