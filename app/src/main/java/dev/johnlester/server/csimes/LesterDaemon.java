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
		
		System.out.println("[SERVER]: Server started at port " + this.PORT);
		Inventory.refresh();
	}
	
	public void starts() throws IOException, UnknownHostException {
		while (true) {
			Socket clientSocket = accept();
			System.out.println("[CONNECT]: A client has been connected. IP = " + (((InetSocketAddress) clientSocket.getRemoteSocketAddress()).getAddress()).toString().replace("/", ""));  

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
			this.clientThreads.add(clientHandlerThread);
			
			clientHandlerThread.start();
		}
	}
	
	public void handler(Socket client, DataInputStream input, DataOutputStream output) {
		while (true) {
			try {
				if (input.available() > 0) {
					String payload = input.readUTF();
					String[] comPayloads = payload.split("\\|\\|\\|");

					if (comPayloads[0].equals("GRP7>EXIT")) {
						client.close();
						System.out.println("[CONNECT]: A client has been disconnected. IP = " + (((InetSocketAddress) client.getRemoteSocketAddress()).getAddress()).toString().replace("/", ""));  
						break;
					}
					
					new Thread(() -> { parsers(client, input, output, comPayloads);}).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		
		return;
	}
	
	public void parsers(Socket client, DataInputStream input, DataOutputStream output, String[] args) {
		String primeCommand = args[0];
		
		switch (primeCommand) {
			case "GRP7>FETCHES" -> fetches(client, input, output, Arrays.copyOfRange(args, 1, args.length));
			case "GRP7>SETS" -> sets(client, input, output, Arrays.copyOfRange(args, 1, args.length));
			default -> {return;}
		};
		
		Inventory.refresh();
	}
	
	public void fetches(Socket client, DataInputStream input, DataOutputStream output, String[] args) {
		System.out.println("[SERVER]: Executing " + Arrays.toString(args) + " from " + (((InetSocketAddress) client.getRemoteSocketAddress()).getAddress()).toString().replace("/", "") + "...");
		String fetchType = args[0];
		
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
		};
		System.out.println("[SERVER]: Command executed successfully.");
	}
	
	public void sets(Socket client, DataInputStream input, DataOutputStream output, String[] args) {
		System.out.println("[SERVER]: Executing " + Arrays.toString(args) + " from " + (((InetSocketAddress) client.getRemoteSocketAddress()).getAddress()).toString().replace("/", "") + "...");
		String fetchType = args[0];
		
		switch (fetchType) {
			case "SETALLID":
				try {
					ArrayList<Integer> allID = Inventory.setAllID();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(allID);
					oos.close();
					byte[] objectBytes = baos.toByteArray();

					String base64String = Base64.getEncoder().encodeToString(objectBytes);
					output.writeUTF(base64String);
				} catch (Exception e) {
					e.printStackTrace();
				};
				break;
			case "SETMAXID":
				try {
					Integer maxID = Inventory.setMaxID();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(maxID);
					oos.close();
					byte[] objectBytes = baos.toByteArray();

					String base64String = Base64.getEncoder().encodeToString(objectBytes);
					output.writeUTF(base64String);
				} catch (Exception e) {
					e.printStackTrace();
				};
				break;
			case "SETPRODSTOID":
				try {
					HashMap<Integer,Product> prodID = Inventory.setProdsToID();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(prodID);
					oos.close();
					byte[] objectBytes = baos.toByteArray();

					String base64String = Base64.getEncoder().encodeToString(objectBytes);
					output.writeUTF(base64String);
				} catch (Exception e) {
					e.printStackTrace();
				};
				break;
			case "SETALLPRODCAT":
				try {
					ArrayList<String> prodCat = Inventory.setAllProdCategories();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(prodCat);
					oos.close();
					byte[] objectBytes = baos.toByteArray();

					String base64String = Base64.getEncoder().encodeToString(objectBytes);
					output.writeUTF(base64String);
				} catch (Exception e) {
					e.printStackTrace();
				};
				break;
			case "SETPRODTABLE":
				try {
					Object[][] prodTable = Inventory.setProductToTable();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(prodTable);
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
		};
		System.out.println("[SERVER]: Command executed successfully.");
	}
}
