import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sidebars extends JPanel {
    private int startX, startY, endX, endY;
    private Timer timer;
    private int stepSize = 5;
    
    public Sidebars(Point starts, Point ends) {
        this.startX = starts.x;
        this.startY = starts.y;
        this.endX = ends.x;
        this.endY = ends.y;
        timer = new Timer(0, new ActionListener() {
            int dx = (endX - startX > 0) ? stepSize : -stepSize;
            int dy = (endY - startY > 0) ? stepSize : -stepSize;
			
            public void actionPerformed(ActionEvent e) {
                int x = getLocation().x;
                int y = getLocation().y;
                if ((dx > 0 && x + dx < endX) || (dx < 0 && x + dx > endX)) {
                    x += dx;
                } else {
                    x = endX;
                }
                if ((dy > 0 && y + dy < endY) || (dy < 0 && y + dy > endY)) {
                    y += dy;
                } else {
                    y = endY;
                }
                setLocation(x, y);
                if (getLocation().equals(new Point(endX, endY))) {
                    timer.stop();
                }
            }
        });
    }
    
    public void shows() {
        timer.start();
    }
}
