import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {
	
	final private int TANK_WITH = 50;
	final private int TANK_HIGHT = 50;
	private int TANK_X = 10;
	private int TANK_Y = 10;
	
	
	@Override
	protected void paintComponent(Graphics g) {
		Color c = g.getColor();
		Graphics2D graphics2d = (Graphics2D)g;
		graphics2d.fillRect(TANK_X, TANK_Y, TANK_HIGHT,TANK_WITH);
		graphics2d.setColor(c);
		TANK_X += 5;
		TANK_Y += 5;
		g.setColor(c);
	}
	
}
