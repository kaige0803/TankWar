import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class MyTank implements Runnable {
	public static final int RED_TANK = 0, GREEN_TANK = 1;
	private static int[][] controlKeys = {{KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_H},
			{KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_NUMPAD0}};
	public int blood = 3;
	public int start_x, start_y;//坦克起始位置
	public int tank_x, tank_y;// 坦克位置
	private int tankSpeed = 5;// 坦克速度
	public int myTankType;//0:红  1:蓝
	public TankState state = TankState.UP;
	public boolean isMoving = false;
	public Rectangle rectangle;
	public boolean isAlive = true;
	public Thread keyboardThread;
	private boolean canFire = true;
	public String owner;

	public MyTank(int start_x, int start_y, int myTankType, String owner) {
		super();
		this.owner = owner;
		this.myTankType = myTankType;
		this.start_x = start_x;
		this.start_y = start_y;
		this.tank_x = start_x;
		this.tank_y = start_y;
		rectangle = new Rectangle(tank_x, tank_y, 60, 60);
		keyboardThread = new Thread(this);
		//keyboardThread.start();
	}

	public void drawMyself(Graphics g) {// 接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		Color c = g.getColor();
		g.drawRect(tank_x, tank_y-6, 60, 6);
		g.setColor(Color.RED);
		for(int i = 0; i < blood; i++) {
			g.fillRect(tank_x + i*20, tank_y-6, 20, 6);
		}
		switch (state) {
		case LEFT:
			g.drawImage(ResourceRepertory.myTanks[myTankType][3], tank_x, tank_y, null);
			if (tank_x > 0 && canMoveLeft() && isMoving) {// 判断坦克是否到达边界或者遇到障碍物。
				tank_x -= tankSpeed;
				rectangle.x = tank_x;
			}
			break;
		case RIGHT:
			g.drawImage(ResourceRepertory.myTanks[myTankType][1], tank_x, tank_y, null);
			if (tank_x < 1200 && canMoveRight() && isMoving) {
				tank_x += tankSpeed;
				rectangle.x = tank_x;
			}
			break;
		case UP:
			g.drawImage(ResourceRepertory.myTanks[myTankType][0], tank_x, tank_y, null);
			if (tank_y > 0 && canMoveUp() && isMoving) {
				tank_y -= tankSpeed;
				rectangle.y = tank_y;
			}
			break;
		case DOWN:
			g.drawImage(ResourceRepertory.myTanks[myTankType][2], tank_x, tank_y, null);
			if (tank_y < 840 && canMoveDown() && isMoving) {
				tank_y += tankSpeed;
				rectangle.y = tank_y;
			}
			break;
		default:
			break;
		}
		g.setColor(c);
	}

	private boolean canMoveDown() {
		for (Obstacle obstacle : DrawPanel.nowStage.obstacles) {
			if ((tank_y == obstacle.y - 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : DrawPanel.nowStage.enemyTanks) {
			if ((tank_y == enemyTank.tank_y - 60) && (tank_x < enemyTank.tank_x + 60)
					&& (tank_x > enemyTank.tank_x - 60))
				return false;
		}
		return true;
	}

	private boolean canMoveUp() {
		for (Obstacle obstacle : DrawPanel.nowStage.obstacles) {
			if ((tank_y == obstacle.y + 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : DrawPanel.nowStage.enemyTanks) {
			if ((tank_y == enemyTank.tank_y + 60) && (tank_x < enemyTank.tank_x + 60)
					&& (tank_x > enemyTank.tank_x - 60))
				return false;
		}
		return true;
	}

	private boolean canMoveRight() {
		for (Obstacle obstacle : DrawPanel.nowStage.obstacles) {
			if ((tank_x == obstacle.x - 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : DrawPanel.nowStage.enemyTanks) {
			if ((tank_x == enemyTank.tank_x - 60) && (tank_y < enemyTank.tank_y + 60)
					&& (tank_y > enemyTank.tank_y - 60))
				return false;
		}
		return true;
	}

	private boolean canMoveLeft() {
		for (Obstacle obstacle : DrawPanel.nowStage.obstacles) {
			if ((tank_x == obstacle.x + 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : DrawPanel.nowStage.enemyTanks) {
			if ((tank_x == enemyTank.tank_x + 60) && (tank_y < enemyTank.tank_y + 60)
					&& (tank_y > enemyTank.tank_y - 60))
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
			if (DrawPanel.keyboardPressing[controlKeys[myTankType][0]]) {
				isMoving = true;
				state = TankState.UP;
			}
			else if (DrawPanel.keyboardPressing[controlKeys[myTankType][1]]) {
				isMoving = true;
				state = TankState.RIGHT;
			}
			else if (DrawPanel.keyboardPressing[controlKeys[myTankType][2]]) {
				isMoving = true;
				state = TankState.DOWN;
			}
			else if (DrawPanel.keyboardPressing[controlKeys[myTankType][3]]) {
				isMoving = true;
				state = TankState.LEFT;
			}
			else  {
				isMoving = false;
			}
			if (DrawPanel.keyboardPressing[controlKeys[myTankType][4]]) fire();
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
