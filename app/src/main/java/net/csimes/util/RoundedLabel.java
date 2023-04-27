package net.csimes.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class RoundedLabel extends JLabel {
    private int arcWidth = 20;
    private int arcHeight = 20;

    public RoundedLabel(String text) {
        super(text);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        super.paintComponent(g2);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += arcWidth;
        size.height += arcHeight;
        return size;
    }

    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
		repaint();
    }

    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
		repaint();
    }
}