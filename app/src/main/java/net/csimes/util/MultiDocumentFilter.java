package net.csimes.util;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

import net.csimes.util.*;



public class MultiDocumentFilter extends DocumentFilter {
	
	private DocumentFilter[] filters;
	public int limit;
	
	public MultiDocumentFilter(int limit) {
		this.limit = limit;
	}
	
	@Override
	public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws  BadLocationException {
		if (text == null) {
			return;
		}
		
		replace(fb, offset, 0, text, attr);
	}
	
	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		if (text == null) {
			return;
		}
		
		String curText = fb.getDocument().getText(0, fb.getDocument().getLength());
		int curLength = curText.length() - length;
		int newLength = text.length();
		int maxLength = limit - curLength - newLength;
		
		if (maxLength <= 0) {
			return;
		}
		
		StringBuilder sb = new StringBuilder(maxLength);
		for (int i=0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (Character.isLetterOrDigit(c) && sb.length() < maxLength) {
				sb.append(c);
			}
		}
		
		super.replace(fb, offset, length, sb.toString(), attrs);
 	}
	
}

