package net.csimes.img;

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.*;

import net.csimes.io.*;
import net.csimes.img.*;
import net.csimes.sec.*;
import net.csimes.res.*;
import net.csimes.init.*;
import net.csimes.page.*;
import net.csimes.temp.*;
import net.csimes.splash.*;



public class ImageControl {
	
	private HashMap<String,ImageIcon> allImages;
	
	public static Image resizeImage(Image src, int w, int h) {
		BufferedImage rImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = rImg.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(src, 0, 0, w, h, null);
		g.dispose();
		
		return rImg;
	}
	
	public ImageControl() {
		
		this.allImages = new HashMap<String,ImageIcon>();
	}
	
	public ImageControl putImage(String name, ImageIcon image) {
		this.allImages.put(name, image);
		
		return this;
	}
	
	public HashMap<String,ImageIcon> getImageList() {
		return this.allImages;
	}
	
}
