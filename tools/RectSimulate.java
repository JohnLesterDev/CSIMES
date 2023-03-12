// package net.csimes;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;


public class RectSimulate extends JFrame {
	public int width, height;
	public JPanel canvas;
	public Point p;
	public Point nextP;
	public Point lp;
	public boolean lmov = false;
	public JLabel curL;
	
	public int lx = 0;
	public int ly = 0;
	
	public Point pp = null;

	
	public Color[] colors = new Color[]{
		Color.black,
		Color.red,
		Color.orange,
		Color.yellow,
		Color.green,
		Color.blue,
		Color.pink,
		Color.cyan,
		Color.magenta,
		Color.lightGray
	};
	
	public void setTips(JComponent comp, Rectangle r) {
		float percent_x = ((float) r.x) / ((float) width);
		float percent_y = ((float) r.y) / ((float) height);
		
		float percent_ww = ((float) r.width) / ((float) width);
		float percent_wh = ((float) r.width) / ((float) height);
		float percent_hw = ((float) r.height) / ((float) width);
		float percent_hh = ((float) r.height) / ((float) height);
		
		comp.setToolTipText(String.format("<html>X:%d Y:%d<br>W:%d H:%d<br><br>PX-W: %.3f PY-H: %.3f<br>PW-W: %.3f PW-H: %.3f<br>PH-W: %.3f PH-H: %.3f</html>",
			r.x, 
			r.y, 
			r.width, 
			r.height,
			percent_x,
			percent_y,
			percent_ww,
			percent_wh,
			percent_hw,
			percent_hh
		));
	}
	
	
	public RectSimulate() {
		this.width = Integer.valueOf(JOptionPane.showInputDialog(null, "Set Width of Frame:"));
		this.height = Integer.valueOf(JOptionPane.showInputDialog(null, "Set Height of Frame:"));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setSize(width, height);
		setLayout(null);
		setLocationRelativeTo(null);
		setTitle("CSIMES - Rectangle Simulator :>");
		//setIconImage(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("icon/csimes.png")).getImage());
		
		this.canvas = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(colors[new Random().nextInt(colors.length - 1)]);
				
				if (p != null) {
					int x = Math.min(p.x, nextP.x);
					int y = Math.min(p.y, nextP.y);
					int w = Math.abs(p.x - nextP.x);
					int h = Math.abs(p.y - nextP.y);
					
					g.drawRect(x, y, w, h);
				}
			}	
		};
		
		JPopupMenu pmenu = new JPopupMenu();
		
		JMenuItem del_ = new JMenuItem("Dispose");
		JMenuItem dupli_ = new JMenuItem("Duplicate");
		
		pmenu.add(del_);
		pmenu.add(dupli_);
		
		del_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (curL != null) {
					canvas.remove(curL);
					canvas.revalidate();
					canvas.repaint();
				}
			}
		});

		dupli_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (curL != null) {
					Rectangle r = curL.getBounds();
					int x = 0;
					int y = 0;
					int w = r.width;
					int h = r.height;
					
					JLabel rlbl = new JLabel(String.format("<html>X:%d Y:%d<br>W:%d H:%d</html>", x, y, w, h), SwingConstants.CENTER);
					rlbl.setName("RECTLBL");
					rlbl.setBounds(x, y, w, h);
					setTips(rlbl, new Rectangle(x, y, w, h));
					rlbl.setFont(new Font("Corbel Light", Font.PLAIN, 15));
					rlbl.setBorder(BorderFactory.createLineBorder(colors[new Random().nextInt(colors.length - 1)], 2));
					rlbl.addMouseListener(new MouseAdapter() {
						public void mousePressed(MouseEvent e) {
							lp = e.getPoint();
							lmov = true;
							
							if (SwingUtilities.isRightMouseButton(e)) {
								pmenu.setVisible(true);
								pmenu.show(rlbl, e.getX(), e.getY());
								curL = (JLabel) e.getSource();
							} else {
								pmenu.setVisible(false);
								curL = null;
							}
						}
						
						public void mouseReleased(MouseEvent e) {
							lmov = false;
						}
					});

					rlbl.addMouseMotionListener(new MouseMotionAdapter() {
						public void mouseMoved(MouseEvent e) {
							lx = e.getX();
							ly = e.getY();		
						}
						
						public void mouseDragged(MouseEvent e) {
							mouseMoved(e);
							
							if (lmov) {
								int dx = e.getX() - lp.x;
								int dy = e.getY() - lp.y;
								
								JLabel curL = (JLabel) e.getSource();
								
								Rectangle rect = curL.getBounds();
								rect.translate(dx, dy);
								curL.setBounds(rect);
								rect = curL.getBounds();
								setTips(curL, rect);
								curL.setText(String.format("<html>X:%d Y:%d<br>W:%d H:%d</html>", rect.x, rect.y, rect.width, rect.height));
							}
						}
					});
					
					canvas.add(rlbl);
					canvas.setComponentZOrder(rlbl, 0);
					canvas.revalidate();
					canvas.repaint();
				}
			}
		});
		
		this.canvas.setLayout(null);
		this.canvas.setName("CANVAS");
		this.canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				p = e.getPoint();
				
				if (SwingUtilities.isRightMouseButton(e)) {
					pp = e.getPoint();
				}
				
				Component comp = (Component) e.getSource();
				System.out.println(comp.getName());
				pmenu.setVisible(false);
				curL = null;
			}
			
			public void mouseReleased(MouseEvent e) {
				int x = Math.min(p.x, e.getX());
				int y = Math.min(p.y, e.getY());
				int w = Math.abs(p.x - e.getX());
				int h = Math.abs(p.y - e.getY());
				
				JLabel rlbl = new JLabel(String.format("<html>X:%d Y:%d<br>W:%d H:%d</html>", x, y, w, h), SwingConstants.CENTER);
				rlbl.setName("RECTLBL");
				rlbl.setBounds(x, y, w, h);
				setTips(rlbl, new Rectangle(x, y, w, h));
				rlbl.setFont(new Font("Corbel Light", Font.PLAIN, 15));
				rlbl.setBorder(BorderFactory.createLineBorder(colors[new Random().nextInt(colors.length - 1)], 2));
				rlbl.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						lp = e.getPoint();
						lmov = true;
						
						if (SwingUtilities.isRightMouseButton(e)) {
							pmenu.setVisible(true);
							pmenu.show(rlbl, e.getX(), e.getY());
							curL = (JLabel) e.getSource();
						} else {
							pmenu.setVisible(false);
							curL = null;
						}
					}
					
					public void mouseReleased(MouseEvent e) {
						lmov = false;
					}
				});

				rlbl.addMouseMotionListener(new MouseMotionAdapter() {
					public void mouseMoved(MouseEvent e) {
						lx = e.getX();
						ly = e.getY();		
					}
					
					public void mouseDragged(MouseEvent e) {
						mouseMoved(e);
						
						if (lmov) {
							int dx = e.getX() - lp.x;
							int dy = e.getY() - lp.y;
							
							JLabel curL = (JLabel) e.getSource();
							
							Rectangle rect = curL.getBounds();
							rect.translate(dx, dy);
							curL.setBounds(rect);
							rect = curL.getBounds();
							setTips(curL, rect);
							curL.setText(String.format("<html>X:%d Y:%d<br>W:%d H:%d</html>", rect.x, rect.y, rect.width, rect.height));
						}
					}
				});
				
				canvas.add(rlbl);
				canvas.setComponentZOrder(rlbl, 0);
				canvas.revalidate();
				canvas.repaint();
				
				p = null;
			}
		});
		
		this.canvas.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (!lmov && SwingUtilities.isLeftMouseButton(e)) {
					mouseMoved(e);
				}
				else if (SwingUtilities.isRightMouseButton(e)) {
					int x_ = e.getXOnScreen() - pp.x;
					int y_ = e.getYOnScreen() - pp.y;
					setLocation(x_, y_);
				}
			}
			
			public void mouseMoved(MouseEvent e) {
				lx = e.getX();
				ly = e.getY();
				
				if (!lmov && SwingUtilities.isLeftMouseButton(e)) {
					nextP = e.getPoint();
					revalidate();
					repaint();
				} else if (SwingUtilities.isRightMouseButton(e)) {
					int x_ = e.getXOnScreen() - pp.x;
					int y_ = e.getYOnScreen() - pp.y;
					setLocation(x_, y_);
				}
			}
		});
		
		this.canvas.setBounds(0, 0, width, height);
		getContentPane().add(canvas);
		
	}
	
	public static void main(String[] argv) {
		EventQueue.invokeLater(() -> {
			new RectSimulate().setVisible(true);
		});
	}
}
