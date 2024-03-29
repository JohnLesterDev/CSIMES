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
import net.csimes.panels.*;
import net.csimes.splash.*;
import net.csimes.listeners.*;



public class MAINPAGE {
	
	public Page page;
	public int rootWidth, rootHeight;
	
	private Point mOffset;
	private Account acc;
	public JLabel ham_;
	
	private JTable table;
	public Sidebars mainPanel, sidebar;
	public static int maxID = 0;
	
	public HashMap<String,Component> components = new HashMap<String,Component>();
	public HashMap<String,SidebarPanel> sidebarPanels = new HashMap<String,SidebarPanel>();
	public static HashMap<Integer,Product> prds = new HashMap<Integer,Product>();
	public HashMap<String,Sidebars> mainPanels = new HashMap<String,Sidebars>();
	
	//Stock content interchanger
	public JScrollPane spane;
	public JLabel snoCon;
	
	
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

		Initialize.currentAccount = null;

		lp.page.setVisible(true);
		lp.page.repaint();
	}
	
	
	public static Object[][] getProducts() {
		Object[][] obj = Inventory.getProdsByTable();
		
		if (Inventory.getMaxID() != null) {
			MAINPAGE.maxID = Inventory.getMaxID();
		}
		
		MAINPAGE.prds = Inventory.getProdsByID();

		if (obj != null) {
			return obj;
		} else {
			return null;
		}
	}
	
	public static String[] getCategories() {
		Inventory.refresh();
		if (Inventory.getAllProdCategories() != null) {
			return Inventory.getAllProdCategories().toArray(new String[]{});
		} else {
			return new String[]{""};
		}
	}
	
	public void deleteProduct(Integer id) {
		try {
			if (!Inventory.removeProductByID(id)) {
				JOptionPane.showMessageDialog(
					null,
					"An error occured while deleting the product.",
					"CSIMES - Delete Product Failed",
					JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
				);
			} else {
				Inventory.refresh();
				JOptionPane.showMessageDialog(
					null,
					"Product deleted successfully",
					"CSIMES - Delete Product Successful",
					JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
				);
			}
			
			mainPanel.repaint();
			table.repaint();
			
			mainPanel.revalidate();
			table.revalidate();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					"An error occured while deleting the product.",
					"CSIMES - Delete Product Failed",
					JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
				);
		}
	}
	
	public void filterRows(String input) {
		if (!input.equals("Search") && !input.equals("")) {
			DefaultTableModel model = (DefaultTableModel) this.table.getModel();
			TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
			table.setRowSorter(sorter);
			sorter.setRowFilter(RowFilter.regexFilter("(?i).*" + input + ".*", 0, 1, 2, 4));
		} else {
			table.setRowSorter(null);
			SwingUtilities.invokeLater(() -> { table.setModel(
					new CTableModel(this.getProducts(), new String[]{"Product ID", "Category", "Item Description", "Quantity", "Unit", "Unit Price", "Total Amount", "Date"})
			);});
		}
	}
	
	
	public void deleteRowFromTable() {
		int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
			String sst = (String) table.getValueAt(selectedRow, 0);
			Integer id = Integer.valueOf(sst);

            String msg_ = "Do you wish to delete this Product? (ID: " + id + ")";
			int confirmation = JOptionPane.showOptionDialog(null,
								msg_, 
								"CSIMES - Delete Product", 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE,
								new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35)),
								new String[]{"Yes", "Cancel"},
								"Yes"
					);
            if (confirmation == JOptionPane.YES_OPTION) {
				this.deleteProduct(id);
				DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(selectedRow);
                table.setModel(
				new CTableModel(this.getProducts(), new String[]{"Product ID", "Category", "Item Description", "Quantity", "Unit", "Unit Price", "Total Amount", "Date"})
				);
			}
        } else {
			JOptionPane.showMessageDialog(
					null,
					"Please select a product to delete.",
					"CSIMES - Delete Product Failed",
					JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
				);
		}
	}
	
	public void insertProduct() {
		JPanel addP = new JPanel();
		addP.setVisible(true);
		addP.setLayout(new GridLayout(6, 1));
		
		JComboBox<String> cbc = new JComboBox<String>(MAINPAGE.getCategories());
		cbc.setEditable(true);
		cbc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JComboBox) e.getSource()).transferFocus();
			}
		});
		
		JComboBox<String> cbcU = new JComboBox<String>(new String[]{"Units"});
		cbcU.setEditable(false);
		cbcU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JComboBox) e.getSource()).transferFocus();
			}
		});
		
		JComboBox<String> msms = new JComboBox<String>(new String[]{"Measurements", "Length", "Area", "Mass", "Volume", "Others"});
		msms.setEditable(false);
		msms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch ((String) msms.getSelectedItem()) {
					case "Length":
						cbcU.setModel(new DefaultComboBoxModel(new String[]{"Units", "cm", "ft", "m", "yrd", "inch"}));
						break;
						
					case "Area":
						cbcU.setModel(new DefaultComboBoxModel(new String[]{"Units", "cm2", "ft2", "m2", "yrd2", "inch2"}));
						break;
					
					case "Mass":
						cbcU.setModel(new DefaultComboBoxModel(new String[]{"Units", "g", "kg", "lb", "ton", "mg"}));
						break;
					
					case "Volume":
						cbcU.setModel(new DefaultComboBoxModel(new String[]{"Units", "L", "mL", "gal", "cm3", "ft3", "m3", "yrd3", "inch3"}));
						break;
					
					case "Others":
						cbcU.setModel(new DefaultComboBoxModel(new String[]{"Units", "pc", "pair", "set", "sack"}));
						break;
					
					default:
						cbcU.setModel(new DefaultComboBoxModel(new String[]{"Units"}));
						break;
				}
				
				((JComboBox) e.getSource()).transferFocus();
			}
		});
		
		JTextField desc = new JTextField();
		JTextField quan = new JTextField();
		JTextField prc = new JTextField();
		
		desc.setText("Description");
		desc.setForeground(Color.gray);
		
		quan.setText("Quantity");
		quan.setForeground(Color.gray);
		
		prc.setText("Price");
		prc.setForeground(Color.gray);
		
		desc.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (desc.getText().trim().equals("Description")) {
						desc.setText("");
					}
					
					desc.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (desc.getText().trim().equals("")) {
						desc.setText("Description");
					}
					
					desc.setForeground(Color.gray);
				}
			});
		desc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JTextField) e.getSource()).transferFocus();
			}
		});
		
		quan.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isDigit(c) || c == '.' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
					e.consume();
				}
				String text = prc.getText();
				if (text.contains(".") && c == '.') {
					e.consume();
				}
			}
		});
		quan.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (quan.getText().trim().equals("Quantity")) {
						quan.setText("");
					}
					
					quan.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (quan.getText().trim().equals("")) {
						quan.setText("Quantity");
					}
					
					quan.setForeground(Color.gray);
				}
			});
		quan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JTextField) e.getSource()).transferFocus();
			}
		});
		
		prc.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isDigit(c) || c == '.' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
					e.consume();
				}
				String text = prc.getText();
				if (text.contains(".") && c == '.') {
					e.consume();
				}
			}
		});
		prc.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (prc.getText().trim().equals("Price")) {
						prc.setText("");
					}
					
					prc.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (prc.getText().trim().equals("")) {
						prc.setText("Price");
					}
					
					prc.setForeground(Color.gray);
				}
			});
		prc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JTextField) e.getSource()).transferFocus();
			}
		});
		
		addP.add(cbc);
		addP.add(msms);
		addP.add(cbcU);
		addP.add(desc);
		addP.add(quan);
		addP.add(prc);
		
		while (true) {
			int stat = JOptionPane.showOptionDialog(
				null,
				addP,
				"CSIMES - Add Product",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 20, 20)),
				new String[]{"ADD"},
				"ADD"
			);
			cbc.requestFocusInWindow();
			
			if (stat != 0) {
				break;
			}
			
			if (stat == 0 && 
				(!cbc.getSelectedItem().equals("") || !cbc.getSelectedItem().equals("Categories")) && 
				(!cbcU.getSelectedItem().equals("") || !cbcU.getSelectedItem().equals("Measurements")) && 
				(!msms.getSelectedItem().equals("") || !msms.getSelectedItem().equals("Units")) && 
				(!desc.getText().equals("") || !desc.getText().equals("Description")) && 
				(!quan.getText().equals("") || !quan.getText().equals("Quantity")) && 
				(!prc.getText().equals("") || !prc.getText().equals("Price"))
				) {
				
				Inventory.insertProduct(
					cbc.getSelectedItem(),
					desc.getText(),
					cbcU.getSelectedItem(),
					Float.parseFloat(quan.getText()),
					Float.parseFloat(prc.getText())
				);
				
				Inventory.refresh();
				
				JOptionPane.showMessageDialog(
					null,
					"Product added successfully!",
					"CSIMES - Add Product Successful",
					JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
				);
				
				table.setModel(
					new CTableModel(this.getProducts(), new String[]{"Product ID", "Category", "Item Description", "Quantity", "Unit", "Unit Price", "Total Amount", "Date"})
				);
				break;
			} else {
				JOptionPane.showMessageDialog(
					null,
					"Invalid input detected. Please try again.",
					"CSIMES - Add Product Error",
					JOptionPane.ERROR_MESSAGE,
					new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
				);
				
				continue;
			}
		}
		
	}
	
	public void modifyProduct() {
		JPanel addP = new JPanel();
		addP.setVisible(true);
		addP.setLayout(new GridLayout(4, 1));
	
		int selectedRow = table.getSelectedRow();	

		JComboBox<String> cbc = new JComboBox<String>(MAINPAGE.getCategories());
		cbc.setEditable(true);
		cbc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JComboBox) e.getSource()).transferFocus();
			}
		});
		
		JTextField desc = new JTextField();
		JTextField quan = new JTextField();
		JTextField prc = new JTextField();
		
		desc.setForeground(Color.gray);
		quan.setForeground(Color.gray);
		prc.setForeground(Color.gray);
		
		desc.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (desc.getText().trim().equals("Description")) {
						desc.setText("");
					}
					
					desc.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (desc.getText().trim().equals("")) {
						desc.setText("Description");
					}
					
					desc.setForeground(Color.gray);
				}
			});
		desc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JTextField) e.getSource()).transferFocus();
			}
		});
		
		quan.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
			if (!(Character.isDigit(c) || c == '-' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
					e.consume();
				}
			}
		});
		quan.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (quan.getText().trim().equals("Quantity")) {
						quan.setText("");
					}
					
					quan.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (quan.getText().trim().equals("")) {
						quan.setText("Quantity");
					}
					
					quan.setForeground(Color.gray);
				}
			});
		quan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JTextField) e.getSource()).transferFocus();
			}
		});
		
		prc.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isDigit(c) || c == '.' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
					e.consume();
				}
				String text = prc.getText();
				if (text.contains(".") && c == '.') {
					e.consume();
				}
			}
		});
		prc.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (prc.getText().trim().equals("Price")) {
						prc.setText("");
					}
					
					prc.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (prc.getText().trim().equals("")) {
						prc.setText("Price");
					}
					
					prc.setForeground(Color.gray);
				}
			});
		prc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JTextField) e.getSource()).transferFocus();
			}
		});
		
		addP.add(cbc);
		addP.add(desc);
		addP.add(quan);
		addP.add(prc);
		
		if (selectedRow != -1) {
			String sst = (String) table.getValueAt(selectedRow, 0);
			Integer id = Integer.valueOf(sst);
			Product prd_ = Inventory.getProductByID(id);
			
			cbc.setSelectedItem((String) prd_.category);
			desc.setText((String) prd_.name);
			quan.setText(String.valueOf(prd_.quantity));
			prc.setText(String.valueOf(prd_.price));
			
			int stat = JOptionPane.showOptionDialog(
				null,
				addP,
				"CSIMES - Update Product",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 20, 20)),
				new String[]{"Update"},
				"Update"
			);
			cbc.requestFocusInWindow();
			
			if (stat == 0 && !cbc.getSelectedItem().equals("") && !desc.getText().equals("") && !quan.getText().equals("") && !prc.getText().equals("")) {
				Inventory.modifyProductByID(
						id, 
						cbc.getSelectedItem(), 
						desc.getText(),
						Float.parseFloat(quan.getText()),
						Float.parseFloat(prc.getText())
					);
					
				Inventory.refresh();
				
				JOptionPane.showMessageDialog(
					null,
					"Product has been updated successfully!",
					"CSIMES - Update Product Successful",
					JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
				);
				
				table.setModel(
					new CTableModel(this.getProducts(), new String[]{"Product ID", "Category", "Item Description", "Quantity", "Unit", "Unit Price", "Total Amount", "Date"})
				);
				return;
			} else {
				JOptionPane.showMessageDialog(
					null,
					"Invalid input detected. Please try again.",
					"CSIMES - Update Product Error",
					JOptionPane.ERROR_MESSAGE,
					new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
				);				
			}
		}
		
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
		this.page.setResizable(false);
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
		
		this.sidebar = this.sidebars(this.mainPanel);
		
		DashboardPanel mp_zero = new DashboardPanel(this);
		this.mainPanels.put("dashboardpanel", mp_zero.panel);
		
		this.mainPanel = mp_zero.panel;

		InventoryPanel mp_one = new InventoryPanel(this);
		this.mainPanels.put("inventorypanel", mp_one.panel);
		
		TransactionPanel mp_two = new TransactionPanel(this);
		this.mainPanels.put("transactionpanel", mp_two.panel);
		
		JLabel ex = this.createLabel(
			new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/x.png")).getImage(), exW, exH)), 
			"exits", new Rectangle(exX, exY, exW, exH), "x");
		
		JLabel logo_ = this.createLabel(
			new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full.png")).getImage(), logW, logH)), 
			"title", new Rectangle(logX, logY, logW, logH), "csimes_full");
		this.ham_ = this.createLabel(
			new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/hamburger.png")).getImage(), hamW, hamH)), 
			"hamburger", new Rectangle(hamX, hamY, hamW, hamH), "hamburger"
			);
		
		this.ham_.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("alt S"), "hamburgershowAction");
		this.ham_.getActionMap().put("hamburgershowAction", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				ham_.dispatchEvent(new MouseEvent(ham_, MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, 0, 0, 1, false, MouseEvent.BUTTON1));
			}
		});
		
		
		JLabel titL = this.createLabel(false, "Dave Housing & Construction Supplies IMES", tFont, new Rectangle(tX, tY, tW, tH));
		JLabel greyBg = this.createLabel("greybg", new Rectangle(greyBgX, greyBgY, greyBgW, greyBgH), 144, 142, 151);
	}

	
	public Sidebars sidebars(Sidebars CurrentPanel) {
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
			new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/user.png")).getImage(), usW, usH)), 
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

		SidebarPanel logoutPanel = this.createSideBarPane(panel, "logout", 0.77f, 0.059f, new Color(144, 142, 151), new MouseAdapter() {
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
				((SidebarPanel) e.getSource()).setBackground(new Color(184, 185, 190));
			}
			
			public void mouseExited(MouseEvent e) {
				((SidebarPanel) e.getSource()).setBackground(new Color(144, 142, 151));
			}
		});
		plotSideBarPane(logoutPanel, "icons/logout.png", "Logout");
		
		
		SidebarPanel dashPanel = this.createSideBarPane(panel, "dashboard", 0.249f, 0.06f, new Color(87, 85, 94), new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				for (SidebarPanel pp : sidebarPanels.values()) {
					if (pp.getName().equals("dashboard")) {
						pp.setBackground(new Color(87, 85, 94));
						pp.selection = true;
						switchMainPanel("dashboardpanel");
					} else {
						pp.setBackground(new Color(144, 142, 151));
						pp.selection = false;
					}
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(184, 185, 190));
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(144, 142, 151));
				}
			}
		});
		dashPanel.selection = true;
		plotSideBarPane(dashPanel, "icons/home.png", "Dashboard");
		
		SidebarPanel invPanel = this.createSideBarPane(panel, "inventory", 0.316f, 0.06f, new Color(144, 142, 151), new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				for (SidebarPanel pp : sidebarPanels.values()) {
					if (pp.getName().equals("inventory")) {
						pp.setBackground(new Color(87, 85, 94));
						pp.selection = true;
						switchMainPanel("inventorypanel");
					} else {
						pp.setBackground(new Color(144, 142, 151));
						pp.selection = false;
					}
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(184, 185, 190));
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(144, 142, 151));
				}
			}
		});
		plotSideBarPane(invPanel, "icons/stock.png", "Inventory");
		
		SidebarPanel posPanel = this.createSideBarPane(panel, "transaction", 0.383f, 0.06f, new Color(144, 142, 151), new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				for (SidebarPanel pp : sidebarPanels.values()) {
					if (pp.getName().equals("transaction")) {
						pp.setBackground(new Color(87, 85, 94));
						pp.selection = true;
						switchMainPanel("transactionpanel");
					} else {
						pp.setBackground(new Color(144, 142, 151));
						pp.selection = false;
					}
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(184, 185, 190));
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(144, 142, 151));
				}
			}
		});
		plotSideBarPane(posPanel, "icons/sell.png", "Transaction");
		
		SidebarPanel hisPanel = this.createSideBarPane(panel, "historylog", 0.45f, 0.06f, new Color(144, 142, 151), new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				for (SidebarPanel pp : sidebarPanels.values()) {
					if (pp.getName().equals("historylog")) {
						pp.setBackground(new Color(87, 85, 94));
						pp.selection = true;
						switchMainPanel("historylogpanel");
					} else {
						pp.setBackground(new Color(144, 142, 151));
						pp.selection = false;
					}
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(184, 185, 190));
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(144, 142, 151));
				}
			}
		});
		plotSideBarPane(hisPanel, "icons/translog.png", "History/Log");
		
		SidebarPanel usrsPanel = this.createSideBarPane(panel, "users", 0.518f, 0.06f, new Color(144, 142, 151), new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				for (SidebarPanel pp : sidebarPanels.values()) {
					if (pp.getName().equals("users")) {
						pp.setBackground(new Color(87, 85, 94));
						pp.selection = true;
						switchMainPanel("userspanel");
					} else {
						pp.setBackground(new Color(144, 142, 151));
						pp.selection = false;
					}
				}
			}
			
			public void mouseEntered(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(184, 185, 190));
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if (!((SidebarPanel) e.getSource()).isSelected()) {
					((SidebarPanel) e.getSource()).setBackground(new Color(144, 142, 151));
				}
			}
		});
		plotSideBarPane(usrsPanel, "icons/multiacc.png", "Users");
		
		
		return panel;
	}
	
	public void switchMainPanel(String panelName) {
		for (Sidebars mpp : this.mainPanels.values()) {
			if (mpp.getName().equals(panelName)) {
				mpp.setVisible(true);
				this.mainPanel = mpp;
			this.ham_.dispatchEvent(new MouseEvent(ham_, MouseEvent.MOUSE_PRESSED,
					System.currentTimeMillis(), 0, 0, 0, 1, false, MouseEvent.BUTTON1));
			} else {
				mpp.setVisible(false);
			}
		}
	}
	
	public void switchMainPanel(String panelName, boolean err) {
		for (Sidebars mpp : this.mainPanels.values()) {
			if (mpp.getName().equals(panelName)) {
				mpp.setVisible(true);
				this.mainPanel = mpp;
				if (this.sidebar.isShown) {
					this.ham_.dispatchEvent(new MouseEvent(ham_, MouseEvent.MOUSE_PRESSED,
						System.currentTimeMillis(), 0, 0, 0, 1, false, MouseEvent.BUTTON1));
				}
			} else {
				mpp.setVisible(false);
			}
		}
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
		this.mainPanels.put(name, mp);
		
		return mp;
	}
	
	public Sidebars createEmptyMainPane(String name) {
		int x = (int) (((float) this.rootWidth) * 0.117);
		int y = (int) (((float) this.rootHeight) * 0.111);
		int w = (int) (((float) this.rootWidth) * 0.766);
		int h = (int) (((float) this.rootHeight) * 0.808);
		
		Sidebars mp = new Sidebars(x, (int) (((float) this.rootWidth) * 0.223));
		mp.setLayout(null);
		mp.setName(name);
		
		mp.setBounds(x, y, w, h);
		System.out.printf("W:%d H:%d\n", w, h);

		return mp;
	}
	
	public Sidebars createEmptyMainPane(String name, boolean isShow) {
		int x = (int) (((float) this.rootWidth) * 0.117);
		int y = (int) (((float) this.rootHeight) * 0.111);
		int w = (int) (((float) this.rootWidth) * 0.766);
		int h = (int) (((float) this.rootHeight) * 0.808);
		
		Sidebars mp = new Sidebars(x, (int) (((float) this.rootWidth) * 0.223));
		mp.setLayout(null);
		mp.setName(name);
		
		mp.setBounds((int) (((float) this.rootWidth) * 0.223), y, w, h);
		System.out.printf("W:%d H:%d\n", w, h);

		return mp;
	}
	
	public SidebarPanel createSideBarPane(JPanel sb, String name, float yp, float hp, Color color, MouseAdapter ma) {
		Rectangle sbRect = sb.getBounds();
		System.out.println("" + sbRect.width + ":" + sbRect.height);
		int x = 0;
		int y = (int) (((float) sbRect.height) * yp);
		int h = (int) (((float) sbRect.height) * hp);
		int w = sbRect.width;
		
		SidebarPanel panels = new SidebarPanel();
		panels.setName(name);
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
		label.setName(name);
		panel.add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	public JLabel createLabel(boolean isText, JLabel panel, String name, int fontSize, Rectangle labelRect) {
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
		label.setName(name);
		panel.add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	public JLabel createLabel(boolean isText, RoundedLabel panel, String name, int fontSize, Rectangle labelRect) {
		JLabel label = new JLabel(name);
		
		label.setBounds(labelRect);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		if (isText == true) {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
			label.setForeground(Color.white);
		} else {
			label.setText(name);
			label.setFont(new Font("Arial", Font.BOLD, fontSize));
			label.setForeground(Color.black);
		}
		label.setName(name);
		panel.add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}
	
	
	public JLabel createLabel(ImageIcon icon, String name, Rectangle rect, String iconType) {
		JLabel label = new JLabel();
		label.setName(name);
		label.setIcon(icon);
		label.addMouseListener(new MainMouse(this, page, this.mainPanel, label, this.spane, iconType, this.components));
		
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
	
	public JLabel createLabel(RoundedLabel parent, ImageIcon icon, String name, Rectangle rect) {
		JLabel label = new JLabel();
		label.setName(name);
		label.setIcon(icon);
		label.setBounds(rect);
		
		parent.add(label);
		
		return label;
	}

	public JLabel createLabel(JPanel panel, ImageIcon icon, String name, Rectangle rect, String iconType) {
		JLabel label = new JLabel();
		label.setName(name);
		label.setIcon(icon);
		label.addMouseListener(new MainMouse(this, page, this.mainPanel, label, this.spane, iconType, this.components));
		
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

	public JLabel createLabel(Sidebars sb, String name, Rectangle rect, int... rgb) {
		JLabel label = new JLabel();
		
		label.setBounds(rect);
		label.setOpaque(true);
		System.out.println("W: " + rect.width + "H: " + rect.height);
		
		label.setBackground(new Color(rgb[0], rgb[1], rgb[2]));
		label.setName(name);
		
		sb.add(label);
		this.components.put(label.getName(), label);
		
		return label;
	}

	public RoundedLabel createRoundedLabel(Sidebars sb, String name, Rectangle rect, int arcWidth, int arcHeight, Runnable runner, int... rgb) {
		RoundedLabel label = new RoundedLabel("");
		
		label.setBounds(rect);
		label.setOpaque(false);
		label.setBorder(null);
		
		label.setBackground(new Color(rgb[0], rgb[1], rgb[2]));
		label.setName(name);
		
		label.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setBackground(null);
            }
            
            public void mouseExited(MouseEvent e) {
                label.setBorder(null);
                label.setBackground(new Color(rgb[0], rgb[1], rgb[2]));
            }
			
			public void mouseClicked(MouseEvent e) {
                runner.run();
            }
        });
		
		sb.add(label);
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
		
		textField.setText("Search");
		textField.setForeground(Color.lightGray);
		
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
			
		sb.add(textField);
		this.components.put(textField.getName(), textField);
		
		return textField;
	}
	
	public JTextField createPanelTextField(JLabel sb, String name, Rectangle rect, String initial) {
		JTextField tf = new JTextField();
		tf.setText(initial);
		tf.setForeground(Color.gray);
		
		tf.setBounds(rect);
		sb.add(tf);
		
		tf.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isDigit(c) || c == '-' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
					e.consume();
				}
			}
		});
		tf.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (tf.getText().trim().equals(initial)) {
						tf.setText("");
					}
					
					tf.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (tf.getText().trim().equals("")) {
						tf.setText(initial);
					}
					
					tf.setForeground(Color.gray);
				}
			});
		tf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JTextField) e.getSource()).transferFocus();
			}
		});
		
		return tf;
	}
	
	public JTextField createPanelTextField(TransactionPanel tpl, JLabel sb, String name, Rectangle rect, String initial, ActionListener runners) {
		JTextField tf = new JTextField();
		tf.setText(initial);
		tf.setName(name);
		tf.setForeground(Color.gray);
		
		tf.setBounds(rect);
		sb.add(tf);
		((AbstractDocument)tf.getDocument()).putProperty("parent", tf);
		((AbstractDocument)tf.getDocument()).addDocumentListener(new PRDIDListener(tpl));
		tf.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
					e.consume();
				}
			}
		});
		tf.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (tf.getText().trim().equals(initial)) {
						tf.setText("");
					}
					
					tf.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (tf.getText().trim().equals("")) {
						tf.setText(initial);
					}
					
					tf.setForeground(Color.gray);
				}
			});
		tf.addActionListener(runners);
		
		return tf;
	}
	
	public JTextField createPanelTextField(TransactionPanel tpl, Sidebars sb, String name, Rectangle rect, String initial, ActionListener runners) {
		JTextField tf = new JTextField();
		tf.setText(initial);
		tf.setForeground(Color.gray);
		
		tf.setBounds(rect);
		tf.setName(name);
		sb.add(tf);
		((AbstractDocument)tf.getDocument()).putProperty("parent", tf);
		((AbstractDocument)tf.getDocument()).addDocumentListener(new PRDIDListener(tpl));
		
		tf.addFocusListener(new FocusAdapter() {  // Our custom FocusAdapter (Further comments will be added soon)
				@Override
				public void focusGained(FocusEvent e) {
					if (tf.getText().trim().equals(initial)) {
						tf.setText("");
					}
					
					tf.setForeground(Color.BLACK);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					if (tf.getText().trim().equals("")) {
						tf.setText(initial);
					}
					
					tf.setForeground(Color.gray);
				}
			});
		tf.addActionListener(runners);
		
		return tf;
	}

}
