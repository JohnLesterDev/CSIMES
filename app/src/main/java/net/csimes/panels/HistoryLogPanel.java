package net.csimes.panels;

import java.io.*;													 
import java.awt.*;										 	         
import java.util.*;													 
import java.beans.*;													 
import javax.swing.*;												 
import java.awt.image.*;
import java.awt.event.*;											 
import javax.swing.text.*;											 
import javax.swing.event.*;											 
import javax.swing.table.*;										 
import javax.swing.border.*;

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.util.*;
import net.csimes.mouse.*;
import net.csimes.table.*;
import net.csimes.audio.*;
import net.csimes.splash.*;
import net.csimes.listeners.*;


/**
* InventoryPanel is a class inside the panels package that
* wraps and represents the main inventory JPanel of the MAINPAGE window. 
* This class inherits the JPanel class with extra methods to ensure the proper position,
* components and hotkeys for the inventory table and other UI components.
* This class will also connect and interact with the Inventory class and incorporates the 
* methods to represent the products and other information about the inventory.
*
* @author JohnLesterDev
* @version 0.7.7
*/
public class HistoryLogPanel {
	/** The MAINPAGE class that will be used.*/
	public MAINPAGE mainp;
	
	/** The main Sidebars class that will be used as the main panel.*/
	public Sidebars panel;
	
	/** The JTable that will be used for displaying information about the product*/
	public CTable table;
	
	/** The JScrollPane that will be used for the table field.*/
	public JScrollPane spane;
	
	
	/**
	* Creates an InventoryPanel instance with the MAINPAGE argument specified.
	*/
	public HistoryLogPanel(MAINPAGE mainpage) {
		this.mainp = mainpage;
		this.panel = this.mainp.createEmptyMainPane("inventorypanel");
		this.setPanel();
		this.setComponents();
		this.setHotkeys();
	}
	
	/**
	* Sets the Sidebars panel with the necessary positions and adding it to the 
	* HashMap fields of the MAINPAGE class inside the InventoryPanel instance.
	*/
	public void setPanel() {
		this.mainp.page.getContentPane().add(this.panel);
		this.mainp.mainPanels.put(this.panel.getName(), this.panel);
	}
	
	/**
	* Sets the components (JComponent) needed for the panel such as JTable and JLabels
	* that will be used to display crucial information about the inventory.
	* 
	*/
	public void setComponents() {
		this.table = new CTable(this.mainp);

		this.table.setModel(
			new CTableModel(this.mainp.getProducts(), new String[]{"Order ID", "Status", "Customer", "Cashier", "Total Sales", "Details", "Date"})
		);
		

		JTableHeader header = this.table.getTableHeader();
        header.setDefaultRenderer(new CHeaderRenderer());
		table.getTableHeader().setReorderingAllowed(false);

		CTableRenderer renderer = new CTableRenderer();
        table.setDefaultRenderer(Object.class, renderer);

		this.spane = new JScrollPane(this.table);
		this.spane.setName("tablespane3");
		
		Rectangle mpR = this.panel.getBounds();
		
		Rectangle tR = new Rectangle(
			(int) (((float) mpR.width) * 0.02),
			(int) (((float) mpR.height) * 0.131),
			(int) (((float) mpR.width) * 0.961),
			(int) (((float) mpR.height) * 0.819)
		);
		
		this.spane.setBounds(tR);
		this.panel.add(this.spane);
		this.mainp.components.put(this.spane.getName(), this.spane);
		
		Rectangle sR = new Rectangle(
			(int) (((float) mpR.width) * 0.742),
			(int) (((float) mpR.height) * 0.056),
			(int) (((float) mpR.width) * 0.221),
			(int) (((float) mpR.height) * 0.05)
		);
		
		Dimension sLD = new Dimension(
			(int) (((float) mpR.width) * 0.025),
			(int) (((float) mpR.height) * 0.040)
		);
		
		JTextField searchF = this.mainp.createTextField(this.panel, "search", (int) (((float) sR.height) * 0.65), sR, null);
		JLabel searchLogo = this.mainp.createLabel(searchF, new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/search.png")).getImage(), sLD.width, sLD.height)), "searchL", sLD);
		
		((AbstractDocument)searchF.getDocument()).putProperty("parent", searchF);
		((AbstractDocument)searchF.getDocument()).addDocumentListener(new SearchListener(this.table, this.mainp));
	
		this.panel.repaint();
	}
	
	/**
	* Sets the hotkeys (InputMap and ActionMap) for the components the panel itself.
	*/
	public void setHotkeys() {
		MAINPAGE tmpmp = this.mainp;
		
	}
}

