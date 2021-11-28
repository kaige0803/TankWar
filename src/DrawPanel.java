import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {

	final private int TANK_WITH = 50;
	final private int TANK_HIGHT = 50;
	int TANK_X = 150;
	int TANK_Y = 150;

	public DrawPanel() {
		super();
		setLayout(null);
		setFocusable(true);
		requestFocus(true);// 这步很关键，否则dpanel无法响应键盘事件
		addKeyListener(new ControlKeyListener());// 给面板添加键盘事件
	}

	@Override
	protected void paintComponent(Graphics g) {
		Color c = g.getColor();
		Graphics2D g2d = (Graphics2D) g;
		// 绘制背景
		g2d.setColor(new Color(255, 200, 200));
		g2d.fillRect(0, 0, getWidth(), getHeight());
		// 绘制坦克
		g2d.setColor(Color.BLACK);
		g2d.fillRect(TANK_X, TANK_Y, TANK_HIGHT, TANK_WITH);
		g.setColor(c);
	}

	private class ControlKeyListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {// 键盘按下去
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				TANK_Y -= 10;
				break;
			case KeyEvent.VK_S:
				TANK_Y += 10;
				break;
			case KeyEvent.VK_A:
				TANK_X -= 10;
				break;
			case KeyEvent.VK_D:
				TANK_X += 10;
				break;
			default:
				break;

			}
		}
	}

}
