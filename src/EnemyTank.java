import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class EnemyTank implements Runnable {
	public static final int NORMAL = 0, SPEED = 1, ARMOR = 2;
	public int score;
	public int tank_x, tank_y;// 坦克位置
	public int tank_width, tank_height;
	private int tankSpeed;// 坦克速度
	public int type;// 0:普通坦克 1：速度型坦克 2：重装坦克
	public int blood;
	private Random r = new Random();// 用于产生随机方向和随机的时间间隔。
	private TankState state = TankState.DOWN;// 初始方向向下运动。
	public boolean isMoving = true;
	public boolean isAlive = true;
	public Rectangle rectangle;
	public Thread thread;

	public EnemyTank(int tank_x, int tank_y, int type) {
		super();
		this.type = type;
		switch (type) {
		case NORMAL:
			tankSpeed = 5;
			blood = 1;
			score = 50;
			break;
		case SPEED:
			tankSpeed = 10;
			blood = 1;
			score = 100;
			break;
		case ARMOR:
			tankSpeed = 5;
			blood = 3;
			score = 200;
			break;

		default:
			break;
		}
		
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		tank_width = ResourceRepertory.enemyTank[0][0].getWidth();
		tank_height = ResourceRepertory.enemyTank[0][0].getHeight();
		rectangle = new Rectangle(tank_x, tank_y, tank_width, tank_height);
		thread = new Thread(this);// 建立敌方坦克线程。
	}

	public void drawMyself(Graphics g) {// 接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		switch (state) {
		case LEFT:
			g.drawImage(ResourceRepertory.enemyTank[type][3], tank_x, tank_y, null);
			if (tank_x > 0 && canMove(tank_x - tankSpeed, tank_y) && isMoving) {
				tank_x -= tankSpeed;
				rectangle.x = tank_x;
			}
			break;
		case RIGHT:
			g.drawImage(ResourceRepertory.enemyTank[type][1], tank_x, tank_y, null);
			if (tank_x < 1200 && canMove(tank_x + tankSpeed, tank_y) && isMoving) {
				tank_x += tankSpeed;
				rectangle.x = tank_x;
			}
			break;
		case UP:
			g.drawImage(ResourceRepertory.enemyTank[type][0], tank_x, tank_y, null);
			if (tank_y > 0 && canMove(tank_x, tank_y - tankSpeed) && isMoving) {
				tank_y -= tankSpeed;
				rectangle.y = tank_y;
			}
			break;
		case DOWN:
			g.drawImage(ResourceRepertory.enemyTank[type][2], tank_x, tank_y, null);
			if (tank_y < 840 && canMove(tank_x, tank_y + tankSpeed) && isMoving) {
				tank_y += tankSpeed;
				rectangle.y = tank_y;
			}
			break;
		default:
			break;
		}
	}
	
	private boolean canMove(int tank_x, int tank_y) {
		Rectangle enemyTankRec = new Rectangle(tank_x, tank_y, tank_height, tank_width);
		for (Obstacle obstacle : DrawPanel.nowStage.obstacles) {
			if ((enemyTankRec.intersects(obstacle.rectangle)) && (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : DrawPanel.nowStage.enemyTanks) {
			if ((enemyTank != this) && enemyTankRec.intersects(enemyTank.rectangle))
				return false;
		}
		for (Player player : DrawPanel.players) {
			if ((player.fightingTank != null) && (enemyTankRec.intersects(player.fightingTank.rectangle)))
				return false;
		}
		return true;
	}

	@Override
	public void run() {
		while (isAlive) {
			// 随机加入子弹
			DrawPanel.bullets.add(new Bullet(tank_x, tank_y, state, "enemytank", false));
			try {
				Thread.sleep(500 + r.nextInt(500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 随机生成坦克状态
			state = TankState.values()[r.nextInt(4)];
			try {
				Thread.sleep(500 + r.nextInt(500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
