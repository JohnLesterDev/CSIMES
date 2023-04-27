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
public class DashboardPanel {
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
	public DashboardPanel(MAINPAGE mainpage) {
		this.mainp = mainpage;
		this.panel = this.mainp.createEmptyMainPane("dashboardpanel");
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
		Rectangle mpR = this.panel.getBounds();
		
		Rectangle dashboardButtonR = new Rectangle(
			(int) (((float) mpR.width) * 0.087),
			(int) (((float) mpR.height) * 0.204),
			(int) (((float) mpR.width) * 0.238),
			(int) (((float) mpR.height) * 0.222)
		);

		RoundedLabel dashboardButton = this.mainp.createRoundedLabel(
			this.panel, 
			"dashboard-dashbtn",
			dashboardButtonR,
			45, 45, 
			() -> {this.mainp.switchMainPanel("dashboardpanel", false);},
			144, 142, 151
		);
		
		Rectangle dashboardButtonLogoR = new Rectangle(
			(int) (((float) dashboardButtonR.width) * 0.395),
			(int) (((float) dashboardButtonR.height) * 0.130),
			(int) (((float) dashboardButtonR.width) * 0.206),
			(int) (((float) dashboardButtonR.height) * 0.325)
		);

		JLabel dashboardButtonLogo = this.mainp.createLabel(
			dashboardButton, 
			new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/home.png")).getImage(), dashboardButtonLogoR.width, dashboardButtonLogoR.height)), 
			"dashLogo", 
			dashboardButtonLogoR
		);
		Rectangle dashboardButtonTextR = new Rectangle(
			(int) (((float) dashboardButtonR.width) * 0.076),
			(int) (((float) dashboardButtonR.height) * 0.561),
			(int) (((float) dashboardButtonR.width) * 0.843),
			(int) (((float) dashboardButtonR.height) * 0.260)
		);
		
		JLabel dashboardButtonText = this.mainp.createLabel(false, dashboardButton, "Dashboard", (int) (((float) dashboardButtonTextR.height) * 0.65), dashboardButtonTextR); 
		
		
		this.panel.repaint();
	}
	
	/**
	* Sets the hotkeys (InputMap and ActionMap) for the components the panel itself.
	*/
	public void setHotkeys() {
		MAINPAGE tmpmp = this.mainp;
		
	}
}

