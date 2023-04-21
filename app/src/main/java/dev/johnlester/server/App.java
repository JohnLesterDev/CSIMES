package dev.johnlester.server;

import dev.johnlester.server.csimes.*;


public class App {
	
	public static void start() {
		Runnable invenRun = new Runnable() {
			public void run() {
				while (true) {
					try {
						Inventory.refresh();
						Thread.sleep(60000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread t = new Thread(invenRun);
		t.setDaemon(true);
		t.start();
		
		try (LesterDaemon server = new LesterDaemon()) {
			server.starts();
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] argv) {
		if (argv[0].equals("GRP7PASSWORDID") && argv[1].equals("aeralesteraxcelsonpaul")) {
			App.start();
		}
	}
}
