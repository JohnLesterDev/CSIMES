package net.csimes.panels;

import java.io.*;													 
import java.awt.*;										 	         
import java.util.*;													 
import java.time.*;													 
import javax.swing.*;												 
import java.awt.image.*;
import java.awt.event.*;											 
import javax.swing.text.*;											 
import javax.swing.table.*;										 
import javax.swing.border.*;

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


/**
* InventoryPanel is a class inside the panels package that
* wraps and represents the main inventory JPanel of the MAINPAGE window. 
* This class inherits the JPanel class with extra methods to ensure the proper position,
* components and hotkeys for the inventory table and other UI components.
* This class will also connect and interact with the Inventory class and incorporates the 
* methods to represent the products and other information about the inventory.
*
* @author JohnLesterDev
* @version 0.7.7
*/
public class TransactionPanel {
	/** The MAINPAGE class that will be used.*/
	public MAINPAGE mainp;
	
	/** The main Sidebars class that will be used as the main panel.*/
	public Sidebars panel;
	
	/** The JTable that will be used for displaying information about the product*/
	public JTable table;
	
	/** The JScrollPane that will be used for the table field.*/
	public JScrollPane spane;
	
	public Thread thread;
	public JTextField maintf;
	
	public JLabel detPRDID_, detDESC_, detSTOCK_, detPRC_;
	
	
	/**
	* Creates an InventoryPanel instance with the MAINPAGE argument specified.
	*/
	public TransactionPanel(MAINPAGE mainpage) {
		this.mainp = mainpage;
		this.panel = this.mainp.createEmptyMainPane("transactionpanel");
		this.panel.isShown = true;
		this.setPanel();
		this.setComponents();
		this.setHotkeys();
	}
	
	public void getProductDetails(int id) {
		if (Inventory.getProdsByID().containsKey(id)) {
			Product prd = Inventory.getProdsByID().get(id);
			
			detDESC_.setText(prd.name);
			detDESC_.setToolTipText(prd.name);
			detPRDID_.setText(String.format("%06d", prd.productID));
			detSTOCK_.setText(String.format("%d", prd.quantity));
			detPRC_.setText(String.format("%.2f", prd.totals()));
		} else {
			detDESC_.setText("");
			detDESC_.setToolTipText("");
			detDESC_.setText("");
			detPRDID_.setText("");
			detSTOCK_.setText("");
			detPRC_.setText("");
		}
	}
	
	public void getProductDetails(String s) {
		detDESC_.setText("");
		detDESC_.setToolTipText("");
			detPRDID_.setText("");
			detSTOCK_.setText("");
			detPRC_.setText("");
	}
	
	public void addProductToTransaction(int id, int quantity) {
		if (Inventory.getProdsByID().containsKey(id) && quantity > 0) {
			Product prd = Inventory.getProdsByID().get(id);

			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			tableModel.addRow(new Object[]{
				String.format("%06d", prd.productID),
				prd.name,
				quantity,
				String.format("%.2f", prd.price),
				String.format("%.2f",  ((float) quantity) * prd.price)
			});
			table.setModel(tableModel);
		}
	}
	
	/**
	* Sets the Sidebars panel with the necessary positions and adding it to the 
	* HashMap fields of the MAINPAGE class inside the InventoryPanel instance.
	*/
	public void setPanel() {
		this.mainp.page.getContentPane().add(this.panel);
		this.mainp.mainPanels.put(this.panel.getName(), this.panel);
	}
	
	/**
	* Sets the components (JComponent) needed for the panel such as JTable and JLabels
	* that will be used to display crucial information about the inventory.
	*/
	public void setComponents() {
		// Ari sugod
		Rectangle mpr = this.panel.getBounds();
		
		this.table = new JTable();

		this.table.setModel(
			new CTableModel(null, new String[]{"Product ID", "Item Description", "Quantity", "Unit Price", "Total Price"})
		);
		

		JTableHeader header = this.table.getTableHeader();
        header.setDefaultRenderer(new CHeaderRenderer());
		table.getTableHeader().setReorderingAllowed(false);

		CTableRenderer renderer = new CTableRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, renderer);

		this.spane = new JScrollPane(this.table);
		this.spane.setName("tablespane2");
		panel.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				panel.requestFocusInWindow();
			}
		});
		
		Rectangle spR = new Rectangle(
			(int) (((float) mpr.width) * 0.341),
			(int) (((float) mpr.height) * 0.014),
			(int) (((float) mpr.width) * 0.657),
			(int) (((float) mpr.height) * 0.778)
		);
		
		this.spane.setBounds(spR);
		this.panel.add(this.spane);
		this.mainp.components.put(this.spane.getName(), this.spane);
		
		
		Rectangle dflR = new Rectangle(
			(int) (((float) mpr.width) * 0.032),
			(int) (((float) mpr.height) * 0.052),
			(int) (((float) mpr.width) * 0.084),
			(int) (((float) mpr.height) * 0.042)
		);
		JLabel dateforLabel = this.mainp.createLabel(
			false, 
			this.panel,
			"Date:",
			(int) (((float) dflR.height) * 0.8),
			dflR
		);
		
		Rectangle iflR = new Rectangle(
			(int) (((float) mpr.width) * 0.032),
			(int) (((float) mpr.height) * 0.103),
			(int) (((float) mpr.width) * 0.084),
			(int) (((float) mpr.height) * 0.042)
		);
		JLabel idforLabel = this.mainp.createLabel(
			false, 
			this.panel,
			"Order ID:",
			(int) (((float) iflR.height) * 0.8),
			iflR
		);
		
		Rectangle dlR = new Rectangle(
			(int) (((float) mpr.width) * 0.122),
			(int) (((float) mpr.height) * 0.052),
			(int) (((float) mpr.width) * 0.166),
			(int) (((float) mpr.height) * 0.042)
		);
		JLabel dateLabel = this.mainp.createLabel(
			false, 
			this.panel,
			"",
			(int) (((float) dflR.height) * 0.68),
			dlR
		);
		
		Rectangle iR = new Rectangle(
			(int) (((float) mpr.width) * 0.122),
			(int) (((float) mpr.height) * 0.105),
			(int) (((float) mpr.width) * 0.134),
			(int) (((float) mpr.height) * 0.042)
		);
		JLabel idLabel = this.mainp.createLabel(
			false, 
			this.panel,
			"",
			(int) (((float) iR.height) * 0.68),
			iR
		);
		
		Rectangle inBgR = new Rectangle(
			(int) (((float) mpr.width) * 0.028),
			(int) (((float) mpr.height) * 0.230),
			(int) (((float) mpr.width) * 0.281),
			(int) (((float) mpr.height) * 0.192)
		);
		JLabel inBg = this.mainp.createLabel(this.panel, "inputbg", inBgR,  144, 142, 151);
		
		Rectangle inProdIDLabelR = new Rectangle(
			(int) (((float) inBgR.width) * 0.042),
			(int) (((float) inBgR.height) * 0.206),
			(int) (((float) inBgR.width) * 0.295),
			(int) (((float) inBgR.height) * 0.234)
		);
		JLabel inProdIDLabel = this.mainp.createLabel(
			false, 
			inBg,
			"Product ID:",
			(int) (((float) inProdIDLabelR.height) * 0.55),
			inProdIDLabelR
		);

		Rectangle inQuanLabelR = new Rectangle(
			(int) (((float) inBgR.width) * 0.042),
			(int) (((float) inBgR.height) * 0.551),
			(int) (((float) inBgR.width) * 0.295),
			(int) (((float) inBgR.height) * 0.234)
		);
		JLabel inQuanLabel = this.mainp.createLabel(
			false, 
			inBg,
			"Quantity:",
			(int) (((float) inProdIDLabelR.height) * 0.60),
			inQuanLabelR
		);
		
		Rectangle inProdIDLabelFR = new Rectangle(
			(int) (((float) inBgR.width) * 0.356),
			(int) (((float) inBgR.height) * 0.196),
			(int) (((float) inBgR.width) * 0.572),
			(int) (((float) inBgR.height) * 0.243)
		);
		JTextField inProdIDLabelF = this.mainp.createPanelTextField(
			this,
			inBg, 
			"ProductID", 
			inProdIDLabelFR,
			"Product ID",
			() -> {}
		);
		this.maintf = inProdIDLabelF;

		Rectangle inQuanLabelFR = new Rectangle(
			(int) (((float) inBgR.width) * 0.356),
			(int) (((float) inBgR.height) * 0.561),
			(int) (((float) inBgR.width) * 0.572),
			(int) (((float) inBgR.height) * 0.243)
		);
		JTextField inQuanIDLabelF = this.mainp.createPanelTextField(
			inBg, 
			"quantity", 
			inQuanLabelFR,
			"Quantity"
		);
		inQuanIDLabelF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addProductToTransaction(Integer.valueOf(inProdIDLabelF.getText()), Integer.valueOf(inQuanIDLabelF.getText()));
				inQuanIDLabelF.setText("");
				inProdIDLabelF.setText("");
				maintf.requestFocusInWindow();
			}
		});

		Rectangle dethedBgR = new Rectangle(
			(int) (((float) mpr.width) * 0.028),
			(int) (((float) mpr.height) * 0.478),
			(int) (((float) mpr.width) * 0.281),
			(int) (((float) mpr.height) * 0.056)
		);
		JLabel dethedBg = this.mainp.createLabel(this.panel, "dethedbg", dethedBgR,  144, 142, 151);
		
		Rectangle dethedLabelR = new Rectangle(
			(int) (((float) dethedBgR.width) * 0.018),
			(int) (((float) dethedBgR.height) * 0.161),
			(int) (((float) dethedBgR.width) * 0.679),
			(int) (((float) dethedBgR.height) * 0.677)
		);
		JLabel dethedLabel = this.mainp.createLabel(
			false, 
			dethedBg,
			"Product Details",
			(int) (((float) dethedLabelR.height) * 0.85),
			dethedLabelR
		);
		
		Rectangle detBgR = new Rectangle(
			(int) (((float) mpr.width) * 0.028),
			(int) (((float) mpr.height) * 0.534),
			(int) (((float) mpr.width) * 0.281),
			(int) (((float) mpr.height) * 0.254)
		);
		JLabel detBg = this.mainp.createLabel(this.panel, "detbg", detBgR,  200, 200, 200);
		
		Rectangle detProdIDLR = new Rectangle(
			(int) (((float) detBgR.width) * 0.045),
			(int) (((float) detBgR.height) * 0.106),
			(int) (((float) detBgR.width) * 0.345),
			(int) (((float) detBgR.height) * 0.170)
		);
		JLabel detProdIDL = this.mainp.createLabel(
			false, 
			detBg,
			"Product ID:",
			(int) (((float) detProdIDLR.height) * 0.69),
			detProdIDLR
		);
		
		Rectangle detdescLR = new Rectangle(
			(int) (((float) detBgR.width) * 0.045),
			(int) (((float) detBgR.height) * 0.326),
			(int) (((float) detBgR.width) * 0.345),
			(int) (((float) detBgR.height) * 0.170)
		);
		JLabel detdescL = this.mainp.createLabel(
			false, 
			detBg,
			"Description:",
			(int) (((float) detdescLR.height) * 0.69),
			detdescLR
		);
		
		Rectangle detstckLR = new Rectangle(
			(int) (((float) detBgR.width) * 0.045),
			(int) (((float) detBgR.height) * 0.539),
			(int) (((float) detBgR.width) * 0.345),
			(int) (((float) detBgR.height) * 0.170)
		);
		JLabel detstckL = this.mainp.createLabel(
			false, 
			detBg,
			"Stock:",
			(int) (((float) detstckLR.height) * 0.69),
			detstckLR
		);
		
		Rectangle detprcLR = new Rectangle(
			(int) (((float) detBgR.width) * 0.045),
			(int) (((float) detBgR.height) * 0.759),
			(int) (((float) detBgR.width) * 0.345),
			(int) (((float) detBgR.height) * 0.170)
		);
		JLabel detprcL = this.mainp.createLabel(
			false, 
			detBg,
			"Unit Price:",
			(int) (((float) detprcLR.height) * 0.69),
			detprcLR
		);
		
		Rectangle detPRDIDR = new Rectangle(
			(int) (((float) detBgR.width) * 0.420),
			(int) (((float) detBgR.height) * 0.113),
			(int) (((float) detBgR.width) * 0.504),
			(int) (((float) detBgR.height) * 0.170)
		);
		this.detPRDID_ = this.mainp.createLabel(
			false, 
			detBg,
			"",
			(int) (((float) detPRDIDR.height) * 0.69),
			detPRDIDR
		);
		
		Rectangle detDESCR = new Rectangle(
			(int) (((float) detBgR.width) * 0.420),
			(int) (((float) detBgR.height) * 0.326),
			(int) (((float) detBgR.width) * 0.504),
			(int) (((float) detBgR.height) * 0.170)
		);
		this.detDESC_ = this.mainp.createLabel(
			false, 
			detBg,
			"",
			(int) (((float) detDESCR.height) * 0.6),
			detDESCR
		);
		
		Rectangle detSTOCKR = new Rectangle(
			(int) (((float) detBgR.width) * 0.420),
			(int) (((float) detBgR.height) * 0.539),
			(int) (((float) detBgR.width) * 0.504),
			(int) (((float) detBgR.height) * 0.170)
		);
		this.detSTOCK_ = this.mainp.createLabel(
			false, 
			detBg,
			"",
			(int) (((float) detDESCR.height) * 0.69),
			detSTOCKR
		);
		
		Rectangle detPRCR = new Rectangle(
			(int) (((float) detBgR.width) * 0.420),
			(int) (((float) detBgR.height) * 0.759),
			(int) (((float) detBgR.width) * 0.504),
			(int) (((float) detBgR.height) * 0.170)
		);
		this.detPRC_ = this.mainp.createLabel(
			false, 
			detBg,
			"",
			(int) (((float) detPRCR.height) * 0.69),
			detPRCR
		);
		
		
		
		
		
		
		
		Runnable timeRunnable = new Runnable() {
			@Override
			public void run() {
				while (true) {
					LocalDateTime currentDateTime = LocalDateTime.now();
					String dateTimeString = String.format("%02d/%02d/%04d-%02d:%02d:%02d",
							currentDateTime.getMonthValue(),
							currentDateTime.getDayOfMonth(),
							currentDateTime.getYear(),
							currentDateTime.getHour(),
							currentDateTime.getMinute(),
							currentDateTime.getSecond());
					dateLabel.setText(dateTimeString);
					try {
						Thread.sleep(1000); // sleep for 1 second
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		this.thread = new Thread(timeRunnable);
		thread.setDaemon(true);
		thread.start();
		this.panel.setVisible(false);
	}
	
	/**
	* Sets the hotkeys (InputMap and ActionMap) for the components the panel itself.
	*/
	public void setHotkeys() {
		this.panel.getInputMap().put(KeyStroke.getKeyStroke("control L"), "focusprodidAction");
		this.panel.getActionMap().put("focusprodidAction", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				maintf.requestFocusInWindow();
			}
		});
		MAINPAGE tmpmp = this.mainp;
	}
}

