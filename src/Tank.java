import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Tank {
	private int TANK_WITH = 50, TANK_HIGHT = 50;
	private int TANK_X, TANK_Y;
	private int TANK_SPEED = 10;
	boolean moving = false;
	Dir dir = Dir.STAY;//初始状态为静止
	KeyEvent e;//用于接收dpanel的键盘事件
	
	public Tank(int tANK_X, int tANK_Y) {
		super();
		TANK_X = tANK_X;
		TANK_Y = tANK_Y;
	}
	
	public void paintMyself(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(TANK_X, TANK_Y, TANK_HIGHT, TANK_WITH);
		switch (dir) {
		case LEFT:
			TANK_X -= TANK_SPEED;
			break;
		case RIGHT:
			TANK_X += TANK_SPEED;
			break;
		case UP:
			TANK_Y -= TANK_SPEED;
			break;
		case DOWN :
			TANK_Y += TANK_SPEED;
			break;

		default:
			break;
		}
	}
	
	//处理dpanel传过来的键盘事件
	public void setKeyPressedEvent(KeyEvent keyEvent) {
		this.e = keyEvent;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			dir = Dir.UP;
			break;
		case KeyEvent.VK_S:
			dir = Dir.DOWN;
			break;
		case KeyEvent.VK_A:
			dir = Dir.LEFT;
			break;
		case KeyEvent.VK_D:
			dir = Dir.RIGHT;
			break;
		default:
			break;

		}
	}
	
	//处理dpanel传过来的键盘事件
	public void setKeyReleasedEvent(KeyEvent keyEvent) {
		this.e = keyEvent;
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			dir = Dir.STAY;
			break;
		case KeyEvent.VK_S:
			dir = Dir.STAY;
			break;
		case KeyEvent.VK_A:
			dir = Dir.STAY;
			break;
		case KeyEvent.VK_D:
			dir = Dir.STAY;
			break;
		default:
			break;

		}
	}
	
	
}
