import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyTank{
	
	public  int tank_x, tank_y;//坦克位置
	private int tank_speed = 5;//坦克速度
	private int type;
	private DrawPanel drawPanel = null;
	public State state = State.UP_STAY;//坦克初始状态为向上静止
	public boolean isalive = true;
	public List<Bullet> bullets = new ArrayList<>();//用于存放坦克发射过的子弹
	
	public MyTank(int tank_x, int tank_y, int type, DrawPanel drawPanel) {
		super();
		this.type = type;
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		this.drawPanel = drawPanel;
	}
	
	public List<Bullet> getBullets() {
		return bullets;
	}

	public void paintMyself(Graphics2D g2d) {//接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		if (isalive) {//如果还活着，根据不同的状态调用不同的图片，并根据边界条件改变坦克坐标。
			switch (state) {
			case LEFT_MOVING:
				g2d.drawImage(ImageUtill.myTanks[type][3], tank_x, tank_y, null);
				if (tank_x > 0 && canMoveLeft())//判断坦克是否到达边界或者遇到障碍物。
					tank_x -= tank_speed;
				break;
			case RIGHT_MOVING:
				g2d.drawImage(ImageUtill.myTanks[type][1], tank_x, tank_y, null);
				if (tank_x < 1200 && canMoveRight())
					tank_x += tank_speed;
				break;
			case UP_MOVING:
				g2d.drawImage(ImageUtill.myTanks[type][0], tank_x, tank_y, null);
				if (tank_y > 0 && canMoveUp())
					tank_y -= tank_speed;
				break;
			case DOWN_MOVING:
				g2d.drawImage(ImageUtill.myTanks[type][2], tank_x, tank_y, null);
				if (tank_y < 840 && canMoveDown())
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
			if(bullet.getBullet_x() < 0 || bullet.getBullet_x() > 1260 || bullet.getBullet_y() < 0 || bullet.getBullet_y() > 900) { 
				iterator.remove();}
			else bullet.drawMyself(g2d);// 将dpanel的g2d画笔传给bullet来绘制子弹
		}
	}
	
	private boolean canMoveDown() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_y == obstacle.y - 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.enemyTanks) {
			if((tank_y == enemyTank.tank_y - 60) && (tank_x < enemyTank.tank_x + 60) && (tank_x > enemyTank.tank_x - 60)) return false;
		}
		return true;
	}

	private boolean canMoveUp() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_y == obstacle.y + 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.enemyTanks) {
			if((tank_y == enemyTank.tank_y + 60) && (tank_x < enemyTank.tank_x + 60) && (tank_x > enemyTank.tank_x - 60)) return false;
		}
		return true;
	}

	private boolean canMoveRight() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_x == obstacle.x - 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.enemyTanks) {
			if((tank_x == enemyTank.tank_x - 60) && (tank_y < enemyTank.tank_y + 60) && (tank_y > enemyTank.tank_y - 60)) return false;
		}
		return true;
	}

	private boolean canMoveLeft() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_x == obstacle.x + 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.enemyTanks) {
			if((tank_x == enemyTank.tank_x + 60) && (tank_y < enemyTank.tank_y + 60) && (tank_y > enemyTank.tank_y - 60)) return false;
		}
		return true;
	}
	
	public class Bullet{
		
		private int bullet_speed = 15;
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



	public void fire() {
		bullets.add(new Bullet(tank_x, tank_y, state));
	}
	
}
