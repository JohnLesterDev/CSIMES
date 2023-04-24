package dev.johnlester.server;

import dev.johnlester.server.csimes.*;


public class App {
	
	public static void start() {
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
