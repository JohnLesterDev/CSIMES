import java.io.*;
import java.util.*;

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.util.*;
import net.csimes.mouse.*;
import net.csimes.audio.*;
import net.csimes.splash.*;
import net.csimes.listeners.*;


public class CLI {
	/*
	public static void run(String cmd, boolean in) {
		String name = cmd.split("/")[1].split(".")[0];
		InputStream is = ResourceControl.getResourceFileStream(cmd, in);
		File file = File.createTempFile(name, ".jar");
		
		OutputStream os = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int bytesRead;
		
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		
		ProcessBuilder pb = new ProcessBuilder(file.getAbsolutePath());
		Process pr = pb.start();
	}
	
	public static void run(String cmd) {
		switch (cmd) {
			case "fontpicker":
				
		}
	}*/
}
