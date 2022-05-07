import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyTank{
	
	private int tank_x, tank_y;//坦克位置
	private int tank_speed = 5;//坦克速度
	private int type;
	private Stage stage = null;
	private State state = State.UP_STAY;//坦克初始状态为向上静止
	public boolean isalive = true;
	private Rectangle rectangle = null;//用于检测碰撞的矩形模型
	private List<Bullet> bullets = new ArrayList<>();//用于存放坦克发射过的子弹
	
	public MyTank(int tank_x, int tank_y, int type, Stage stage) {
		super();
		this.type = type;
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		this.stage = stage;
	}
	
	public Rectangle getRectangle() {
		rectangle = new Rectangle(tank_x, tank_y, 60, 60);
		return rectangle;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void paintMyself(Graphics2D g2d) {//接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		if (isalive) {//如果还活着，根据不同的状态调用不同的图片，并根据边界条件改变坦克坐标。
			switch (state) {
			case LEFT_MOVING:
				g2d.drawImage(ImageUtill.myTanks[type][3], tank_x, tank_y, null);
				if ((tank_x > 0) && canMoveLeft())//判断坦克是否到达边界或者遇到障碍物。
					tank_x -= tank_speed;
				break;
			case RIGHT_MOVING:
				g2d.drawImage(ImageUtill.myTanks[type][1], tank_x, tank_y, null);
				if (tank_x < 1140)
					tank_x += tank_speed;
				break;
			case UP_MOVING:
				g2d.drawImage(ImageUtill.myTanks[type][0], tank_x, tank_y, null);
				if (tank_y > 0)
					tank_y -= tank_speed;
				break;
			case DOWN_MOVING:
				g2d.drawImage(ImageUtill.myTanks[type][2], tank_x, tank_y, null);
				if (tank_y < 840)
					tank_y += tank_speed;
				break;
			case LEFT_STAY:
				g2d.drawImage(ImageUtill.myTanks[type][3], tank_x, tank_y, null);
				break;
			case RIGHT_STAY:
				g2d.drawImage(ImageUtill.myTanks[type][1], tank_x, tank_y, null);
				break;
			case UP_STAY:
				g2d.drawImage(ImageUtill.myTanks[type][0], tank_x, tank_y, null);
				break;
			case DOWN_STAY:
				g2d.drawImage(ImageUtill.myTanks[type][2], tank_x, tank_y, null);
				break;

			default:
				break;
			}
		}else {
			//如果已死，在坦克的中心位置生成爆炸gif，并调用音频。
		}
		//遍历已经打出去的子弹集合，如果越界就删除，否则画在dpanel面板上
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = (Bullet) iterator.next();
			if(bullet.getBullet_x() < 0 || bullet.getBullet_x() > 1200 || bullet.getBullet_y() < 0 || bullet.getBullet_y() > 900) { 
				iterator.remove();}
			else bullet.drawMyself(g2d);// 将dpanel的g2d画笔传给bullet来绘制子弹
		}
		
		//g2d.setColor(color);
	}
	
	private boolean canMoveLeft() {
		// TODO Auto-generated method stub
		return false;
	}

	//处理dpanel传过来的keyPressed键盘事件e，改变状态state，供paintMyself函数画图
	public void setKeyPressedEvent(KeyEvent e) {
		switch (e.getKeyCode()) {//如果用swith的话只能垂直和水平走，因为swith中有break 所以只能响应最先按下的键。如果用if-else就可以斜着走了。
		case KeyEvent.VK_W:
			state = State.UP_MOVING;
			break;
		case KeyEvent.VK_S:
			state = State.DOWN_MOVING;
			break;
		case KeyEvent.VK_A:
			state = State.LEFT_MOVING;
			break;
		case KeyEvent.VK_D:
			state = State.RIGHT_MOVING;
			break;
		default:
			break;

		}
	}
	
	//处理dpanel传过来的keyReleased键盘事件e，来确定抬起按键后坦克的状态
	public void setKeyReleasedEvent(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			state = State.UP_STAY;
			break;
		case KeyEvent.VK_S:
			state = State.DOWN_STAY;
			break;
		case KeyEvent.VK_A:
			state = State.LEFT_STAY;
			break;
		case KeyEvent.VK_D:
			state = State.RIGHT_STAY;
			break;
		default:
			break;
		}
		if(e.getKeyCode() == KeyEvent.VK_H) {//每敲击一下h键就new一个子弹加入集合，需要告诉子弹此时的坦克起始位置和方向。
			bullets.add(new Bullet(tank_x, tank_y, state));
		}
	}

	
	
	public class Bullet{
		
		private int bullet_speed = 20;
		private int bullet_x, bullet_y;//用于接收生成子弹的这一时刻坦克的位置
		private State state;//用于接收生成子弹的这一时刻坦克的状态
		public Point point = null;//用于检测碰撞的点模型
		
		public Bullet(int tank_x, int tank_y, State state) {//需传入这颗子弹在生成的时候的坦克的位置和状态。
			this.bullet_x = tank_x;
			this.bullet_y = tank_y;
			this.state = state;
		}
		
		

		public int getBullet_x() {
			return bullet_x;
		}

		public int getBullet_y() {
			return bullet_y;
		}

		public void drawMyself(Graphics2D g2d) {
			//根据坦克的状态调用不同的子弹图潘，并调整子弹位置。
			if((state == State.DOWN_MOVING) || (state == State.DOWN_STAY)) {
				g2d.drawImage(ImageUtill.bullet[2], bullet_x + 27, bullet_y + 50, null);
				bullet_y += bullet_speed;
			}
			if((state == State.LEFT_MOVING) || (state == State.LEFT_STAY)) {
				g2d.drawImage(ImageUtill.bullet[3], bullet_x - 16, bullet_y + 27, null);
				bullet_x -= bullet_speed;
			}
			if((state == State.RIGHT_MOVING) || (state == State.RIGHT_STAY)) {
				g2d.drawImage(ImageUtill.bullet[1], bullet_x + 50, bullet_y + 27, null);
				bullet_x += bullet_speed;
			}
			if((state == State.UP_MOVING) || (state == State.UP_STAY)) {
				g2d.drawImage(ImageUtill.bullet[0], bullet_x + 27, bullet_y - 16, null);
				bullet_y -= bullet_speed;
			}
		}
		
	}
	
}
