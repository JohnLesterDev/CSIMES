package net.csimes.res;

import java.io.*;
import java.net.*;
import java.util.*;

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.splash.*;



public class ResourceControl {
	
	public static String[] strList;

	public static InputStreamReader getResourceFileStream(String path) {
		
		return new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
	}
	
	public static InputStream getResourceFileStream(String path, boolean in) {
		
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		return is;
	}
	
	public static URL getResourceFile(String path) {
		
		return Thread.currentThread().getContextClassLoader().getResource(path);
	}
	
	public static ArrayList<String> getResourceList(ArrayList<String> fileList, String list) {
		try {
			BufferedReader br = new BufferedReader(ResourceControl.getResourceFileStream("list/" + list + ".list"));
			String str = "";
			String line;
			
			while ((line = br.readLine()) != null) {
				str += line + ":";
			};
			
			strList = str.split(":");
			
			br.close();
		} catch (IOException e) {e.printStackTrace();}
		
		for (String f : strList) {
			fileList.add(f);
		}
		
		return fileList;
	}
	
}