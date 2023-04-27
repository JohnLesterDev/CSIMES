package net.csimes;

import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.*;
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
import net.csimes.client.*;
import net.csimes.panels.*;
import net.csimes.splash.*;
import net.csimes.listeners.*;



/**

The App class contains the main methods for initializing the application, fetching inventory data from a file, and
printing device information to the console. It also includes a test method that is currently commented out.
The class imports several packages including java.io, java.awt, java.net, java.util, javax.swing, java.awt.geom,
java.awt.event, and javax.swing.border.
The class also imports several custom packages including net.csimes.io, net.csimes.img, net.csimes.sec, net.csimes.res,
net.csimes.init, net.csimes.page, net.csimes.temp, net.csimes.util, net.csimes.mouse, net.csimes.table, net.csimes.audio,
net.csimes.client, net.csimes.panels, net.csimes.splash, and net.csimes.listeners.
*/
public class App {
    
	/**
 * A test method that is currently commented out.
 * @throws Exception
 */
	public static void test() throws Exception {
		JFrame root = new JFrame();
		
		root.setLayout(null);
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		root.setSize(444, 444);
		
		JButton btn = new JButton("TEST");
		btn.setContentAreaFilled(false);
		btn.setFocusable(false);
		btn.setBorder(
		 new LineBorder(Color.black) {
					public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
						Graphics2D gg = (Graphics2D) g;
						gg.setColor(getLineColor());
						gg.draw(new RoundRectangle2D.Double(x, y, width, height, 45, 45));
					}
				}
		);
		btn.setBounds(55, 55, 55, 55);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("bruh");
			}
		});
		
		root.getContentPane().add(btn);
		root.setLocationRelativeTo(null);
		root.setVisible(true);
	}
	
	/**
 * The main method that starts the application.
 */
	public static void start() {
		System.out.println(System.getProperty("java.io.tmpdir"));	
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				
				try {
					CLIENT.now.output.writeUTF("GRP7>EXIT");
				} catch (Exception e_) {
					
				};
				
				Initialize.LockFile(true);
			}));

		Initialize.LockFile(() -> {
			Runnable lockRun = new Runnable() {
				public void run() {
					while (true) {
						Initialize.LockFile();
						
						try {
							Thread.sleep(30000);
						} catch (Exception e) {
							
						}
					}
				}
			};
			Thread lockThread = new Thread(lockRun);
			lockThread.setDaemon(true);
			lockThread.start();

			new Initialize();
		}, () -> {
			String msg_ = "Another instance of CSIMES is already running. Please close it and try again.";
			JOptionPane.showMessageDialog(null,
						msg_, 
						"CSIMES - Another CSIMES Instance Running", 
						JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(ImageControl.resizeImage(new ImageIcon(ResourceControl.getResourceFile("icons/csimes_full_bg.png")).getImage(), 35, 35))
						);
		});
	}
	
	
	public static void fetchInventory(String path) {
		ArrayList<String> categoryList = new ArrayList<String>();
        ArrayList<String> descriptionList = new ArrayList<String>();
        ArrayList<String> unitList = new ArrayList<String>();
        ArrayList<Float> quantityList = new ArrayList<Float>();
        ArrayList<Float> priceList = new ArrayList<Float>();
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split("\\|");
				System.out.println(Arrays.toString(row));
				System.out.println(row.length);
                categoryList.add(row[1].trim());
                descriptionList.add(row[2].trim());
                unitList.add(row[3].trim());
                quantityList.add(Float.parseFloat(row[4].trim()));
                priceList.add(Float.parseFloat(row[5].replace("$", "").trim()) * 55.0f);
            }
            reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < categoryList.size(); i++) {
				ProductIO.write(new SecurityControl(
					new Product(
						i + 1,
						categoryList.get(i),
						descriptionList.get(i),
						unitList.get(i),
						quantityList.get(i),
						priceList.get(i)
					)
				).encryptProduct(), Inventory.inventoryPath);
			}  
	}

	
	public static void printDeviceInfo() {
		String os = System.getProperty("os.name");
		String osVersion = System.getProperty("os.version");
		String deviceName = System.getProperty("user.name");
		String deviceBrand = System.getenv("USERDOMAIN");
		String cpuArch = System.getProperty("os.arch");		
		String cpuInfo = System.getenv("PROCESSOR_IDENTIFIER");
		
		Runtime rt = Runtime.getRuntime();
		String pid = String.valueOf(ProcessHandle.current().pid());
		
		System.out.println("\n\nDEVELOPER INFORMATION:\n--------------------------\n");
		System.out.println("OS:        " + os + " (" + osVersion + ")");
		System.out.println("Device:    " + deviceName + " (" + deviceBrand + ")");
		System.out.println("CPU:       " + cpuArch + " | " + cpuInfo);
		System.out.println("PID:       " + pid);
		System.out.println("\n--------------------------\n\n");
	}
	
	/**

This method sets up a daemon process that runs the application if it's not already running.

It uses the CLIENT object to check if the application is already running, and if not, it starts a new process using ProcessBuilder.

The PID of the new process is written to a file named LESTERDAEMONPROC.wtf in the temporary directory.

The method then recursively calls itself to keep the daemon process running.

@throws Exception if an error occurs during the process
*/
	public static void daemonizer() throws Exception {
		String path = System.getProperty("java.io.tmpdir") + File.separator + "LESTERDAEMONPROC.wtf";

        File pidFile = new File(path);
        
		try {
			CLIENT.sets();
			return;
		} catch (java.net.ConnectException e) {
			String jarFilePath = new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
			ProcessBuilder pb = new ProcessBuilder("java", "-cp", jarFilePath, "dev.johnlester.server.App", "GRP7PASSWORDID", "aeralesteraxcelsonpaul");
			Process process = pb.start();
			
			System.out.println("[CSIMES] Lester's Daemon is running... PID: " + Long.toString(process.pid()));
			
			try {
				Thread.sleep(4000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try (FileOutputStream out = new FileOutputStream(pidFile)) {
				out.write(Long.toString(process.pid()).getBytes());
			}

			daemonizer();
		}
	}
	
	/**

The App class serves as the main entry point for the CSIMES application.
This class includes several static methods that perform various functions.
Available methods:
test() - This method can be used for testing and is currently commented out.
start() - This method initializes the application, acquires a file lock,
and runs the Initialize class to start the main program.
fetchInventory(String path) - This method reads data from a file at the given
path and writes it to the Inventory inventoryPath.
printDeviceInfo() - This method prints out device and operating system information.
daemonizer() - This method is used to daemonize the process.
main(String[] args) - This method is the main entry point for the application and
simply calls the start() method.
This class also imports various classes and packages, including java.io, java.awt,
java.net, java.util, javax.swing, net.csimes.io, net.csimes.img, net.csimes.sec,
net.csimes.res, net.csimes.init, net.csimes.page, net.csimes.temp, net.csimes.util,
net.csimes.mouse, net.csimes.table, net.csimes.audio, net.csimes.client, net.csimes.panels,
net.csimes.splash, and net.csimes.listeners.
*/
    public static void main(String[] args) throws Exception {	
		daemonizer();
		printDeviceInfo();
		
		if (args.length == 0) {
			start();
			return;
		}
		
		String filePath = "";
		boolean fileMode = false, testMode = false, startMode = false;
		
		for (String arg : args) {
			if (arg.endsWith(".txt")) {
				fileMode = true;
				filePath = arg;
			}
			if (arg.equals("test")) {
				testMode = true;
			}
			if (arg.equals("run")) {
				startMode = true;
			}
		}
		
		if (!fileMode && !testMode && !startMode) {
			System.out.println("[]> Argument Error: Invalid argument/s provided.");
		}
		
		if (testMode) {
			test();
			System.out.println("\n\n--------------------------\nTest/s Completed :> \t\n--------------------------\n\n");
		}
		if (fileMode) {
			fetchInventory(filePath);
			System.out.println("\n\n--------------------------\nFetching Inventory Completed :> \t\n--------------------------\n\n");
		}
		if (startMode) {
			start();
		}
    }
}
