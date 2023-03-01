package net.csimes.splash;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.splash.*;



public class Splash {
	
	private JFrame root;
	private JLabel label;
	private int rootWidth;
	private int rootHeight;
	private Image[] splashes;
	private int splashCount = 0;

	public Splash() {
		this.root = new JFrame();
		this.root.setType(JFrame.Type.UTILITY);
		this.root.setUndecorated(true);
		
		Dimension dimens = Toolkit.getDefaultToolkit().getScreenSize();
		this.rootWidth = (int) (((float) dimens.width) * 0.6f);
		this.rootHeight = (int) (((float) this.rootWidth) / 1.778f);
		int heightOffset = (int) (((float) this.rootHeight) * 0.05f);
		
		JPanel panel = new JPanel();
		this.label = new JLabel();
		ImageIcon icon = new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_mainsplash.png")).getImage(), this.rootWidth, this.rootHeight));
		
		this.label.setIcon(icon);
		
		panel.setBounds(0, - heightOffset, this.rootWidth, this.rootHeight);
		
		this.root.setLayout(null);
		this.root.setSize(this.rootWidth, this.rootHeight - heightOffset);
		this.root.setLocationRelativeTo(null);
		
		this.root.add(panel);
		panel.add(this.label);
		
	}
	
	public Splash display() {
		this.root.setVisible(true);
		
		return this;
	}
	
	public Splash init() {
		this.label.setIcon(new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/splash_1.png")).getImage(), rootWidth, rootHeight)));
		
		this.splashes = new Image[]{
			ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/splash_2.png")).getImage(), this.rootWidth, this.rootHeight),
			ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/splash_3.png")).getImage(), this.rootWidth, this.rootHeight),
			ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/splash_4.png")).getImage(), this.rootWidth, this.rootHeight)
		};
		
		return this;
	}
	
	public void delete() {
		this.root.dispose();
	}
	
	public Splash nextSplash() {
		if (this.splashCount == this.splashes.length) {
			this.delete();
			
			return this;
		}
		
		this.label.setIcon(new ImageIcon(this.splashes[this.splashCount]));
		this.splashCount++;
		
		return this;
	}

}