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
	private int TANK_X = 150;
	private int TANK_Y = 150;
	private int TANK_SPEED = 10;
	State state = State.STAY;// 初始状态设置为：静止

	public DrawPanel() {
		super();
		setLayout(null);
		// 下面两步很关键，否则dpanel无法响应键盘事件
		requestFocus(true);
		setFocusable(true);
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
		Boolean BL = false;
		Boolean BR = false;
		Boolean BU = false;
		Boolean BD = false;
		private void setdir() {
			
		}
		
		@Override
		public void keyPressed(KeyEvent e) {// 键盘按下去，大约20-40毫秒触发一次

			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				BU = true;
				break;
			case KeyEvent.VK_S:
				BD = true;
				break;
			case KeyEvent.VK_A:
				BL = true;
				break;
			case KeyEvent.VK_D:
				BR = true;
				break;
			default:
				break;

			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_W:
				BU = false;
				break;
			case KeyEvent.VK_S:
				BD = false;
				break;
			case KeyEvent.VK_A:
				BL = false;
				break;
			case KeyEvent.VK_D:
				BR = false;
				break;
			default:
				break;

			}

		}

	}

}
