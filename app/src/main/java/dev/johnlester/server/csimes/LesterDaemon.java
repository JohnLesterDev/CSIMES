package dev.johnlester.server.csimes;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.nio.charset.*;

import net.csimes.temp.Product;
import dev.johnlester.server.csimes.Inventory;


public class LesterDaemon extends ServerSocket {
	
	private final InetAddress IP;
	private final int PORT;
	
	private final String STOPS = "\0";
	private final String SEPARATE = "\r";
	private final String CLIENTID = "dev.johnlester.daemon=JOHNlester119:grp7;";
	
	private ArrayList<Thread> clientThreads = new ArrayList<Thread>();
	private int clientThreadID = 1;
	
	public LesterDaemon() throws IOException {
		super(6969, 0, InetAddress.getByName("127.0.0.1"));

		this.PORT = 6969;
		this.IP = InetAddress.getByName("127.0.0.1");		
		
		Inventory.refresh();
	}
	
	public void starts() throws IOException, UnknownHostException {
		while (true) {
			Socket clientSocket = accept();

			DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
			
			String clientID = input.readUTF();
			
			if (!CLIENTID.equals(clientID)) {
				output.writeBoolean(false);
				clientSocket.close();
				continue;
			}
			
			output.writeBoolean(true);
			
			Runnable clientHandlerRun = new Runnable() {
				@Override
				public void run() {
					handler(clientSocket, input, output);
				}
			};
			Thread clientHandlerThread = new Thread(clientHandlerRun);
			clientHandlerThread.setDaemon(true);
			this.clientThreads.add(clientHandlerThread);
			
			clientHandlerThread.start();
		}
	}
	
	public void handler(Socket client, DataInputStream input, DataOutputStream output) {
		while (true) {
			try {
				if (input.available() > 0) {
					String payload = input.readUTF();
					System.out.println(payload);
					String[] comPayloads = payload.split("\\|\\|\\|");
					for (String cc : comPayloads) {
						System.out.println(cc);
					}
					
					if (comPayloads[0].equals("GRP7>EXIT")) {
						client.close();
						break;
					}
					
					parsers(input, output, comPayloads);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		
		return;
	}
	
	public void parsers(DataInputStream input, DataOutputStream output, String[] args) {
		String primeCommand = args[0];   System.out.println("WTF: " + primeCommand);
		
		switch (primeCommand) {
			case "GRP7>FETCHES" -> fetches(input, output, Arrays.copyOfRange(args, 1, args.length));
			
			default -> {return;}
		};
		
		Inventory.refresh();
	}
	
	public void fetches(DataInputStream input, DataOutputStream output, String[] args) {
		String fetchType = args[0];
		System.out.println(fetchType);
		
		switch (fetchType) {
			case "BYID":
				try {
					int productID = Integer.parseInt(args[1]);
					Product prd = Inventory.getProductByID(productID);

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(prd);
					oos.close();
					byte[] objectBytes = baos.toByteArray();

					String base64String = Base64.getEncoder().encodeToString(objectBytes);
					output.writeUTF(base64String);
				} catch (Exception e) {
					e.printStackTrace();
				};
				break;

			case "ALLIDS":
				try {
					ArrayList<Integer> prdList = Inventory.getProdsID();

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(prdList);
					oos.close();
					byte[] objectBytes = baos.toByteArray();

					String base64String = Base64.getEncoder().encodeToString(objectBytes);
					output.writeUTF(base64String);
				} catch (Exception e) {
					e.printStackTrace();
				};
				break;
			default:
				return;
		}

	}
}
