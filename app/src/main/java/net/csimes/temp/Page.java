package net.csimes.temp;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.*;

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



public class Page extends JFrame {
	public String pageName;
	
	public Page clean() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().removeAll();
		removeListeners();
		revalidate();
		repaint();
		revalidate();
		repaint();
		
		return this;
	}
	
	public Page removeListeners() {
		for (WindowListener wl : getWindowListeners()) {
			removeWindowListener(wl);
		};	
		
		return this;
	}
	
	public Page(String name) {
		this.pageName = name;
		setName(name);
	}
	
}
