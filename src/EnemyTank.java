import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnemyTank implements Runnable{
	
	public int tank_x, tank_y;//坦克位置
	private int tank_speed = 5;//坦克速度
	private int type;
	private DrawPanel drawPanel = null;
	private Random r = new Random();//用于产生随机方向和随机的时间间隔。
	private State state = State.DOWN_MOVING;//初始方向向下运动。
	public List<Bullet> bullets = new ArrayList<>();//用于存放坦克发射过的子弹
	public Iterator<Bullet> iterator = null;
	public boolean isalive = true;
	public EnemyTank(int tank_x, int tank_y, int type, DrawPanel drawPanel) {
		super();
		this.type = type;
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		this.drawPanel = drawPanel;
		new Thread(this).start();//开启地方坦克线程。
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void paintMyself(Graphics2D g2d) {//接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		if (isalive) {//如果还活着，根据不同的状态调用不同的图片，并根据边界条件改变坦克坐标。
			switch (state) {
			case LEFT_MOVING:
				g2d.drawImage(ImageUtill.enemyTank[type][3], tank_x, tank_y, null);
				if (tank_x > 0 && canMoveLeft())
					tank_x -= tank_speed;
				break;
			case RIGHT_MOVING:
				g2d.drawImage(ImageUtill.enemyTank[type][1], tank_x, tank_y, null);
				if (tank_x < 1200 && canMoveRight())
					tank_x += tank_speed;
				break;
			case UP_MOVING:
				g2d.drawImage(ImageUtill.enemyTank[type][0], tank_x, tank_y, null);
				if (tank_y > 0 && canMoveUp())
					tank_y -= tank_speed;
				break;
			case DOWN_MOVING:
				g2d.drawImage(ImageUtill.enemyTank[type][2], tank_x, tank_y, null);
				if (tank_y < 840 && canMoveDown())
					tank_y += tank_speed;
				break;
			case LEFT_STAY:
				g2d.drawImage(ImageUtill.enemyTank[type][3], tank_x, tank_y, null);
				break;
			case RIGHT_STAY:
				g2d.drawImage(ImageUtill.enemyTank[type][1], tank_x, tank_y, null);
				break;
			case UP_STAY:
				g2d.drawImage(ImageUtill.enemyTank[type][0], tank_x, tank_y, null);
				break;
			case DOWN_STAY:
				g2d.drawImage(ImageUtill.enemyTank[type][2], tank_x, tank_y, null);
				break;

			default:
				break;
			}
		}else {//如果已死，在坦克的中心位置生成爆炸gif，并调用音频。
			
		}
		//遍历已经打出去的子弹集合，如果越界就删除，否则画在dpanel面板上
		iterator = bullets.iterator();
		while (iterator.hasNext()) {
			Bullet bullet = (Bullet) iterator.next();
			if(bullet.bullet_x < 0 || bullet.bullet_x > 1260 || bullet.bullet_y < 0 || bullet.bullet_y > 900) { 
				iterator.remove();}
			else bullet.drawMyself(g2d);// 将dpanel的g2d画笔传给bullet来绘制子弹
		}
	}
	
	private boolean canMoveDown() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_y == obstacle.y - 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if((tank_y == enemyTank.tank_y - 60) && (tank_x < enemyTank.tank_x + 60) && (tank_x > enemyTank.tank_x - 60)) return false;
		}
		for (MyTank myTank : drawPanel.myTanks) {
			if((tank_y == myTank.tank_y - 60) && (tank_x < myTank.tank_x + 60) && (tank_x > myTank.tank_x - 60)) return false;
		}
		return true;
	}

	private boolean canMoveUp() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_y == obstacle.y + 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if((tank_y == enemyTank.tank_y + 60) && (tank_x < enemyTank.tank_x + 60) && (tank_x > enemyTank.tank_x - 60)) return false;
		}
		for (MyTank myTank : drawPanel.myTanks) {
			if((tank_y == myTank.tank_y + 60) && (tank_x < myTank.tank_x + 60) && (tank_x > myTank.tank_x - 60)) return false;
		}
		return true;
	}

	private boolean canMoveRight() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_x == obstacle.x - 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if((tank_x == enemyTank.tank_x - 60) && (tank_y < enemyTank.tank_y + 60) && (tank_y > enemyTank.tank_y - 60)) return false;
		}
		for (MyTank myTank : drawPanel.myTanks) {
			if((tank_x == myTank.tank_x - 60) && (tank_y < myTank.tank_y + 60) && (tank_y > myTank.tank_y - 60)) return false;
		}
		return true;
	}

	private boolean canMoveLeft() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if((tank_x == obstacle.x + 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)) return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if((tank_x == enemyTank.tank_x + 60) && (tank_y < enemyTank.tank_y + 60) && (tank_y > enemyTank.tank_y - 60)) return false;
		}
		for (MyTank myTank : drawPanel.myTanks) {
			if((tank_x == myTank.tank_x + 60) && (tank_y < myTank.tank_y + 60) && (tank_y > myTank.tank_y - 60)) return false;
		}
		return true;
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
