package net.csimes.util;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;



public class AlphaNumericFilter extends DocumentFilter {
	
	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws  BadLocationException {
		String filteredString = filter(string);
		super.insertString(fb, offset, filteredString, attr);
	}
	
	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		String filteredString = filter(text);
		super.replace(fb, offset, length, filteredString, attrs);
	}
	
	private String filter(String str) {
		StringBuilder sb = new StringBuilder(str);
		for (int i=sb.length() - 1; i >= 0; i--) {
			char c = str.charAt(i);
			if (!Character.isLetterOrDigit(c)) {
				sb.deleteCharAt(i);
			}
		}
		
		return sb.toString();
	}
}
