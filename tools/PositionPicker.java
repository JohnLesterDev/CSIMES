package net.csimes;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;


public class PositionPicker extends JFrame {
	
	public int width, height;
	public int x = 0;
	public int y = 0;

	public Timer timer;

	public PositionPicker() {		
		this.width = Integer.valueOf(JOptionPane.showInputDialog(null, "Set Width of Frame:"));
		this.height = Integer.valueOf(JOptionPane.showInputDialog(null, "Set Height of Frame:"));

		setUndecorated(true);
		setSize(width, height);
		setLayout(null);
		setTitle(String.format("CSIMES - Position Picker (%d, %d)", this.x, this.y));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("icon/csimes.png")).getImage());
		
		JPanel panels = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.red);
				g.drawLine(0, y, getWidth(), y);
				g.drawLine(x, 0, x, getHeight());
			}			
		};
		panels.setForeground(Color.red);
		panels.setBounds(0, 0, getWidth(), getHeight());
		getContentPane().add(panels);
		
		JLabel lbl = new JLabel();
		lbl.setBounds(0, 0, 35, 19);
		lbl.setForeground(Color.black);
		lbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 17));
		panels.add(lbl);
		
		panels.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent ex) {
				mouseMoved(ex);
			}
			
			public void mouseMoved(MouseEvent ex) {
				x = ex.getX();
				y = ex.getY();
				lbl.setLocation(x + 19, y + 19);
				lbl.setText(String.format("Coordinates(%d, %d)", x, y));
				setTitle(String.format("CSIMES - Position Picker (%d, %d)", x, y));
				revalidate();
				repaint();	
			}
			
		});
		
		setVisible(true);
	}
	
	public static void main(String[] argv) {
		new PositionPicker();
	}
}
