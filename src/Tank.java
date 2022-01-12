import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class Tank{
	
	private static final int TANK_WITH = 50, TANK_HIGHT = 50;//坦克尺寸
	private int tank_x, tank_y;//坦克位置
	private int tank_speed = 10;//坦克速度
	private State state = State.UP_STAY;//坦克初始状态为向上静止
	private List<Bullet> bullets = new ArrayList<>();//用于存放坦克发射过的子弹
	
	public Tank(int tank_x, int tank_y) {
		super();
		this.tank_x = tank_x;
		this.tank_y = tank_y;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void paintMyself(Graphics2D g2d) {//接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		Color color = g2d.getColor();
		g2d.setColor(Color.BLACK);
		g2d.fillRect(tank_x, tank_y, TANK_HIGHT, TANK_WITH);
		switch (state) {
		case LEFT_MOVING:
			tank_x -= tank_speed;
			break;
		case RIGHT_MOVING:
			tank_x += tank_speed;
			break;
		case UP_MOVING:
			tank_y -= tank_speed;
			break;
		case DOWN_MOVING :
			tank_y += tank_speed;
			break;

		default:
			break;
		}
		
		//遍历已经打出去的子弹集合，如果越界就删除，否则画在dpanel面板上
		Iterator<Bullet> iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = (Bullet) iterator.next();
			if(bullet.getBullet_x() < 0 || bullet.getBullet_x() > 1000 || bullet.getBullet_y() < 0 || bullet.getBullet_y() > 1000) { 
				iterator.remove();}
			else bullet.drawMyself(g2d);// 将dpanel的g2d画笔传给bullet来绘制子弹
		}
		
		g2d.setColor(color);
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
		if(e.getKeyCode() == KeyEvent.VK_H) {//每敲击一下h键就new一个子弹加入集合，需要告诉子弹起始位置和方向。
			bullets.add(new Bullet(tank_x, tank_y, state));
		}
	}

	
	
	public class Bullet{
		
		private static final int BULLET_HIGHT = 8, BULLET_WITH = 8;//子弹大小
		private int bullet_speed = 20;
		private int bullet_x, bullet_y;//子弹起始位置（需关联坦克位置）
		private State state;//子弹起始状态（需关联坦克位置）
		
		public Bullet(int bullet_x, int bullet_y, State state) {//需传入这颗子弹在生成的时候的坦克的位置和状态
			super();
			this.bullet_x = bullet_x;
			this.bullet_y = bullet_y;
			this.state = state;
		}

		public int getBullet_x() {
			return bullet_x;
		}

		public int getBullet_y() {
			return bullet_y;
		}

		public void drawMyself(Graphics2D g2d) {
			Color color = g2d.getColor();
			g2d.setColor(Color.red);
			g2d.fillOval(bullet_x, bullet_y, BULLET_WITH, BULLET_HIGHT);
			//我需要知道你按ctrl键的时候坦克是什么位置
			if((state == State.DOWN_MOVING) || (state == State.DOWN_STAY)) bullet_y += bullet_speed;
			if((state == State.LEFT_MOVING) || (state == State.LEFT_STAY)) bullet_x -= bullet_speed;
			if((state == State.RIGHT_MOVING) || (state == State.RIGHT_STAY)) bullet_x += bullet_speed;
			if((state == State.UP_MOVING) || (state == State.UP_STAY)) bullet_y -= bullet_speed;
			g2d.setColor(color);
		}
		
	}
	
}
