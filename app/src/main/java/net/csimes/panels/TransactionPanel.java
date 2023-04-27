package net.csimes.panels;

import java.io.*;													 
import java.awt.*;										 	         
import java.util.*;													 
import java.time.*;													 
import java.beans.*;													 
import javax.swing.*;												 
import java.awt.image.*;
import java.awt.event.*;											 
import javax.imageio.*;											 
import javax.swing.text.*;											 
import javax.swing.event.*;											 
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
	public CTable table;
	
	public String dateTimeString = "";
	
	/** The JScrollPane that will be used for the table field.*/
	public JScrollPane spane;
	
	public Thread thread;
	public JTextField maintf, incashLabelF, incosLabelF, inProdIDLabelF;
	
	public JLabel detPRDID_, detDESC_, detSTOCK_, detPRC_;
	
	public JLabel ttl, changeValue, orderLabel;
	
	
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
		if (Inventory.getProdsID().contains(id)) {
			Product prd = Inventory.getProductByID(id);
			
			detDESC_.setText(prd.name);
			detDESC_.setToolTipText(prd.name);
			detPRDID_.setText(String.format("%06d", prd.productID));
			detSTOCK_.setText(String.format("%.1f", prd.quantity));
			detPRC_.setText(String.format("%.2f", prd.price));
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
	
	public void setAmoutDue() {
		double amtDue = 0.00;
		int rowCount = table.getRowCount();
		
		for (int i_ = 0; i_ < rowCount; i_++) {
			if ((i_ == 0)) {
				amtDue = Double.parseDouble(String.valueOf(table.getValueAt(i_, 5)).replace(",", ""));
			} else {
				amtDue += Double.parseDouble(String.valueOf(table.getValueAt(i_, 5)).replace(",", ""));
			}
		}
		
		this.ttl.setText(String.format("%,.2f", amtDue));
		
	}
	
	public void addProductToTransaction(int id, int quantity) {
		int rowCount = table.getRowCount();
		if (Inventory.getProdsByID().containsKey(id) && Math.abs(quantity) > 0) {
			Product prd = Inventory.getProdsByID().get(id);

			if (rowCount > 0) {
				int lastProdID = Integer.valueOf(String.valueOf(table.getValueAt(rowCount - 1, 0)));
					if (id == lastProdID) {
						int lastQuantity = (int) table.getValueAt(rowCount - 1, 3);
						quantity  += lastQuantity;
						
						if (quantity < 0) {
							JOptionPane.showMessageDialog(
								null,
								"Quantity cannot be zero or negative. Please enter a valid quantity.",
								"CSIMES - Invalid Quantity",
								JOptionPane.INFORMATION_MESSAGE,
								new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
							);
							
							return;
						}
						
						DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
						tableModel.removeRow(rowCount  - 1);
						tableModel.insertRow(rowCount  - 1, new Object[]{
							String.format("%06d", prd.productID),
							prd.name,
							prd.unit,
							quantity,
							String.format("%,.2f", prd.price),
							String.format("%,.2f",  ((float) quantity) * prd.price)
						});
						Order.addProductToOrder(Integer.parseInt(this.orderLabel.getText()), prd.productID, prd.name, prd.unit, quantity, prd.price);
						table.setModel(tableModel);
						this.setAmoutDue();
						return;
					}
				
				if (quantity < 0) {
					JOptionPane.showMessageDialog(
								null,
								"Quantity cannot be zero or negative. Please enter a valid quantity.",
								"CSIMES - Invalid Quantity",
								JOptionPane.INFORMATION_MESSAGE,
								new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
							);

					return;
				}
				
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
						tableModel.addRow(new Object[]{
							String.format("%06d", prd.productID),
							prd.name,
							prd.unit,
							quantity,
							String.format("%,.2f", prd.price),
							String.format("%,.2f",  ((float) quantity) * prd.price)
						});
						table.setModel(tableModel);
						this.setAmoutDue();
			} else {
				if (quantity < 0) {
					JOptionPane.showMessageDialog(
								null,
								"Quantity cannot be zero or negative. Please enter a valid quantity.",
								"CSIMES - Invalid Quantity",
								JOptionPane.INFORMATION_MESSAGE,
								new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
							);

					return;
				}
				
				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
				tableModel.addRow(new Object[]{
					String.format("%06d", prd.productID),
					prd.name,
					prd.unit,
					quantity,
					String.format("%,.2f", prd.price),
					String.format("%,.2f",  ((float) quantity) * prd.price)
				});
				table.setModel(tableModel);
						this.setAmoutDue();
			}	
		}
	}
	
	public void generateReceipt() {
		// int width = 782;
		// int height = 593;
		// BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Get the Graphics2D object from the BufferedImage
        // Graphics2D g2d = (Graphics2D) image.createGraphics();
		// g2d.setColor(Color.WHITE);
        // g2d.fillRect(0, 0, width, height);

        // Set the font to be used
        // Font font = new Font("Arial", Font.BOLD, 24);
        // g2d.setFont(font);

        // Set the color to be used for drawing
        // g2d.setColor(Color.BLACK);

        // Draw the string
        // String message = "Dave Housing & Construction Supplies";
        // g2d.drawString(message, 172, 33);
		
		// font = new Font("Arial", Font.BOLD, 16);
        // g2d.setFont(font);
		
		// message = "Cor. H. Abellana & Cubacub Rd., Cubacub, Mandaue City";
        // g2d.drawString(message, 177, 73);
		
		// message = "Prop. - Elisea T. Cabatingan Tel.";
        // g2d.drawString(message, 250, 91);

		// message = "Nos.: 514-2900 / 511-5230 / 422-2915";
        // g2d.drawString(message, 230, 110);

        // Dispose of the Graphics2D object
        // g2d.dispose();
		
		// File output = new File(System.getProperty("user.dir") + File.separator + "receipt.png");
        // try {
			// ImageIO.write(image, "png", output);
		// } catch (Exception e) {e.printStackTrace();}
	}
	
	public String processChange() {
		String changeS_ = "";
		if (!(Float.parseFloat(ttl.getText().replace(",", "")) > Float.parseFloat(incashLabelF.getText())) &&
			!(incashLabelF.getText().equals("Enter Cash") || incashLabelF.getText().equals(""))) {
			float change_ = Float.parseFloat(incashLabelF.getText()) - Float.parseFloat(ttl.getText().replace(",", ""));
			changeS_ = String.format("%,.2f", change_);
			changeValue.setText(changeS_);
		} else {
			changeValue.setText("0.00");
		}
		
		return changeS_;
	}
	
	public void processTransaction() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowCount = model.getRowCount();
		
		if (rowCount <= 0) {
			JOptionPane.showMessageDialog(
				null,
				"No Products to be processed.",
				"CSIMES - Process Products",
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
			);
			
			SwingUtilities.invokeLater(() -> {inProdIDLabelF.requestFocusInWindow();});
			
			return;
		}
		
		if (incashLabelF.getText().equals("Enter Cash") || incashLabelF.getText().equals("")) {
			JOptionPane.showMessageDialog(
				null,
				"Please enter the cash amount.",
				"CSIMES - Process Cash",
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
			);
			
			SwingUtilities.invokeLater(() -> {incashLabelF.requestFocusInWindow();});
			
			return;
		}
		
		if (Float.parseFloat(ttl.getText().replace(",", "")) > Float.parseFloat(incashLabelF.getText())) {
			JOptionPane.showMessageDialog(
				null,
				"Insufficient cash amount.",
				"CSIMES - Process Cash",
				JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
			);
			incashLabelF.setText("");
			SwingUtilities.invokeLater(() -> {incashLabelF.requestFocusInWindow();});
			
			return;
		}
		
		
		Runnable procRun = new Runnable() {
			public void run() {
				JOptionPane.showOptionDialog(
					null,
					"Processing Transaction...",
					"CSIMES - Process Transaction",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE,
					new ImageIcon(ResourceControl.getResourceFile("icons/loadingGIF_small.gif")),
					new Object[]{},
					null
				);
			}
		};
		Thread procThread = new Thread(procRun);
		procThread.start();

		for (int i = 0; i < rowCount; i++) {
			int productId = Integer.valueOf(String.valueOf(table.getValueAt(i, 0)));
			int quantity = (int) model.getValueAt(i, 3);
			if (!decrementProductQuantity(productId, quantity)) {
				Window[] windows = Window.getWindows();
				for (Window window : windows) {
				   if (window instanceof JDialog) {
					  JDialog dialog = (JDialog) window;
					  Container contentPane = dialog.getContentPane();
					  if (contentPane.getComponentCount() > 0 && contentPane.getComponent(0) instanceof JOptionPane) {
						 dialog.dispose();
					  }
				   }
				}
				
				try {
					procThread.interrupt();
				} catch (Exception e) {
					e.printStackTrace();
				};
				
				JOptionPane.showMessageDialog(
								null,
								"Quantity result cannot be zero or negative. Please enter a valid quantity. (" + i + ")",
								"CSIMES - Invalid Quantity",
								JOptionPane.INFORMATION_MESSAGE,
								new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
							);
				return;
			}
		}
		
		this.processChange();
		this.generateReceipt();

		Window[] windows = Window.getWindows();
		for (Window window : windows) {
				   if (window instanceof JDialog) {
					  JDialog dialog = (JDialog) window;
					  Container contentPane = dialog.getContentPane();
					  if (contentPane.getComponentCount() > 0 && contentPane.getComponent(0) instanceof JOptionPane) {
						 dialog.dispose();
					  }
				   }
				}
			
			try {
					procThread.interrupt();
				} catch (Exception e) {
					e.printStackTrace();
				};
				
		
			JOptionPane.showOptionDialog(
			null, 
			"<html><p align='center'>Transaction Completed!</p><br><p align='center'>Change: " + this.processChange() + "</p><br></html>", "CSIMES - Transaction Status", 
			JOptionPane.DEFAULT_OPTION, 
			JOptionPane.PLAIN_MESSAGE,
			new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35)),
			new String[]{"OK"},
			"OK"
		);
		
		Order.finalOrder(Integer.parseInt(this.orderLabel.getText()), Float.parseFloat(this.ttl.getText().replace(",", "")), Float.parseFloat(this.incashLabelF.getText().replace(",", "")), Float.parseFloat(this.changeValue.getText().replace(",", "")));	
		
		model.setRowCount(0); // Clear the table for a new transaction
		this.incashLabelF.setText("Enter Cash");
		this.incosLabelF.setText("Unknown Customer");
		this.setAmoutDue();
		this.changeValue.setText("0.00");
		Order.createOrder(orderLabel);
	}
	
	public void voidTransaction() {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowCount = model.getRowCount();
		this.maintf.setText("Unknown Customer");
		SwingUtilities.invokeLater(() -> {maintf.requestFocusInWindow();});
		model.setRowCount(0);
		JOptionPane.showOptionDialog(
			null, 
			"The transaction has been successfully voided!", "CSIMES - Void Transaction Status", 
			JOptionPane.DEFAULT_OPTION, 
			JOptionPane.PLAIN_MESSAGE,
			new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35)),
			new String[]{"OK"},
			"OK"
		);
		Order.voidOrder(Integer.parseInt(this.orderLabel.getText()));
		Order.createOrder(orderLabel);
		this.setAmoutDue();
	}
	
	public void deleteProductFromTransaction() {
		int rowIndex = table.getSelectedRow();
		String msg_ = "Do you wish to delete this Product from Transaction? (ROW: " + rowIndex + ")";
			int confirmation = JOptionPane.showOptionDialog(null,
								msg_, 
								"CSIMES - Delete Product from Transaction", 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.QUESTION_MESSAGE,
								new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35)),
								new String[]{"Yes", "Cancel"},
								"Yes"
					);
            if (confirmation != JOptionPane.YES_OPTION) {
				return;
			}
		// If a row is selected
		if (rowIndex != -1) {
			// Remove the selected row from the table model
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.removeRow(rowIndex);
		}
	}
	
	public boolean decrementProductQuantity(int id, float quantity) {
		Product prd = Inventory.getProductByID(id);
		
		if ((prd.quantity -= quantity) <= 0) {
			return false;
		}
		
		prd.filePath.delete();
		
		ProductIO.write(new SecurityControl(new Product(
			prd.productID,
			prd.category,
			prd.name,
			prd.unit,
			prd.quantity,
			prd.price,
			prd.dateTime
		)).encryptProduct(), Inventory.inventoryPath);
		
		Inventory.refresh();
		
		return true;
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
		
		this.table = new CTable(this.mainp, "transaction");

		this.table.setModel(
			new CTableModel(null, new String[]{"Product ID", "Item Description", "Unit", "Quantity", "Unit Price", "Total Price"})
		);
		

		JTableHeader header = this.table.getTableHeader();
        header.setDefaultRenderer(new CHeaderRenderer("transaction"));
		table.getTableHeader().setReorderingAllowed(false);

		CTableRenderer renderer = new CTableRenderer("transaction");
        table.setDefaultRenderer(Object.class, renderer);

		this.table.setColumnWidths();
		this.table.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("model")) {
					table.setColumnWidths();
				}
			}
		});

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
			(int) (((float) iflR.height) * 0.76),
			iflR
		);
		
		Rectangle cosflR = new Rectangle(
			(int) (((float) mpr.width) * 0.032),
			(int) (((float) mpr.height) * 0.153),
			(int) (((float) mpr.width) * 0.084),
			(int) (((float) mpr.height) * 0.042)
		);
		
		JLabel cosforLabel = this.mainp.createLabel(
			false, 
			this.panel,
			"Customer:",
			(int) (((float) cosflR.height) * 0.68),
			cosflR
		);
		
		Rectangle incosLabelFR = new Rectangle(
			(int) (((float) mpr.width) * 0.129),
			(int) (((float) mpr.height) * 0.153),
			(int) (((float) mpr.width) * 0.155),
			(int) (((float) mpr.height) * 0.042)
		);
		
		this.incosLabelF = this.mainp.createPanelTextField(
			this,
			this.panel, 
			"costumer", 
			incosLabelFR,
			"Unknown Customer",
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField tf = (JTextField)  e.getSource();
					Order.addCustomerToOrder(Integer.parseInt(orderLabel.getText()), tf.getText().trim());
					tf.transferFocus();
				}
			}
		);
		
		this.maintf = incosLabelF;
		
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
		
		Rectangle ordlR = new Rectangle(
			(int) (((float) mpr.width) * 0.122),
			(int) (((float) mpr.height) * 0.103),
			(int) (((float) mpr.width) * 0.166),
			(int) (((float) mpr.height) * 0.042)
		);
		this.orderLabel = this.mainp.createLabel(
			false, 
			this.panel,
			"",
			(int) (((float) dflR.height) * 0.68),
			ordlR
		);
		
		Order.createOrder(orderLabel);
		
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
		this.inProdIDLabelF = this.mainp.createPanelTextField(
			this,
			inBg, 
			"ProductID", 
			inProdIDLabelFR,
			"Product ID",
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					((JTextField)  e.getSource()).transferFocus();
				}
			}
		);

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
				 SwingUtilities.invokeLater(() -> {
					inProdIDLabelF.requestFocusInWindow();
				});
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
			(int) (((float) detdescLR.height) * 0.64),
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

		Rectangle ttlBgR = new Rectangle(
			(int) (((float) mpr.width) * 0.341),
			(int) (((float) mpr.height) * 0.795),
			(int) (((float) mpr.width) * 0.657),
			(int) (((float) mpr.height) * 0.073)
		);
		JLabel ttlBg = this.mainp.createLabel(this.panel, "ttlbg", ttlBgR,  219, 219, 219);
		
		Rectangle ttlLabelR = new Rectangle(
			(int) (((float) ttlBgR.width) * 0.61),
			(int) (((float) ttlBgR.height) * 0.13),
			(int) (((float) ttlBgR.width) * 0.209),
			(int) (((float) ttlBgR.height) * 0.750)
		);
		JLabel ttlLabel = this.mainp.createLabel(
			false, 
			ttlBg,
			"Amount Due:",
			(int) (((float) detstckLR.height) * 0.69),
			ttlLabelR
		);
		
		Rectangle ttlR = new Rectangle(
			(int) (((float) ttlBgR.width) * 0.81),
			(int) (((float) ttlBgR.height) * 0.13),
			(int) (((float) ttlBgR.width) * 0.209),
			(int) (((float) ttlBgR.height) * 0.750)
		);
		this.ttl = this.mainp.createLabel(
			false, 
			ttlBg,
			"0.00",
			(int) (((float) detstckLR.height) * 0.74),
			ttlR
		);
		
		Rectangle cashlR = new Rectangle(
			(int) (((float) mpr.width) * 0.8),
			(int) (((float) mpr.height) * 0.8),
			(int) (((float) mpr.width) * 0.133),
			(int) (((float) mpr.height) * 0.224)
		);
		JLabel cashLabel = this.mainp.createLabel(
			false, 
			this.panel,
			"Cash:",
			(int) (((float) detstckLR.height) * 0.69),
			cashlR
		);
		
		Rectangle incashLabelFR = new Rectangle(
			(int) (((float) mpr.width) * 0.86),
			(int) (((float) mpr.height) * 0.89),
			(int) (((float) mpr.width) * 0.086),
			(int) (((float) mpr.height) * 0.042)
		);
		
		this.incashLabelF = this.mainp.createPanelTextField(
			this,
			this.panel, 
			"cash", 
			incashLabelFR,
			"Enter Cash",
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					processTransaction();
				}
			}
		);
		this.incashLabelF.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
					e.consume();
				}
			}
		});
		
		Rectangle changelR = new Rectangle(
			(int) (((float) mpr.width) * 0.78),
			(int) (((float) mpr.height) * 0.86),
			(int) (((float) mpr.width) * 0.133),
			(int) (((float) mpr.height) * 0.224)
		);
		JLabel changeLabel = this.mainp.createLabel(
			false, 
			this.panel,
			"Change:",
			(int) (((float) detstckLR.height) * 0.69),
			changelR
		);
		
		Rectangle changeR = new Rectangle(
			(int) (((float) mpr.width) * 0.86),
			(int) (((float) mpr.height) * 0.946),
			(int) (((float) ttlBgR.width) * 0.209),
			(int) (((float) ttlBgR.height) * 0.750)
		);
		this.changeValue = this.mainp.createLabel(
			false, 
			this.panel,
			"0.00",
			(int) (((float) detstckLR.height) * 0.7),
			changeR
		);
		
		Runnable timeRunnable = new Runnable() {
			@Override
			public void run() {
				while (true) {
					LocalDateTime currentDateTime = LocalDateTime.now();
					dateTimeString = String.format("%02d/%02d/%04d-%02d:%02d:%02d",
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
		
		table.getInputMap().put(KeyStroke.getKeyStroke("alt W"), "modWPRDAction");
		table.getActionMap().put("modWPRDAction", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				TableColumnModel columnModel = table.getColumnModel();
				for (int i = 0; i < columnModel.getColumnCount(); i++) {
					int columnWidth = columnModel.getColumn(i).getWidth();
					System.out.println("Column " + i + " width: " + columnWidth + " PERCENT TO WIDTH: " + String.format("%.3f", ((float) columnWidth / mainp.rootWidth)));
				}
			}
		});
		
		this.panel.getInputMap().put(KeyStroke.getKeyStroke("alt P"), "prostransAction");
		this.panel.getActionMap().put("prostransAction", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				processTransaction();
			}
		});
		
		this.panel.getInputMap().put(KeyStroke.getKeyStroke("alt V"), "voidtransAction");
		this.panel.getActionMap().put("voidtransAction", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				voidTransaction();
			}
		});
		
		this.panel.getInputMap().put(KeyStroke.getKeyStroke("alt D"), "delprdAction");
		this.panel.getActionMap().put("delprdAction", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				deleteProductFromTransaction();
			}
		});
		
		MAINPAGE tmpmp = this.mainp;
	}
}

