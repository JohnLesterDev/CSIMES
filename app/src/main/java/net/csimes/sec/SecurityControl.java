package net.csimes.sec;

import java.io.*;
import java.util.*;
import java.math.*;
import java.security.*;

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.splash.*;




public class SecurityControl implements Serializable {
	
	public static String token = "LIcayAn.LesTER119pythonPINAKAGWApo";
	
	private Account acc;
	
	private String str = " A&BCDEF@!GHIJKLMNO#PQRSTUVWXYZabcdefghijklmn-opqrstuvwxyz0123456789,~`";
	private char[] strArray = str.toCharArray();
	
	private int key;
	private char[] digestArray;
	private char[][] digestArrays;
	
	private String digested;
	private String result;
	
	private Random rd = new Random();


	public static String toMD5(String passwd) {
		String newPasswd = "";
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(passwd.getBytes());
			
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b: digest) {
				sb.append(String.format("%02x", b & 0xff));
			};
			
			newPasswd = sb.toString();
		} catch (NoSuchAlgorithmException e) {e.printStackTrace();}
		
		return newPasswd;
	}

	private char[][] sliceArray(int key) {
		char[][] dArrays = {
			Arrays.copyOfRange(this.strArray, this.key, this.strArray.length - 1),
			Arrays.copyOfRange(this.strArray, 0, this.key)
		};
		
		this.digestArrays = dArrays;
		
		return dArrays;
	}

	private char[] digesterArray(char[][] dArrays) {
		char[] dArray = new char[this.strArray.length];
		int i = 0;
		
		for (char[] halfDArray : digestArrays) {
			for (char content : halfDArray) {
				dArray[i] = content;
				i++;
			}
		};
		
		return dArray;
	}
	
	private String digesterString(char[] dArray) {
		String tempString = "";
		
		for (char content : dArray) {
			tempString += "" + content;
		}
		
		return tempString;
	}

	public SecurityControl encryptRaw(String string, int key) {
		this.key = key;
		
		this.digestArrays = sliceArray(key);
		this.digestArray = digesterArray(this.digestArrays);
		this.digested = digesterString(this.digestArray);

		String resultDigested = "";
		for (char c : string.toCharArray()) {
			int idx = this.str.indexOf("" + c);
			resultDigested += "" + this.digestArray[idx];
		}
		
		this.result = resultDigested;
		
		return this;
		
	}
	
	public SecurityControl decryptRaw(String str, int key) {
		this.key = key;
		
		this.digestArrays = sliceArray(key);
		this.digestArray = digesterArray(this.digestArrays);
		this.digested = digesterString(this.digestArray);
		
		String resultDigested = "";
		for (char c : str.toCharArray()) {
			int idx = this.digested.indexOf("" + c);
			resultDigested += "" + this.strArray[idx];
		}
		
		this.result = resultDigested;
		
		return this;
		
	}

	public SecurityControl encryptString(String str) {
		this.key = rd.nextInt(this.str.length() - 13) + 10;
		
		String sKey = "" + key;
		String keyF = "" + sKey.toCharArray()[0];
		String keyS = "" + sKey.toCharArray()[1];
		
		this.result = keyF + encryptRaw(str, key).getResult() + keyS;
		
		return this;
	}
	
	public SecurityControl decryptString(String string) {
		this.key = Integer.parseInt(("" + string.toCharArray()[0]) + ("" + string.toCharArray()[string.length() - 1]));
		char[] newChar = Arrays.copyOfRange(string.toCharArray(), 1, string.length() - 1);
		String newStr = "";
		for (char c : newChar) {
			newStr += c;
		}
		
		this.result = decryptRaw(newStr, this.key).getResult();
		
		return this;
	}
	
	public SecurityControl(Account acc) {
		
		this.acc = acc;
		
	}
	
	public Account encryptAccount() {
		return this.acc.setUserName(this.encryptString(this.acc.getUserName()).getResult())
					  .setPasswd(this.encryptString(this.acc.getPasswd()).getResult(), SecurityControl.token);
	}
	
	public Account decryptAccount() {
		return this.acc.setUserName(this.decryptString(acc.getUserName()).getResult())
					  .setPasswd(this.decryptString(acc.getPasswd()).getResult(), SecurityControl.token);
	}
	
	public SecurityControl() {
		
	}
	
	public static String getCheckSum(String path) {
		byte[] byteArray = null;
		byte[] hash = null;
		try {
			byteArray = ResourceControl.getResourceFileStream(path, true).readAllBytes();
		} catch (IOException e) {e.printStackTrace();};
		
		try {
			hash = MessageDigest.getInstance("MD5").digest(byteArray);
		} catch (NoSuchAlgorithmException e) {e.printStackTrace();};
		
		String hashString = new BigInteger(1, hash).toString(16);
		return hashString;
	}
	
	public static boolean integrityVerified(ArrayList<String> files, String checksumsFile) {
		ArrayList<String> checksums = new ArrayList<String>();
		boolean Status = false;
		
		try {
			BufferedReader reader = new BufferedReader(ResourceControl.getResourceFileStream("checksum/" + checksumsFile + ".checks"));
			String line;
			
			while ((line = reader.readLine()) != null) {
				checksums.add(line);
			};
			
			reader.close();
		} catch (IOException e) {e.printStackTrace();};
		
		Status = true;
		
		for (int i = 0; i < checksums.size(); i++) {
			String checksum = files.get(i) + ":" + SecurityControl.getCheckSum(files.get(i));
			
			if (!checksums.contains(checksum)) {
				Status = false;
			}
			
		};
		
		return Status;
	}
	
	
	public int getKey() {return this.key;}
	public String getResult() {return this.result;}
	
}
