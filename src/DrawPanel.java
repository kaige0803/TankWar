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
		g.setColor(c);
	}

	
	//将dpanel接收到的键盘信息都发送给坦克类的实例，有实例去实现各个键的功能。
	//为了提高效率也可以在传之前先判断一下按键信息，如果是（wasd）再传递给坦克类。
	private class ControlKeyListener extends KeyAdapter {
		
		@Override//用于坦克移动
		public void keyPressed(KeyEvent e) {// 键盘按下去，大约20-40毫秒触发一次
			myTank.setKeyPressedEvent(e);//将得到的keyEvent传给myTank
		}

		@Override//用于坦克恢复静止以及发射子弹
		public void keyReleased(KeyEvent e) {//键盘抬起触发一次
			myTank.setKeyReleasedEvent(e);//将得到的keyEvent传给myTank
		}

		
		

	}

}
