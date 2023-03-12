package net.csimes.temp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sidebars extends JPanel {
	public int sX, eX;
	public boolean isShown = false;
	public int vel = 4;
	public Timer timer;
	
	public Sidebars(int sx, int ex) {
		this.sX = sx;
		this.eX = ex;
		Timer timers = new Timer(5, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Timer tx = (Timer) e.getSource();
				
				if (vel < 40) {
					vel += 4;
				};
				
				int curX = getLocation().x;
				
				if (curX < sX) {
					vel = 3;
					setLocation(sX, getLocation().y);
					isShown = false;
					tx.stop();
				} else if (curX >= eX) {
					vel = 3;
					setLocation(eX, getLocation().y);
					isShown = true;
					tx.stop();
					
					System.out.println("Shown");
				} 
				
				
				if (isShown == false) {
					int newX = getLocation().x + vel;
					setLocation(newX, getLocation().y);
				} else {
					int newX = getLocation().x - vel;
					setLocation(newX, getLocation().y);
				}				
			}
		});
		this.timer = timers;
	}
	
	public void runs() {
		this.timer.start();
	}
}
