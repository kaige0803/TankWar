import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {

	Tank myTank = new Tank(50, 50);

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
		// 将dpanel的画笔传给myTank来绘制坦克
		myTank.paintMyself(g2d);
		g2d.setColor(Color.BLACK);
		g.setColor(c);
	}

	private class ControlKeyListener extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {// 键盘按下去，大约20-40毫秒触发一次
			myTank.setKeyPressedEvent(e);//将得到的keyEvent传给myTank
		}

		@Override
		public void keyReleased(KeyEvent e) {//键盘抬起触发一次
			myTank.setKeyReleasedEvent(e);//将得到的keyEvent传给myTank
		}

	}

}
