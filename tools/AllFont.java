package net.csimes;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;


public class AllFont {
	
	public static void main(String[] argv) {
		
		JFrame root = new JFrame();
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		root.setSize(488,555);
		root.setLayout(null);
		root.setResizable(false);
		root.setTitle("CSIMES - System Font Picker Tool");
		root.getContentPane().setBackground(Color.black);
		root.setLocationRelativeTo(null);
		
		JLabel credits = new JLabel("Written By: JohnLesterDev :>");
		credits.setBounds(300, 485, 255, 25);
		credits.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 10));
		credits.setForeground(Color.white);
		root.getContentPane().add(credits);
		root.setIconImage(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("icon/csimes.png")).getImage());
		
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] allfonts = ge.getAllFonts();
		
		Vector<String> fontNames = new Vector<String>();
		for (Font font : allfonts) {
			fontNames.addElement(font.getName());
		};
		
		JLabel labels = new JLabel("<html>ABCDEFGHIJ<br>KLMNOPQRSTVUWXYZ<br>abcdefghij<br>klmnopqrstuvwxyz<br>1234567890<br>'\"\\/><:;+=-_}][{|</html>");
		labels.setBounds(20, 255, 255, 255);
		labels.setForeground(Color.white);
		labels.setFont(new Font("Arial", Font.PLAIN, 21));	
		root.getContentPane().add(labels);
		
		JList list_ = new JList(fontNames);
		list_.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lev) {
				root.setTitle("CSIMES - System Font Picker : " + (String)list_.getSelectedValue());
				labels.setFont(new Font((String)list_.getSelectedValue(), Font.PLAIN, 21));
				return;
			}
		});
		

		JScrollPane panes = new JScrollPane();
		panes.setBounds(14, 18, 222, 222);
		panes.getViewport().setView(list_);
		root.getContentPane().add(panes);
		
		root.setVisible(true);
	}
}
