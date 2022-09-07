import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class MyTank implements Runnable {
	public int blood = 3;
	public int start_x, start_y;//坦克起始位置
	public int tank_x, tank_y;// 坦克位置
	private int tank_speed = 5;// 坦克速度
	public int player;//0:player1  1:player2
	private DrawPanel drawPanel = null;
	public State state = State.UP;
	public boolean isMoving = false;
	public Rectangle rectangle;
	public boolean isalive = true;
	private static int[][] controlkeys = {{KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_H},
								   {KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_NUMPAD0}};
	private Thread keyboardThread;
	private boolean canFire = true;

	public MyTank(int player, DrawPanel drawPanel) {
		super();
		this.drawPanel = drawPanel;
		this.player = player;
		switch (this.player) {
		case 0:
			this.start_x = 480;
			this.start_y = 840;
			break;
		case 1:
			this.start_x = 720;
			this.start_y = 840;
			break;
		default:
			break;
		}
		this.tank_x = this.start_x;
		this.tank_y = this.start_y;
		rectangle = new Rectangle(tank_x, tank_y, 60, 60);
		keyboardThread = new Thread(this);
		keyboardThread.start();
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
			g.drawImage(ImageUtill.myTanks[player][3], tank_x, tank_y, null);
			if (tank_x > 0 && canMoveLeft() && isMoving) {// 判断坦克是否到达边界或者遇到障碍物。
				tank_x -= tank_speed;
				rectangle.x = tank_x;
			}
			break;
		case RIGHT:
			g.drawImage(ImageUtill.myTanks[player][1], tank_x, tank_y, null);
			if (tank_x < 1200 && canMoveRight() && isMoving) {
				tank_x += tank_speed;
				rectangle.x = tank_x;
			}
			break;
		case UP:
			g.drawImage(ImageUtill.myTanks[player][0], tank_x, tank_y, null);
			if (tank_y > 0 && canMoveUp() && isMoving) {
				tank_y -= tank_speed;
				rectangle.y = tank_y;
			}
			break;
		case DOWN:
			g.drawImage(ImageUtill.myTanks[player][2], tank_x, tank_y, null);
			if (tank_y < 840 && canMoveDown() && isMoving) {
				tank_y += tank_speed;
				rectangle.y = tank_y;
			}
			break;
		default:
			break;
		}
		g.setColor(c);
	}

	private boolean canMoveDown() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if ((tank_y == obstacle.y - 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if ((tank_y == enemyTank.tank_y - 60) && (tank_x < enemyTank.tank_x + 60)
					&& (tank_x > enemyTank.tank_x - 60))
				return false;
		}
		return true;
	}

	private boolean canMoveUp() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if ((tank_y == obstacle.y + 60) && (tank_x < obstacle.x + 60) && (tank_x > obstacle.x - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if ((tank_y == enemyTank.tank_y + 60) && (tank_x < enemyTank.tank_x + 60)
					&& (tank_x > enemyTank.tank_x - 60))
				return false;
		}
		return true;
	}

	private boolean canMoveRight() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if ((tank_x == obstacle.x - 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if ((tank_x == enemyTank.tank_x - 60) && (tank_y < enemyTank.tank_y + 60)
					&& (tank_y > enemyTank.tank_y - 60))
				return false;
		}
		return true;
	}

	private boolean canMoveLeft() {
		for (Obstacle obstacle : drawPanel.nowStage.obstacles) {
			if ((tank_x == obstacle.x + 60) && (tank_y < obstacle.y + 60) && (tank_y > obstacle.y - 60)
					&& (!obstacle.canCrossIn))
				return false;
		}
		for (EnemyTank enemyTank : drawPanel.nowStage.enemyTanks) {
			if ((tank_x == enemyTank.tank_x + 60) && (tank_y < enemyTank.tank_y + 60)
					&& (tank_y > enemyTank.tank_y - 60))
				return false;
		}
		return true;
	}

	public void fire() {
		if (canFire) {
			drawPanel.bullets.add(new Bullet(tank_x, tank_y, state, "mytank"));
			new Thread(() -> new PlayWav("audio/bullet_flying.wav")).start();
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
		while (isalive) {
			if (drawPanel.keyboardPressing[controlkeys[player][0]]) {
				isMoving = true;
				state = State.UP;
			}
			else if (drawPanel.keyboardPressing[controlkeys[player][1]]) {
				isMoving = true;
				state = State.RIGHT;
			}
			else if (drawPanel.keyboardPressing[controlkeys[player][2]]) {
				isMoving = true;
				state = State.DOWN;
			}
			else if (drawPanel.keyboardPressing[controlkeys[player][3]]) {
				isMoving = true;
				state = State.LEFT;
			}
			else  {
				isMoving = false;
			}
			if (drawPanel.keyboardPressing[controlkeys[player][4]]) fire();
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
			state = State.UP;
			isMoving = false;
	}

}
