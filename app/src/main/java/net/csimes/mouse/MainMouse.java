package net.csimes.mouse;

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




public class MainMouse extends MouseAdapter {
	private JLabel label;
	private Page page;
	private String iconType;
	private HashMap<String,Component> components;
	
	public MainMouse(Page page, JLabel label, String iconType, HashMap<String,Component> components) {
		this.label = label;
		this.page = page;
		this.components = components;
		this.iconType = iconType;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (this.label.getName().equals("exits")) {
				Icon img = label.getIcon();
			int w = img.getIconWidth();
			int h = img.getIconHeight();
			label.setIcon(new ImageIcon(
				ImageControl.resizeImage(
					Initialize.icons.get("icons/" + this.iconType + "_selected.png").getImage(),
				w, h
			)));
			
			WindowEvent we = new WindowEvent(this.page, WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(we);
		} else if (this.label.getName().equals("hamburger")) {
			JPanel panel = (JPanel) this.components.get("sidebar");
			
			if (panel.isVisible()) {
				panel.setVisible(false);
			} else {
				panel.setVisible(true);
			}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (this.label.getName().equals("exits")) {
			Icon img = label.getIcon();
			int w = img.getIconWidth();
			int h = img.getIconHeight();
			label.setIcon(new ImageIcon(
				ImageControl.resizeImage(
					Initialize.icons.get("icons/" + this.iconType + "_clicked.png").getImage(),
				w, h
			)));
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if (this.label.getName().equals("exits")) {
			Icon img = label.getIcon();
			int w = img.getIconWidth();
			int h = img.getIconHeight();
			label.setIcon(new ImageIcon(
				ImageControl.resizeImage(
					Initialize.icons.get("icons/" + this.iconType + ".png").getImage(),
				w, h
			)));
		}
	}
}
