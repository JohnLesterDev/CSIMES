package net.csimes.page;

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




public class MAINPAGE {
	public Page page;
	public int rootWidth, rootHeight;
	
	private Point mOffset;
	
	public HashMap<String,Component> components = new HashMap<String,Component>();
	
	
	public void paints() {
		//GreyBG rect
		int greyBgX = -10;
		int greyBgY = -10;
		int greyBgW = rootWidth + rootWidth;
		int greyBgH = (int) (((float) this.rootHeight) * 0.13);
		
		// Exit rect
		int exW = (int) (((float) this.rootWidth) * 0.05); 
		int exH = (int) (((float) this.rootWidth) * 0.05); 	
		int exX = rootWidth - (int) (((float) this.rootWidth) * 0.07);
		int exY = (int) (((float) this.rootHeight) * 0.015);
		
		
		JLabel ex = this.createLabel(
			new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/x.png").getImage(), exW, exH)), 
			"exits", new Rectangle(exX, exY, exW, exH));
		JLabel greyBg = this.createLabel("greybg", new Rectangle(greyBgX, greyBgY, greyBgW, greyBgH), 144, 142, 151);
	}
	
	public JLabel createLabel(ImageIcon icon, String name, Rectangle rect) {
		JLabel label = new JLabel();
		label.setIcon(icon);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				 WindowEvent we = new WindowEvent(page, WindowEvent.WINDOW_CLOSING);
				 Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(we);
			}
		});
		
		this.page.getContentPane().add(label);
		label.setBounds(rect);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	public JLabel createLabel(String name, Rectangle rect, int... rgb) {
		JLabel label = new JLabel();
		
		label.setBounds(rect);
		label.setOpaque(true);
		
		label.setBackground(new Color(rgb[0], rgb[1], rgb[2]));
		
		this.page.getContentPane().add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	public MAINPAGE(Page page) {
		this.page = page;
		this.setFrame();
	}
	
	
	public void setFrame() {
		this.page.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mOffset = e.getPoint();
			}
		});
		this.page.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (mOffset.y < (int) (((float) rootHeight) * 0.08)) {
					int x_ = e.getXOnScreen() - mOffset.x;
					int y_ = e.getYOnScreen() - mOffset.y;
					page.setLocation(x_, y_);
				}
			}
		});
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		this.rootWidth = (int) (((float) dimension.width) * 0.8);
		this.rootHeight = (int) (((float) dimension.height) * 0.8);
		
		this.page.getContentPane().setBackground(new Color(240, 240, 240));
		this.page.setTitle("CSIMES - Dashboard");
		this.page.setSize(this.rootWidth, this.rootHeight);
		this.page.setLocationRelativeTo(null);
		this.page.setUndecorated(true);
		this.page.setLayout(null);
		this.page.setIconImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_cropped.png")).getImage());
		this.page.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.page.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
					String msg_ = "Do you wish to exit?";
					int stat_ = JOptionPane.showOptionDialog(null,
								msg_, 
								"CSIMES - Exiting Dashboard", 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE,
								new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35)),
								new String[]{"Exit", "Cancel"},
								"Cancel"
					);
					if (stat_ == 0) {
						JFrame root = (JFrame) we.getSource();
						Initialize.LockFile(true);
						root.dispose();
				}
			}
			
			public void windowClosed(WindowEvent we) {
				Initialize.LockFile(true);
			}
		});
	}
}

