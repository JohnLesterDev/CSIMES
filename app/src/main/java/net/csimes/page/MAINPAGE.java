package net.csimes.page;

import java.io.*;													 // Importing the java I/O package
import java.awt.*;										 	         // Importing the Abstract Window Toolkit package
import java.util.*;													 // Importing the java Utility package
import javax.swing.*;												 // Importing the java Swing package
import java.awt.image.*;
import java.awt.event.*;											 // Importing the AWT Event package
import javax.swing.text.*;											 // Importing the Text package from java Swing
import javax.swing.table.*;										 // Importing the Border package from java Swing
import javax.swing.border.*;										 // Importing the Border package from java Swing

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



public class MAINPAGE {
	
	public Page page;
	public int rootWidth, rootHeight;
	
	private Point mOffset;
	private Account acc;
	
	private JTable table;
	private Sidebars mainPanel;
	private JScrollPane spane;
	public int maxID_ = 0;
	
	public HashMap<String,Component> components = new HashMap<String,Component>();
	public HashMap<String,JPanel> sidebarPanels = new HashMap<String,JPanel>();
	
	
	public MAINPAGE(Page page, Account acc) {
		Initialize.LockFile();
		this.page = page;
		this.acc = acc;
		this.setFrame();
	}
	
	public void backToLogin() {
		RegisterPage rp = new RegisterPage(Initialize.pages.get("credentials"));
		rp.root.clean();

		LoginPage lp = new LoginPage(Initialize.pages.get("credentials"));
		lp.paints();
		
		this.page.clean();
		this.page.dispose();
		Initialize.pages.remove("MAIN");
		Initialize.pages.put("MAIN", new Page("MAIN"));
		Initialize.pages.get("MAIN").clean();

		lp.page.setVisible(true);
		lp.page.repaint();
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
		
		System.out.printf("W: %d H: %d \n", this.rootWidth, this.rootHeight);
		
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
	
	
	public void paints() {
		//GreyBG rect
		int greyBgX = -10;
		int greyBgY = -10;
		int greyBgW = rootWidth + rootWidth;
		int greyBgH = (int) (((float) this.rootHeight) * 0.11);
		
		// Exit rect
		int exW = (int) (((float) this.rootWidth) * 0.04); 
		int exH = (int) (((float) this.rootWidth) * 0.04); 	
		int exX = rootWidth - (int) (((float) this.rootWidth) * 0.065);
		int exY = (int) (((float) this.rootHeight) * 0.016);
		
		// Title rect
		int tX = (int) (((float) this.rootWidth) * 0.15);
		int tY = (int) (((float) this.rootHeight) * 0.009);
		int tW = rootWidth / 2;
		int tH = (int) (((float) this.rootWidth) * 0.05); 	
		int tFont = (int) (((float) this.rootHeight) * 0.03);
		
		// Logo rect
		int logW = (int) (((float) this.rootHeight) * 0.08);
		int logH = (int) (((float) this.rootHeight) * 0.08);
		int logX = tX - logW - (int) (((float) this.rootWidth) * 0.013);
		int logY = (int) (((float) this.rootHeight) * 0.009);
		
		// hamburger rect
		int hamX =  (int) (((float) this.rootWidth) * 0.01);
		int hamY = (int) (((float) this.rootHeight) * 0.009);
		int hamW = (int) (((float) this.rootHeight) * 0.08);
		int hamH = (int) (((float) this.rootHeight) * 0.08);
		
		// hamburger rect
		int btnX =  (int) (((float) this.rootWidth) * 0.5);
		int btnY = (int) (((float) this.rootHeight) * 0.9);
		int btnW = (int) (((float) this.rootHeight) * 0.08);
		int btnH = (int) (((float) this.rootHeight) * 0.05);
		
		Sidebars sidebar = this.sidebars();

		Sidebars mp_one = this.createMainPane("stock");
		
		this.table = new JTable();
		JTableHeader header = this.table.getTableHeader();
        header.setDefaultRenderer(new CHeaderRenderer());

		table.setModel(
			new CTableModel(null, new String[]{"Product ID", "Category", "Description", "Quantity", "Price", "Total Amount"})
		);

		this.spane = new JScrollPane(table);
		this.spane.setName("tablespane");
		
		Rectangle mpR = mp_one.getBounds();
		
		Rectangle tR = new Rectangle(
			(int) (((float) mpR.width) * 0.02),
			(int) (((float) mpR.height) * 0.131),
			(int) (((float) mpR.width) * 0.961),
			(int) (((float) mpR.height) * 0.819)
		);
		
		this.spane.setBounds(tR);
		mp_one.add(spane);
		this.components.put(this.spane.getName(), this.spane);
		
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
		
		JTextField searchF = this.createTextField(mp_one, "search", (int) (((float) sR.height) * 0.65), sR, null);
		JLabel searchLogo = this.createLabel(searchF, new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/user.png").getImage(), sLD.width, sLD.height)), "searchL", sLD);
		
		this.mainPanel = mp_one;
		mp_one.repaint();
		
		JLabel ex = this.createLabel(
			new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/x.png").getImage(), exW, exH)), 
			"exits", new Rectangle(exX, exY, exW, exH), "x");
		
		JLabel logo_ = this.createLabel(
			new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/csimes_full.png").getImage(), logW, logH)), 
			"title", new Rectangle(logX, logY, logW, logH), "csimes_full");
		JLabel ham_ = this.createLabel(
			new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/hamburger.png").getImage(), hamW, hamH)), 
			"hamburger", new Rectangle(hamX, hamY, hamW, hamH), "hamburger"
			);
		
		ham_.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("alt S"), "hamburgershowAction");
		ham_.getActionMap().put("hamburgershowAction", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				ham_.dispatchEvent(new MouseEvent(ham_, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 0, 0, 1, false, MouseEvent.BUTTON1));
			}
		});
		
		
		JLabel titL = this.createLabel(false, "Dave Housing & Construction Supplies IMES", tFont, new Rectangle(tX, tY, tW, tH));
		JLabel greyBg = this.createLabel("greybg", new Rectangle(greyBgX, greyBgY, greyBgW, greyBgH), 144, 142, 151);
	}

	
	public Sidebars sidebars() {
		Sidebars panel = new Sidebars(0 - (int) (((float) this.rootWidth) * 0.25), 0);
		panel.setName("sidebar");
		panel.setBackground(new Color(144, 142, 151));
		panel.setLayout(null);
		panel.setVisible(true);
		
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control L"), "logoutAction");
		panel.getActionMap().put("logoutAction", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (panel.isShown) {
					String msg_ = "Are you sure you want to logout?";
					int stat_ = JOptionPane.showConfirmDialog(null,
								msg_, 
								"CSIMES - Logout", 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE,
								new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
					);

					if ((stat_ == 0)) {
						backToLogin();
					}
				}
			}
		});
		
		// Panel rect              
		int sX = 0 - (int) (((float) this.rootWidth) * 0.22);
		int sY = (int) (((float) this.rootHeight) * 0.09);
		int sW = (int) (((float) this.rootWidth) * 0.22);
		int sH = this.rootHeight;
		
		System.out.printf("W: %d H: %d \n", sW, sH);
				
		// User logo Rect
		int usX = (int) (((float) sW) * 0.15);
		int usY = (int) (((float) this.rootHeight) * 0.04);
		int usW = (int) (((float) sW) * 0.15);
		int usH = (int) (((float) sW) * 0.15);
		
		//  Acccount rect
		int acX =  usX + usW +  (int) (((float) sW) * 0.1);
		int acY = usY;
		int acW = sW;
		int acH =  (int) (((float) this.rootWidth) * 0.045); 	
		int acFont = (int) (((float) this.rootHeight) * 0.034);
		
		JLabel usrlogo_ = this.createLabel(
			panel,
			new ImageIcon(ImageControl.resizeImage(Initialize.icons.get("icons/user.png").getImage(), usW, usH)), 
			"userlogo", new Rectangle(usX, usY, usW, usH), "user");
		
		String accN = "";
		switch (acc.accTypes.get(Integer.valueOf("" + acc.getAccountType()))) {
			case "admin":
				accN = "Admin"; break;
			case "owner":
				accN = "Owner"; break;
			case "staff":
				accN = "Staff"; break;
		}
		
		JLabel accT = this.createLabel(false, panel, accN, acFont, new Rectangle(acX, acY, acW, acH));
		
		
		panel.setBounds(new Rectangle(sX, sY, sW, sH));
		this.page.getContentPane().add(panel);
		this.components.put(panel.getName(), panel);
		
		JPanel logoutPanel = this.createSideBarPane(panel, "logout", 0.77f, 0.059f, new Color(144, 142, 151), new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String msg_ = "Are you sure you want to logout?";
				int stat_ = JOptionPane.showConfirmDialog(null,
							msg_, 
							"CSIMES - Logout", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.QUESTION_MESSAGE,
							new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
				);

				if ((stat_ == 0)) {
					backToLogin();
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				((JPanel) e.getSource()).setBackground(new Color(184, 185, 190));
			}
			
			public void mouseExited(MouseEvent e) {
				((JPanel) e.getSource()).setBackground(new Color(144, 142, 151));
			}
		});
		plotSideBarPane(logoutPanel, "icons/logout.png", "Logout");
		
		return panel;
	}
	
	public Sidebars createMainPane(String name) {
		int x = (int) (((float) this.rootWidth) * 0.117);
		int y = (int) (((float) this.rootHeight) * 0.111);
		int w = (int) (((float) this.rootWidth) * 0.766);
		int h = (int) (((float) this.rootHeight) * 0.808);
		
		Sidebars mp = new Sidebars(x, (int) (((float) this.rootWidth) * 0.223));
		mp.setVisible(true);
		mp.setLayout(null);
		mp.setName(name);		
		
		mp.setBounds(x, y, w, h);
		this.page.getContentPane().add(mp);
		
		return mp;
	}
	
	public JPanel createSideBarPane(JPanel sb, String name, float yp, float hp, Color color, MouseAdapter ma) {
		Rectangle sbRect = sb.getBounds();
		System.out.println("" + sbRect.width + ":" + sbRect.height);
		int x = 0;
		int y = (int) (((float) sbRect.height) * yp);
		int h = (int) (((float) sbRect.height) * hp);
		int w = sbRect.width;
		
		JPanel panels = new JPanel();
		panels.setLayout(null);
		panels.setBounds(x, y, w, h);
		panels.setBackground(color);
		panels.setVisible(true);
		panels.addMouseListener(ma);
		
		sb.add(panels);
		sb.setComponentZOrder(panels, 0);
		sidebarPanels.put(name, panels);
		
		return panels;
	}
	
	public void plotSideBarPane(JPanel sbp, String icon, String labelText) {
		Rectangle sbpB = sbp.getBounds();
		JLabel iconL = new JLabel();
		
		int iconX = (int) (((float) sbpB.width) * 0.23);
		int iconY = (int) (((float) sbpB.height) * 0.18);
		int iconH = (int) (((float) sbpB.height) * 0.63);
		
		iconL.setIcon(new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile(icon)).getImage(), iconH, iconH)));
		iconL.setBounds(iconX, iconY, iconH, iconH);
		iconL.setOpaque(false);
		
		JLabel textL = new JLabel(labelText);
		textL.setForeground(Color.black);
		textL.setOpaque(false);
		
		int txtX = (int) (((float) sbpB.width) * 0.37);
		int txtY = (int) (((float) sbpB.height) * 0.18);
		int txtW = (int) (((float) sbpB.width) * 0.44);
		textL.setFont(new Font("Arial", Font.BOLD, (int) (((float) iconH) * 0.72)));
		textL.setBounds(txtX, txtY, txtW, iconH);
		
		sbp.add(iconL);
		sbp.add(textL);
		
		sbp.repaint();
	}
	
	
	public JLabel createLabel(boolean isText, String name, int fontSize, Rectangle labelRect) {
		JLabel label = new JLabel(name);
		
		label.setBounds(labelRect);
		
		if (isText == true) {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
			label.setForeground(Color.white);
		} else {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
			label.setForeground(Color.black);
		}
		
		this.page.getContentPane().add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	
	public JLabel createLabel(boolean isText, JPanel panel, String name, int fontSize, Rectangle labelRect) {
		JLabel label = new JLabel(name);
		
		label.setBounds(labelRect);
		
		if (isText == true) {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
			label.setForeground(Color.white);
		} else {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
			label.setForeground(Color.black);
		}
		
		panel.add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	
	public JLabel createLabel(ImageIcon icon, String name, Rectangle rect, String iconType) {
		JLabel label = new JLabel();
		label.setName(name);
		label.setIcon(icon);
		label.addMouseListener(new MainMouse(page, this.mainPanel, label, this.spane, iconType, this.components));
		
		this.page.getContentPane().add(label);
		label.setBounds(rect);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	
	public JLabel createLabel(JTextField parent, ImageIcon icon, String name, Dimension rect) {
		JLabel label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setName(name);
		label.setIcon(icon);
		
		parent.add(label, BorderLayout.EAST);
		label.setSize(rect);
		
		return label;
	}
	
	
	public JLabel createLabel(JPanel panel, ImageIcon icon, String name, Rectangle rect, String iconType) {
		JLabel label = new JLabel();
		label.setName(name);
		label.setIcon(icon);
		label.addMouseListener(new MainMouse(page, this.mainPanel, label, this.spane, iconType, this.components));
		
		panel.add(label);
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

	
	public JTextField createTextField(Sidebars sb, String name, int fontSize, Rectangle fieldRect, SearchListener listener) {
		JTextField textField = new JTextField();
		DocumentFilter dfilter = new DocumentLimiter(16);
		
		DocumentFilter oldFilter = ((AbstractDocument)textField.getDocument()).getDocumentFilter();
		if (oldFilter != null) {
			((AbstractDocument)textField.getDocument()).setDocumentFilter(null);
		}
		
		((AbstractDocument)textField.getDocument()).setDocumentFilter(dfilter);
		((AbstractDocument)textField.getDocument()).putProperty("parent", textField);
		
		textField.setName(name);
		textField.setPreferredSize(new Dimension(fieldRect.width, fieldRect.height));
		textField.setFont(new Font("Arial", Font.PLAIN, fontSize));
		textField.setBounds(fieldRect);
		textField.setOpaque(false);
		textField.setLayout(new BorderLayout());
		textField.setForeground(Color.BLACK);
		textField.setCaretColor(Color.black);
		textField.setHorizontalAlignment(JTextField.LEFT);
		textField.setFocusable(true);
		textField.setBorder(BorderFactory.createMatteBorder(0, 0, (int) ((float) fieldRect.height * 0.05), 0, Color.black));
		
		textField.addActionListener(listener);
		((AbstractDocument)textField.getDocument()).addDocumentListener(listener);
		
		textField.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (textField.getText().trim().equals("Search")) {
						textField.setText("");
					}
					
					textField.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (textField.getText().trim().equals("")) {
						textField.setText("Search");
					}
					
					textField.setForeground(Color.lightGray);
				}
			});
		
		textField.setFocusable(false);
		
		sb.add(textField);
		this.components.put(textField.getName(), textField);
		
		return textField;
	}

}
