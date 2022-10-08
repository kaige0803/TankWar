import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

public class EnemyTank implements Runnable {
	public static final int NORMAL = 0, SPEED = 1, ARMOR = 2;
	public int score;
	public int tank_x, tank_y;// 坦克位置
	private int tankSpeed;// 坦克速度
	public int type;// 0:普通坦克 1：速度型坦克 2：重装坦克
	public int blood;
	private Random r = new Random();// 用于产生随机方向和随机的时间间隔。
	private TankState state = TankState.DOWN;// 初始方向向下运动。
	public boolean isMoving = true;
	public boolean isAlive = true;
	public Rectangle rectangle;
	public Thread thread;
	public Timer enemyTankActionTimer;

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
		rectangle = new Rectangle(tank_x, tank_y, 60, 60);
		thread = new Thread(this);// 建立敌方坦克线程。
		enemyTankActionTimer = new Timer(2000, new enemytankListioner());
	}

	public void drawMyself(Graphics g) {// 接受dpanel传来的画笔g2d，将坦克自己画在dpanel上
		switch (state) {
		case LEFT:
			g.drawImage(ResourceRepertory.enemyTank[type][3], tank_x, tank_y, null);
			if (tank_x > 0 && canMoveLeft() && isMoving) {
				tank_x -= tankSpeed;
				rectangle.x = tank_x;
			}
			break;
		case RIGHT:
			g.drawImage(ResourceRepertory.enemyTank[type][1], tank_x, tank_y, null);
			if (tank_x < 1200 && canMoveRight() && isMoving) {
				tank_x += tankSpeed;
				rectangle.x = tank_x;
			}
			break;
		case UP:
			g.drawImage(ResourceRepertory.enemyTank[type][0], tank_x, tank_y, null);
			if (tank_y > 0 && canMoveUp() && isMoving) {
				tank_y -= tankSpeed;
				rectangle.y = tank_y;
			}
			break;
		case DOWN:
			g.drawImage(ResourceRepertory.enemyTank[type][2], tank_x, tank_y, null);
			if (tank_y < 840 && canMoveDown() && isMoving) {
				tank_y += tankSpeed;
				rectangle.y = tank_y;
			}
			break;
		default:
			break;
		}
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
		for (Player player : DrawPanel.players) {
			if ((player.fightingTank != null) && (tank_y == player.fightingTank.tank_y - 60) && (tank_x < player.fightingTank.tank_x + 60) && (tank_x > player.fightingTank.tank_x - 60))
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
		for (Player player : DrawPanel.players) {
			if ((player.fightingTank != null) && (tank_y == player.fightingTank.tank_y + 60) && (tank_x < player.fightingTank.tank_x + 60) && (tank_x > player.fightingTank.tank_x - 60))
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
		for (Player player : DrawPanel.players) {
			if ((player.fightingTank != null) && (tank_x == player.fightingTank.tank_x - 60) && (tank_y < player.fightingTank.tank_y + 60) && (tank_y > player.fightingTank.tank_y - 60))
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
		for (Player player : DrawPanel.players) {
			if ((player.fightingTank != null) && (tank_x == player.fightingTank.tank_x + 60) && (tank_y < player.fightingTank.tank_y + 60) && (tank_y > player.fightingTank.tank_y - 60))
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private class enemytankListioner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int firdelay = r.nextInt(500);
			int dirdelay = r.nextInt(500);
			enemyTankActionTimer.setDelay(firdelay + dirdelay);
			if(isAlive) {
				// 随机加入子弹
				DrawPanel.bullets.add(new Bullet(tank_x, tank_y, state, "enemytank", false));
				try {
					Thread.sleep(500 + firdelay);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				// 随机生成坦克状态
				state = TankState.values()[r.nextInt(4)];
				try {
					Thread.sleep(500 + dirdelay);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}else {
				enemyTankActionTimer.stop();
			}
		} 
	}

}
