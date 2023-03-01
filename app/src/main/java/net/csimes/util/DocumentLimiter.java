package net.csimes.util;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;



public class DocumentLimiter extends DocumentFilter {
	
	private int limit;
	
	public DocumentLimiter(int limit) {
		if (limit <=0) {
			throw new IllegalArgumentException("Limit must be > 0.");
		}
		
		this.limit = limit;
	}
	
	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		int curLen = fb.getDocument().getLength();
		int overLimit = (curLen + text.length()) - limit - length;
		
		if (overLimit > 0) {
			text = text.substring(0, text.length() - overLimit);
		}
		
		super.replace(fb, offset, length, text, attrs);
	}
}
