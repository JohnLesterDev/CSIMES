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




public class App {
    
	public static void test() throws Exception {
		/*JFrame root = new JFrame();
		
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
		root.setVisible(true);*/
	}
	
	
	public static void start() {
		System.out.println(System.getProperty("java.io.tmpdir"));	

		Initialize.LockFile(() -> {
			Runnable lockRun = new Runnable() {
				public void run() {
					while (true) {
						Initialize.LockFile();
						
						try {
							Thread.sleep(6666);
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
                categoryList.add(row[0].trim());
                descriptionList.add(row[1].trim());
                unitList.add(row[2].trim());
                quantityList.add(Float.parseFloat(row[3].trim()));
                priceList.add(Float.parseFloat(row[4].replace("$", "").trim()) * 55.0f);
            }
            reader.close();
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
			
			for (int i = 0; i < categoryList.size(); i++) {
				ProductIO.write(new SecurityControl(
					new Product(
						i,
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
	
	public static void daemonizer() throws Exception {
		/*String path = System.getProperty("java.io.tmpdir") + File.separator + "LESTERDAEMONPROC.wtf";

        File pidFile = new File(path);
        if (pidFile.exists()) {
            String pid = new String(new FileInputStream(pidFile).readAllBytes()).trim();

            ProcessHandle.of(Long.parseLong(pid)).ifPresent(processHandle -> {
                if (processHandle.isAlive() && processHandle.info().command().orElse("").startsWith("java")) {
                    System.out.println("BWESIT");
					CLIENT.sets();
					return;
                }
            });
            
            pidFile.delete();
        }

		String jarFilePath = new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getAbsolutePath();
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", jarFilePath, "dev.johnlester.server.App", "GRP7PASSWORDID", "aeralesteraxcelsonpaul");
        Process process = pb.start();
		
		CLIENT.sets();
		
		System.out.println("[CSIMES] Lester's Daemon is running... PID: " + Long.toString(process.pid()));

        try (FileOutputStream out = new FileOutputStream(pidFile)) {
            out.write(Long.toString(process.pid()).getBytes());
        }*/
		CLIENT.sets();
	}
	
	
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
