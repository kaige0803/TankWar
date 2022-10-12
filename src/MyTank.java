import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.Timer;

public class MyTank implements Runnable {
	public static final int RED_TANK = 0, GREEN_TANK = 1;
	private int[] controlKeys;
	public int blood = 3;
	public int start_x, start_y;//坦克起始位置
	public int tank_x, tank_y;// 坦克位置
	public int tank_width, tank_height;
	private int tankSpeed = 5;// 坦克速度
	public int myTankType;//0:红  1:蓝
	public TankState state = TankState.UP;
	public boolean isMoving = false;
	public Rectangle rectangle;
	public boolean isAlive = true;
	public Thread keyboardThread;
	private boolean canFire = true;
	public String owner;
	public Timer timer;

	public MyTank(int start_x, int start_y, int myTankType, String owner, int[] controlKeys) {
		this.controlKeys = controlKeys;
		this.owner = owner;
		this.myTankType = myTankType;
		this.start_x = start_x;
		this.start_y = start_y;
		this.tank_x = start_x;
		this.tank_y = start_y;
		tank_width = ResourceRepertory.myTanks[0][0].getWidth();
		tank_height = ResourceRepertory.myTanks[0][0].getHeight();
		rectangle = new Rectangle(tank_x, tank_y, tank_width, tank_height);
		keyboardThread = new Thread(this);
	}

	public void drawMyself(Graphics g) {// 接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		Color c = g.getColor();
		for(int i = 0; i < 3; i++) {
			g.drawRoundRect(tank_x + i*20, tank_y-6, 20, 6, 8, 8);
		}
		g.setColor(Color.RED);
		for(int i = 0; i < blood; i++) {
			g.fillRoundRect(tank_x + i*20, tank_y-6, 20, 6, 8, 8);
		}
		switch (state) {
		case LEFT:
			g.drawImage(ResourceRepertory.myTanks[myTankType][3], tank_x, tank_y, null);
			if (tank_x > 0 && canMove(tank_x - tankSpeed, tank_y) && isMoving) {// 判断坦克是否到达边界或者遇到障碍物。
				tank_x -= tankSpeed;
				rectangle.x = tank_x;
			}
			break;
		case RIGHT:
			g.drawImage(ResourceRepertory.myTanks[myTankType][1], tank_x, tank_y, null);
			if (tank_x < 1200 && canMove(tank_x + tankSpeed, tank_y) && isMoving) {
				tank_x += tankSpeed;
				rectangle.x = tank_x;
			}
			break;
		case UP:
			g.drawImage(ResourceRepertory.myTanks[myTankType][0], tank_x, tank_y, null);
			if (tank_y > 0 && canMove(tank_x, tank_y - tankSpeed) && isMoving) {
				tank_y -= tankSpeed;
				rectangle.y = tank_y;
			}
			break;
		case DOWN:
			g.drawImage(ResourceRepertory.myTanks[myTankType][2], tank_x, tank_y, null);
			if (tank_y < 840 && canMove(tank_x, tank_y + tankSpeed) && isMoving) {
				tank_y += tankSpeed;
				rectangle.y = tank_y;
			}
			break;
		default:
			break;
		}
		g.setColor(c);
	}
	
	private boolean canMove(int tank_x, int tank_y) {
		Rectangle myTankRec = new Rectangle(tank_x, tank_y, tank_height, tank_width);
		for (Obstacle obstacle : DrawPanel.nowStage.obstacles) {
			if ((myTankRec.intersects(obstacle.rectangle)) && (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : DrawPanel.nowStage.enemyTanks) {
			if (myTankRec.intersects(enemyTank.rectangle))
				return false;
		}
		for (Player player : DrawPanel.players) {
			if ((player.fightingTank != null) && (player.fightingTank != this) && (myTankRec.intersects(player.fightingTank.rectangle)))
				return false;
		}
		return true;
	}

	public void fire() {
		if (canFire) {
			DrawPanel.bullets.add(new Bullet(tank_x, tank_y, state, this.owner, true));
			new Thread(() -> new PlayWav(PlayWav.BULLET_FLYING).play()).start();
			new Thread(() -> {
				canFire = false;
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				canFire = true;
			}).start();
		}
	}

	@Override
	public void run() {
		while (isAlive) {
			if (DrawPanel.keyboardPressing[controlKeys[0]]) {
				isMoving = true;
				state = TankState.UP;
			}
			else if (DrawPanel.keyboardPressing[controlKeys[1]]) {
				isMoving = true;
				state = TankState.RIGHT;
			}
			else if (DrawPanel.keyboardPressing[controlKeys[2]]) {
				isMoving = true;
				state = TankState.DOWN;
			}
			else if (DrawPanel.keyboardPressing[controlKeys[3]]) {
				isMoving = true;
				state = TankState.LEFT;
			}
			else  {
				isMoving = false;
			}
			if (DrawPanel.keyboardPressing[controlKeys[4]]) fire();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			keyboardThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void rest() {
			tank_x = start_x;
			tank_y = start_y;
			state = TankState.UP;
			isMoving = false;
	}
}
