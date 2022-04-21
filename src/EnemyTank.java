import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnemyTank implements Runnable{
	
	//private static final int TANK_WITH = 50, TANK_HIGHT = 50;//坦克尺寸
	private int tank_x, tank_y;//坦克位置
	private int tank_speed = 1;//坦克速度
	private int type ;
	private Random r = new Random();//用于产生随机方向和随机的时间间隔。
	private State state = State.values()[r.nextInt(8)];//随机生成敌方坦克状态。
	private List<Bullet> bullets = new ArrayList<>();//用于存放坦克发射过的子弹
	public boolean isalive = true;
	public EnemyTank(int tank_x, int tank_y, int type) {
		super();
		this.type = type;
		this.tank_x = tank_x;
		this.tank_y = tank_y;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void paintMyself(Graphics2D g2d) {//接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		//Color color = g2d.getColor();
		//g2d.setColor(Color.BLACK);
		//g2d.fillRect(tank_x, tank_y, TANK_HIGHT, TANK_WITH);
		switch (state) {//根据不同的状态调用不同的图片
		case LEFT_MOVING:
			g2d.drawImage(ImageUtill.enemytank[type][3], tank_x, tank_y, null);
			tank_x -= tank_speed;
			break;
		case RIGHT_MOVING:
			g2d.drawImage(ImageUtill.enemytank[type][1], tank_x, tank_y, null);
			tank_x += tank_speed;
			break;
		case UP_MOVING:
			g2d.drawImage(ImageUtill.enemytank[type][0], tank_x, tank_y, null);
			tank_y -= tank_speed;
			break;
		case DOWN_MOVING :
			g2d.drawImage(ImageUtill.enemytank[type][2], tank_x, tank_y, null);
			tank_y += tank_speed;
			break;
		case LEFT_STAY:
			g2d.drawImage(ImageUtill.enemytank[type][3], tank_x, tank_y, null);
			break;
		case RIGHT_STAY:
			g2d.drawImage(ImageUtill.enemytank[type][1], tank_x, tank_y, null);
			break;
		case UP_STAY:
			g2d.drawImage(ImageUtill.enemytank[type][0], tank_x, tank_y, null);
			break;
		case DOWN_STAY:
			g2d.drawImage(ImageUtill.enemytank[type][2], tank_x, tank_y, null);
			break;

		default:
			break;
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
	
	//处理dpanel传过来的keyPressed键盘事件e，改变状态state，供paintMyself函数画图
//	public void setKeyPressedEvent(KeyEvent e) {
//		switch (e.getKeyCode()) {//如果用swith的话只能垂直和水平走，因为swith中有break 所以只能响应最先按下的键。如果用if-else就可以斜着走了。
//		case KeyEvent.VK_W:
//			state = State.UP_MOVING;
//			break;
//		case KeyEvent.VK_S:
//			state = State.DOWN_MOVING;
//			break;
//		case KeyEvent.VK_A:
//			state = State.LEFT_MOVING;
//			break;
//		case KeyEvent.VK_D:
//			state = State.RIGHT_MOVING;
//			break;
//		default:
//			break;
//
//		}
//	}
	
	//处理dpanel传过来的keyReleased键盘事件e，来确定抬起按键后坦克的状态
//	public void setKeyReleasedEvent(KeyEvent e) {
//		switch (e.getKeyCode()) {
//		case KeyEvent.VK_W:
//			state = State.UP_STAY;
//			break;
//		case KeyEvent.VK_S:
//			state = State.DOWN_STAY;
//			break;
//		case KeyEvent.VK_A:
//			state = State.LEFT_STAY;
//			break;
//		case KeyEvent.VK_D:
//			state = State.RIGHT_STAY;
//			break;
//		default:
//			break;
//		}
//		if(e.getKeyCode() == KeyEvent.VK_H) {//每敲击一下h键就new一个子弹加入集合，需要告诉子弹此时的坦克起始位置和方向。
//			bullets.add(new Bullet(tank_x, tank_y, state));
//		}
//	}

	
	
	public class Bullet{
		
		//private static final int BULLET_HIGHT = 8, BULLET_WITH = 8;//子弹大小
		private int bullet_speed = 20;
		private int bullet_x, bullet_y;//用于接收生成子弹的这一时刻坦克的位置
		private State state;//用于接收生成子弹的这一时刻坦克的状态
		
		public Bullet(int tank_x, int tank_y, State state) {//需传入这颗子弹在生成的时候的坦克的位置和状态。
			super();
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
			//Color color = g2d.getColor();
			//g2d.setColor(Color.red);
			//g2d.fillOval(bullet_x, bullet_y, BULLET_WITH, BULLET_HIGHT);
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
			//g2d.setColor(color);
		}
		
	}

	@Override
	public void run() {
		while(isalive) {
			//随机生成坦克状态
			try {
				Thread.sleep(1000 + r.nextInt(2000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			state = State.values()[r.nextInt(8)];
			//随机加入子弹
			try {
				Thread.sleep(1000+r.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			bullets.add(new Bullet(tank_x, tank_y, state));
		}
	}
	
}
