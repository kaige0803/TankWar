import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {
	
	final private int TANK_WITH = 50;
	final private int TANK_HIGHT = 50;
	private int TANK_X = 50;
	private int TANK_Y = 50;
	
	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.fillRect(TANK_X, TANK_Y, TANK_HIGHT,TANK_WITH);
		g.setColor(c);
		TANK_X += 5;
		TANK_Y += 5;
	}
//	@Override
//	public void run() {
//		while (true) {
//			try {
//				Thread.sleep(150);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} 
//			repaint();
//		}
//	}
}
