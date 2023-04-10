package net.csimes.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.charset.*;



public class CLIENT {
	public static CLIENT now;
	
	public static final String ip = "127.0.0.1";
	public static final int port = 6969;
	
	public int PORT;
	
	public Socket socket;
	public DataOutputStream output;
	public DataInputStream input;
	public String IP, STOPS, SEPARATE, CLIENTID;
	
	
	public static void sets() {
		try {
			CLIENT.now = new CLIENT();
		} catch (Exception e) {
			System.out.println("AAAHHHHHH");
			e.printStackTrace();
		};
		
		return;
	}
	
	
	
	public CLIENT() throws Exception {
		this.IP = "127.0.0.1";
		this.PORT = 6969;
		
		this.STOPS = "\0";
		this.SEPARATE = "\r";
		this.CLIENTID = "dev.johnlester.daemon=JOHNlester119:grp7;";
		
		this.socket = new Socket(IP, PORT);
		this.output = new DataOutputStream(this.socket.getOutputStream());
        this.input = new DataInputStream(this.socket.getInputStream());

        this.output.writeUTF(this.CLIENTID);

		boolean idValidated = this.input.readBoolean();
        if (!idValidated) {
            this.socket.close();
        }
	}
}

