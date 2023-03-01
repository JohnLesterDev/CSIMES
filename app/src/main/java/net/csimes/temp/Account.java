package net.csimes.temp;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.security.*;

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.splash.*;



public class Account implements Serializable {
	
	private String userName;
	private String passwd;
	private long UID;
	private int accountType;
	
	public HashMap<Integer, String> accTypes = new HashMap<Integer,String>();
	
	private boolean isDev = false;
	private boolean isLordDev = false;
	
	public Account setUserName(String userName) {
		
		this.userName = userName;
		
		return this;
	}
	
	public Account setPasswd(String passwd, String token) {
		
		if (SecurityControl.token.equals(token)) {
			this.passwd = passwd;
		}
		
		return this;
	}
	
	public Account setPasswd(String passwd) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(passwd.getBytes());
			
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b: digest) {
				sb.append(String.format("%02x", b & 0xff));
			};
			
			this.passwd = sb.toString();
			
			return this;

		} catch (NoSuchAlgorithmException e) {e.printStackTrace(); return this;}
	}
	
	public Account setUID() {
		this.UID = ObjectStreamClass.lookup(this.getClass()).getSerialVersionUID();
		
		return this;
	}
	
	public Account setDev(boolean status) {
		if (status == true) {
			this.isDev = true;
		}
		
		return this;
	}
	
	public Account setLordDev(boolean status) {
		if (status == true) {
			this.isDev = true;
			this.isLordDev = true;
		}
		
		return this;
	}
	
	public Account setAccountType(int type) {
		if (type >= 3) {
			throw new IllegalArgumentException("Invalid account type.");
		}
		
		this.accountType = type;
		
		return this;
	}
	
	public String getUID() {
		return "" + this.UID;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public String getPasswd() {
		return this.passwd;
	}
	
	public boolean getDevStatus() {
		return this.isDev;
	}
	
	public boolean getLordDevStatus() {
		return this.isLordDev;
	}
	
	public int getAccountType() {
		return this.accountType;
	}
	
	public Account() {
		setUID();
		this.accTypes.put(new Integer(0), "admin");
		this.accTypes.put(new Integer(1), "owner");
		this.accTypes.put(new Integer(2), "staff");
	
	}
}

